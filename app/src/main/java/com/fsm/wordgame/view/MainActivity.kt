package com.fsm.wordgame.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fsm.wordgame.R
import com.fsm.wordgame.databinding.ActivityMainBinding
import com.fsm.wordgame.viewmodel.GameViewModel

class MainActivity : AppCompatActivity(), Animator.AnimatorListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: GameViewModel

    private var animationCanceled = false

    private val animator by lazy {
        ObjectAnimator.ofFloat(
                binding.text,
                View.TRANSLATION_Y,
                binding.originalText.y
        ).apply {
            addListener(this@MainActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        viewModel.loadWords(assets.open("words_v2.json"))
        binding.viewModel = viewModel

        viewModel.errorMessage.observe(this, Observer {
            AlertDialog.Builder(this)
                    .setMessage(it)
                    .setTitle(R.string.error)
                    .setPositiveButton(android.R.string.yes) { _: DialogInterface, _: Int ->
                        viewModel.loadWords(assets.open("words_v2.json"))
                    }
                    .setNegativeButton(android.R.string.no, null)
        })

        binding.btnCorrect.setOnClickListener {
            viewModel.isCorect()
            animator.cancel()
            animateText(3000)
        }

        binding.btnWrong.setOnClickListener {
            viewModel.isWrong()
            animator.cancel()
            animateText(3000)
        }

        binding.btnStart.setOnClickListener {
            animateText(3000)
        }
    }

    fun animateText(duration: Long) {
        animator.setDuration(duration).start()
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearCD()
    }

    //------------ Animator Callbacks ------------//
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        if (!animationCanceled) {
            binding.text.clearAnimation()
            binding.text.visibility = GONE
            binding.text.translationY = 0f
            viewModel.missedWord()
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        animationCanceled = true
        binding.text.clearAnimation()
        binding.text.visibility = GONE
        binding.text.translationY = 0f
    }

    override fun onAnimationStart(animation: Animator?) {
        animationCanceled = false
        binding.text.visibility = VISIBLE
    }
    //------------ Animator Callbacks ------------//
}
