package ktxGamePrototype01.fragments

import android.os.Bundle
import android.os.Environment
import android.system.Os.mkdir
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentCreateQuizBinding
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.IOException


class CreateQuizFragment : Fragment() {
    private lateinit var binding: FragmentCreateQuizBinding
    //private lateinit var tempQuizList: MutableList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_quiz, container, false)

        binding.addButton.setOnClickListener {
            addToQuiz()
            //getTotalPlayerScoreFromPrefs() // For Debugging
        }
        binding.createQuizButton.setOnClickListener{
            createQuiz()

        }
        return binding.root
    }
    private val tempQuizList = mutableListOf<String>()
    var hasAddedAnswer = false
    var hasCreatedQuestion = false

    private fun addToQuiz() {
        //hasAddedAnswer = true   // Needed so that a user must add a answer to question before creating a new question
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
                when {
                    isQuestion && !TextUtils.isEmpty(binding.giveQuestionMaxScore.text.toString()) && !hasCreatedQuestion-> {
                        maxPoints = binding.giveQuestionMaxScore.text.toString().toInt()
                        nrToQuestion += 1
                        tempQuizList.add(nrToQuestion.toString() + questAnsw + "-" + isQuestion + "-" + isCorrect + "-" + maxPoints)
                        hasAddedAnswer = false
                        hasCreatedQuestion = true
                        Toast.makeText(activity, "Added question", Toast.LENGTH_SHORT).show()
                    }
                    !isQuestion && (hasCreatedQuestion || hasAddedAnswer)-> {
                        tempQuizList.add(nrToQuestion.toString() + questAnsw + "-" + isQuestion + "-" + isCorrect)
                        hasAddedAnswer = true
                        hasCreatedQuestion = false
                        binding.createQuizButton.visibility = View.VISIBLE
                        binding.createQuizTextIn.visibility = View.VISIBLE
                        Toast.makeText(activity, "Added answer", Toast.LENGTH_SHORT).show()
                    }
                    !hasCreatedQuestion && !isQuestion -> Toast.makeText(activity, "Error: You must add a question before creating an answer!", Toast.LENGTH_SHORT).show()
                    hasCreatedQuestion && isQuestion -> Toast.makeText(activity, "Error: You must add an answer before creating a new answer!", Toast.LENGTH_SHORT).show()

                    else -> Toast.makeText(activity, "Error: You must add a score to the question!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createQuiz(){
        when {
            TextUtils.isEmpty(binding.createQuizTextIn.text.toString()) -> {
                Toast.makeText(activity, "Error: You must give your quiz a name!", Toast.LENGTH_SHORT).show()
            }
            tempQuizList.isNotEmpty() -> {
                val quizName = binding.createQuizTextIn.text.toString()
                writeQuizToFile(quizName, tempQuizList)
                hasAddedAnswer = false
                hasCreatedQuestion = false
                binding.createQuizButton.visibility = View.INVISIBLE
                binding.createQuizTextIn.visibility = View.INVISIBLE
                tempQuizList.clear()
            }
            tempQuizList.isNullOrEmpty() -> {
                Toast.makeText(activity, "Error: You must add questions and answers to your quiz!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun writeQuizToFile(quizName : String, quizData : MutableList<String>) {
        val pathInternal = activity?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (!pathTextFile.exists()){
                pathTextFile.mkdirs()
                Toast.makeText(activity, "Creating dir", Toast.LENGTH_SHORT).show()
            }
            val quizTextFile = File(pathTextFile, quizName + ".txt")
            var tempStr = ""
            tempQuizList.forEach { line ->
                tempStr += line + '\n'
            }
            FileOutputStream(quizTextFile).use {
                it.write((tempStr).toByteArray())
            }
            Toast.makeText(activity, "Quiz written to file", Toast.LENGTH_SHORT).show()
            }
        }

    // For debugging
    private fun getTotalPlayerScoreFromPrefs(){
        var totalPlayerScore = 0f
        val prefs: Preferences = Gdx.app.getPreferences("playerData")
        totalPlayerScore= prefs.getFloat("totalPlayerScore")
        Toast.makeText(activity, "Total player score = $totalPlayerScore", Toast.LENGTH_SHORT).show()
    }
}
