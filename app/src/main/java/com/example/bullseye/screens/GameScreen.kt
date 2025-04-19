package com.example.bullseye.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bullseye.R
import com.example.bullseye.components.GameDetail
import com.example.bullseye.components.GamePrompt
import com.example.bullseye.components.ResultDialog
import com.example.bullseye.components.TargetSlider
import com.example.bullseye.ui.theme.BullseyeTheme
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun GameScreen(
    onNavigateToAboutScreen: () -> Unit
) {

    fun newTargetValue() = Random.nextInt(1, 100)

    var alertsVisible by rememberSaveable { mutableStateOf(false) }
    var sliderValue by rememberSaveable { mutableFloatStateOf(0.5f) }
    var targetValue by rememberSaveable { mutableIntStateOf(newTargetValue()) }

    val sliderToInt = (sliderValue * 100).toInt()

    var totalScore by rememberSaveable { mutableStateOf(0) }
    var currentRound by rememberSaveable { mutableStateOf(1) }

    fun differenceAmount() = abs(targetValue - sliderToInt)

    fun pointsForCurrentRound(): Int {
        val maxScore = 100
        val difference = differenceAmount()
        val bonus = when (difference) {
            0 -> maxScore
            1 -> maxScore / 2
            else -> 0
        }
        return (maxScore - difference) + bonus
    }

    fun startNewGame() {
        totalScore = 0
        currentRound = 1
        sliderValue = 0.5f
        targetValue = newTargetValue()
    }

    fun alertTitle(): Int = when (differenceAmount()) {
        0 -> R.string.alert_title_1
        in 1..4 -> R.string.alert_title_2
        in 5..10 -> R.string.alert_title_3
        else -> R.string.alert_title_4
    }

    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.background),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.background_image_desc)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(.5f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(9f)
            ) {
                GamePrompt(targetValue = targetValue)
                TargetSlider(
                    value = sliderValue,
                    valueChanged = { value ->
                        sliderValue = value
                    }
                )
                Button(
                    onClick = {
                        alertsVisible = true
                        totalScore += pointsForCurrentRound()
                        Log.i("ALERT VISIBLE?", alertsVisible.toString())
                    },
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(text = stringResource(R.string.hit_me_button_text))
                }
                GameDetail(
                    modifier = Modifier.fillMaxWidth(),
                    totalScore = totalScore,
                    currentRound = currentRound,
                    onNavigateToAbout = onNavigateToAboutScreen,
                    onStartOver = { startNewGame() }
                )
            }
            Spacer(modifier = Modifier.weight(.5f))

            if (alertsVisible) {
                ResultDialog(
                    dialogTitle = alertTitle(),
                    hideDialog = { alertsVisible = false },
                    sliderValue = sliderToInt,
                    points = pointsForCurrentRound(),
                    onRoundIncrement = {
                        currentRound += 1
                        targetValue = newTargetValue()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 864, heightDp = 432)
@Composable
fun GreetingPreview() {
    BullseyeTheme {
        GameScreen(onNavigateToAboutScreen = {})
    }
}
