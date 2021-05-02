package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomLeaderboardBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomModuleBinding
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.adapters.Chat
import ktxGamePrototype01.adapters.ClassroomIndexRecyclerAdapter
import ktxGamePrototype01.adapters.ClassroomModuleRecyclerAdapter
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

        binding.btnCreateModule.setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                classroomModuleList.add(editText.text.toString())
                adapter.notifyItemInserted(classroomModuleList.size - 1)
            }
            builder.show()
        }

        return binding.root
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

