package com.fsm.wordgame.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranslationPair(
        @Json(name = "text_eng")val textEng: String,
        @Json(name = "text_spa")val textSpa: String,
        @Transient var isCorrect: Boolean = true
)