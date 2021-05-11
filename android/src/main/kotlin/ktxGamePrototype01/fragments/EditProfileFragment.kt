package ktxGamePrototype01.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.badlogic.gdx.Gdx

import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.badlogic.gdx.Preferences
import ktxGamePrototype01.User

class EditProfileFragment : Fragment() /*ListAdapterProfileEdit.ListClickListener*/{

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    val items = arrayOf("colour1", "colour2", "colour3", "colour4")
    //val imegs = arrayOf(R.drawable.default1, R.drawable.default2, R.drawable.ebin, R.drawable.gond)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth


        var type1 = ""
        var type2 = ""
        val headImage = binding.root.findViewById<ImageView>(R.id.headPicture)
        val bodyImage = binding.root.findViewById<ImageView>(R.id.bodyPicture)
        //get the spinner from the xml.
        val spinner1 = binding.root.findViewById<Spinner>(R.id.spinner1)
        val spinner2 = binding.root.findViewById<Spinner>(R.id.spinner2)

        val adapter: ArrayAdapter<String> = object: ArrayAdapter<String>(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            items
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
                if (position == spinner1.selectedItemPosition){
                    view.background = ColorDrawable(Color.parseColor("#FAEBD7"))
                    view.setTextColor(Color.parseColor("#008000"))
                }
                if (position == spinner2.selectedItemPosition){
                    view.background = ColorDrawable(Color.parseColor("#FAEBD7"))
                    view.setTextColor(Color.parseColor("#EB6C49"))
                }

                return view
            }
        }

        spinner1.adapter = adapter
        spinner2.adapter = adapter


        val setswitch1 = getHead(User.getName())
        val setswitch2 = getBody(User.getName())

        when (setswitch1){
            "colour1" ->  {spinner1.setSelection(0)}
            "colour2" ->  {spinner1.setSelection(1)}
            "colour3" ->  {spinner1.setSelection(2)}
            "colour4" ->  {spinner1.setSelection(3)}
        }

        when (setswitch2) {
            "colour1" -> {spinner2.setSelection(0)}
            "colour2" -> {spinner2.setSelection(1)}
            "colour3" -> {spinner2.setSelection(2)}
            "colour4" -> {spinner2.setSelection(3)}
        }


        // spinner1 on item selected listener
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                type1 = parent.getItemAtPosition(position).toString()
                when (type1){
                    "colour1" ->  {headImage.setImageResource(R.drawable.head1); saveDatahead(type1, User.getName()) }
                    "colour2" ->  {headImage.setImageResource(R.drawable.head2); saveDatahead(type1, User.getName()) }
                    "colour3" ->  {headImage.setImageResource(R.drawable.head3); saveDatahead(type1, User.getName()) }
                    "colour4" ->  {headImage.setImageResource(R.drawable.head4); saveDatahead(type1, User.getName()) }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // not possible to select nothing, but it doesn't work without this function
            }
        }



        // spinner2 on item selected listener
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // setting the previewed characters clothing according to the user's selection of magic type
                type2 = parent.getItemAtPosition(position).toString()
                when (type2) {
                    "colour1" -> {bodyImage.setImageResource(R.drawable.body1); saveDatabody(type2, User.getName()) }
                    "colour2" -> {bodyImage.setImageResource(R.drawable.body2); saveDatabody(type2, User.getName()) }
                    "colour3" -> {bodyImage.setImageResource(R.drawable.body3); saveDatabody(type2, User.getName()) }
                    "colour4" -> {bodyImage.setImageResource(R.drawable.body4); saveDatabody(type2, User.getName()) }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // not possible to select nothing, but it doesn't work without this function
            }
        }




        return binding.root
    }

    private fun saveDatahead(head : String, userName : String){
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        prefs.putString("avatarHead", head)
        prefs.flush()
    }

    private fun saveDatabody(body : String, userName : String){
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        prefs.putString("avatarBody", body)
        prefs.flush()
    }


    private fun getHead(userName : String): String {
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        val headPog : String = prefs.getString("avatarHead")
        return headPog
    }

    private fun getBody(userName : String): String {
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        val bodyPog : String = prefs.getString("avatarBody")
        return bodyPog
    }




    //-------------code below here goes unused but was meant for a recycler view for this fragment.
    //-------------you may use it for a different purpose

    /*

    override fun onItemClick(pos: Int) {
        val headImage = binding.root.findViewById<ImageView>(R.id.headPicture)
        val bodyImage = binding.root.findViewById<ImageView>(R.id.imageView2)

        headImage.setImageResource(imegs[pos])
    }

    private fun makeDaList(size: Int) : List<ListItemProfileEdit> {
        val list = ArrayList<ListItemProfileEdit>()
        for (i in 0 until size) {
            val name = items[i]
            val drawable = imegs[i]
            val item = ListItemProfileEdit(drawable, name)
            list += item
        }
        return list
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val exampleListHead = makeDaList(items.size)
        val exampleListBody = makeDaList(items.size)
        edit_profile_head_recycler_view.adapter = ListAdapterProfileEdit(exampleListHead, this)
        edit_profile_head_recycler_view.layoutManager = LinearLayoutManager(requireActivity())
        edit_profile_body_recycler_view.adapter = ListAdapterProfileEdit(exampleListBody, this)
        edit_profile_body_recycler_view.layoutManager = LinearLayoutManager(requireActivity())
    }

     */


}