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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_quiz, container, false)


        //val addQuestion = addQuestionButton
        binding.addQuestionButton.setOnClickListener {
        //addQuestion.setOnClickListener {
            createQuiz()
        }
        return binding.root
    }

    private fun createQuiz(){
        if (TextUtils.isEmpty(createQuestionTextIn.text.toString())) {
            Toast.makeText(activity, "You must add a question!", Toast.LENGTH_SHORT).show()
        } else {
            val question = binding.createQuestionTextIn.text.toString()
            val isQuestion = binding.checkBoxIsQuestion.isChecked
            val isAnswer = binding.checkBoxIsAnswer.isChecked
            val statementIsTrue = binding.checkBoxTrueStatement.isChecked
            val statementIsFalse = binding.checkBoxFalseStatement.isChecked
            //Toast.makeText(activity, isQuestion, Toast.LENGTH_SHORT).show()
            writeQuizToFile(question, isQuestion, isAnswer, statementIsTrue, statementIsFalse)
        }
    }

    private fun writeQuizToFile(question : String, isQuestion : Boolean, isAnswer : Boolean,
                                statementIsTrue : Boolean, statementIsFalse : Boolean) {
        //val pathTextFile = File(Environment.getExternalStorageState()+"/assets/quizFiles")//File(Environment.getExternalStorageState()+"/assets")
        val pathInternal = activity?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (!pathTextFile.exists()){
                pathTextFile.mkdirs()
                Toast.makeText(activity, "Creating dir", Toast.LENGTH_SHORT).show()
            }
            val quizTextFile = File(pathTextFile, "quizNr.txt")
            /*quizTextFile.appendText(question + "-" + isQuestion + "-" + isAnswer
                    + "-" + statementIsTrue + "-" + statementIsFalse)
            */
            FileOutputStream(quizTextFile).use {
                it.write((question + "-" + isQuestion + "-" + isAnswer
                        + "-" + statementIsTrue + "-" + statementIsFalse).toByteArray())
            }
            Toast.makeText(activity, "Quiz written to file", Toast.LENGTH_SHORT).show()
            }
        }
}
