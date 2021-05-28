package ktxGamePrototype01.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_setting.*
import ktxGamePrototype01.*

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var savedDarkData: sharedprefs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //checks if the switch has been toggled on or of, if it on then dark mode is loaded

        binding.dModes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                savedDarkData = sharedprefs(requireContext() as AppActivity)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                savedDarkData.setDarkModeState(true)
            } else{ //this else has to duplicate the if, or else the switch won't go back to light mode
                savedDarkData = sharedprefs(requireContext() as AppActivity)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                savedDarkData.setDarkModeState(false)
            }
        }

        savedDarkData = sharedprefs(requireContext() as AppActivity)
        //because the switch is defaulted to off, toggle will set that on on but only if darkmode was enabled after closing the app.
        if(savedDarkData.loadDarkModeState() == true){
            binding.dModes.toggle()
        }

        binding.button.setOnClickListener{
            //This is required for every button
            savedDarkData = sharedprefs(requireContext() as AppActivity)
            //sets a string to the keyword in the file, checked by loadThemeColour
            savedDarkData.setThemeColour("Red")
            //this is also required for every button, the app needs to recreate itself to the theme to be set
            (activity as AppActivity).recreate();
        }

        binding.button2.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)
            savedDarkData.setThemeColour("Green")
            (activity as AppActivity).recreate();
        }

        binding.button3.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)
            savedDarkData.setThemeColour("Purple")
            (activity as AppActivity).recreate();
        }

        binding.button4.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)
            savedDarkData.setThemeColour("Orange")
            (activity as AppActivity).recreate();
        }

        binding.button5.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)
            savedDarkData.setThemeColour("")
            (activity as AppActivity).recreate();
        }

        binding.button6.setOnClickListener {
            val nuke = NukeLocalFiles()
            nuke.nukeStoredQuizzes(activity as AppActivity)
        }
    }




}