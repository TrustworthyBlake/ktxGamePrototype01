package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.content.ClipData
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.DBObject
import ktxGamePrototype01.adapters.Chat
import ktxGamePrototype01.adapters.ClassroomChatRecyclerAdapter
import ktxGamePrototype01.adapters.ModuleRecyclerAdapter
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class ClassroomViewModel : ViewModel() {
    var selected: String = ""

    fun select(item: String) {
        selected = item
    }
}





class ClassroomFragment : Fragment() {
    private lateinit var binding: FragmentClassroomBinding
    private var chatList: ArrayList<Chat> = ArrayList()
    private lateinit var adapter: ClassroomChatRecyclerAdapter
    private val classroomVM: ClassroomViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom, container, false)
        adapter = ClassroomChatRecyclerAdapter(chatList)
        binding.recyclerViewClassesChat.adapter = adapter
        binding.recyclerViewClassesChat.layoutManager = LinearLayoutManager(context)
        val navController = requireActivity().findNavController(R.id.nav_fragment)


        val className = arguments?.getString("classroom")
        if(classroomVM.selected.isNullOrBlank() && !className.isNullOrEmpty()){
            classroomVM.select(className.toString())
        }
        binding.classroomText.text = classroomVM.selected
        Toast.makeText(context,classroomVM.selected,Toast.LENGTH_SHORT)

        binding.classroomChatBtn.setOnClickListener {
            val tempMessage = binding.classroomChatInput.text.toString()
            val tempTimestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneOffset.UTC).format(Instant.now())
            val tempChat = Chat(classroomVM.selected, tempMessage, tempTimestamp)
            DBObject.newAnnouncement(classroomVM.selected, tempTimestamp + " - " +  tempMessage)
            chatList.add(tempChat)
            adapter.notifyItemInserted(chatList.size - 1)
        }

        //  Populate the announcements recycler view
        populateAnnouncements(classroomVM.selected)


        binding.classroomNav.setupWithNavController(navController)
        return binding.root
    }

    private fun populateAnnouncements(id: String){
        db.collection("classrooms").document(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val classroomList = task.result?.get("announcements") as List<String>
                for(announcement in classroomList) {
                    var tempQuizList: List<String> = announcement.split("-")
                    val tempChat = Chat(id, tempQuizList[1], tempQuizList[0])
                     chatList.add(tempChat)
                     adapter.notifyItemInserted(chatList.size - 1)
                }
            }
        }
    }
}
