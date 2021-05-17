package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomLeaderboardBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomModuleBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.DBObject
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.Chat
import ktxGamePrototype01.adapters.ClassroomIndexRecyclerAdapter
import ktxGamePrototype01.adapters.ClassroomModuleRecyclerAdapter
import ktxGamePrototype01.adapters.Game
import java.util.ArrayList

class ClassroomModuleFragment : Fragment() {
    private lateinit var binding: FragmentClassroomModuleBinding
    private var classroomModuleList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ClassroomModuleRecyclerAdapter
    private val classroomVM: ClassroomViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom_module, container, false)
        val navController = requireActivity().findNavController(R.id.nav_fragment)
        binding.classroomNav.setupWithNavController(navController)

        populateModules(classroomVM.selected)

        adapter = ClassroomModuleRecyclerAdapter(classroomModuleList)
        binding.recyclerViewModules.adapter = adapter
        binding.recyclerViewModules.layoutManager = LinearLayoutManager(context)
        binding.lblClassroomName.text = classroomVM.selected

        if(User.checkForTeacher()){
            binding.btnImportModule.visibility = View.VISIBLE
            binding.btnCreateModule.visibility = View.VISIBLE
            binding.btnOpenWorld.visibility = View.VISIBLE
        }


        binding.btnOpenWorld.setOnClickListener {
            findNavController().navigate(R.id.dest_open_world_edit)
        }

        binding.btnCreateModule.setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->

                addModule(editText.text.toString())

                classroomModuleList.add(editText.text.toString())
                adapter.notifyItemInserted(classroomModuleList.size - 1)
            }
            builder.show()
        }

        binding.btnImportModule.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->



                db.collection("modules").document(editText.text.toString()).get().addOnCompleteListener() { task ->
                 if (task.isSuccessful) {
                    db.collection("classrooms")
                        .document(classroomVM.selected)
                        .update("modules", FieldValue.arrayUnion(editText.text.toString()))

                     /* // Code for placing all of modules quizzes inside of openworld quiz list.
                     var quizList: List<String> = emptyList()
                     if (task.isSuccessful) {
                         val quizes = task.result?.get("quizes") as? List<String>
                         if(quizes != null) {
                             for (quiz in quizes) {
                                 quizList = quizList + quiz
                             }
                         }
                     }
                    for(quiz in quizList){
                        db.collection("classrooms")
                                .document(classroomVM.selected)
                                .update("quizes", FieldValue.arrayUnion(quiz))
                    }

                      */


                     classroomModuleList.add(editText.text.toString())
                     adapter.notifyItemInserted(classroomModuleList.size - 1)
                    }else{
                        Toast.makeText(activity, "Quiz not found", Toast.LENGTH_LONG).show()
                    }
                }




            }
            builder.show()
        }

        return binding.root
    }

    private fun addModule(id: String){
        // Put module into database
        val module = hashMapOf(
            "classroom" to classroomVM.selected,
            "name" to id,
            "quizes" to mutableListOf<String>(),
        )
        db.collection("modules")
            .document(id)
            .set(module)

        // Add module to the classrooms module list

        db.collection("classrooms")
            .document(classroomVM.selected)
            .update("modules", FieldValue.arrayUnion(id))
    }

    private fun populateModules(id: String){
        db.collection("classrooms").document(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val moduleList = task.result?.get("modules") as List<String>
                for(module in moduleList) {
                    if(!classroomModuleList.contains(module)) {
                        classroomModuleList.add(module)
                        adapter.notifyItemInserted(classroomModuleList.size - 1)
                    }
                }
            }
        }
    }
}

