package com.chensi.editapplication

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chensi.editapplication.data.MenuData

/**
 * 文件名：MenuAdapter <br/>
 *
 * @author chensi
 * @since 2022/10/28 15:49
 */
class MenuAdapter : BaseQuickAdapter<MenuData, BaseViewHolder>(R.layout.item_menu) {
    override fun convert(holder: BaseViewHolder, item: MenuData) {
        holder.setText(R.id.tvName, item.name)
    }
}