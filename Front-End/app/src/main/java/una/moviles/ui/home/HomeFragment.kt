package una.moviles.ui.home

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import una.moviles.LoginViewModel
import una.moviles.R
import una.moviles.databinding.FragmentHomeBinding
import una.moviles.logic.Flight
import una.moviles.logic.User
import una.moviles.persistence.BD
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    var position: Int = 0
    var discount: Boolean = false
    lateinit var lista: RecyclerView
    private var originalList = ArrayList<Flight>()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!


    private fun initRecyclerView(): HomeAdapter {
        val adapter = HomeAdapter()
        binding.flightRecycle.adapter = adapter

        return adapter
    }

    private fun OnInitViewmodel(adapter: HomeAdapter) {

        homeViewModel.flights.observe(viewLifecycleOwner) { items ->
            adapter.items = items
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = HomeViewModel()

        lista = binding.flightRecycle
        lista.setHasFixedSize(true)


        binding.filtrado.setOnClickListener{

            homeViewModel.flights.value = homeViewModel.backup.value

            filterFlifhts(binding.spinner2.selectedItem.toString() , binding.spinner3.selectedItem.toString(), binding.spinner4.selectedItem.toString())

        }

       /* var bundle = requireActivity().intent.extras

        if (bundle != null) {
            var us = bundle.get("user") as User
            Log.d("user",us.cellphone)
        }*/

        /*binding.searchBox.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                BD.flight.value = originalList
                BD.filterFlight(newText!!)
                return false
            }
        })*/



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


                    val bundle = bundleOf("flight" to homeViewModel.flights.value!![viewHolder.adapterPosition] )
                    view!!.findNavController().navigate(R.id.nav_check,bundle)

                } else {

                    /*val bundle = bundleOf("pos" to (position +1))*/
                    view!!.findNavController().navigate(R.id.nav_home)

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


        binding.discountSubmmit.setText("Click on me to see discounts")

        binding.discountSubmmit.setOnClickListener{


            if(!discount) {
                homeViewModel.flights.value = homeViewModel.backup.value
                homeViewModel.flights.value = homeViewModel.flights.value?.filter { it.discount > 0 }
                discount = true;

                binding.discountSubmmit.setText("Click on me to see all")
            }
            else
            {
                homeViewModel.flights.value = homeViewModel.backup.value
                binding.discountSubmmit.setText("Click on me to see discounts")
                discount = false
            }


        }

        return binding.root
    }

    private fun filterFlifhts(p1: String, p2: String, p3: String) {

        Log.d("filtro",p1+" "+p2 + " " + p3)

        var f1 = p1
        var f2 = p2
        var f3 = p3

        var f4 = 0

        if (p3 == "Ida")
        {
            f4 = 0
        }else
            f4 = 1

        if(p1 == "Origen")
        {
            f1 = ""
        }

        if(p2 == "Destino")
        {
            f2 = ""
        }

        if(p3 == "Otros")
        {
            f3 = ""
        }

        if (f1 == "" && f2 == "" && f3 =="")
        {

        }else {
            homeViewModel.flights.value = homeViewModel.flights.value?.filter { f -> (f.origen.contains(p1) || f1 == "") && (f.destino.contains(p2) || f2 == "") && (f.isreturned == f4 || f3 == "") }
        }
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

        homeViewModel.open(lifecycleScope)
        homeViewModel.openPurchase(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.close()
        homeViewModel.closePurchase()
    }
}