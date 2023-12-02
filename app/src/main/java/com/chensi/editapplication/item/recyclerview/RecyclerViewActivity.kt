package com.chensi.editapplication.item.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chensi.editapplication.databinding.ActivityRecyclerviewBinding

class RecyclerViewActivity : AppCompatActivity() {

    private val mBinding: ActivityRecyclerviewBinding by lazy {
        ActivityRecyclerviewBinding.inflate(layoutInflater)
    }

    private val mAdapter: ItemRecyclerAdapter by lazy {
        ItemRecyclerAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.rvMain.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //添加 divider
        mBinding.rvMain.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST
            )
        )


        mBinding.rvMain.adapter = mAdapter


    }

    override fun onResume() {
        super.onResume()
        mAdapter.setData(createData())
    }

    private fun createData(): List<Item> {
        return arrayListOf<Item>().apply {
            for (i in 0..30) {
                this.add(Item("position $i", "${i.hashCode()}"))
            }
        }
    }

}