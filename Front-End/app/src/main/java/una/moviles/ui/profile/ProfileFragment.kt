package una.moviles.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import una.moviles.R
import una.moviles.RegisterActivity
import una.moviles.databinding.FragmentHomeBinding
import una.moviles.databinding.FragmentProfileBinding
import una.moviles.persistence.BD

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    var enable = false

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    override fun onResume() {
        super.onResume()

        binding.proSurnames.setText(BD.user?.surnames)
        binding.proSurnames.isEnabled = false
        binding.proAddress.setText(BD.user?.address)
        binding.proAddress.isEnabled = false
        binding.proCellphone.setText(BD.user?.cellphone)
        binding.proCellphone.isEnabled = false
        binding.proPass.setText("***********")
        binding.proPass.isEnabled = false
        binding.viewFullname.setText(BD.user?.name + BD.user?.surnames)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener{
            if (!enable)
            {
                binding.proSurnames.isEnabled = true
                binding.proAddress.isEnabled = true
                binding.proCellphone.isEnabled = true
                binding.proPass.isEnabled = true
                binding.button.text = "Save"
                enable = true

            }else{


                var surnames =  binding.proSurnames.text.toString()
                var address = binding.proAddress.text.toString()
                var cellphone = binding.proCellphone.text.toString()
                var password = binding.proPass.text.toString()

                BD.user?.surnames = surnames
                BD.user?.address = address
                BD.user?.cellphone = cellphone
                BD.user?.password = password

                binding.proSurnames.isEnabled = false
                binding.proAddress.isEnabled = false
                binding.proCellphone.isEnabled = false
                binding.proPass.isEnabled = false


                binding.button.text = "Modify"
                enable = false

            }
        }


        return binding.root

    }
}