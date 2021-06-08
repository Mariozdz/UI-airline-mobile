package una.moviles.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import una.moviles.databinding.FlightListBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>()
{

    public var items = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    public class ViewHolder(private val binding: FlightListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            //binding.application = item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FlightListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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