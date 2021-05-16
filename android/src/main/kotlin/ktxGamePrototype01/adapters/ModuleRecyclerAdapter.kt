package ktxGamePrototype01.adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.game_entry.view.*
import kotlinx.android.synthetic.main.module_entry.view.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.inflate
import java.io.File
import java.io.FileOutputStream
import java.lang.Thread.sleep

class ModuleRecyclerAdapter(private val gameObject: ArrayList<Game>) : RecyclerView.Adapter<ModuleRecyclerAdapter.ModuleHolder>() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleRecyclerAdapter.ModuleHolder {
        val view = parent.inflate(R.layout.game_entry, false)
        return ModuleHolder(view)
    }

    override fun getItemCount() = gameObject.size

    fun getQuizFromDatabase(name: String, view: View) {

    }

    override fun onBindViewHolder(holder: ModuleRecyclerAdapter.ModuleHolder, position: Int) {
        val gameItem = gameObject[position]
        holder.bindText(gameItem)
    }

    //1
    class ModuleHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //2
        private var gName: String? = null
        private var gType: String? = null

        //3
        init {
            view.setOnClickListener(this)
        }

        fun bindText(gameEntity: Game) {
            this.gName = gameEntity.gamename
            this.gType = gameEntity.gametype

            var newGameName = this!!.gName!!.split("-")
            view.game_name.text = newGameName[0]
        }

        //4
        override fun onClick(v: View) {

            val gameName = this.gName.toString()
            var tempQuizName = (gameName + "-" + User.getName())

            val db = FirebaseFirestore.getInstance()
            db.collection("quiz").document(gameName).get().addOnCompleteListener() { task ->
                if(task.isSuccessful){
                    val quizList = task.result?.get("question") as? MutableList<String>

                    val pathInternal = view.context?.filesDir
                    if (pathInternal != null) {
                        val pathTextFile = File(pathInternal, "assets/quizFiles")
                        if (!pathTextFile.exists()){
                            pathTextFile.mkdirs()
                            Toast.makeText(view.context, "Creating dir", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(view.context, "Creating dir", Toast.LENGTH_SHORT).show()
                        val quizTextFile = File(pathTextFile, tempQuizName + ".txt")
                        var tempStr = ""
                        quizList?.forEach { line ->
                            tempStr += line + '\n'
                        }
                        FileOutputStream(quizTextFile).use {
                            it.write((tempStr).toByteArray())
                        }
                    }
                }
            }
            sleep(200)
             val x = User.getTeacherAvatars()
             val y = User.getName()
             (view.context as AppActivity?)!!.launchGame("QuizScreen", y, tempQuizName, x)
        }

    }



    private fun writeQuizToFile(quizName: String, quizData: MutableList<String>, view: View) {

        val pathInternal = view.context?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (!pathTextFile.exists()){
                pathTextFile.mkdirs()
                Toast.makeText(view.context, "Creating dir", Toast.LENGTH_SHORT).show()
            }
            val quizTextFile = File(pathTextFile, quizName + "-" + User.getName() + ".txt")
            var tempStr = ""
            quizData.forEach { line ->
                tempStr += line + '\n'
            }
            FileOutputStream(quizTextFile).use {
                it.write((tempStr).toByteArray())
            }
            Toast.makeText(view.context, "Quiz written to file", Toast.LENGTH_SHORT).show()
        }
    }
}