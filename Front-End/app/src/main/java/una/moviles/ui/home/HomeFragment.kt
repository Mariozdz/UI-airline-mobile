package una.moviles.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import una.moviles.R
import una.moviles.databinding.FragmentHomeBinding
import una.moviles.persistence.BD

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    lateinit var lista: RecyclerView
    var originalList = BD.flight.value

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!


    private fun initRecyclerView(): HomeAdapter {
        val adapter = HomeAdapter()
        binding.flightRecycle.adapter = adapter

        return adapter
    }

    private fun OnInitViewmodel(adapter: HomeAdapter) {

        BD.flight.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        lista = binding.flightRecycle
        lista.setHasFixedSize(true)


        binding.searchBox.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                BD.flight.value = originalList
                BD.filterFlight(newText!!)
                return false
            }
        })


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initRecyclerView().let { adapter ->
            OnInitViewmodel(adapter)
        }

    }
}