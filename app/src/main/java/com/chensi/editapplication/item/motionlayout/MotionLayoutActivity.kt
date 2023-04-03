package com.chensi.editapplication.item.motionlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chensi.editapplication.databinding.ActivityMotionlayoutBinding
import com.chensi.editapplication.databinding.ActivityMotionlayoutOriginBinding

class MotionLayoutActivity : AppCompatActivity() {

    private val mBinding by lazy {
//        ActivityMotionlayoutBinding.inflate(layoutInflater)
        ActivityMotionlayoutOriginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }


}