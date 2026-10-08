package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VehicleType
import com.example.ui.theme.*

@Composable
fun LiveMapSimulation(
    origin: String,
    destination: String,
    vehicleType: VehicleType,
    etaMinutes: Int = 3,
    driverName: String = "Kardo Dalisay",
    driverPlate: String = "NXX-982",
    modifier: Modifier = Modifier
) {
    // Infinite Animation for pulse and vehicle movement
    val infiniteTransition = rememberInfiniteTransition(label = "map_anim")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )

    val vehicleProgress by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vehicle_progress"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_map_simulation")
    ) {
        Column {
            // Live Simulation Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF070E1A))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw subtle grid lines
                    val step = 36f
                    var x = 0f
                    while (x < w) {
                        drawLine(
                            color = Color(0xFF132035),
                            start = Offset(x, 0f),
                            end = Offset(x, h),
                            strokeWidth = 1f
                        )
                        x += step
                    }
                    var y = 0f
                    while (y < h) {
                        drawLine(
                            color = Color(0xFF132035),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                        y += step
                    }

                    // Key coordinates
                    val startPt = Offset(w * 0.18f, h * 0.72f)
                    val controlPt1 = Offset(w * 0.40f, h * 0.82f)
                    val controlPt2 = Offset(w * 0.55f, h * 0.25f)
                    val endPt = Offset(w * 0.84f, h * 0.28f)

                    // Draw Route curve
                    val routePath = Path().apply {
                        moveTo(startPt.x, startPt.y)
                        cubicTo(controlPt1.x, controlPt1.y, controlPt2.x, controlPt2.y, endPt.x, endPt.y)
                    }

                    // Background route glow
                    drawPath(
                        path = routePath,
                        color = EmeraldPrimary.copy(alpha = 0.25f),
                        style = Stroke(width = 12f)
                    )

                    // Main road path
                    drawPath(
                        path = routePath,
                        color = EmeraldPrimary,
                        style = Stroke(
                            width = 4f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                        )
                    )

                    // Draw radar pulse at Start (Pickup)
                    drawCircle(
                        color = EmeraldLight.copy(alpha = pulseAlpha),
                        radius = pulseRadius,
                        center = startPt
                    )
                    drawCircle(
                        color = EmeraldPrimary,
                        radius = 7f,
                        center = startPt
                    )

                    // Draw End (Dropoff)
                    drawCircle(
                        color = BlueAccent.copy(alpha = 0.3f),
                        radius = 18f,
                        center = endPt
                    )
                    drawCircle(
                        color = BlueAccent,
                        radius = 7f,
                        center = endPt
                    )

                    // Approximate current vehicle point along cubic bezier
                    val t = vehicleProgress
                    val u = 1 - t
                    val vx = (u * u * u * startPt.x) + (3 * u * u * t * controlPt1.x) + (3 * u * t * t * controlPt2.x) + (t * t * t * endPt.x)
                    val vy = (u * u * u * startPt.y) + (3 * u * u * t * controlPt1.y) + (3 * u * t * t * controlPt2.y) + (t * t * t * endPt.y)
                    val vehiclePos = Offset(vx, vy)

                    // Vehicle glow and icon circle
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(EmeraldLight, Color.Transparent),
                            center = vehiclePos,
                            radius = 24f
                        ),
                        radius = 24f,
                        center = vehiclePos
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 9f,
                        center = vehiclePos
                    )
                    drawCircle(
                        color = EmeraldDark,
                        radius = 6f,
                        center = vehiclePos
                    )
                }

                // Top Badge: GPS Active
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SlateSurfaceVariant.copy(alpha = 0.9f))
                        .border(1.dp, SlateBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldLight)
                    )
                    Text(
                        text = "LIVE GPS DISPATCH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Top Right Badge: ETA
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(EmeraldContainer)
                        .border(1.dp, EmeraldPrimary, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "ETA ~${etaMinutes} MINS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldLight
                    )
                }
            }

            // Driver & Vehicle Card Information Below Canvas
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(EmeraldContainer)
                                .border(1.dp, EmeraldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = vehicleType.iconEmoji,
                                fontSize = 18.sp
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = driverName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "4.95",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = GoldAccent
                                )
                            }
                            Text(
                                text = "Plate: $driverPlate • ${vehicleType.displayName}",
                                fontSize = 11.sp,
                                color = SlateSubtext
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldPrimary.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Approaching",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Origin / Destination Preview
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SlateSurfaceVariant)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🟢 ", fontSize = 10.sp)
                        Text(
                            text = origin,
                            fontSize = 11.sp,
                            color = SlateText,
                            maxLines = 1
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📍 ", fontSize = 10.sp)
                        Text(
                            text = destination,
                            fontSize = 11.sp,
                            color = SlateSubtext,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
