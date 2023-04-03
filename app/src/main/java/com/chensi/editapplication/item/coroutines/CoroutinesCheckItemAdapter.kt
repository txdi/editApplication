package com.chensi.editapplication.item.coroutines

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chensi.editapplication.R
import com.chensi.editapplication.data.CoroutinesCheckItem

class CoroutinesCheckItemAdapter :
    BaseQuickAdapter<CoroutinesCheckItem, BaseViewHolder>(R.layout.item_coroutines_check) {

    override fun convert(holder: BaseViewHolder, item: CoroutinesCheckItem) {
        holder.setText(R.id.button, item.itemName)
    }

}