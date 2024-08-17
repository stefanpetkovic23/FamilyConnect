package com.example.familyconnectv2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.models.ShoppingGroup
import com.example.familyconnectv2.models.ShoppingItem
import com.example.familyconnectv2.ui.shopping.ShoppingViewModel

class ShoppingListAdapter(private var shoppingItems: List<Pair<String, ShoppingItem>>, private val viewModel: ShoppingViewModel) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        private val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        private val itemQuantityTextView: TextView = itemView.findViewById(R.id.itemQuantityTextView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(groupName: String, shoppingItem: ShoppingItem) {
            groupNameTextView.text = groupName
            itemNameTextView.text = shoppingItem.name
            itemQuantityTextView.text = shoppingItem.quantity.toString()
            checkBox.isChecked = shoppingItem.isCompleted
            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    viewModel.updateShoppingItem(shoppingItem._id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shoppingitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (groupName, shoppingItem) = shoppingItems[position]
        holder.bind(groupName, shoppingItem)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    fun updateData(newData: List<Pair<String, ShoppingItem>>) {
        shoppingItems = newData
        notifyDataSetChanged()
    }
}