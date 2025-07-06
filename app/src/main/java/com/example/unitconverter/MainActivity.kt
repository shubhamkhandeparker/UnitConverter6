package com.example.unitconverter

import android.R
import android.R.attr.category
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.SyncStateContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.theme.UnitConverterTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.AnimatedVisibility

import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import kotlinx.coroutines.delay
import java.util.Locale
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import kotlinx.coroutines.channels.BroadcastChannel
import kotlin.math.exp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                UnitConverterApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UnitConverterApp() {
    //This will Manage which screen to show

    var showMainScreen by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<ConversionCategory?>(null) }

    AnimatedContent(
        targetState = when {
            selectedCategory != null -> "converter"
            showMainScreen -> "main"
            else -> "intro"
        },
        transitionSpec = {
            when (targetState) {
                "main" -> {
                    //Slide in from right with fade
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(800, easing = FastOutSlowInEasing)
                    ) + fadeIn(
                        animationSpec = tween(600)
                    ) with slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(800, easing = FastOutSlowInEasing)
                    ) + fadeOut(
                        animationSpec = tween(400)
                    )
                }

                "converter" -> {
                    //Slide in from bottom with scale
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(700, easing = FastOutSlowInEasing)
                    ) + fadeIn(
                        animationSpec = tween(500)
                    ) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(700, easing = FastOutSlowInEasing)
                    ) with slideOutVertically(
                        targetOffsetY = { fullHeight -> -fullHeight },
                        animationSpec = tween(600)
                    ) + fadeOut(
                        animationSpec = tween(300)
                    )
                }

                else -> {
                    //Simple fade for going to intro

                    fadeIn(
                        animationSpec = tween(600)
                    ) with fadeOut(
                        animationSpec = tween(400)
                    )
                }
            }
        },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            "intro" -> IntroScreen(
                onGetStarted = { showMainScreen = true }
            )

            "main" -> MainScreen(
                onCategorySelected = { category -> selectedCategory = category }
            )

            "converter" -> {
                //safe null check
                selectedCategory?.let { category ->
                    ConverterScreen(
                        category = category,
                        onBackPressed = { selectedCategory = null }
                    )
                }
            }

        }
    }
}

@Composable
fun IntroScreen(onGetStarted: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9c27B0),
                        Color(0xFF3F51B5)
                    ),
                    radius = 1000f
                )
            )
    ) {

        //Floating Background Elements
        FloatingBackgroundElements()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            //App Icon With glow effect

            GlowingAppIcon()

            Spacer(modifier = Modifier.height(24.dp))

            //App tittle With Gradient

            Text(
                text = "Unit Converter",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White, Color(0xFFBBDEFB))
                    )
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Convert between different units easily",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            EnhancedButton(
                text = "Get Started",
                onClick = onGetStarted
            )
        }
    }
}

@Composable
fun MainScreen(onCategorySelected: (ConversionCategory) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63), //pink
                        Color(0xff9c27B0) //Purple
                    )
                )
            )

    ) {
        //Add floating bubbles to main screen
        FloatingBackgroundElements()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            //Animate title entrance
            var titleVisible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                titleVisible = true
            }

            AnimatedVisibility(
                visible = titleVisible,
                enter = fadeIn(tween(800)) +
                        slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(800, easing = FastOutSlowInEasing)
                        )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(
                        text = "Select Converter",
                        style = MaterialTheme.typography.headlineMedium
                            .copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    //Animate Underline
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(3.dp)
                            .background(
                                Color.White.copy(alpha = 0.8f),
                                RoundedCornerShape(2.dp)
                            )

                    )

                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(ConversionCategories) { index, category ->
                    var cardVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 150L)  //Staggered delay
                        cardVisible = true
                    }

                    AnimatedVisibility(
                        visible = cardVisible,
                        enter = fadeIn(
                            tween(600, easing = FastOutSlowInEasing)
                        ) +
                                slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                                ) +
                                scaleIn(
                                    initialScale = 0.8f,
                                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                                )
                    ) {
                        CategoryCard(
                            category = category,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: ConversionCategory,
    onClick: (ConversionCategory) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick(category) }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE91E63), //pink
                            Color(0xff9c27B0) //Purple
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.icon,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }
}

