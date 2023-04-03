package com.chensi.editapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chensi.editapplication.data.MenuData
import com.chensi.editapplication.data.getMenu
import com.chensi.editapplication.databinding.ActivityMenuBinding

/**
 * 文件名：MenuActivity <br/>
 *
 * @author chensi
 * @since 2022/10/28 14:49
 */
class MenuActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMenuBinding

    private val mAdapter by lazy {
        MenuAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.rvMain.run {
            layoutManager = LinearLayoutManager(this@MenuActivity)
            adapter = mAdapter
        }

        mAdapter.setList(getMenu())
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val menu = adapter.getItem(position) as? MenuData
            menu?.callback?.invoke(this)
        }
    }

}