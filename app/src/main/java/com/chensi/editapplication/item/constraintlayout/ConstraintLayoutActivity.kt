package com.chensi.editapplication.item.constraintlayout

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.chensi.editapplication.R
import com.chensi.editapplication.databinding.ActivityConstraintlayoutMainBinding

/**
 * 文件名：ConstraintLayout <br/>
 * 描述：
 *
 * @author chensi
 * @since 2022/12/14 11:00
 */
class ConstraintLayoutActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityConstraintlayoutMainBinding.inflate(layoutInflater)
    }

    private var isConstraintOne = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val constrainSetOne = ConstraintSet()
        val constrainSetTwo = ConstraintSet()
        constrainSetOne.clone(mBinding.consMain)
        constrainSetTwo.clone(this, R.layout.constraintlayout_second)

        mBinding.loadImage.setOnClickListener {
            isConstraintOne = if (isConstraintOne) {
                constrainSetTwo.applyTo(mBinding.consMain)
//                TransitionManager.beginDelayedTransition(mBinding.consMain)
                false
            } else {
                constrainSetOne.applyTo(mBinding.consMain)
                true
            }

        }
    }


}