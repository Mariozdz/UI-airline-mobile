package una.moviles.ui.seats

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import una.moviles.MenuActivity
import una.moviles.R
import una.moviles.databinding.FragmentProfileBinding
import una.moviles.databinding.FragmentSeatsBinding
import una.moviles.logic.PlaneType
import una.moviles.logic.Purchase
import una.moviles.ui.home.HomeViewModel
import kotlin.math.log


class SeatsFragment : Fragment() {

    private lateinit var seatsViewModel: SeatsViewModel

    private var _binding: FragmentSeatsBinding? = null
    private val binding: FragmentSeatsBinding
        get() = _binding!!
    private var type: PlaneType? = null

    val seats = mutableListOf<String>()

    val ocupied : MutableLiveData<List<Pair<Int,Int>>>

    init {
        ocupied = MutableLiveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSeatsBinding.inflate(inflater, container, false)

        seatsViewModel = SeatsViewModel()

        if (binding.layoutAsi.childCount > 0 )
        {
            binding.layoutAsi.removeAllViews()
        }

       var bund =  requireArguments().getSerializable("purchase") as Purchase

        //  colum i row j

        /*var str = binding.tickets.text.toString()*/

        seatsViewModel.seats.observe(viewLifecycleOwner){

            ocupied.value = seatsViewModel.seats.value
            Log.d("Asientos", "Se llamaron a los ocupados")
        }

        seatsViewModel.flag.observe(viewLifecycleOwner)
        {
            if (seatsViewModel.flag.value == true) {
                seatsViewModel.solicitarDimension(bund.flightid)
            }
        }

        seatsViewModel.dimension.observe(viewLifecycleOwner){
            user ->
            if (seatsViewModel.flag.value == true) {
                cargarAsientos(user.NumberRow, user.numbercolums, bund.tickets)
            }
        }


        binding.bkSubbmit.setOnClickListener{

            Log.d("PruebaS", seats[0] + " " + seats[1])
            view?.findNavController()?.navigate(R.id.nav_purchase)
        }

        return binding.root
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cargarAsientos(filas: Int, columnas: Int, cantid : Int) {

        Log.d("is", "entraaaaaaa")
        if (ocupied.value != null)
        {
            for(i in 0 until ocupied.value?.size!!) {
                Log.d("ocupied", ocupied.value?.get(i)?.first.toString() + ocupied.value?.get(i)?.second.toString());
            }
        }
        var cant: TextView = binding.tvCantidad

        //var bundle = intent.extras
        var cantidadA: String = cantid.toString()//bundle!!.getInt("cantidadAsientos").toString()

        cant.text = ""+cantidadA
        for (i in 1..columnas) {
            val layout: LinearLayout = LinearLayout(context)
            layout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layout.setHorizontalGravity(Gravity.CENTER)
            layout.orientation = LinearLayout.VERTICAL
            for (j in 1..filas) {
                val btn: Button = Button(context)


                btn.layoutParams = LinearLayout.LayoutParams(200, 110)
                btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#087E8B"))
                btn.tag = j.toString() + i.toString()

                btn.text = j.toString()+ ' ' +i.toString()

                layout.addView(btn)

                if (ocupied.value?.contains(Pair(i,j)) == true)
                //i colum j row
                {
                    btn.isEnabled = false
                    btn.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                }else
                {
                    btn.setOnClickListener {
                        if (btn.backgroundTintList!!.defaultColor == -391168) {
                            btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#087E8B"))
                            seats.remove(btn.tag.toString())
                        } else if(seats.count() < cantidadA.toInt()) {
                            seats.add(btn.tag.toString());
                            btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FA0800"))
                        }
                    }
                }
            }
            binding.layoutAsi.addView(layout)
        }
    }


    private fun setSeats( lista : List<Pair<Int,Int>>){



    }

    override fun onResume() {
        super.onResume()


        seatsViewModel.flag.value = false
        if (binding.layoutAsi.childCount > 0 )
        {
            binding.layoutAsi.removeAllViews()
        }

        var bund =  requireArguments().getSerializable("purchase") as Purchase

        seatsViewModel.open(lifecycleScope,bund.flightid)
        seatsViewModel.openPurchase(lifecycleScope,bund.flightid)

        type = seatsViewModel.dimension.value
    }

    override fun onPause() {
        super.onPause()

        seatsViewModel.close()
        seatsViewModel.closePurchase()
    }


}