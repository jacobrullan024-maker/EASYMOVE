package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiImageService
import com.example.model.AiImagePreset
import com.example.ui.theme.*
import com.example.ui.viewmodel.EasyMoveViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiStudioScreen(
    viewModel: EasyMoveViewModel,
    modifier: Modifier = Modifier
) {
    val promptInput by viewModel.aiPromptInput.collectAsState()
    val aspectRatio by viewModel.aiAspectRatio.collectAsState()
    val resolution by viewModel.aiResolution.collectAsState()
    val isGenerating by viewModel.isGeneratingAiImage.collectAsState()
    val result by viewModel.aiImageResult.collectAsState()
    val studioTab by viewModel.aiStudioTab.collectAsState()
    val inputBitmap by viewModel.inputBitmapToEdit.collectAsState()

    val presets = listOf(
        AiImagePreset(
            label = "Delivery Landmark",
            prompt = "Emerald green residential gate with yellow bougainvillea flowers for delivery landmark in Ortigas",
            category = "Landmark",
            emoji = "🏡"
        ),
        AiImagePreset(
            label = "Cargo Box Note",
            prompt = "Fragile retail logistics box with red warning sticker and delivery label on wooden pallet",
            category = "Cargo",
            emoji = "📦"
        ),
        AiImagePreset(
            label = "Chicken Inasal Dish",
            prompt = "Crispy char-broiled Filipino chicken inasal platter with steaming garlic rice on banana leaf",
            category = "Food",
            emoji = "🍗"
        ),
        AiImagePreset(
            label = "Rider Avatar",
            prompt = "Friendly Filipino motorcycle delivery rider with emerald green jacket and sleek helmet, 3D render avatar",
            category = "Avatar",
            emoji = "🛵"
        ),
        AiImagePreset(
            label = "Delivery Proof Stamp",
            prompt = "Package securely placed at the doorstep with verified green delivered badge and timestamp",
            category = "Proof",
            emoji = "📸"
        )
    )

    // Sample bitmaps available for editing
    val sampleEditOptions = remember {
        listOf(
            "Cargo Package" to createSampleBitmap("📦 Retail Cargo Package", 0xFF064E3B.toInt()),
            "House Gate" to createSampleBitmap("🏡 Residential Dropoff Gate", 0xFF1E293B.toInt()),
            "Inasal Meal" to createSampleBitmap("🍗 Chicken Inasal Dish", 0xFF831843.toInt()),
            "Driver Selfie" to createSampleBitmap("🛵 Rider Kuya Kardo", 0xFF0F172A.toInt())
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI Studio Header
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(EmeraldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎨", fontSize = 20.sp)
                            }
                            Column {
                                Text(
                                    text = "AI Visual Studio",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "Create & Edit Images with AI",
                                    fontSize = 11.sp,
                                    color = SlateSubtext
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = EmeraldPrimary.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary)
                        ) {
                            Text(
                                text = GeminiImageService.MODEL_NAME,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = EmeraldLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Generate clear delivery landmarks, cargo condition photos, mouthwatering menu visuals, or custom rider avatars using prompts.",
                        fontSize = 11.sp,
                        color = SlateSubtext
                    )
                }
            }
        }

        // Mode Selector: CREATE vs EDIT
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.setAiStudioTab("CREATE")
                        viewModel.setInputBitmapToEdit(null)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (studioTab == "CREATE") EmeraldPrimary else SlateSurfaceVariant
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_mode_create")
                ) {
                    Text(
                        text = "✨ Create Image",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (studioTab == "CREATE") OnEmerald else SlateText
                    )
                }

                Button(
                    onClick = {
                        viewModel.setAiStudioTab("EDIT")
                        if (inputBitmap == null) {
                            viewModel.setInputBitmapToEdit(sampleEditOptions.first().second)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (studioTab == "EDIT") EmeraldPrimary else SlateSurfaceVariant
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_mode_edit")
                ) {
                    Text(
                        text = "🖌️ Edit Image",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (studioTab == "EDIT") OnEmerald else SlateText
                    )
                }
            }
        }

        // If in EDIT mode: Show selectable images to edit
        if (studioTab == "EDIT") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "CHOOSE IMAGE TO EDIT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateMuted
                    )
                    val scroll = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scroll),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sampleEditOptions.forEach { (name, bmp) ->
                            val isSelected = inputBitmap == bmp
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) EmeraldContainer else SlateSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldPrimary else SlateBorder
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { viewModel.setInputBitmapToEdit(bmp) }
                                    .testTag("edit_sample_$name")
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Image(
                                        bitmap = bmp.asImageBitmap(),
                                        contentDescription = name,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Text(
                                        text = name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) EmeraldLight else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Presets Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "QUICK SUGGESTED PROMPTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateMuted
                )
                val scroll = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scroll),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presets.forEach { p ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SlateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setAiPrompt(p.prompt) }
                                .testTag("ai_preset_${p.category}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(p.emoji, fontSize = 12.sp)
                                Text(
                                    text = p.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldLight
                                )
                            }
                        }
                    }
                }
            }
        }

        // Prompt Input & Configuration Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (studioTab == "CREATE") "Describe Image to Generate" else "Describe Edits to Apply",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    OutlinedTextField(
                        value = promptInput,
                        onValueChange = { viewModel.setAiPrompt(it) },
                        label = { Text("Prompt for gemini-nano-banana-2.1") },
                        placeholder = { Text("e.g. Add red fragile handling sticker on package") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = SlateSurfaceVariant,
                            unfocusedContainerColor = SlateSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
                            .testTag("ai_prompt_input")
                    )

                    // Aspect Ratio & Resolution selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Aspect ratio selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Aspect Ratio:", fontSize = 11.sp, color = SlateSubtext)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("1:1", "16:9", "4:3").forEach { ratio ->
                                    val isSelected = aspectRatio == ratio
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) EmeraldContainer else SlateSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) EmeraldPrimary else SlateBorder
                                        ),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.setAiAspectRatio(ratio) }
                                    ) {
                                        Text(
                                            text = ratio,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) EmeraldLight else SlateText,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Resolution selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Resolution:", fontSize = 11.sp, color = SlateSubtext)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("1K", "2K").forEach { res ->
                                    val isSelected = resolution == res
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) EmeraldContainer else SlateSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) EmeraldPrimary else SlateBorder
                                        ),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.setAiResolution(res) }
                                    ) {
                                        Text(
                                            text = res,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) EmeraldLight else SlateText,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Action Button
                    Button(
                        onClick = { viewModel.generateOrEditAiImage() },
                        enabled = !isGenerating,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("ai_generate_button")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                color = OnEmerald,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Processing via gemini-nano-banana-2.1...", color = OnEmerald, fontWeight = FontWeight.Bold)
                        } else {
                            Text(
                                text = if (studioTab == "CREATE") "✨ Generate with gemini-nano-banana-2.1" else "🖌️ Edit with gemini-nano-banana-2.1",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = OnEmerald
                            )
                        }
                    }
                }
            }
        }

        // Output Result Card
        if (result != null) {
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_result_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "Generated Image Output",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            IconButton(
                                onClick = { viewModel.clearAiImage() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = SlateSubtext)
                            }
                        }

                        // Display the Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = result!!.bitmap.asImageBitmap(),
                                contentDescription = result!!.promptUsed,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Text(
                            text = "\"${result!!.promptUsed}\"",
                            fontSize = 12.sp,
                            color = EmeraldLight,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = result!!.description ?: "Processed with gemini-nano-banana-2.1",
                            fontSize = 10.sp,
                            color = SlateSubtext
                        )

                        // Action buttons: Use as Delivery Note / Avatar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.setItemDescription("AI Delivery Note: ${result!!.promptUsed.take(35)}")
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldContainer),
                                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Apply as Note", fontSize = 11.sp, color = EmeraldLight, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.dismissNotification()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save Image", fontSize = 11.sp, color = OnEmerald, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper to create clean sample bitmaps for the AI edit studio
private fun createSampleBitmap(label: String, colorHex: Int): Bitmap {
    val size = 256
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)

    val paint = Paint().apply {
        color = colorHex
        isAntiAlias = true
    }
    canvas.drawRoundRect(0f, 0f, size.toFloat(), size.toFloat(), 30f, 30f, paint)

    val textPaint = Paint().apply {
        color = 0xFFFFFFFF.toInt()
        textSize = 24f
        isFakeBoldText = true
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(label.take(16), size / 2f, size / 2f, textPaint)
    return bmp
}
