package com.chensi.editapplication.item.custom_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chensi.editapplication.databinding.ActivityCustomShowBinding

class CustomShowActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityCustomShowBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}