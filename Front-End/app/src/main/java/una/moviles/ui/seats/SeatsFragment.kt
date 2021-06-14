package una.moviles.ui.seats

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import androidx.navigation.findNavController
import una.moviles.R
import una.moviles.databinding.FragmentProfileBinding
import una.moviles.databinding.FragmentSeatsBinding


class SeatsFragment : Fragment() {

    private var _binding: FragmentSeatsBinding? = null
    private val binding: FragmentSeatsBinding
        get() = _binding!!

    val seats = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSeatsBinding.inflate(inflater, container, false)


        cargarAsientos(15, 6)

        binding.bkSubbmit.setOnClickListener{
            view?.findNavController()?.navigate(R.id.nav_purchase)
        }

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cargarAsientos(filas: Int, columnas: Int) {

        var cant: TextView = binding.tvCantidad
        //var bundle = intent.extras
        var cantidadA: String = "7"//bundle!!.getInt("cantidadAsientos").toString()

        cant.text = ""+cantidadA
        for (i in 1..filas) {
            val layout: LinearLayout = LinearLayout(context)
            layout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layout.setHorizontalGravity(Gravity.CENTER)
            layout.orientation = LinearLayout.HORIZONTAL
            for (j in 1..columnas) {
                val btn: Button = Button(context)
                btn.layoutParams = LinearLayout.LayoutParams(110, 110)
                btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#087E8B"))
                btn.tag = i.toString() + j.toString()
                layout.addView(btn)
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
            binding.layoutAsi.addView(layout)
        }
    }
}