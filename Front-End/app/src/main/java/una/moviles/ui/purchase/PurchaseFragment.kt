package una.moviles.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import una.moviles.R

class PurchaseFragment : Fragment() {

    private lateinit var purchaseViewModel: PurchaseViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        purchaseViewModel =
                ViewModelProvider(this).get(PurchaseViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_purchase, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        purchaseViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}