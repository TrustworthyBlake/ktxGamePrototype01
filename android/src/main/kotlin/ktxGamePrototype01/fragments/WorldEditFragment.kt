package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentHomeBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentWorldEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.Game
import ktxGamePrototype01.adapters.ModuleRecyclerAdapter
import ktxGamePrototype01.adapters.OpenWorldGameRecyclerAdapter
import java.util.ArrayList

class WorldEditFragment : Fragment() {
    private lateinit var binding: FragmentWorldEditBinding
    private lateinit var auth: FirebaseAuth
    private var openWorldGameList: ArrayList<Game> = ArrayList()
    private lateinit var adapter: OpenWorldGameRecyclerAdapter
    private val db = FirebaseFirestore.getInstance()
    private val classroomVM: ClassroomViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_world_edit, container, false)
        val navController = requireActivity().findNavController(R.id.nav_fragment)
        binding.classroomNav.setupWithNavController(navController)
        // Initialize Firebase Auth
        auth = Firebase.auth

        populateGames(classroomVM.selected)

        adapter = OpenWorldGameRecyclerAdapter(openWorldGameList, classroomVM.selected)
        binding.recyclerViewOpenWorldGames.adapter = adapter
        binding.recyclerViewOpenWorldGames.layoutManager = LinearLayoutManager(context)





        binding.btnImportOpenWorldGame.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->

                val doc = db.collection("quiz").whereEqualTo("name", editText.text.toString()).get()
                doc.addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("classrooms")
                            .document(classroomVM.selected)
                            .update("quizes", FieldValue.arrayUnion(editText.text.toString()))

                        openWorldGameList.add(Game(editText.text.toString(), "Quiz"))
                        adapter.notifyItemInserted(openWorldGameList.size - 1) }

                    }
                Toast.makeText(activity, "Quiz added if it exists", Toast.LENGTH_LONG).show()
            }




                /*
                db.collection("quiz").document(editText.text.toString()).get()
                    .addOnSuccessListener() {
                        // if query is successful, reads the data and stores in variables
                        db.collection("classrooms")
                            .document(classroomVM.selected)
                            .update("quizes", FieldValue.arrayUnion(editText.text.toString()))

                        openWorldGameList.add(Game(editText.text.toString(), "Quiz"))
                        adapter.notifyItemInserted(openWorldGameList.size - 1) }
                    .addOnFailureListener { e ->  Toast.makeText(activity, "Quiz not found", Toast.LENGTH_LONG).show()  }
                 }


                 */
            builder.show()

        }

        return binding.root
    }







    private fun populateGames(id: String){
        db.collection("classrooms").document(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val gameList = task.result?.get("quizes") as? List<String>
                if(gameList != null) {
                    for (game in gameList) {
                        val tempGame = Game(game, "Quiz")
                        if (!openWorldGameList.contains(tempGame)) {
                            openWorldGameList.add(tempGame)
                            adapter.notifyItemInserted(openWorldGameList.size - 1)
                        }
                    }
                }
            }
        }
    }

}