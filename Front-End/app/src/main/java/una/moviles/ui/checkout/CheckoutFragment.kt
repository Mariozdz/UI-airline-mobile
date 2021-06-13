package una.moviles.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import una.moviles.R
import una.moviles.RegisterActivity
import una.moviles.databinding.FragmentCheckoutBinding
import una.moviles.databinding.FragmentHomeBinding
import una.moviles.persistence.BD
import una.moviles.ui.home.HomeAdapter
import una.moviles.ui.home.HomeViewModel

class CheckoutFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    lateinit var lista: RecyclerView
    var originalList = BD.flight.value

    private var _binding: FragmentCheckoutBinding? = null
    private val binding: FragmentCheckoutBinding
        get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)



        binding.detSubmmit.setOnClickListener {

            view?.findNavController()?.navigate(R.id.nav_home)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()


    }


}