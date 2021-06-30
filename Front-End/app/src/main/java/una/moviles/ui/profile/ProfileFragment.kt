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
import androidx.lifecycle.lifecycleScope
import una.moviles.MenuActivity
import una.moviles.R
import una.moviles.RegisterActivity
import una.moviles.databinding.FragmentHomeBinding
import una.moviles.databinding.FragmentProfileBinding
import una.moviles.logic.User
import una.moviles.persistence.BD

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    var enable = false

    var use: User? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel = ProfileViewModel()

        var bundle = requireActivity().intent.extras
        use = bundle?.get("user") as User

        profileViewModel.use.value = use

        profileViewModel.use.observe(viewLifecycleOwner){
            user ->

            this.use = profileViewModel.use.value

            requireActivity().intent.removeExtra("user")

            requireActivity().intent.putExtra("user",use)

            updateValues()
        }

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

                use!!.surnames = surnames
                use!!.address = address
                use!!.cellphone = cellphone
                use!!.password = password

                binding.proSurnames.isEnabled = false
                binding.proAddress.isEnabled = false
                binding.proCellphone.isEnabled = false
                binding.proPass.isEnabled = false


                binding.button.text = "Modify"
                enable = false

                profileViewModel.update(use!!)

            }
        }

        return binding.root

    }

    fun updateValues()
    {
        binding.proSurnames.setText(use?.surnames)
        binding.proSurnames.isEnabled = false
        binding.proAddress.setText(use?.address)
        binding.proAddress.isEnabled = false
        binding.proCellphone.setText(use?.cellphone)
        binding.proCellphone.isEnabled = false
        binding.proPass.setText("***********")
        binding.proPass.isEnabled = false
        binding.viewFullname.setText(use?.name + use?.surnames)
    }


    override fun onResume() {
        super.onResume()

        updateValues()
        profileViewModel.open(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        profileViewModel.close()
    }
}