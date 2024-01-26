package com.lithium.kotlin.dictionary.domain.usecases

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.lithium.kotlin.dictionary.data.api.ApiScope
import com.lithium.kotlin.dictionary.domain.api.TranslateApi
import com.lithium.kotlin.dictionary.domain.api.TranslateBody
import com.lithium.kotlin.dictionary.domain.models.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ApiScope
class TranslateEnteredWordUseCase @Inject constructor(
    private val translateApi: TranslateApi
) {

    private val translationRequestsContext = SupervisorJob() + Dispatchers.IO
    private val translate = MutableStateFlow("")

    suspend operator fun invoke(word: String): String{
        translationRequestsContext.cancelChildren()

        withContext(translationRequestsContext){
            delay(3000)

            val body = TranslateBody(q = word)
            if (isActive) {
                try{
                    translate.value = translateWord(body)
                }catch (e: Exception){
                    Log.d("request", e.toString())
                }
            }
        }

        return translate.value
    }

    private suspend fun translateWord(body: TranslateBody): String = translateApi.translate(body).translatedText
}