@Composable
fun ConverterScreen(category: ConversionCategory, onBackPressed: () -> Unit) {
    when (category.name) {
        "Length" -> LengthConverterScreen(onBackPressed = onBackPressed)
        "Weight" -> WeightConverterScreen(onBackPressed = onBackPressed)
        "Temperature" -> TemperatureConverterScreen(onBackPressed = onBackPressed)
        "Volume" -> VolumeConverterScreen(onBackPressed = onBackPressed)
        "Speed" -> SpeedConverterScreen(onBackPressed = onBackPressed)
        "Time" -> TimeConverterScreen(onBackPressed = onBackPressed)
        "Area" -> AreaConverterScreen(onBackPressed = onBackPressed)
        "Energy" -> EnergyConverterScreen(onBackPressed = onBackPressed)

        else-> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("${category.name}Converter")
                Text("Coming Soon!")
                Button(onClick = onBackPressed)
                {
                    Text("Back")
                }
            }
        }
    }
}


    @Composable
    fun LengthConverterScreen(onBackPressed: () -> Unit) {

        var inputValue by remember { mutableStateOf("") }

        var fromUnit by remember { mutableStateOf(lengthUnits[0]) }  //meter

        var toUnit by remember { mutableStateOf(lengthUnits[1]) }  //centimeter

        var result by remember { mutableStateOf("0") }

        var showParticles by remember { mutableStateOf(false) }


        //Calculate result whenever input or unit changes

        LaunchedEffect(inputValue, fromUnit, toUnit) {
            val value = inputValue.toDoubleOrNull() ?: 0.0
            val convertedValue = convertLength(value, fromUnit, toUnit)
            result =
                if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                    .trimEnd('0')
                    .trimEnd('.')
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE91E63),
                            Color(0xFF9C27B0)
                        )
                    )
                )
        ) {

            //Floating background elements

            FloatingBackgroundElements()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //Header with back button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                    ) {
                        Text("←", color = Color.White, fontSize = 20.sp)
                    }

                    Text(
                        text = "Length Converter",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(48.dp)) //Balance the back button
                }

                Spacer(modifier = Modifier.height(40.dp))

                //Input Section

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "From",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //Input Field

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            placeholder = {
                                Text(
                                    "Enter Value", color = Color.White.copy(alpha = 0.6f)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White.copy(alpha = 0.8f),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        //from Unit selector (Simplified for now )
                        UnitDropdown(
                            selectedUnit = fromUnit,
                            onUnitSelected = { fromUnit = it },
                            label = "From Unit",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                //Swap button with particle effect
                Box(contentAlignment = Alignment.Center) {

                    FloatingActionButton(
                        onClick = {
                            val temp = fromUnit
                            fromUnit = toUnit
                            toUnit = temp
                            showParticles = true  //Trigger particle
                        },
                        containerColor = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Text("⇅", fontSize = 24.sp, color = Color.White)
                    }
                    //particle effect overlay

                    ParticleEffect(
                        isVisible = showParticles,
                        onAnimationComplete = { showParticles = false }

                    )

                }


                Spacer(modifier = Modifier.height(20.dp))

                //Result Section

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "To",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        //Result Display

                        Text(
                            text = result,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        //To Unit Display

                        UnitDropdown(
                            selectedUnit = toUnit,
                            onUnitSelected = { toUnit = it },
                            label = "To Unit",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun FloatingBackgroundElements() {
        //We'll add floating elements here
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            repeat(6) { index ->
                val infiniteTransition = rememberInfiniteTransition(label = "bubble_$index")

                //vertical floating animation

                val animatedOffsety by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 100f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 4000 + index * 1000,
                            easing = FastOutSlowInEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "Float_y_$index"
                )

                //Horizontal floating animation

                val animatedOffsetX by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 30f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 3000 + index * 800,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "float_x_$index"
                )

                //pulsing animation
                val animatedAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.1f,
                    targetValue = 0.3f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000 + index * 500),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse_$index"
                )

                Box(
                    modifier = Modifier
                        .offset(
                            x = (30 + index * 60).dp + animatedOffsetX.dp,
                            y = (80 + index * 120).dp + animatedOffsety.dp
                        )
                        .size((60 + index * 20).dp)
                        .background(
                            Color.White.copy(alpha = animatedAlpha),
                            CircleShape

                        )
                )

            }

        }
    }


    @Composable
    fun GlowingAppIcon() {
        val infiniteTransition = rememberInfiniteTransition(label = "app_icon")

        //Bounce animation
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bounce"
        )


        //Rotation animation
        val rotation by infiniteTransition.animateFloat(
            initialValue = -5f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "rotate"
        )

        //Glow animation
        val glowAlpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glow"
        )

        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .rotate(rotation)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(40.dp)
                )
                .padding(8.dp)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    RoundedCornerShape(32.dp)
                ),
            contentAlignment = Alignment.Center

        ) {

            Text(
                text = "\uD83D\uDCD0",
                fontSize = 72.sp,
                modifier = Modifier.scale(scale * 0.8f)
            )
        }
    }


    @Composable
    fun EnhancedButton(
        text: String,
        onClick: () -> Unit
    ) {
        var isPressed by remember { mutableStateOf(false) }

        val infiniteTransition = rememberInfiniteTransition(label = "button")
        val shimmer by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer"
        )
        Button(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = Modifier
                .height(64.dp)
                .widthIn(min = 240.dp)
                .scale(if (isPressed) 0.95f else 1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.25f)
            ),
            shape = RoundedCornerShape(32.dp),
            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.4f))
        ) {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

        }

    }

    @Composable
    fun UnitDropdown(
        selectedUnit: LengthUnit,
        onUnitSelected: (LengthUnit) -> Unit,
        label: String,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(modifier = modifier) {
            //Dropdown tigger button

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = label,
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "${selectedUnit.name}(${selectedUnit.symbol})",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    Text(
                        text = if (expanded) "▲" else "▼",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
            }

            //Animated DropDown Menu

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(
                    Color.White.copy(alpha = 0.95f),
                    RoundedCornerShape(12.dp)
                )
            ) {
                lengthUnits.forEach { unit ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = unit.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (unit == selectedUnit) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (unit == selectedUnit) Color(0xFFE91E63) else Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "(${unit.symbol})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        },
                        onClick = {
                            onUnitSelected(unit)
                            expanded = false
                        },
                        modifier = Modifier.background(
                            if (unit == selectedUnit) Color(0xFFE91E63).copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                    )
                }
            }
        }
    }


    @Composable
    fun <T> GenericUnitDropdown(
        selectedUnit: T,
        onUnitSelected: (T) -> Unit,
        label: String,
        units: List<T>,
        unitName: (T) -> String,
        unitSymbol: (T) -> String,
        modifier: Modifier = Modifier
    ) {

        var expanded by remember { mutableStateOf(false) }

        Box(modifier = modifier) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = label,
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "${unitName(selectedUnit)} (${unitSymbol(selectedUnit)})",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                    Text(
                        text = if (expanded) "▲" else "▼",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
            }

            //dropdown menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(
                    Color.White.copy(alpha = 0.95f),
                    RoundedCornerShape(12.dp)
                )
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            )
                            {
                                Text(
                                    text = unitName(unit),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (unit == selectedUnit) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (unit == selectedUnit) Color(0xFFE91E63) else Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "(${unitSymbol(unit)})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )

                            }
                        },
                        onClick = {
                            onUnitSelected(unit)
                            expanded = false
                        },
                        modifier = Modifier.background(
                            if (unit == selectedUnit) Color(0xFFE91E63).copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                    )
                }
            }
        }
    }


    @Composable
    fun ParticleEffect(
        isVisible: Boolean,
        onAnimationComplete: () -> Unit
    ) {
        if (isVisible) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                //Create multiple particles
                repeat(12) { index ->
                    val infiniteTransition = rememberInfiniteTransition(
                        label = "particle_$index"
                    )

                    //Random direction and distance for each particle

                    val offsetX = remember { (-100..100).random() }
                    val offsetY = remember { (-100..100).random() }

                    val animatedOffsetX by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = offsetX.toFloat(),
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "particle_x_$index"
                    )

                    val animatedOffsetY by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = offsetY.toFloat(),
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "particle_y_$index"
                    )

                    val animateAlpha by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "particle_alpha_$index"
                    )
                    //Particle Visual

                    Box(
                        modifier = Modifier
                            .offset(
                                x = animatedOffsetX.dp,
                                y = animatedOffsetY.dp
                            )
                            .size(6.dp)
                            .background(
                                Color.White.copy(alpha = animateAlpha),
                                CircleShape
                            )
                    )

                }
                //Auto-hide after animation

                LaunchedEffect(isVisible) {
                    if (isVisible) {
                        delay(1000)
                        onAnimationComplete()
                    }
                }
            }
        }
    }

    @Composable
    fun WeightConverterScreen(onBackPressed: () -> Unit) {
        var inputValue by remember { mutableStateOf("") }
        var fromUnit by remember { mutableStateOf(weightUnits[0]) }
        var toUnit by remember { mutableStateOf(weightUnits[1]) }
        var result by remember { mutableStateOf("0") }
        var showParticles by remember { mutableStateOf(false) }

        LaunchedEffect(inputValue, fromUnit, toUnit) {
            val value = inputValue.toDoubleOrNull() ?: 0.0
            val convertedValue = convertWeight(value, fromUnit, toUnit)
            result =
                if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                    .trimEnd('0')
                    .trimEnd('.')
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE91E63),
                            Color(0xFF9C27B0)
                        )
                    )
                )
        ) {
            FloatingBackgroundElements()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                    ) {
                        Text("←", color = Color.White, fontSize = 20.sp)

                    }
                    Text(
                        text = "Weight Converter",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(48.dp))

                }
                Spacer(modifier = Modifier.height(40.dp))

                //Input Selection
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "from",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            placeholder = {
                                Text("Enter Value", color = Color.White.copy(alpha = 0.6f))

                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White.copy(alpha = 0.8f),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GenericUnitDropdown(
                            selectedUnit = fromUnit,
                            onUnitSelected = { fromUnit = it },
                            label = "from Unit",
                            units = weightUnits,
                            unitName = { it.name },
                            unitSymbol = { it.symbol },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                //swap button with particles

                Box(contentAlignment = Alignment.Center) {
                    FloatingActionButton(
                        onClick = {
                            val temp = fromUnit
                            fromUnit = toUnit
                            toUnit = temp
                            showParticles = true
                        },
                        containerColor = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Text("⇅", fontSize = 24.sp, color = Color.White)
                    }

                    ParticleEffect(
                        isVisible = showParticles,
                        onAnimationComplete = { showParticles = false }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                //Result section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "To",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = result,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GenericUnitDropdown(
                            selectedUnit = toUnit,
                            onUnitSelected = { toUnit = it },
                            label = "To Unit",
                            units = weightUnits,
                            unitName = { it.name },
                            unitSymbol = { it.symbol },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        }

    }


@Composable
fun TemperatureConverterScreen(onBackPressed: () -> Unit) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(temperatureUnits[0]) }
    var toUnit by remember { mutableStateOf(temperatureUnits[1]) }
    var result by remember { mutableStateOf("0") }
    var showParticles by remember { mutableStateOf(false) }

    LaunchedEffect(inputValue, fromUnit, toUnit) {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        val convertedValue = convertTemperature (value, fromUnit, toUnit)
        result =
            if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                .trimEnd('0')
                .trimEnd('.')
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0)
                    )
                )
            )
    ) {
        FloatingBackgroundElements()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.background(
                        Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                ) {
                    Text("←", color = Color.White, fontSize = 20.sp)

                }
                Text(
                    text = "Temperature Converter",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp))

            }
            Spacer(modifier = Modifier.height(40.dp))

            //Input Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "from",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = {
                            Text("Enter Value", color = Color.White.copy(alpha = 0.6f))

                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = fromUnit,
                        onUnitSelected = { fromUnit = it },
                        label = "from Unit",
                        units = temperatureUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //swap button with particles

            Box(contentAlignment = Alignment.Center) {
                FloatingActionButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                        showParticles = true
                    },
                    containerColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("⇅", fontSize = 24.sp, color = Color.White)
                }

                ParticleEffect(
                    isVisible = showParticles,
                    onAnimationComplete = { showParticles = false }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //Result section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "To",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = toUnit,
                        onUnitSelected = { toUnit = it },
                        label = "To Unit",
                        units = temperatureUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }

}

