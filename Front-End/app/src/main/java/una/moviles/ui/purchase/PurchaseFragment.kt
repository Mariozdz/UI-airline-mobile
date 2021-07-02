package una.moviles.ui.purchase

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import una.moviles.R
import una.moviles.databinding.FragmentPurchaseBinding
import una.moviles.logic.Flight
import una.moviles.logic.Purchase
import una.moviles.logic.User
import una.moviles.persistence.BD

class PurchaseFragment : Fragment() {

    private lateinit var purchaseViewModel: PurchaseViewModel
    lateinit var lista: RecyclerView
    lateinit var originalList : List<Purchase>

    var position: Int = 0

    private var _binding: FragmentPurchaseBinding? = null
    private val binding: FragmentPurchaseBinding
        get() = _binding!!


    private fun initRecyclerView(): PurchaseAdapter {
        val adapter = PurchaseAdapter()
        binding.purchaseRecycle.adapter = adapter

        return adapter
    }

    private fun OnInitViewmodel(adapter: PurchaseAdapter) {

        purchaseViewModel.purchase.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)


        purchaseViewModel = PurchaseViewModel()

        lista = binding.purchaseRecycle
        lista.setHasFixedSize(true)



        binding.searchBox.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

               /* BD.purchase.value = originalList*/
               // purchaseViewModel.purchase.value = originalList

               /* purchaseViewModel.purchase.value = purchaseViewModel.backup.value

                if (newText != null) {
                    purchaseViewModel.purchase.value = purchaseViewModel.purchase.value!!.filter {  it.tickets.toString().toLowerCase().contains(newText.toLowerCase()) }
                }*/

                return false
            }
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition

                /*var listaMove : List<Application> = BD.items.value!!
                Collections.swap(listaMove, fromPosition, toPosition)
                BD.items.value = listaMove*/

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                position = viewHolder.adapterPosition
                var quien: String = ""

                if (direction == ItemTouchHelper.LEFT) {

                    var pur = purchaseViewModel.purchase.value!![viewHolder.adapterPosition]

                    if (pur.isselected == true)
                    {
                        Toast.makeText(context,"Seats are selected",Toast.LENGTH_LONG).show()
                        view!!.findNavController().navigate(R.id.nav_purchase)

                    }else {

                        val bundle = bundleOf("purchase" to pur)
                        view!!.findNavController().navigate(R.id.seatsFragment, bundle)
                    }
                } else {

                    var pur = purchaseViewModel.purchase.value!![viewHolder.adapterPosition]

                    if (pur.isselected == true)
                    {
                        Toast.makeText(context,"Seats are selected",Toast.LENGTH_LONG).show()
                        view!!.findNavController().navigate(R.id.nav_purchase)

                    }else {

                        val bundle = bundleOf("purchase" to pur)
                        view!!.findNavController().navigate(R.id.seatsFragment, bundle)
                    } // Puede joder



                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                RecyclerViewSwipeDecorator.Builder(activity , c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(R.color.red)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit_24)
                    .addSwipeRightBackgroundColor(R.color.green)
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }


        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(lista)



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

    override fun onResume() {
        super.onResume()


        purchaseViewModel.viewModelScope
        var bundle = requireActivity().intent.extras

        var us = bundle?.get("user") as User

        purchaseViewModel.open(lifecycleScope,us.id)

    }

    override fun onPause() {
        super.onPause()
        purchaseViewModel.close()
    }

}