package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.DialogDeleteQuizBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentCreateQuizBinding
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.NukeLocalFiles
import ktxGamePrototype01.User
import java.io.File
import java.io.FileOutputStream
import java.lang.Thread.sleep


class CreateQuizFragment : Fragment() {
    private lateinit var binding: FragmentCreateQuizBinding
    private val classroomVM: ClassroomViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_quiz, container, false)


        // TODO remove: just for testing
        val className = arguments?.getString("module")
        Toast.makeText(activity, className, Toast.LENGTH_SHORT).show()


        binding.addButton.setOnClickListener {
            addToQuiz()
            //getTotalPlayerScoreFromPrefs() // For Debugging
        }
        binding.createQuizButton.setOnClickListener{
            createQuiz(className.toString())
        }
        binding.checkBoxIsQuestion.setOnClickListener {
            checkBoxIsAnswer.isChecked = false
        }
        binding.checkBoxIsAnswer.setOnClickListener {
            checkBoxIsQuestion.isChecked = false
        }
        binding.buttonFinished.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.checkBoxIsAnswer.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.checkBoxIsCorrect.visibility = View.VISIBLE
                binding.checkBoxIsWrong.visibility = View.VISIBLE
                binding.giveQuestionMaxScore.isEnabled = false
            }else{
                binding.checkBoxIsCorrect.visibility = View.GONE
                binding.checkBoxIsWrong.visibility = View.GONE
                binding.giveQuestionMaxScore.isEnabled = true
            }
        }
        binding.checkBoxIsCorrect.setOnCheckedChangeListener { _, isChecked ->
            binding.checkBoxIsWrong.isChecked = !isChecked
        }
        binding.checkBoxIsWrong.setOnCheckedChangeListener { _, isChecked ->
            binding.checkBoxIsCorrect.isChecked = !isChecked
        }
        binding.buttonDeleteQuiz.setOnClickListener {
            deleteExistingQuiz()
        }

        return binding.root
    }
    private val tempQuizList = mutableListOf<String>()
    var hasAddedAnswer = false
    var hasCreatedQuestion = false
    var amountOfAnswers = 0
    // values for firestore under
    var qzName= ""
    var noOfQuestions = 0


    private fun addToQuiz() {
        when {
            (TextUtils.isEmpty(binding.createQuestionTextIn.text.toString())) -> {
                Toast.makeText(activity, "Error: You must add a question or answer!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val questAnsw = binding.createQuestionTextIn.text.toString()
                val isQuestion = binding.checkBoxIsQuestion.isChecked
                val isCorrect = binding.checkBoxIsCorrect.isChecked
                var maxPoints = 0
                var nrToQuestion = 0
                if (tempQuizList.isNotEmpty()) {                            // If here because kotlin complained with the use of when
                    val lastNumInQuizChar = tempQuizList.last().first()
                    nrToQuestion = Character.getNumericValue(lastNumInQuizChar)
                }
                when {  // question box is ticked and no question is given and is not empty and question not too long
                    isQuestion && !TextUtils.isEmpty(binding.giveQuestionMaxScore.text.toString()) && !hasCreatedQuestion && questAnsw.count() < 170 -> {
                        maxPoints = binding.giveQuestionMaxScore.text.toString().toInt()

                        nrToQuestion += 1
                        noOfQuestions += 1

                        tempQuizList.add(nrToQuestion.toString() + questAnsw + "-" + isQuestion + "-" + isCorrect + "-" + maxPoints)

                        hasAddedAnswer = false
                        hasCreatedQuestion = true
                        amountOfAnswers = 0
                        binding.giveQuestionMaxScore.setText("")
                        binding.createQuestionTextIn.setText("")

                        //binding.giveQuestionMaxScore.isEnabled = false

                        Toast.makeText(activity, "Added question", Toast.LENGTH_SHORT).show()
                    }
                    !isQuestion && (hasCreatedQuestion || hasAddedAnswer) && questAnsw.count() < 170 && amountOfAnswers < 4-> {
                        tempQuizList.add(nrToQuestion.toString() + questAnsw + "-" + isQuestion + "-" + isCorrect)
                        hasAddedAnswer = true
                        hasCreatedQuestion = false
                        //binding.createQuizButton.visibility = View.VISIBLE
                        //binding.createQuizTextIn.visibility = View.VISIBLE
                        binding.createQuizButton.isEnabled = true
                        binding.createQuizTextIn.isEnabled = true
                        binding.createQuestionTextIn.setText("")

                        Toast.makeText(activity, "Added answer", Toast.LENGTH_SHORT).show()
                        amountOfAnswers += 1



                    }
                    amountOfAnswers >= 4 -> Toast.makeText(activity, "Error: Max amount of 4 answers exceeded", Toast.LENGTH_SHORT).show()
                    !hasCreatedQuestion && !isQuestion -> Toast.makeText(activity, "Error: You must add a question before creating an answer!", Toast.LENGTH_SHORT).show()
                    hasCreatedQuestion && isQuestion -> Toast.makeText(activity, "Error: You must add an answer before creating a new question!", Toast.LENGTH_SHORT).show()
                    questAnsw.count() > 170 -> Toast.makeText(activity, "Error: Max length of 170 characters exceeded", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(activity, "Error: You must add a score to the question!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createQuiz(module: String){
        when {
            TextUtils.isEmpty(binding.createQuizTextIn.text.toString()) -> {
                Toast.makeText(activity, "Error: You must give your quiz a name!", Toast.LENGTH_SHORT).show()
            }
            tempQuizList.isNotEmpty() -> {
                val quizName = binding.createQuizTextIn.text.toString()
                writeQuizToFile(quizName, tempQuizList)
                hasAddedAnswer = false
                hasCreatedQuestion = false
                //binding.createQuizButton.visibility = View.INVISIBLE
                //binding.createQuizTextIn.visibility = View.INVISIBLE
                binding.createQuizButton.isEnabled = false
                binding.createQuizTextIn.isEnabled = false

                qzName = "quiz-$quizName-" + User.getName()

                addQuizToModuleDatabase(module)
                addQuizToDatabase(module)
                tempQuizList.clear()
                binding.createQuizTextIn.setText("")
            }
            tempQuizList.isNullOrEmpty() -> {
                Toast.makeText(activity, "Error: You must add questions and answers to your quiz!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun writeQuizToFile(quizName: String, quizData: MutableList<String>) {
        val pathInternal = activity?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (!pathTextFile.exists()){
                pathTextFile.mkdirs()
                Toast.makeText(activity, "Creating dir", Toast.LENGTH_SHORT).show()
            }
            val quizTextFile = File(pathTextFile, quizName + "-" + User.getName() + ".txt")
            var tempStr = ""
            quizData.forEach { line ->
                tempStr += line + '\n'
            }
            FileOutputStream(quizTextFile).use {
                it.write((tempStr).toByteArray())
            }
            Toast.makeText(activity, "Quiz written to file", Toast.LENGTH_SHORT).show()
            }
        }

    private fun deleteExistingQuiz(){
        val pathInternal = activity?.filesDir
        //var quizName : String
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (!pathTextFile.exists()){
                Toast.makeText(activity, "Directory for quiz files does not exist", Toast.LENGTH_SHORT).show()
            }else{
                val li = LayoutInflater.from(context)
                val dialogDeleteQuizBinding = DialogDeleteQuizBinding.inflate(li)
                val builder = AlertDialog.Builder(activity)
                with(builder){
                    setTitle("Enter name of quiz you want to delete")
                    setPositiveButton("OK"){ _, _ ->
                        val quizName = dialogDeleteQuizBinding.deleteQuizTextInput.text.toString()
                        val fullPathQuiz = File(pathTextFile, quizName + ".txt")
                        if(fullPathQuiz.exists()){
                            fullPathQuiz.delete()
                            Toast.makeText(activity, "Quiz deleted", Toast.LENGTH_SHORT).show()
                        }else Toast.makeText(activity, "Could not find a quiz with that name to delete", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Cancel"){ _, _ ->
                        dialogDeleteQuizBinding.deleteQuizTextInput.setText("")
                    }
                    setView(dialogDeleteQuizBinding.root)
                    show()
                }
            }
        }
    }
    // For debugging
    private fun getTotalPlayerScoreFromPrefs(){
        var totalPlayerScore = 0f
        val prefs: Preferences = Gdx.app.getPreferences("playerData")
        totalPlayerScore= prefs.getFloat("totalPlayerScore")
        Toast.makeText(activity, "Total player score = $totalPlayerScore", Toast.LENGTH_SHORT).show()
    }

    private fun addQuizToDatabase(module: String) {
        val db = FirebaseFirestore.getInstance()

        val quiz = hashMapOf(
                "name" to qzName,
                "course" to classroomVM.selected,
                "question" to tempQuizList
        )

        // add the data into the database
        db.collection("quiz").document(qzName).set(quiz).addOnSuccessListener {
            Log.d("FAIL", "Successfully added quiz to DB")
            Toast.makeText(activity, "Quiz successfully created", Toast.LENGTH_LONG).show()


            db.collection("modules")
                .document(module)
                .update("quizes", FieldValue.arrayUnion(qzName))
                .addOnFailureListener { e ->
                    Log.w("FAIL", "Error adding classroom to DB", e)
                    Toast.makeText(activity, "Quiz creation error", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun addQuizToModuleDatabase(module: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("modules")
            .document(module)
            .update("quizes", FieldValue.arrayUnion(qzName))

    }

    private fun getQuizFromDatabase(name: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("quiz").document(name).get().addOnCompleteListener() { task ->
            if(task.isSuccessful){
                val quizList = task.result?.get("question") as MutableList<String>

                writeQuizToFile(name, quizList)
            }
        }
    }

}


