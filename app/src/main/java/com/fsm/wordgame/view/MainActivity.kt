package com.fsm.wordgame.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fsm.wordgame.R
import com.fsm.wordgame.databinding.ActivityMainBinding
import com.fsm.wordgame.viewmodel.GameViewModel

class MainActivity : AppCompatActivity(), Animator.AnimatorListener {

    private lateinit var animation: ViewPropertyAnimator
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: GameViewModel

    private var animationCanceled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        viewModel.loadWords(assets.open("words_v2.json"))
        binding.viewModel = viewModel

        viewModel.currentPair.observe(this, Observer {
            animateText(3000)
        })

        viewModel.correctCount.observe(this, Observer {
            if (it > 0) animateFeedback(binding.imgCorrect)
        })

        viewModel.wrongCount.observe(this, Observer {
            if (it > 0) animateFeedback(binding.imgWrong)
        })

        viewModel.gameRunning.observe(this, Observer {
            if (it){
                binding.translatedText.visibility = VISIBLE
                binding.txtGameOver.visibility = GONE
            } else {
                binding.translatedText.visibility = GONE
                binding.txtGameOver.visibility = VISIBLE

            }
        })

        viewModel.errorMessage.observe(this, Observer {
            AlertDialog.Builder(this)
                    .setMessage(it)
                    .setTitle(R.string.error)
                    .setPositiveButton(android.R.string.yes) { _: DialogInterface, _: Int ->
                        viewModel.loadWords(assets.open("words_v2.json"))
                    }
                    .setNegativeButton(android.R.string.no, null)
                .create().show()
        })

        binding.txtGameOver.visibility = GONE

        binding.btnCorrect.setOnClickListener {
            animation.cancel()
            viewModel.checkCorrectPair()
        }

        binding.btnWrong.setOnClickListener {
            animation.cancel()
            viewModel.checkWrongPair()
        }

        binding.btnStart.setOnClickListener {
            viewModel.startGame()
            binding.btnStart.visibility = GONE
        }
    }

    private fun animateText(duration: Long) {
        animation = binding.translatedText
                .animate()
                .translationY(binding.originalText.y)
                .setListener(this)
                .setDuration(duration)
        animation.start()
    }

    private fun animateFeedback(view: View) {
        val fadein = ObjectAnimator.ofFloat(view, View.ALPHA, 1f)
        fadein.duration = 300

        val fadeout = ObjectAnimator.ofFloat(view, View.ALPHA, 0f)
        fadeout.duration = 300

        val set = AnimatorSet()
        set.play(fadeout)
                .after(fadein)
                .after(500)

        set.doOnStart {
            view.visibility = VISIBLE
            binding.btnCorrect.isEnabled = false
            binding.btnWrong.isEnabled = false
        }
        set.doOnEnd {
            view.visibility = GONE
            viewModel.prepareNextWord()
            binding.btnCorrect.isEnabled = true
            binding.btnWrong.isEnabled = true
        }
        set.start()
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
            binding.translatedText.clearAnimation()
            binding.translatedText.visibility = GONE
            binding.translatedText.translationY = 0f
            viewModel.missedWord()
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        animationCanceled = true
        binding.translatedText.clearAnimation()
        binding.translatedText.visibility = GONE
        binding.translatedText.translationY = 0f
    }

    override fun onAnimationStart(animation: Animator?) {
        animationCanceled = false
        binding.translatedText.visibility = VISIBLE
    }
    //------------ Animator Callbacks ------------//
}
