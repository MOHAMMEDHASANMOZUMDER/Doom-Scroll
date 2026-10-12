package org.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.example.ui.components.AnimatedAppContent
import org.example.ui.components.DoomButton
import org.example.ui.components.DoomCard
import org.example.ui.components.DoomOutlinedButton
import org.example.ui.components.DoomTextChip
import org.example.ui.theme.DoomScrollTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoomScrollTheme {
                ScrollGuardApp()
            }
        }
    }
}

private enum class AppScreen { Onboarding, Dashboard, Blocking }

@Composable
fun ScrollGuardApp() {
    var isLoading by remember { mutableStateOf(true) }
    var screen by remember { mutableStateOf(AppScreen.Onboarding) }
    var selectedLimit by remember { mutableStateOf(30) }

    LaunchedEffect(Unit) {
        delay(1200)
        isLoading = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedAppContent(isLoading to screen) { state ->
            if (state.first) {
                LoadingScreen()
            } else {
                when (state.second) {
                    AppScreen.Onboarding -> OnboardingScreen {
                        screen = AppScreen.Dashboard
                    }
                    AppScreen.Dashboard -> DashboardScreen(
                        selectedLimit = selectedLimit,
                        onLimitChanged = { selectedLimit = it },
                        onPreviewBlock = { screen = AppScreen.Blocking }
                    )
                    AppScreen.Blocking -> BlockingScreen(
                        onTakeABreak = { screen = AppScreen.Dashboard },
                        onAdjustLimit = { screen = AppScreen.Dashboard }
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
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing)),
        label = "rotation"
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
            text = "SCROLL GUARD",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            letterSpacing = 3.sp
        )
        Spacer(modifier = Modifier.height(18.dp))
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(4.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
        )
    }
}

@Composable
private fun OnboardingScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SCROLL GUARD",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(46.dp))
        Box(
            modifier = Modifier
                .size(168.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("☀", style = MaterialTheme.typography.displaySmall)
        }
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = "Take back your\nattention.",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Scroll Guard gives you a gentle pause when social media starts taking more time than you planned.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        OnboardingFeature("01", "Set a daily limit", "Choose a time budget that feels realistic for your day.")
        Spacer(modifier = Modifier.height(12.dp))
        OnboardingFeature("02", "Notice your patterns", "See where your time goes with simple, calm weekly stats.")
        Spacer(modifier = Modifier.height(12.dp))
        OnboardingFeature("03", "Pause before you scroll", "When time is up, a full-screen moment helps you choose what comes next.")
        Spacer(modifier = Modifier.height(32.dp))
        DoomButton(
            text = "Create my limit  →",
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "You can change this any time.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun OnboardingFeature(number: String, title: String, description: String) {
    DoomCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    number,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun DashboardScreen(
    selectedLimit: Int,
    onLimitChanged: (Int) -> Unit,
    onPreviewBlock: () -> Unit
) {
    val animatedLimit by animateIntAsState(
        targetValue = selectedLimit,
        animationSpec = tween(350, easing = FastOutSlowInEasing),
        label = "limit"
    )
    val limitOptions = listOf(15, 30, 45, 60)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text("Good morning,", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Your focus today",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Text("☀", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(26.dp))

        DoomCard(
            modifier = Modifier.fillMaxWidth(),
            selected = true
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Today’s limit", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "$animatedLimit min",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                    Text("✦", style = MaterialTheme.typography.titleLarge)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "12 min used  •  18 min left",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { 0.4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Set your daily limit", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "A little structure makes space for what matters.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            limitOptions.forEach { minutes ->
                DoomTextChip(
                    label = "$minutes min",
                    selected = minutes == selectedLimit,
                    onClick = { onLimitChanged(minutes) }
                )
            }
        }
        Slider(
            value = selectedLimit.toFloat(),
            onValueChange = { onLimitChanged((it / 15).toInt() * 15) },
            valueRange = 15f..60f,
            steps = 2,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))
        Text("This week", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard("4h 18m", "Total time", Modifier.weight(1f))
            StatCard("↓ 24%", "vs last week", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        DoomCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Most used today", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(14.dp))
                UsageRow("◎", "Social feed", "12 min", 0.72f)
                Spacer(modifier = Modifier.height(14.dp))
                UsageRow("◌", "Short videos", "8 min", 0.46f)
                Spacer(modifier = Modifier.height(14.dp))
                UsageRow("◍", "Messages", "4 min", 0.25f)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        DoomOutlinedButton(
            text = "Preview block screen",
            onClick = onPreviewBlock,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Hardcoded preview data for now",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier) {
    DoomCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                value,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun UsageRow(icon: String, title: String, time: String, progress: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, style = MaterialTheme.typography.bodyMedium)
                Text(time, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(5.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun BlockingScreen(onTakeABreak: () -> Unit, onAdjustLimit: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "blocking")
    val pulse by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "blockingPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scale(pulse),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("TIME TO PAUSE", style = MaterialTheme.typography.labelMedium, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .size(148.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "12:00",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "Your scrolling\nlimit is up.",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "You planned 30 minutes for today. Take a breath and choose what deserves your attention next.",
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(34.dp))
            DoomButton(
                text = "Take a mindful break",
                onClick = onTakeABreak,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onAdjustLimit) {
                Text(
                    "Adjust my limit",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
