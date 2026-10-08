package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EasyMoveRepository
import com.example.model.*
import com.example.ui.components.FareAuditCard
import com.example.ui.components.LiveMapSimulation
import com.example.ui.theme.*
import com.example.ui.viewmodel.EasyMoveViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerRideScreen(
    viewModel: EasyMoveViewModel,
    onNavigateToAiStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedVehicle by viewModel.selectedVehicle.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()
    val originInput by viewModel.originInput.collectAsState()
    val destinationInput by viewModel.destinationInput.collectAsState()
    val distanceKm by viewModel.distanceKm.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val fareQuote by viewModel.fareQuote.collectAsState()
    val activeBooking by viewModel.activeBooking.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val itemDesc by viewModel.itemDescription.collectAsState()

    var showRecentHistory by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Warm Friendly Greeting Header
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Kumusta, Juan! 🌟",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "Saan tayo pupunta ngayon? Maingat at mabilis ang biyahe.",
                                fontSize = 11.sp,
                                color = SlateSubtext
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldContainer,
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "12% FAIR FEE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Friendly Quick Shortcut Bubbles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldPrimary.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateToAiStudio() }
                                .testTag("shortcut_ai_studio")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("🎨 ", fontSize = 12.sp)
                                Text("AI Studio", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldLight)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SlateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.openChat() }
                                .testTag("shortcut_chat_rider")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("💬 ", fontSize = 12.sp)
                                Text("Chat Rider", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SlateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.triggerSafetySos() }
                                .testTag("shortcut_sos")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("🛡️ ", fontSize = 12.sp)
                                Text("24/7 SOS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateText)
                            }
                        }
                    }
                }
            }
        }

        // Active Booking Status Card if ongoing
        if (activeBooking != null && activeBooking?.status != BookingStatus.COMPLETED) {
            item {
                ActiveBookingStatusCard(
                    booking = activeBooking!!,
                    onOpenChat = { viewModel.openChat() },
                    onAdvance = { newStatus ->
                        viewModel.updateBookingStatus(activeBooking!!.id, newStatus)
                    }
                )
            }
        }

        // Live Simulation Map
        item {
            LiveMapSimulation(
                origin = originInput,
                destination = destinationInput,
                vehicleType = selectedVehicle,
                etaMinutes = 3,
                driverName = "Kardo Dalisay",
                driverPlate = "NXX-982"
            )
        }

        // Quick Preset Routes
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "POPULAR METRO ROUTES",
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
                    EasyMoveRepository.PRESET_ROUTES.forEach { route ->
                        val isCurrent = originInput == route.origin && destinationInput == route.destination
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) EmeraldContainer else SlateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isCurrent) EmeraldPrimary else SlateBorder
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.applyPresetRoute(route) }
                                .testTag("preset_route_${route.distanceKm}")
                        ) {
                            Text(
                                text = "${route.label} (${route.distanceKm} km)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) EmeraldLight else SlateText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // Booking Form Card
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
                        text = "Book an Easy Move Trip",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Origin Input
                    OutlinedTextField(
                        value = originInput,
                        onValueChange = { viewModel.setOrigin(it) },
                        label = { Text("Pickup Location") },
                        leadingIcon = {
                            Icon(Icons.Default.TripOrigin, contentDescription = null, tint = EmeraldLight)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = SlateBorder,
                            focusedLabelColor = EmeraldLight,
                            unfocusedLabelColor = SlateSubtext,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = SlateSurfaceVariant,
                            unfocusedContainerColor = SlateSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("origin_input")
                    )

                    // Destination Input
                    OutlinedTextField(
                        value = destinationInput,
                        onValueChange = { viewModel.setDestination(it) },
                        label = { Text("Dropoff Destination") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = RedAccent)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = SlateBorder,
                            focusedLabelColor = EmeraldLight,
                            unfocusedLabelColor = SlateSubtext,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = SlateSurfaceVariant,
                            unfocusedContainerColor = SlateSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("destination_input")
                    )

                    // Optional Special Note / AI Note
                    OutlinedTextField(
                        value = itemDesc,
                        onValueChange = { viewModel.setItemDescription(it) },
                        label = { Text("Package Note / Special Instructions (Optional)") },
                        placeholder = { Text("e.g. Fragile package, Gate with yellow flowers") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = SlateSurfaceVariant,
                            unfocusedContainerColor = SlateSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Distance Slider Control
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Distance:", fontSize = 12.sp, color = SlateSubtext)
                            Text(
                                "${String.format("%.1f", distanceKm)} km",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldLight
                            )
                        }
                        Slider(
                            value = distanceKm.toFloat(),
                            onValueChange = { viewModel.setDistance(it.toDouble()) },
                            valueRange = 1f..40f,
                            steps = 39,
                            colors = SliderDefaults.colors(
                                thumbColor = EmeraldPrimary,
                                activeTrackColor = EmeraldPrimary,
                                inactiveTrackColor = SlateSurfaceVariant
                            ),
                            modifier = Modifier.testTag("distance_slider")
                        )
                    }

                    // Vehicle Type Selector (Motorcycle, Sedan, Cargo Van)
                    Text(
                        text = "SELECT VEHICLE FLEET",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateMuted
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VehicleType.values().forEach { v ->
                            val isSelected = selectedVehicle == v
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) EmeraldContainer else SlateSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldPrimary else SlateBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { viewModel.setVehicle(v) }
                                    .testTag("vehicle_selector_${v.name}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = v.iconEmoji, fontSize = 24.sp)
                                    Text(
                                        text = v.displayName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) EmeraldLight else Color.White
                                    )
                                    Text(
                                        text = "₱${v.baseFare.toInt()} base",
                                        fontSize = 10.sp,
                                        color = if (isSelected) EmeraldLight.copy(alpha = 0.8f) else SlateMuted
                                    )
                                }
                            }
                        }
                    }

                    // Payment Method Options
                    Text(
                        text = "PAYMENT METHOD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateMuted
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaymentMethod.values().forEach { pm ->
                            val isSelected = paymentMethod == pm
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) EmeraldPrimary.copy(alpha = 0.2f) else SlateSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldPrimary else SlateBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setPaymentMethod(pm) }
                                    .testTag("payment_method_${pm.name}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = pm.icon, fontSize = 14.sp)
                                    Text(
                                        text = pm.displayName.split(" ").first(),
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) EmeraldLight else SlateSubtext
                                    )
                                }
                            }
                        }
                    }

                    // Live Fare Breakdown Card
                    FareAuditCard(fare = fareQuote)

                    // Booking Submit Button
                    Button(
                        onClick = { viewModel.confirmBooking() },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("confirm_booking_button")
                    ) {
                        Text(
                            text = "Confirm & Request Easy Move • ₱${String.format("%.2f", fareQuote.grossFare)}",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = OnEmerald
                        )
                    }
                }
            }
        }

        // Toggle Past Transactions / Bookings
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showRecentHistory = !showRecentHistory }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = EmeraldLight)
                        Text(
                            text = "Recent Easy Move Bookings (${bookings.size})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Icon(
                        imageVector = if (showRecentHistory) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = SlateSubtext
                    )
                }
            }
        }

        if (showRecentHistory) {
            items(bookings) { b ->
                BookingHistoryItem(booking = b)
            }
        }
    }
}

