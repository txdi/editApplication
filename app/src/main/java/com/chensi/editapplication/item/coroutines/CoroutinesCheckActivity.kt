package com.chensi.editapplication.item.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chensi.editapplication.R
import com.chensi.editapplication.data.CoroutinesCheckItem
import com.chensi.editapplication.data.getCheckItems
import com.chensi.editapplication.databinding.ActivityCoroutinesCheckBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CoroutinesCheckActivity : AppCompatActivity() {

    companion object {
        val LOG_TAG = "Coroutines_Log"
    }

    private val mAdapter by lazy {
        CoroutinesCheckItemAdapter()
    }

    private val mBinding by lazy {
        ActivityCoroutinesCheckBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initView()
    }

    private fun initView() {
        mBinding.rvMain.adapter = mAdapter
        mBinding.rvMain.layoutManager = LinearLayoutManager(this)
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as? CoroutinesCheckItem
            item?.action?.invoke()
        }
        mAdapter.setList(getCheckItems())
    }
}