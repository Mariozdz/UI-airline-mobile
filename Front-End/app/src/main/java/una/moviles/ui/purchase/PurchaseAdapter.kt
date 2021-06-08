package una.moviles.ui.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import una.moviles.databinding.FlightListBinding
import una.moviles.databinding.PurchaseListBinding
import una.moviles.logic.Purchase
import una.moviles.ui.home.HomeAdapter

class PurchaseAdapter : RecyclerView.Adapter<PurchaseAdapter.ViewHolder>()
{
    public var items = listOf<Purchase>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    public class ViewHolder(private val binding: PurchaseListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Purchase) {
            binding.purchases = item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PurchaseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

}