package ktxGamePrototype01.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import kotlinx.android.synthetic.main.classroom_chat_entry.view.*
import kotlinx.android.synthetic.main.fragment_classroom.view.*
import kotlinx.android.synthetic.main.module_entry.view.*
import ktxGamePrototype01.inflate
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ClassroomChatRecyclerAdapter(private val classChatText: ArrayList<String>) : RecyclerView.Adapter<ClassroomChatRecyclerAdapter.ClassroomChatHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomChatRecyclerAdapter.ClassroomChatHolder {
        val view = parent.inflate(R.layout.classroom_chat_entry, false)
        return ClassroomChatHolder(view)
    }

    override fun getItemCount() = classChatText.size

    override fun onBindViewHolder(holder: ClassroomChatRecyclerAdapter.ClassroomChatHolder, position: Int) {
        val itemText = classChatText[position]
        holder.bindText(itemText)
    }

    class ClassroomChatHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //2
        private var text: String? = null
        private var text2: String? = null
        private var timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())

        //3
        init {
            view.setOnClickListener(this)
        }

        fun bindText(tempText: String) {
            this.text = tempText
            this.text2 = tempText
            view.classroom_chat_name.text = this.text
            view.classroom_chat_message.text = this.text2
            view.classroom_chat_timestamp.text = timestamp

        }

        //4
        override fun onClick(v: View) {





            Log.d("RecyclerView", "CLICK!")
        }

    }


}