@Composable
fun VolumeConverterScreen (onBackPressed: () -> Unit) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(VolumeUnits[0]) }
    var toUnit by remember { mutableStateOf(VolumeUnits[1]) }
    var result by remember { mutableStateOf("0") }
    var showParticles by remember { mutableStateOf(false) }

    LaunchedEffect(inputValue, fromUnit, toUnit) {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        val convertedValue = convertVolume (value, fromUnit, toUnit)
        result =
            if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                .trimEnd('0')
                .trimEnd('.')
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0)
                    )
                )
            )
    ) {
        FloatingBackgroundElements()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.background(
                        Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                ) {
                    Text("←", color = Color.White, fontSize = 20.sp)

                }
                Text(
                    text = "Volume Converter",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp))

            }
            Spacer(modifier = Modifier.height(40.dp))

            //Input Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "from",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = {
                            Text("Enter Value", color = Color.White.copy(alpha = 0.6f))

                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = fromUnit,
                        onUnitSelected = { fromUnit = it },
                        label = "from Unit",
                        units = VolumeUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //swap button with particles

            Box(contentAlignment = Alignment.Center) {
                FloatingActionButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                        showParticles = true
                    },
                    containerColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("⇅", fontSize = 24.sp, color = Color.White)
                }

                ParticleEffect(
                    isVisible = showParticles,
                    onAnimationComplete = { showParticles = false }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //Result section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "To",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = toUnit,
                        onUnitSelected = { toUnit = it },
                        label = "To Unit",
                        units = VolumeUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }

}

