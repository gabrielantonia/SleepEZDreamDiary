package com.example.sleepezdreamdiary.presentation.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sleepezdreamdiary.theme.GreatVibesFontFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val backgroundColor = MaterialTheme.colorScheme.primary

    // Animation state
    val alphaAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animate alpha from 0f to 1f over 1 second
        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            )
        )
        // Delay for the remaining 1 second
        delay(1000)
        navController.navigate("authentication") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "SleepEZ Dream Diary",
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = GreatVibesFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 48.sp
            ),
            modifier = Modifier.alpha(alphaAnimation.value)
        )
    }
}