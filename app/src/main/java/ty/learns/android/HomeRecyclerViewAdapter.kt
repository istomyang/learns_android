package ty.learns.android

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import ty.learns.android.databinding.FragmentHomeListItemBinding

class HomeRecyclerViewAdapter : RecyclerView.Adapter<HomeRecyclerViewAdapter.CellViewHolder>() {

    private val values = listOf<Cell>(
        Cell("First", "The First Demo!", R.id.action_contentFragment_to_firstFragment)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        return CellViewHolder(
            FragmentHomeListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.descriptionView.text = item.description
        holder.rootView.setOnClickListener {
            it.findNavController().navigate(item.targetId)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class CellViewHolder(binding: FragmentHomeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.homeListItemTitle
        val descriptionView: TextView = binding.homeListItemNameDescription
        val rootView: LinearLayout = binding.homeListItemRoot
    }

    data class Cell(val title: String, val description: String, val targetId: Int)
}