package com.fsm.wordgame.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fsm.wordgame.model.TranslationPair
import com.jraska.livedata.test
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: GameViewModel

    @Before
    fun setup() {
        viewModel = GameViewModel()
    }

    @Test
    fun testStartGame() {
        viewModel.translationList = listOf(TranslationPair("Pair0", "par0", true))

        viewModel.wrongCount.test().assertHasValue().assertValue(0)
        viewModel.missedCount.test().assertHasValue().assertValue(0)
        viewModel.correctCount.test().assertHasValue().assertValue(0)
        viewModel.gameRunning.test().assertValue(false)
        assertEquals(viewModel.roundCount, 0)

        viewModel.startGame()

        viewModel.wrongCount.test().assertHasValue().assertValue(0)
        viewModel.missedCount.test().assertHasValue().assertValue(0)
        viewModel.correctCount.test().assertHasValue().assertValue(0)
        viewModel.gameRunning.test().assertValue(true)
        assertEquals(viewModel.roundCount, 1)
    }

    @Test
    fun checkCorrectPair_forGuessingWrong() {
        viewModel.translationList = listOf(TranslationPair("Pair1", "Par 1", true))

        viewModel.startGame()
        viewModel.checkWrongPair()

        viewModel.currentPair.test().assertHasValue()
        viewModel.wrongCount.test().assertHasValue().assertValue(1)
        viewModel.correctCount.test().assertHasValue().assertValue(0)
        viewModel.missedCount.test().assertHasValue().assertValue(0)
        viewModel.gameRunning.test().assertValue(true)
        assertEquals(viewModel.roundCount, 1)
    }

    @Test
    fun checkCorrectPair_forGuessingCorrect() {
        viewModel.translationList = listOf(TranslationPair("Pair2", "Par2", true))

        viewModel.startGame()
        viewModel.checkCorrectPair()

        viewModel.currentPair.test().assertHasValue()
        viewModel.wrongCount.test().assertHasValue().assertValue(0)
        viewModel.correctCount.test().assertHasValue().assertValue(1)
        viewModel.missedCount.test().assertHasValue().assertValue(0)
        viewModel.gameRunning.test().assertValue(true)
        assertEquals(viewModel.roundCount, 1)
    }

    @Test
    fun checkWrongPair_forGuessingWrong() {
        viewModel.translationList = listOf(TranslationPair("Pair3", "Par3", false))

        viewModel.startGame()
        viewModel.checkWrongPair()

        println("W-W")
        println(viewModel.currentPair.value!!.isCorrect)
        println(viewModel.currentPair.value!!.textEng)
        println(viewModel.currentPair.value!!.textSpa)
        viewModel.currentPair.test().assertHasValue()
        viewModel.wrongCount.test().assertHasValue().assertValue(0)
        viewModel.correctCount.test().assertHasValue().assertValue(1)
        viewModel.missedCount.test().assertHasValue().assertValue(0)
        viewModel.gameRunning.test().assertValue(true)
        assertEquals(viewModel.roundCount, 1)
    }

    @Test
    fun checkWrongPair_forGuessingCorrect() {
        viewModel.translationList = listOf(TranslationPair("Pair4", "Par4", false))

        viewModel.startGame()
        viewModel.checkCorrectPair()

        println("W-C")
        println(viewModel.currentPair.value!!.isCorrect)
        println(viewModel.currentPair.value!!.textEng)
        println(viewModel.currentPair.value!!.textSpa)
        println(viewModel.roundCount.toString())
        viewModel.currentPair.test().assertHasValue()
        viewModel.wrongCount.test().assertHasValue().assertValue(1)
        viewModel.correctCount.test().assertHasValue().assertValue(0)
        viewModel.missedCount.test().assertHasValue().assertValue(0)
        viewModel.gameRunning.test().assertValue(true)
        assertEquals(viewModel.roundCount, 1)
    }
}