@Composable
fun SpeedConverterScreen (onBackPressed: () -> Unit) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(speedUnits[0]) }
    var toUnit by remember { mutableStateOf(speedUnits[1]) }
    var result by remember { mutableStateOf("0") }
    var showParticles by remember { mutableStateOf(false) }

    LaunchedEffect(inputValue, fromUnit, toUnit) {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        val convertedValue = convertSpeed(value, fromUnit, toUnit)
        result =
            if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                .trimEnd('0')
                .trimEnd('.')
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0)
                    )
                )
            )
    ) {
        FloatingBackgroundElements()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.background(
                        Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                ) {
                    Text("←", color = Color.White, fontSize = 20.sp)

                }
                Text(
                    text = "Speed Converter",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp))

            }
            Spacer(modifier = Modifier.height(40.dp))

            //Input Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "from",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = {
                            Text("Enter Value", color = Color.White.copy(alpha = 0.6f))

                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = fromUnit,
                        onUnitSelected = { fromUnit = it },
                        label = "from Unit",
                        units = speedUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //swap button with particles

            Box(contentAlignment = Alignment.Center) {
                FloatingActionButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                        showParticles = true
                    },
                    containerColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("⇅", fontSize = 24.sp, color = Color.White)
                }

                ParticleEffect(
                    isVisible = showParticles,
                    onAnimationComplete = { showParticles = false }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //Result section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "To",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = toUnit,
                        onUnitSelected = { toUnit = it },
                        label = "To Unit",
                        units = speedUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }

}


