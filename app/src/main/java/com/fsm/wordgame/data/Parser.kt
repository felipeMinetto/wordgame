package com.fsm.wordgame.data

import com.fsm.wordgame.model.TranslationPair
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InputStream

object Parser {
    private val gson = GsonBuilder().create()

    fun parseFromJsonIS(jsonIS: InputStream): Single<MutableList<TranslationPair>> {
        return Single.fromCallable {
            val jsonText = String(jsonIS.readBytes())
            val type = emptyList<TranslationPair>().toMutableList().javaClass
            return@fromCallable gson.fromJson(jsonText, type)
        }.subscribeOn(Schedulers.io())
    }
}