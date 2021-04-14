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
import androidx.recyclerview.widget.LinearLayoutManager
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomIndexBinding
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.adapters.ClassroomIndexRecyclerAdapter
import java.util.*


class ClassroomIndexFragment : Fragment() {
    private lateinit var binding: FragmentClassroomIndexBinding
    private var textList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ClassroomIndexRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom_index, container, false)
        adapter = ClassroomIndexRecyclerAdapter(textList)
        binding.recyclerViewClasses.adapter = adapter
        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(context)

        binding.btnCreateModule.setOnClickListener() {
            /*
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                textList.add(editText.text.toString())
                adapter.notifyItemInserted(textList.size - 1)
            }
            builder.show()
            */

            //initialize(Prot01(), AndroidApplicationConfiguration())
            (activity as AppActivity?)!!.launchGame(1)
        }
        return binding.root
    }
}