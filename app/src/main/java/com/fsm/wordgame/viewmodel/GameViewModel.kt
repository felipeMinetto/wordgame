package com.fsm.wordgame.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fsm.wordgame.data.Parser
import com.fsm.wordgame.model.TranslationPair
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.InputStream
import kotlin.random.Random

class GameViewModel : ViewModel() {

    var roundCount = 0
    var translationList = emptyList<TranslationPair>()

    val correctCount = MutableLiveData<Int>().apply { value = 0 }
    val missedCount = MutableLiveData<Int>().apply { value = 0 }
    val wrongCount = MutableLiveData<Int>().apply { value = 0 }
    val errorMessage = MutableLiveData<String>()
    val currentPair = MutableLiveData<TranslationPair>()
    val gameRunning = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    fun loadWords(fileIS: InputStream) {
        compositeDisposable.add(
            Parser.parseFromJsonIS(fileIS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { translationList = it },
                    onError = {
                        errorMessage.value = "Could not load data for the game! Try again?"
                    }
                ))
    }

    fun prepareNextWord() {
        if (gameRunning.value == true && roundCount < MAX_ROUNDS) {
            roundCount++
            val index = Random.nextInt(translationList.size)
            val pair = translationList[index]
            if (Random.nextInt(3) == 1) { // 1 in 3 chance of being the wrong word plus the natural chance of coincidence
                val index2 = Random.nextInt(translationList.size)
                val wrongPair = translationList[index2]
                currentPair.value = TranslationPair(pair.textEng, wrongPair.textSpa, index == index2)
            } else {
                currentPair.value = pair
            }
        } else {
            gameRunning.value = false
        }
    }

    fun checkCorrectPair() {
        currentPair.value?.let { translationPair ->
            if (translationPair.isCorrect) {
                correctCount.postValue(correctCount.value?.plus(1))
            } else {
                wrongCount.postValue(wrongCount.value?.plus(1))
            }
        }
    }

    fun checkWrongPair() {
        currentPair.value?.let { translationPair ->
            if (!translationPair.isCorrect) {
                correctCount.postValue(correctCount.value?.plus(1))
            } else {
                wrongCount.postValue(wrongCount.value?.plus(1))
            }
        }
    }

    fun missedWord() {
        currentPair.value?.let {
            missedCount.postValue(missedCount.value?.plus(1))
        }
        prepareNextWord()
    }

    fun clearCD() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun startGame() {
        gameRunning.value = true
        correctCount.value = 0
        missedCount.value = 0
        wrongCount.value = 0
        roundCount = 0
        prepareNextWord()
    }

    companion object {
        private const val MAX_ROUNDS = 30
    }
}