package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.EasyMoveViewModel

@Composable
fun DriverConsoleScreen(
    viewModel: EasyMoveViewModel,
    onOpenWallet: () -> Unit,
    onNavigateToAiStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val driverProfile by viewModel.driverProfile.collectAsState()
    val bookings by viewModel.bookings.collectAsState()

    // Filter incoming and active dispatches for driver
    val activeTrips = bookings.filter {
        (it.status == BookingStatus.PENDING || it.status == BookingStatus.ACCEPTED || it.status == BookingStatus.IN_TRANSIT)
    }
    val completedTrips = bookings.filter { it.status == BookingStatus.COMPLETED }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcoming Friendly Driver Header Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Magandang araw, Kuya Kardo! 🛵",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "Plate: ${driverProfile.plateNumber} • ${driverProfile.vehicleModel}",
                                fontSize = 11.sp,
                                color = SlateSubtext
                            )
                        }

                        Button(
                            onClick = { viewModel.toggleDriverDuty() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (driverProfile.isOnDuty) EmeraldPrimary else SlateSurfaceVariant
                            ),
                            border = if (!driverProfile.isOnDuty) androidx.compose.foundation.BorderStroke(1.dp, SlateBorder) else null,
                            modifier = Modifier.testTag("driver_duty_toggle_button")
                        ) {
                            Text(
                                text = if (driverProfile.isOnDuty) "ONLINE ON DUTY" else "OFFLINE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (driverProfile.isOnDuty) OnEmerald else SlateSubtext
                            )
                        }
                    }

                    // Friendly Rider Quick Shortcuts
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
                                .testTag("rider_shortcut_ai_studio")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("📸 AI Proof", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldLight)
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
                                .testTag("rider_shortcut_chat")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("💬 Chat Customer", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SlateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onOpenWallet() }
                                .testTag("rider_shortcut_wallet")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("💳 Wallet Payout", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldLight)
                            }
                        }
                    }

                    // Net Earnings Box (88% Payout Ledger)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(EmeraldContainer)
                            .border(1.dp, EmeraldPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "TODAY'S NET EARNINGS (88% PAYOUT)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldLight.copy(alpha = 0.85f)
                                    )
                                    Text(
                                        text = "₱${String.format("%.2f", driverProfile.todayNetEarnings)}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }

                                Button(
                                    onClick = onOpenWallet,
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Cash Out", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnEmerald)
                                }
                            }

                            Divider(color = EmeraldPrimary.copy(alpha = 0.3f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Completed Trips: ${driverProfile.completedTripsToday} trips",
                                    fontSize = 11.sp,
                                    color = EmeraldLight
                                )
                                Text(
                                    text = "Wallet: ₱${String.format("%.2f", driverProfile.walletBalance)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Dispatches Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE DISPATCH REQUESTS (${activeTrips.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldLight
                )
                if (!driverProfile.isOnDuty) {
                    Text(
                        text = "Turn duty ON to receive orders",
                        fontSize = 11.sp,
                        color = GoldAccent
                    )
                }
            }
        }

        if (activeTrips.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SlateSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldLight,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "All Dispatches Handled",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "New bookings will appear here instantly with 88% net pay breakdown.",
                                fontSize = 11.sp,
                                color = SlateSubtext
                            )
                        }
                    }
                }
            }
        } else {
            items(activeTrips) { trip ->
                DriverTripDispatchCard(
                    trip = trip,
                    onAccept = { viewModel.acceptDispatch(trip.id) },
                    onAdvance = { newStatus -> viewModel.updateBookingStatus(trip.id, newStatus) }
                )
            }
        }

        // Driver Completed Trips History
        item {
            Text(
                text = "RECENT COMPLETED EARNINGS LEDGER",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateMuted
            )
        }

        items(completedTrips) { trip ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SlateSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "${trip.reference} • ${trip.serviceType.displayName}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${trip.distanceKm} km • ${trip.originAddress.take(20)}...",
                            fontSize = 11.sp,
                            color = SlateSubtext
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+₱${String.format("%.2f", trip.driverNetEarnings)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldLight
                        )
                        Text(
                            text = "88% Net Payout",
                            fontSize = 10.sp,
                            color = SlateMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DriverTripDispatchCard(
    trip: Booking,
    onAccept: () -> Unit,
    onAdvance: (BookingStatus) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("driver_dispatch_card_${trip.id}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header with Ref and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = EmeraldPrimary
                    ) {
                        Text(
                            text = "DISPATCH",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = OnEmerald,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Ref: ${trip.reference}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldContainer
                ) {
                    Text(
                        text = trip.status.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Route details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SlateSurfaceVariant)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "📍 Pickup: ${trip.originAddress}",
                    fontSize = 11.sp,
                    color = Color.White
                )
                Text(
                    text = "🎯 Dropoff: ${trip.destinationAddress}",
                    fontSize = 11.sp,
                    color = SlateSubtext
                )
                Text(
                    text = "Trip Distance: ${trip.distanceKm} km • ${trip.vehicleType.displayName}",
                    fontSize = 10.sp,
                    color = EmeraldLight
                )
                if (!trip.itemDescription.isNullOrBlank()) {
                    Text(
                        text = "Package / Order: ${trip.itemDescription}",
                        fontSize = 10.sp,
                        color = SlateMuted
                    )
                }
            }

            // Payout calculation box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(EmeraldContainer)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Net Earnings (88%):",
                        fontSize = 11.sp,
                        color = EmeraldLight.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "Platform fee (12%): ₱${String.format("%.2f", trip.surcharge12Pct)}",
                        fontSize = 10.sp,
                        color = EmeraldLight.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = "₱${String.format("%.2f", trip.driverNetEarnings)}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            // Action Buttons
            if (trip.status == BookingStatus.PENDING) {
                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("driver_accept_trip_button")
                ) {
                    Text(
                        text = "Accept Dispatch & Navigate",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = OnEmerald
                    )
                }
            } else if (trip.status == BookingStatus.ACCEPTED) {
                Button(
                    onClick = { onAdvance(BookingStatus.IN_TRANSIT) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Picked Up Customer/Item ➔ Start Trip",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = OnEmerald
                    )
                }
            } else if (trip.status == BookingStatus.IN_TRANSIT) {
                Button(
                    onClick = { onAdvance(BookingStatus.COMPLETED) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Arrived at Destination ➔ Complete Trip",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = OnEmerald
                    )
                }
            }
        }
    }
}

