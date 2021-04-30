package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomLeaderboardBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomModuleBinding
import ktxGamePrototype01.adapters.ClassroomIndexRecyclerAdapter
import java.util.ArrayList

class ClassroomModuleFragment : Fragment() {
    private lateinit var binding: FragmentClassroomModuleBinding
    private var textList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ClassroomIndexRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom_module, container, false)
        val navController = requireActivity().findNavController(R.id.nav_fragment)
        binding.classroomNav.setupWithNavController(navController)

        adapter = ClassroomIndexRecyclerAdapter(textList)
        binding.recyclerViewModules.adapter = adapter
        binding.recyclerViewModules.layoutManager = LinearLayoutManager(context)

        binding.btnCreateModule.setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                textList.add(editText.text.toString())
                adapter.notifyItemInserted(textList.size - 1)
            }
            builder.show()

            //initialize(Prot01(), AndroidApplicationConfiguration())
            //(activity as AppActivity?)!!.launchGame(1)
        }

        return binding.root
    }

}

