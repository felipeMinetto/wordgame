package com.fsm.wordgame.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fsm.wordgame.data.Parser
import com.fsm.wordgame.model.TranslationPair
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.InputStream

class GameViewModel : ViewModel() {

    val correctCount = MutableLiveData<Int>().apply { value = 0 }
    val missedCount = MutableLiveData<Int>().apply { value = 0 }
    val wrongCount = MutableLiveData<Int>().apply { value = 0 }
    val tranlationList = MutableLiveData<List<TranslationPair>>().apply { value = emptyList() }
    val errorMessage = MutableLiveData<String>().apply { value = "" }

    private val compositeDisposable = CompositeDisposable()

    fun loadWords(fileIS: InputStream) {
        compositeDisposable.add(
                Parser.parseFromJsonIS(fileIS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onSuccess = { tranlationList.value = it },
                                onError = {
                                    Log.e("error - loadWords", it.localizedMessage)
                                    errorMessage.value = "Could not load data for the game! Try again?"
                                }
                        ))
    }

    fun clearCD() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun isCorect() {
        correctCount.postValue(correctCount.value?.plus(1))
    }

    fun isWrong() {
        wrongCount.postValue(wrongCount.value?.plus(1))
    }

    fun missedWord() {
        missedCount.postValue(missedCount.value?.plus(1))
    }
}