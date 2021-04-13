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
    val tempQuizList = mutableListOf<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_quiz, container, false)

        binding.addButton.setOnClickListener {
            addToQuiz()
        }
        binding.createQuizButton.setOnClickListener{
            createQuiz()
        }
        return binding.root
    }
    private fun addToQuiz(){
        if (TextUtils.isEmpty(binding.createQuestionTextIn.text.toString())) {
            Toast.makeText(activity, "Error: You must add a question or answer!", Toast.LENGTH_SHORT).show()
        } else {
            val questAnsw = binding.createQuestionTextIn.text.toString()
            val isQuestion = binding.checkBoxIsQuestion.isChecked
            val isCorrect = binding.checkBoxIsCorrect.isChecked
            var nrToQuestion = 0
            if(tempQuizList.isNotEmpty()){
                val lastNumInQuizChar = tempQuizList.last().first()
                nrToQuestion = Character.getNumericValue(lastNumInQuizChar)
            }
            if(isQuestion){
                nrToQuestion += 1
                tempQuizList.add(nrToQuestion.toString() + questAnsw + "-" + isQuestion + "-" + isCorrect)
            }else{
                tempQuizList.add(nrToQuestion.toString() + questAnsw + "-" + isQuestion + "-" + isCorrect)
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
}
