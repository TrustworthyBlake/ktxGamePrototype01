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

class ModuleRecyclerAdapter(private val gameObject: ArrayList<Game>, private val module: String) : RecyclerView.Adapter<ModuleRecyclerAdapter.ModuleHolder>() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleRecyclerAdapter.ModuleHolder {
        val view = parent.inflate(R.layout.game_entry, false)
        return ModuleHolder(view)
    }

    override fun getItemCount() = gameObject.size


    private var onClickListener : OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int){
        }
    }

    private fun removeItem(position: Int){
        gameObject.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, gameObject.size)
        var gameNameList = mutableListOf<String>()
        for(game in gameObject){
            gameNameList.add(game.gamename)
        }

        db.collection("modules")
            .document(module)
            .update("quizes", gameNameList)
            .addOnFailureListener { e ->
                Log.w("FAIL", "Error deleting module from classroom", e)
            }
    }


    override fun onBindViewHolder(holder: ModuleRecyclerAdapter.ModuleHolder, position: Int) {
        val gameItem = gameObject[position]
        holder.bindText(gameItem)

        if(User.checkForTeacher()){
            holder.itemView.game_delete_icon.visibility = View.VISIBLE
        }

        holder.itemView.game_delete_icon.setOnClickListener {
            removeItem(position)
        }
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
            val tempName = newGameName[1]
            view.game_name.text = tempName
        }

        //4
        override fun onClick(v: View) {

            val gameName = this.gName.toString()
            val newGameName = this!!.gName!!.split("-")
            val gameType = newGameName[0].replace(" ", "")
            val dbGameName = gameType+"-"+newGameName[1]+"-"+newGameName[2]

            Log.d("asdasdsdsssssssssssssssssssssssssssssssssssssssssss",dbGameName)

            val tempQuizName = (newGameName[1] + "-" + User.getName())
            var gameTypeString = "assets/miscFiles"
            var screenTypeString = "ERROR 404"
            val db = FirebaseFirestore.getInstance()


            db.collection("quiz").document(dbGameName).get().addOnCompleteListener() { task ->
                if(task.isSuccessful){
                    val quizList = task.result?.get("question") as? MutableList<String>

                    val pathInternal = view.context?.filesDir
                    if (pathInternal != null) {


                        when(gameType){
                            "quiz" -> gameTypeString = "assets/quizFiles"
                            "categorization" -> gameTypeString = "assets/categorizeFiles"
                            else -> gameTypeString = "ERROR 404"
                        }


                        val pathTextFile = File(pathInternal, gameTypeString)
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

            val screenTypeName = newGameName[0].replace(" ", "")
            when(gameType){
                "quiz" -> screenTypeString = "QuizScreen"
                "categorization" ->  screenTypeString = "CategorizeScreen"
                else -> gameTypeString = "ERROR 404"
            }




            (view.context as AppActivity?)!!.launchGame(screenTypeString, y, tempQuizName, x)
        }

    }
}