@Composable
fun TimeConverterScreen (onBackPressed: () -> Unit) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(timeUnits[0]) }
    var toUnit by remember { mutableStateOf(timeUnits[1]) }
    var result by remember { mutableStateOf("0") }
    var showParticles by remember { mutableStateOf(false) }

    LaunchedEffect(inputValue, fromUnit, toUnit) {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        val convertedValue = convertTime(value, fromUnit, toUnit)
        result =
            if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                .trimEnd('0')
                .trimEnd('.')
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0)
                    )
                )
            )
    ) {
        FloatingBackgroundElements()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.background(
                        Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                ) {
                    Text("←", color = Color.White, fontSize = 20.sp)

                }
                Text(
                    text = "Time Converter",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp))

            }
            Spacer(modifier = Modifier.height(40.dp))

            //Input Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "from",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = {
                            Text("Enter Value", color = Color.White.copy(alpha = 0.6f))

                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = fromUnit,
                        onUnitSelected = { fromUnit = it },
                        label = "from Unit",
                        units = timeUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //swap button with particles

            Box(contentAlignment = Alignment.Center) {
                FloatingActionButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                        showParticles = true
                    },
                    containerColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("⇅", fontSize = 24.sp, color = Color.White)
                }

                ParticleEffect(
                    isVisible = showParticles,
                    onAnimationComplete = { showParticles = false }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //Result section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "To",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = toUnit,
                        onUnitSelected = { toUnit = it },
                        label = "To Unit",
                        units = timeUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }

}


