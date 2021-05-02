package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomModuleBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentModuleBinding
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.adapters.ClassroomIndexRecyclerAdapter
import ktxGamePrototype01.adapters.ClassroomModuleRecyclerAdapter
import ktxGamePrototype01.adapters.ModuleRecyclerAdapter
import java.util.ArrayList

class ModuleFragment : Fragment() {
    private lateinit var binding: FragmentModuleBinding
    private var moduleGameList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ModuleRecyclerAdapter
    private val classroomVM: ClassroomViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()

    val gameTypes = arrayOf("Quiz", "Theory")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_module, container, false)
        val navController = requireActivity().findNavController(R.id.nav_fragment)
        binding.classroomNav.setupWithNavController(navController)
        val moduleName = arguments?.getString("module")
        binding.lblModuleName.text = moduleName
        populateGames(moduleName.toString())

        adapter = ModuleRecyclerAdapter(moduleGameList)
        binding.recyclerViewGames.adapter = adapter
        binding.recyclerViewGames.layoutManager = LinearLayoutManager(context)

        val spinnerAdapter: ArrayAdapter<String> = object: ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            gameTypes
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text bold
                view.setTypeface(view.typeface, Typeface.BOLD)

                // set selected item style for both spinners
                 view.background = ColorDrawable(Color.parseColor("#FAEBD7"))
                 view.setTextColor(Color.parseColor("#008000"))

                return view
            }
        }
        binding.gameSpinner.adapter = spinnerAdapter

        binding.btnCreateGame.setOnClickListener {
            val module = binding.lblModuleName.text.toString()
            val bundle = bundleOf("module" to module)
            when (binding.gameSpinner.selectedItem.toString()){
                "Theory" -> print("PLACEHOLDER")
                "Quiz" -> findNavController().navigate(R.id.dest_create_quiz)
            }
        }

/*


        binding.btnCreateModule.setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                moduleList.add(editText.text.toString())
                adapter.notifyItemInserted(moduleList.size - 1)
            }
            builder.show()

            //initialize(Prot01(), AndroidApplicationConfiguration())
            //(activity as AppActivity?)!!.launchGame(1)
        }
*/

        return binding.root
    }
    private fun populateGames(id: String){
        db.collection("modules").document(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val gameList = task.result?.get("quizes") as List<String>
                for(game in gameList) {
                    if(!moduleGameList.contains(game)) {
                        moduleGameList.add(game)
                        adapter.notifyItemInserted(moduleGameList.size - 1)
                    }
                }
            }
        }
    }

}

