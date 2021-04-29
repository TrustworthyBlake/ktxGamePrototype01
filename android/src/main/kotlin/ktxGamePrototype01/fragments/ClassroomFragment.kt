package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomBinding
import ktxGamePrototype01.adapters.ModuleRecyclerAdapter
import java.util.*


class ClassroomFragment : Fragment() {
    private lateinit var binding: FragmentClassroomBinding
    private var textList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ModuleRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom, container, false)
        adapter = ModuleRecyclerAdapter(textList)
        //binding.recyclerViewClasses.adapter = adapter
        //binding.recyclerViewClasses.layoutManager = LinearLayoutManager(context)

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