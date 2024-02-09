package com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.calculateTargetValue
import com.lithium.kotlin.dictionary.utils.WindowUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun educationalCardFling(
    decay: DecayAnimationSpec<Float>,
    cardTranslationX: Animatable<Float, AnimationVector1D>,
    onSwipeCompeted: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit

    ): suspend CoroutineScope.(Float) -> Unit = { _velocity ->

        val velocity = _velocity / 3

        val windowWidth = WindowUtil.getWindowWidth()

        val decayX = decay.calculateTargetValue(
            cardTranslationX.value,
            velocity
        )

        launch {
            val targetX = if (decayX > windowWidth * 0.48) {
                windowWidth.toFloat()
            } else if (decayX < -windowWidth * 0.48) {
                -windowWidth.toFloat()
            } else {
                0f
            }

            val canReachTargetWithDecay =
                (decayX > 0 && targetX == windowWidth.toFloat())
                        || (decayX < 0 && targetX == -windowWidth.toFloat())


            if (canReachTargetWithDecay) {
                cardTranslationX.animateDecay(
                    initialVelocity = velocity,
                    animationSpec = decay
                )

                if (targetX == windowWidth.toFloat()){
                    onAccept()
                } else {
                    onReject()
                }

                onSwipeCompeted()

            } else {
                cardTranslationX.animateTo(
                    targetX,
                    initialVelocity = velocity
                )
            }
        }
    }