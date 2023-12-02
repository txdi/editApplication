package com.chensi.editapplication.item.recyclerview

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chensi.editapplication.R
import com.chensi.editapplication.databinding.ItemRecyclerBinding

class ItemRecyclerAdapter : RecyclerView.Adapter<RecyclerHolder>() {
    private var data: List<Item> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(View.inflate(parent.context, R.layout.item_recycler, null))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val mBinding = ItemRecyclerBinding.bind(holder.itemView)
        val item = data[position]
        mBinding.tvFirst.text = item.id
        mBinding.tvSecond.text = item.name
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Item>) {
        data = list
        notifyDataSetChanged()
    }
}

class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


}

data class Item(val name: String, val id: String)