@Composable
fun ActiveBookingStatusCard(
    booking: Booking,
    onOpenChat: () -> Unit,
    onAdvance: (BookingStatus) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_booking_card")
    ) {
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(EmeraldLight)
                    )
                    Text(
                        text = "ACTIVE TRIP: ${booking.reference}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldDark
                ) {
                    Text(
                        text = booking.status.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = "${booking.vehicleType.displayName} • ${booking.originAddress} ➔ ${booking.destinationAddress}",
                fontSize = 11.sp,
                color = EmeraldLight.copy(alpha = 0.9f)
            )

            // Friendly Rider & Chat Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SlateSurfaceVariant.copy(alpha = 0.8f))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🛵", fontSize = 14.sp)
                    Column {
                        Text(
                            text = booking.driverName ?: "Kardo Dalisay",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Plate: ${booking.driverPlate ?: "NXX-982"}",
                            fontSize = 9.sp,
                            color = SlateSubtext
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = onOpenChat,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("chat_rider_btn")
                    ) {
                        Text("💬 Chat", fontSize = 10.sp, color = OnEmerald, fontWeight = FontWeight.Bold)
                    }

                    if (booking.status != BookingStatus.COMPLETED) {
                        Button(
                            onClick = { onAdvance(BookingStatus.COMPLETED) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldLight),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Done", fontSize = 10.sp, color = OnEmerald, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingHistoryItem(booking: Booking) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SlateSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${booking.reference} • ${booking.serviceType.displayName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "₱${String.format("%.2f", booking.grossFare)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldLight
                )
            }
            Text(
                text = "${booking.originAddress} ➔ ${booking.destinationAddress}",
                fontSize = 11.sp,
                color = SlateSubtext,
                maxLines = 1
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${booking.status.displayName}",
                    fontSize = 10.sp,
                    color = if (booking.status == BookingStatus.COMPLETED) EmeraldLight else GoldAccent
                )
                Text(
                    text = "Driver 88%: ₱${String.format("%.2f", booking.driverNetEarnings)} | Surcharge 12%: ₱${String.format("%.2f", booking.surcharge12Pct)}",
                    fontSize = 10.sp,
                    color = SlateMuted
                )
            }
        }
    }
}

