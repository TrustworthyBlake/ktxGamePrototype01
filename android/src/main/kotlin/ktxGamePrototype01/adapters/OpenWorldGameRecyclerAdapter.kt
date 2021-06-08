package ktxGamePrototype01.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.game_entry.view.*
import kotlinx.android.synthetic.main.game_entry.view.game_name
import kotlinx.android.synthetic.main.open_world_game_entry.view.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.fragments.ClassroomViewModel
import ktxGamePrototype01.inflate
import java.io.File
import java.io.FileOutputStream

class OpenWorldGameRecyclerAdapter(private val openWorldGameObject: ArrayList<Game>, private val classroom: String) : RecyclerView.Adapter<OpenWorldGameRecyclerAdapter.OpenWorldGameHolder>() {
    private val db = FirebaseFirestore.getInstance()

    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenWorldGameRecyclerAdapter.OpenWorldGameHolder {
        val view = parent.inflate(R.layout.open_world_game_entry, false)
        return OpenWorldGameHolder(view)
    }

    override fun getItemCount() = openWorldGameObject.size

    private var onClickListener : OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int){
        }
    }

    private fun removeItem(position: Int){
        openWorldGameObject.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, openWorldGameObject.size)
        var openWorldGameNameList = mutableListOf<String>()
        for(game in openWorldGameObject){
            openWorldGameNameList.add(game.gamename)
        }

        db.collection("classrooms")
            .document(classroom)
            .update("quizes", openWorldGameNameList)
            .addOnFailureListener { e ->
                Log.w("FAIL", "Error deleting module from classroom", e)
            }
    }

    override fun onBindViewHolder(holder: OpenWorldGameRecyclerAdapter.OpenWorldGameHolder, position: Int) {
        val gameItem = openWorldGameObject[position]
        holder.bindText(gameItem)

        if(User.checkForTeacher()){
            holder.itemView.open_world_game_delete_icon.visibility = View.VISIBLE
        }

        holder.itemView.open_world_game_delete_icon.setOnClickListener {
            removeItem(position)
        }
    }

    //1
    class OpenWorldGameHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
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
            view.game_name.text = newGameName[1]
        }

        override fun onClick(v: View?) {
            Toast.makeText(view.context, "Playable from the Open World", Toast.LENGTH_SHORT).show()
        }


    }


}