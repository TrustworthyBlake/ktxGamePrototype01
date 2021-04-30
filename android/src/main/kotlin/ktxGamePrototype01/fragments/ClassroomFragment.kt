package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import ktxGamePrototype01.AppActivity
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




        val navController = requireActivity().findNavController(R.id.nav_fragment)
        binding.classroomNav.setupWithNavController(navController)

        return binding.root
    }


}