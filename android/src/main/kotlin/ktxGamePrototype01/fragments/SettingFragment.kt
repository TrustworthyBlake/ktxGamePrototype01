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
import kotlinx.android.synthetic.main.fragment_setting.*
import ktxGamePrototype01.AndroidLauncher
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.sharedprefs

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

        binding.dModes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                savedDarkData = sharedprefs(requireContext() as AppActivity)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                savedDarkData.setDarkModeState(true)
            } else{
                savedDarkData = sharedprefs(requireContext() as AppActivity)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                savedDarkData.setDarkModeState(false)
            }
        }

        savedDarkData = sharedprefs(requireContext() as AppActivity)

        if(savedDarkData.loadDarkModeState() == true){
            binding.dModes.toggle()                   //because the switch is defaulted to off, toggle will set that on on but only if darkmode was enabled after closing the app.
        }

        binding.button.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)

            savedDarkData.setRedModeState(true)
            savedDarkData.setPurpleModeState(false)
            savedDarkData.setGreenModeState(false)
            savedDarkData.setOrangeModeState(false)
            (activity as AppActivity).recreate();
        }

        binding.button2.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)

            savedDarkData.setRedModeState(false)
            savedDarkData.setPurpleModeState(false)
            savedDarkData.setGreenModeState(true)
            savedDarkData.setOrangeModeState(false)
            (activity as AppActivity).recreate();
        }

        binding.button3.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)

            savedDarkData.setRedModeState(false)
            savedDarkData.setPurpleModeState(true)
            savedDarkData.setGreenModeState(false)
            savedDarkData.setOrangeModeState(false)
            (activity as AppActivity).recreate();
        }

        binding.button4.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)

            savedDarkData.setRedModeState(false)
            savedDarkData.setOrangeModeState(true)
            savedDarkData.setGreenModeState(false)
            savedDarkData.setPurpleModeState(false)
            (activity as AppActivity).recreate();
        }

        binding.button5.setOnClickListener{
            savedDarkData = sharedprefs(requireContext() as AppActivity)

            savedDarkData.setRedModeState(false)
            savedDarkData.setOrangeModeState(false)
            savedDarkData.setGreenModeState(false)
            savedDarkData.setPurpleModeState(false)
            (activity as AppActivity).recreate();
        }

    }


}