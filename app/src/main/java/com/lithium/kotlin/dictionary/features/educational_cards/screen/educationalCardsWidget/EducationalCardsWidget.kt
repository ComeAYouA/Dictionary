package com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.lithium.kotlin.dictionary.features.educational_cards.screen.EducationalCardsViewModel
import com.lithium.kotlin.dictionary.utils.WindowUtil


@Composable
fun EducationalCardWidget(
    viewModel: EducationalCardsViewModel
){

    val windowWidth = remember {
        WindowUtil.getWindowWidth()
    }

    val cardTranslationX = remember{
        Animatable(0f)
    }
    cardTranslationX.updateBounds(-windowWidth.toFloat(),  windowWidth.toFloat())


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Canvas(modifier = Modifier.fillMaxSize()){
            drawCircle(
                color = Color.Green,
                radius = windowWidth.toFloat(),
                center = Offset(y = windowWidth.toFloat(), x = windowWidth * 1.66f),
                alpha = lerp(0.1f.dp, 1f.dp, (cardTranslationX.value / windowWidth / 2f).let { if (it >= 0.0f) it else 0.0f }).value
            )

            drawCircle(
                color = Color.Red,
                radius = windowWidth.toFloat(),
                center = Offset(y = windowWidth.toFloat(), x = -windowWidth * 0.66f),
                alpha = lerp(0.1f.dp, 1f.dp, (-cardTranslationX.value / windowWidth / 2f).let { if (it >= 0.0f) it else 0.0f }).value
            )
        }
        EducationalCard(
            viewModel = viewModel,
            cardTranslationX = cardTranslationX
        )

    }
}