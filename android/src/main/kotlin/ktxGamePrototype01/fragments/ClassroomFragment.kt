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
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.adapters.ClassroomChatRecyclerAdapter
import ktxGamePrototype01.adapters.ModuleRecyclerAdapter
import java.util.*


class ClassroomViewModel : ViewModel() {
    var selected: String = ""

    fun select(item: String) {
        selected = item
    }
}



class ClassroomFragment : Fragment() {
    private lateinit var binding: FragmentClassroomBinding
    private var textList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ClassroomChatRecyclerAdapter
    private val classroomVM: ClassroomViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom, container, false)
        adapter = ClassroomChatRecyclerAdapter(textList)
        binding.recyclerViewClassesChat.adapter = adapter
        binding.recyclerViewClassesChat.layoutManager = LinearLayoutManager(context)
        val navController = requireActivity().findNavController(R.id.nav_fragment)

        val className = arguments?.getString("classroom")
        Toast.makeText(context,className.toString(),Toast.LENGTH_SHORT)
        if(classroomVM.selected.isNullOrBlank() && !className.isNullOrEmpty()){
            classroomVM.select(className.toString())
        }
        binding.classroomText.text = classroomVM.selected
        Toast.makeText(context,classroomVM.selected,Toast.LENGTH_SHORT)

        binding.classroomChatBtn.setOnClickListener {
            val tempText = binding.classroomChatInput.text.toString()
            textList.add(tempText)
            adapter.notifyItemInserted(textList.size - 1)
        }




        binding.classroomNav.setupWithNavController(navController)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
