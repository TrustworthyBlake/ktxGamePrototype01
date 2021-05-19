package ktxGamePrototype01.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomLeaderboardBinding
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.adapters.ClassroomModuleRecyclerAdapter
import ktxGamePrototype01.adapters.Game
import ktxGamePrototype01.adapters.StudentRecyclerAdapter
import java.util.ArrayList


class ClassroomLeaderboardFragment : Fragment() {
    private lateinit var binding: FragmentClassroomLeaderboardBinding
    private val db = FirebaseFirestore.getInstance()
    private var studentList: ArrayList<String> = ArrayList()
    private lateinit var adapter: StudentRecyclerAdapter
    private val classroomVM: ClassroomViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom_leaderboard, container, false)
        val navController = requireActivity().findNavController(R.id.nav_fragment)
        binding.classroomNav.setupWithNavController(navController)

        populateStudents(classroomVM.selected)

        adapter = StudentRecyclerAdapter(studentList)
        binding.recyclerViewStudents.adapter = adapter
        binding.recyclerViewStudents.layoutManager = LinearLayoutManager(context)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    private fun populateStudents(id: String){
        db.collection("classrooms").document(classroomVM.selected).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val classroomStudents = task.result?.get("students") as? List<String>
                if(classroomStudents != null) {
                    for (student in classroomStudents) {
                        db.collection("users").document(student).get().addOnCompleteListener() { taskTwo ->
                            if (taskTwo.isSuccessful) {
                                val studentName = taskTwo.result?.get("name") as? String
                                if (!studentList.contains(studentName)) {
                                    studentList.add(studentName.toString())
                                    adapter.notifyItemInserted(studentList.size - 1)
                                    }
                            }
                        }

                    }
                    //    if (!moduleGameList.contains(tempGame)) {
                    //        moduleGameList.add(tempGame)
                    //        adapter.notifyItemInserted(moduleGameList.size - 1)
                    //    }
                    }
                }
            }
        }
}
