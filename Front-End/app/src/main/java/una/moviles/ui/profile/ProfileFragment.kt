package una.moviles.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    override fun onResume() {
        super.onResume()

        binding.proSurnames.setText(BD.user?.surnames)
        binding.proAddress.setText(BD.user?.address)
        binding.proCellphone.setText(BD.user?.cellphone)
        binding.proPass.setText("***********")
        binding.viewFullname.setText(BD.user?.name + BD.user?.surnames)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        return binding.root

    }
}