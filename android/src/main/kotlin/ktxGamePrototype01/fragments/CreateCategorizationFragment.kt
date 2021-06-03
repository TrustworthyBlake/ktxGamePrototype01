package ktxGamePrototype01.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentCreateCategorizationBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentCreateQuizBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import ktxGamePrototype01.User
import java.io.File
import java.io.FileOutputStream


class CreateCategorizationFragment : Fragment() {
    private lateinit var binding: FragmentCreateCategorizationBinding
    private val classroomVM: ClassroomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val className = arguments?.getString("module")

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_categorization, container, false)

        binding.addButton.setOnClickListener {
            addToCategorization()
            //getTotalPlayerScoreFromPrefs() // For Debugging
        }
        binding.createCategorizationButton.setOnClickListener{
            createCategorization(className.toString())
        }
        binding.checkBoxIsQuestion.setOnClickListener {
            binding.checkBoxIsAnswer.isChecked = false
            binding.checkBoxIsCategory.isChecked = false
        }
        binding.checkBoxIsAnswer.setOnClickListener {
            binding.checkBoxIsQuestion.isChecked = false
            binding.checkBoxIsCategory.isChecked = false
        }
        binding.checkBoxIsCategory.setOnClickListener {
            binding.checkBoxIsQuestion.isChecked = false
            binding.checkBoxIsAnswer.isChecked = false
        }
        binding.buttonFinished.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.checkBoxIsAnswer.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.giveQuestionMaxScore.isEnabled = false
            }
        }
        binding.buttonDeleteCategorization.setOnClickListener {
            //deleteExistingQuiz()
        }



        return binding.root
    }

    private val tempQuizList = mutableListOf<String>()
    var hasAddedAnswer = false
    var hasCreatedQuestion = false
    var hasAddedCategory = false
    var amountOfCategories = 0
    var amountOfAnswers = 0
    // values for firestore under
    var qzName= ""
    var noOfQuestions = 0



    private fun addToCategorization() {
        when {
            (TextUtils.isEmpty(binding.createQuestionTextIn.text.toString())) -> {
                Toast.makeText(activity, "Error: You must add a question or answer!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val questAnsw = binding.createQuestionTextIn.text.toString()
                val isQuestion = binding.checkBoxIsQuestion.isChecked
                val isCategory = binding.checkBoxIsCategory.isChecked
                val isAnswer = binding.checkBoxIsAnswer.isChecked
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

                        tempQuizList.add("question-" + questAnsw + "-" + maxPoints)

                        hasAddedAnswer = false
                        hasAddedCategory = false
                        hasCreatedQuestion = true
                        amountOfCategories = 0
                        amountOfAnswers = 0
                        binding.giveQuestionMaxScore.setText("")
                        binding.createQuestionTextIn.setText("")

                        //binding.giveQuestionMaxScore.isEnabled = false

                        Toast.makeText(activity, "Added question", Toast.LENGTH_SHORT).show()
                    }
                    isCategory && (hasCreatedQuestion) && questAnsw.count() < 170 && amountOfCategories <= 2-> {
                        amountOfCategories += 1
                        tempQuizList.add("category-" + amountOfCategories.toString() + questAnsw)
                        hasAddedAnswer = false
                        hasAddedCategory = true


                        binding.createCategorizationButton.isEnabled = true
                        binding.createCategorizationTextIn.isEnabled = true
                        binding.createQuestionTextIn.setText("")

                        Toast.makeText(activity, "Added category", Toast.LENGTH_SHORT).show()
                    }
                    isAnswer && (hasAddedCategory) && questAnsw.count() < 170 && amountOfAnswers < 20-> {
                        tempQuizList.add("item-" + amountOfCategories.toString() + questAnsw)
                        hasAddedAnswer = true


                        binding.createCategorizationButton.isEnabled = true
                        binding.createCategorizationButton.isEnabled = true
                        binding.createQuestionTextIn.setText("")

                        Toast.makeText(activity, "Added answer", Toast.LENGTH_SHORT).show()
                        amountOfAnswers += 1
                    }


                    amountOfCategories > 2 -> Toast.makeText(activity, "Error: Max amount of 4 answers exceeded", Toast.LENGTH_SHORT).show()
                    !hasCreatedQuestion && !isQuestion -> Toast.makeText(activity, "Error: You must add a question before creating a categorization!", Toast.LENGTH_SHORT).show()
                    !hasAddedCategory && !isCategory -> Toast.makeText(activity, "Error: You must add a category before creating an answer!", Toast.LENGTH_SHORT).show()
                    //hasCreatedQuestion && isQuestion -> Toast.makeText(activity, "Error: You must add an answer before creating a new question!", Toast.LENGTH_SHORT).show()
                    questAnsw.count() > 170 -> Toast.makeText(activity, "Error: Max length of 170 characters exceeded", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(activity, "Error: You must add a score to the question!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createCategorization(module: String){
        when {
            TextUtils.isEmpty(binding.createCategorizationTextIn.text.toString()) -> {
                Toast.makeText(activity, "Error: You must give your quiz a name!", Toast.LENGTH_SHORT).show()
            }
            tempQuizList.isNotEmpty() -> {
                val quizName = binding.createCategorizationTextIn.text.toString()
                writeQuizToFile(quizName, tempQuizList)
                hasAddedAnswer = false
                hasCreatedQuestion = false
                //binding.createQuizButton.visibility = View.INVISIBLE
                //binding.createQuizTextIn.visibility = View.INVISIBLE
                binding.createCategorizationButton.isEnabled = false
                binding.createCategorizationTextIn.isEnabled = false

                qzName = "categorization-$quizName-" + User.getName()

                addCategorizationToModuleDatabase(module)
                addCategorizationToDatabase(module)
                tempQuizList.clear()
                binding.createCategorizationTextIn.setText("")
            }
            tempQuizList.isNullOrEmpty() -> {
                Toast.makeText(activity, "Error: You must add questions and answers to your quiz!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun writeQuizToFile(quizName: String, quizData: MutableList<String>) {
        val pathInternal = activity?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/categorizeFiles")
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


    private fun addCategorizationToDatabase(module: String) {
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

    private fun addCategorizationToModuleDatabase(module: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("modules")
            .document(module)
            .update("quizes", FieldValue.arrayUnion(qzName))

    }


}