@Composable
fun  AreaConverterScreen (onBackPressed: () -> Unit) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(areaUnits[0]) }
    var toUnit by remember { mutableStateOf(areaUnits[1]) }
    var result by remember { mutableStateOf("0") }
    var showParticles by remember { mutableStateOf(false) }

    LaunchedEffect(inputValue, fromUnit, toUnit) {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        val convertedValue = convertArea(value, fromUnit, toUnit)
        result =
            if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                .trimEnd('0')
                .trimEnd('.')
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0)
                    )
                )
            )
    ) {
        FloatingBackgroundElements()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.background(
                        Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                ) {
                    Text("←", color = Color.White, fontSize = 20.sp)

                }
                Text(
                    text = "Area Converter",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp))

            }
            Spacer(modifier = Modifier.height(40.dp))

            //Input Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "from",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = {
                            Text("Enter Value", color = Color.White.copy(alpha = 0.6f))

                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = fromUnit,
                        onUnitSelected = { fromUnit = it },
                        label = "from Unit",
                        units = areaUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //swap button with particles

            Box(contentAlignment = Alignment.Center) {
                FloatingActionButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                        showParticles = true
                    },
                    containerColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("⇅", fontSize = 24.sp, color = Color.White)
                }

                ParticleEffect(
                    isVisible = showParticles,
                    onAnimationComplete = { showParticles = false }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //Result section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "To",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = toUnit,
                        onUnitSelected = { toUnit = it },
                        label = "To Unit",
                        units = areaUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }

}



@Composable
fun  EnergyConverterScreen (onBackPressed: () -> Unit) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(energyUnits[0]) }
    var toUnit by remember { mutableStateOf(energyUnits[1]) }
    var result by remember { mutableStateOf("0") }
    var showParticles by remember { mutableStateOf(false) }

    LaunchedEffect(inputValue, fromUnit, toUnit) {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        val convertedValue = convertEnergy(value, fromUnit, toUnit)
        result =
            if (convertedValue == 0.0) "0" else String.format("%.6f", convertedValue)
                .trimEnd('0')
                .trimEnd('.')
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0)
                    )
                )
            )
    ) {
        FloatingBackgroundElements()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.background(
                        Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                ) {
                    Text("←", color = Color.White, fontSize = 20.sp)

                }
                Text(
                    text = "Energy Converter",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp))

            }
            Spacer(modifier = Modifier.height(40.dp))

            //Input Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "from",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = {
                            Text("Enter Value", color = Color.White.copy(alpha = 0.6f))

                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = fromUnit,
                        onUnitSelected = { fromUnit = it },
                        label = "from Unit",
                        units = energyUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //swap button with particles

            Box(contentAlignment = Alignment.Center) {
                FloatingActionButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                        showParticles = true
                    },
                    containerColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("⇅", fontSize = 24.sp, color = Color.White)
                }

                ParticleEffect(
                    isVisible = showParticles,
                    onAnimationComplete = { showParticles = false }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            //Result section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "To",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GenericUnitDropdown(
                        selectedUnit = toUnit,
                        onUnitSelected = { toUnit = it },
                        label = "To Unit",
                        units = energyUnits,
                        unitName = { it.name },
                        unitSymbol = { it.symbol },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }

}



