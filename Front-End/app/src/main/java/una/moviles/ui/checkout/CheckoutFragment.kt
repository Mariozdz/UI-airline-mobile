package una.moviles.ui.checkout

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import una.moviles.R
import una.moviles.RegisterActivity
import una.moviles.databinding.FragmentCheckoutBinding
import una.moviles.databinding.FragmentHomeBinding
import una.moviles.logic.Flight
import una.moviles.logic.User
import una.moviles.persistence.BD
import una.moviles.ui.home.HomeAdapter
import una.moviles.ui.home.HomeViewModel

class CheckoutFragment : Fragment() {

    private lateinit var checkViewModel: CheckViewModel

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

        checkViewModel = CheckViewModel()


        binding.detSubmmit.setOnClickListener {

            var bund =  requireArguments().getSerializable("flight") as Flight

            var str = binding.tickets.text.toString()

            if ( str == ""  || str.toInt() > bund.disponibles )
            {
                Log.d("Vuelos", bund.disponibles.toString() + " p: " + str)
                Toast.makeText(context,"Cantidad invalida o fuera del limite", Toast.LENGTH_LONG).show()
            }
            else {

                var bundle = requireActivity().intent.extras
                var use = bundle?.get("user") as User

                checkViewModel.purchase(use.id, binding.tickets.text.toString().toInt(), bund.id )


                Toast.makeText(context,"Reservación confirmada", Toast.LENGTH_LONG).show()
                view?.findNavController()?.navigate(R.id.nav_home)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        var bund =  requireArguments().getSerializable("flight") as Flight

            if (bund != null) {

                binding.editform = bund
            }

        checkViewModel.open(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        checkViewModel.close()
    }
}