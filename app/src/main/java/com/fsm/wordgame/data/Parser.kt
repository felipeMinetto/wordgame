package com.fsm.wordgame.data

import com.fsm.wordgame.model.TranslationPair
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InputStream


object Parser {

    fun parseFromJsonIS(jsonIS: InputStream): Single<MutableList<TranslationPair>> {
        val moshi = Moshi.Builder().build()
        return Single.fromCallable {
            val jsonText = String(jsonIS.readBytes())
            val type = Types.newParameterizedType(MutableList::class.java, TranslationPair::class.java)
            val adapter = moshi.adapter<MutableList<TranslationPair>>(type)
            return@fromCallable adapter.fromJson(jsonText) ?: emptyList<TranslationPair>().toMutableList()
        }.subscribeOn(Schedulers.io())
    }
}