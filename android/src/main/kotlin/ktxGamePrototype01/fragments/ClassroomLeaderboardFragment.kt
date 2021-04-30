package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomLeaderboardBinding
import ktxGamePrototype01.adapters.ModuleRecyclerAdapter
import java.util.ArrayList

class ClassroomLeaderboardFragment : Fragment() {
    private lateinit var binding: FragmentClassroomLeaderboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom_leaderboard, container, false)
        val navController = requireActivity().findNavController(R.id.nav_fragment)
        binding.classroomNav.setupWithNavController(navController)

        return binding.root

    }

}
