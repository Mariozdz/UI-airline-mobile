package una.moviles.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import una.moviles.databinding.FragmentPurchaseBinding
import una.moviles.persistence.BD

class PurchaseFragment : Fragment() {

    private lateinit var purchaseViewModel: PurchaseViewModel
    lateinit var lista: RecyclerView
    var originalList = BD.purchase.value

    private var _binding: FragmentPurchaseBinding? = null
    private val binding: FragmentPurchaseBinding
        get() = _binding!!


    private fun initRecyclerView(): PurchaseAdapter {
        val adapter = PurchaseAdapter()
        binding.purchaseRecycle.adapter = adapter

        return adapter
    }

    private fun OnInitViewmodel(adapter: PurchaseAdapter) {

        BD.purchase.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)


        lista = binding.purchaseRecycle
        lista.setHasFixedSize(true)


        binding.searchBox.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                BD.purchase.value = originalList
                BD.filterPurchase(newText!!)
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