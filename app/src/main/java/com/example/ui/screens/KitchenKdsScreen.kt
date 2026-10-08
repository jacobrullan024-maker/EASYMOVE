package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.model.ServiceType
import com.example.ui.theme.*
import com.example.ui.viewmodel.EasyMoveViewModel

@Composable
fun KitchenKdsScreen(
    viewModel: EasyMoveViewModel,
    onBackToFood: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookings by viewModel.bookings.collectAsState()
    val foodOrders = bookings.filter { it.serviceType == ServiceType.FOOD }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // KDS Header Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBackToFood,
                        modifier = Modifier.testTag("kds_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = "MERCHANT KDS PORTAL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight
                        )
                        Text(
                            text = "Kitchen Display System",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = EmeraldContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
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
                            text = "STORE OPEN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldLight
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "INCOMING & ACTIVE TICKETS (${foodOrders.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateMuted
            )
        }

        if (foodOrders.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SlateSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No active kitchen orders at the moment", color = SlateSubtext, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(foodOrders) { order ->
                KitchenOrderTicketCard(
                    order = order,
                    onUpdateStatus = { newStatus -> viewModel.updateBookingStatus(order.id, newStatus) }
                )
            }
        }
    }
}

@Composable
fun KitchenOrderTicketCard(
    order: Booking,
    onUpdateStatus: (BookingStatus) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (order.status == BookingStatus.COOKING) GoldAccent else EmeraldPrimary
        ),
        modifier = Modifier.fillMaxWidth().testTag("kds_ticket_${order.reference}")
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
                Column {
                    Text(
                        text = "TICKET #${order.reference}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Customer: ${order.customerName}",
                        fontSize = 11.sp,
                        color = SlateSubtext
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (order.status == BookingStatus.COOKING) GoldAccent.copy(alpha = 0.2f) else EmeraldContainer
                ) {
                    Text(
                        text = order.status.displayName.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = if (order.status == BookingStatus.COOKING) GoldAccent else EmeraldLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Items breakdown
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SlateSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "ITEMS TO PREPARE:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateMuted
                    )
                    Text(
                        text = order.itemDescription ?: "Standard Menu Package",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Destination: ${order.destinationAddress}",
                        fontSize = 11.sp,
                        color = SlateSubtext
                    )
                }
            }

            // Action Buttons based on status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (order.status == BookingStatus.COOKING) {
                    Button(
                        onClick = { onUpdateStatus(BookingStatus.READY_FOR_PICKUP) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("kds_ready_btn")
                    ) {
                        Text(
                            text = "Mark Ready for Rider Pickup",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = OnEmerald
                        )
                    }
                } else if (order.status == BookingStatus.READY_FOR_PICKUP) {
                    Button(
                        onClick = { onUpdateStatus(BookingStatus.IN_TRANSIT) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Handed to Rider (Kardo Dalisay) ➔ In Transit",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                } else if (order.status == BookingStatus.IN_TRANSIT) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SlateSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                            Text("🛵 Order In Transit with Rider", fontSize = 11.sp, color = EmeraldLight)
                        }
                    }
                }
            }
        }
    }
}
