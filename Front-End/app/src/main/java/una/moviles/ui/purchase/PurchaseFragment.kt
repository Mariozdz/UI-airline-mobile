package una.moviles.ui.purchase

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import una.moviles.R
import una.moviles.databinding.FragmentPurchaseBinding
import una.moviles.persistence.BD

class PurchaseFragment : Fragment() {

    private lateinit var purchaseViewModel: PurchaseViewModel
    lateinit var lista: RecyclerView
    var originalList = BD.purchase.value

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


                    view!!.findNavController().navigate(R.id.seatsFragment)

                   /* val intent = Intent(context, BookingActivity::class.java)
                    startActivity(intent)*/

                } else {

                    /*val bundle = bundleOf("pos" to (position +1))*/
                        view!!.findNavController().navigate(R.id.seatsFragment)

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

}