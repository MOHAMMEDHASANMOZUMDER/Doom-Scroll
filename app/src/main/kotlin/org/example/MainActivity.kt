package org.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.ui.components.AnimatedAppContent
import org.example.ui.components.DoomButton
import org.example.ui.components.DoomCard
import org.example.ui.components.DoomTextChip
import org.example.ui.theme.DoomScrollTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoomScrollTheme {
                WelcomeApp()
            }
        }
    }
}

private enum class AppScreen { Welcome, Goals, Success }

@Composable
fun WelcomeApp() {
    var isLoading by remember { mutableStateOf(true) }
    var screen by remember { mutableStateOf(AppScreen.Welcome) }
    var selectedGoal by remember { mutableStateOf("Reduce screen time") }

    LaunchedEffect(Unit) {
        delay(1200)
        isLoading = false
    }

    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedAppContent(isLoading to screen) { state ->
            if (state.first) {
                LoadingScreen()
            } else {
                when (state.second) {
                    AppScreen.Welcome -> WelcomeScreen { screen = AppScreen.Goals }
                    AppScreen.Goals -> GoalScreen(
                        selectedGoal = selectedGoal,
                        onGoalSelected = { selectedGoal = it },
                        onContinue = { screen = AppScreen.Success },
                        onBack = { screen = AppScreen.Welcome }
                    )
                    AppScreen.Success -> SuccessScreen(
                        goal = selectedGoal,
                        onBack = { screen = AppScreen.Welcome }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    val transition = rememberInfiniteTransition(label = "loading")
    val pulse by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loadingPulse"
    )
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing)
        ),
        label = "loadingRotation"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(116.dp)
                .scale(pulse)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "☀",
                modifier = Modifier.rotate(rotation),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displaySmall
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "DOOM SCROLL",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            letterSpacing = 3.sp
        )
        Spacer(modifier = Modifier.height(18.dp))
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                )
        )
    }
}

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "DOOM SCROLL",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing * 2
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("☀", style = MaterialTheme.typography.displaySmall)
            }

            Spacer(modifier = Modifier.height(38.dp))
            Text(
                text = "Make room for\nwhat matters.",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "A little less scrolling.\nA little more living.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                DoomTextChip("Be present")
                DoomTextChip("Build habits")
                DoomTextChip("Find balance")
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DoomButton(
                text = "Get started  →",
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Your time. Your attention. Your life.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun GoalScreen(
    selectedGoal: String,
    onGoalSelected: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val goals = listOf(
        "Reduce screen time" to "📱",
        "Improve focus" to "🎯",
        "Sleep more peacefully" to "🌙",
        "Be more present" to "🌿"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TextButton(onClick = onBack) {
            Text("← Back", color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "What brings\nyou here?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Choose one goal to start building a healthier relationship with your phone.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(26.dp))

        goals.forEach { (goal, emoji) ->
            DoomCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                selected = selectedGoal == goal,
                onClick = { onGoalSelected(goal) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(emoji, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = goal,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 14.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (selectedGoal == goal) "✓" else "○",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        DoomButton(
            text = "Continue  →",
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SuccessScreen(goal: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌱", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "A fresh start.",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "You're taking a step toward a more intentional digital life.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(28.dp))
        DoomCard(
            modifier = Modifier.fillMaxWidth(),
            selected = true
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "YOUR FIRST GOAL",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = goal,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        DoomButton(
            text = "Back to welcome screen",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
