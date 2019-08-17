package com.fsm.wordgame

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fsm.wordgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val animator by lazy {
        ObjectAnimator.ofFloat(
                binding.text,
                View.TRANSLATION_Y,
                binding.originalText.y
        ).apply {
            this.duration = duration
            startDelay = 0
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    binding.text.visibility = GONE
                    binding.text.translationY = 0f
                }

                override fun onAnimationCancel(animation: Animator?) {
                    binding.text.visibility = GONE
                    binding.text.translationY = 0f
                }

                override fun onAnimationStart(animation: Animator?) {
                    binding.text.visibility = VISIBLE
                }

            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnCorrect.setOnClickListener {
            animateText(3000)
        }
        binding.btnWrong.setOnClickListener {
            animator.cancel()
        }
    }

    fun animateText(duration: Long) {
        animator.setDuration(duration).start()
    }
}
