package com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.lithium.kotlin.dictionary.common.ui.LightGray300

import com.lithium.kotlin.dictionary.utils.WindowUtil
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun EducationalCard(
    uiState: EducationalCardUiState,
    cardTranslationX: Animatable<Float, AnimationVector1D>,
    onAcceptWord: () -> Unit = {},
    nextWord: () -> Unit = {}
) {

    val wordIdx = remember {
        mutableIntStateOf(0)
    }

    val coroutineScope = rememberCoroutineScope()

    val launchAnimation = remember {
        Animatable(0f)
    }

    val decay = rememberSplineBasedDecay<Float>()

    val cardDraggableState = rememberDraggableState(
        onDelta = { dragAmount ->
            coroutineScope.launch {
                cardTranslationX.snapTo(cardTranslationX.value + dragAmount)
            }
        }
    )

    val onSwipeCompeted = {
        wordIdx.intValue += 1
    }


    LaunchedEffect(key1 = wordIdx.intValue) {
        Log.d("myTag", "launch")
        launchAnimation.animateTo(
            0f,
            animationSpec = tween(durationMillis = 240)
        )
        cardTranslationX.snapTo(0f)
        if (wordIdx.intValue != 0) nextWord()
        launchAnimation.animateTo(
            1f,
            animationSpec = tween(durationMillis = 300)
        )

    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = LightGray300
        ),
        modifier = Modifier
            .graphicsLayer {
                this.translationX = cardTranslationX.value
                val scale = lerp(
                    1f.dp,
                    0.8f.dp,
                    abs(cardTranslationX.value) / this.size.width
                )
                this.scaleX = scale.value * launchAnimation.value
                this.scaleY = scale.value * launchAnimation.value
            }
            .fillMaxHeight(0.70f)
            .fillMaxWidth(0.72f)
            .draggable(
                cardDraggableState,
                Orientation.Horizontal,
                onDragStopped = educationalCardFling(
                    decay,
                    cardTranslationX,
                    onSwipeCompeted,
                    onAcceptWord,
                    onReject = {}
                )

            )
            .rotate(
                lerp(0f.dp, 8f.dp, cardTranslationX.value / WindowUtil.getWindowWidth()).value
            ),
        elevation = CardDefaults.cardElevation(
            8.dp,
            5.dp,
            9.dp,
        )

    ) {
        EducationalCardContent(
            uiState.word
        )
    }
}