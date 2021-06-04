package ktxGamePrototype01.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentEditProfileBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.DBObject
import ktxGamePrototype01.User

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        showUserData()

        User.clearLists()
        DBObject.getUserData(User.getId())


        binding.btnLaunchGame.setOnClickListener {
            val x = User.getTeacherAvatars()
            val y = User.getName()
            (activity as AppActivity?)!!.launchGame("OpenWorldScreen", y, "test9", x )
        }


        return binding.root
    }

    private fun showUserData() {
        // getting the reference to the textViews
        val userName = binding.root.findViewById<TextView>(R.id.homeUser_name)

        // displaying the data in the textViews
        userName.text = AppActivity().userObject.getName()

    }

}