package com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.domain.models.Word
import com.lithium.kotlin.dictionary.common.ui.Light
import com.lithium.kotlin.dictionary.common.ui.LightGreen
import com.lithium.kotlin.dictionary.common.ui.Typography

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EducationalCardContent(
    word: Word
){

    val hintVisible = remember {
        mutableStateOf(false)
    }

    val hintBlur by animateIntAsState(
        targetValue = if (hintVisible.value){
            0
        } else {
            12
        },
        label = ""
    )

    LaunchedEffect(key1 = word){
        hintVisible.value = false
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (word.photoFilePath != ""){
            GlideImage(
                model = word.photoFilePath,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.8f)
                    .fillMaxSize(0.25f),
                contentScale = ContentScale.Fit
            )
        }else {
            Image(
                painter = painterResource(id = R.drawable.ic_empty_picture),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.8f)
                    .fillMaxSize(0.25f),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            color = Light,
            text = word.sequence,
            fontSize = Typography.headlineLarge.fontSize,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Text(
            color = Light,
            text = word.translation.toString(),
            fontSize = Typography.headlineSmall.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .align(Alignment.CenterHorizontally)
                .blur(hintBlur.dp)
                .padding(8.dp)
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                LightGreen
            ),
            onClick = { hintVisible.value = true },
            modifier = Modifier
                .padding(top = 48.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Подзазка")
        }
    }
}