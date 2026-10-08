package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.EasyMoveViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: EasyMoveViewModel,
    modifier: Modifier = Modifier
) {
    val bookings by viewModel.bookings.collectAsState()
    val metrics = viewModel.getAdminMetrics()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dashboard Title
        item {
            Column {
                Text(
                    text = "EXECUTIVE ANALYTICS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldLight
                )
                Text(
                    text = "Easy Move Enterprise Ledger",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Real-time 12% surcharge revenue & 88% driver net disbursement auditing.",
                    fontSize = 11.sp,
                    color = SlateSubtext
                )
            }
        }

        // Top 3 Analytics Cards (GMV, 12% Platform Revenue, 88% Driver Net)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier.fillMaxWidth().testTag("admin_gmv_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "GROSS MERCHANDISE VALUE (GMV)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateMuted
                    )
                    Text(
                        text = "₱${String.format("%.2f", metrics.grossMerchandiseValueGmv)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "↑ 100% Platform Volume Processed",
                        fontSize = 11.sp,
                        color = EmeraldLight
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Easy Move 12% Revenue Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
                    modifier = Modifier.weight(1f).testTag("admin_surcharge_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "12% PLATFORM SURCHARGE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight
                        )
                        Text(
                            text = "₱${String.format("%.2f", metrics.easymove12PctRevenue)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Net Enterprise Revenue",
                            fontSize = 10.sp,
                            color = EmeraldLight.copy(alpha = 0.8f)
                        )
                    }
                }

                // Driver 88% Net Payouts Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.weight(1f).testTag("admin_driver_net_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "88% DRIVER PAYOUTS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateMuted
                        )
                        Text(
                            text = "₱${String.format("%.2f", metrics.driverNetPayouts88Pct)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldLight
                        )
                        Text(
                            text = "Disbursed to Workers",
                            fontSize = 10.sp,
                            color = SlateSubtext
                        )
                    }
                }
            }
        }

        // Trips and Drivers KPI Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SlateSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null, tint = EmeraldLight)
                        Column {
                            Text("Total Trips", fontSize = 10.sp, color = SlateSubtext)
                            Text("${metrics.totalCompletedTrips} Completed", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SlateSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = BlueAccent)
                        Column {
                            Text("Active Fleet", fontSize = 10.sp, color = SlateSubtext)
                            Text("${metrics.activeOnDutyDrivers} Driver On-Duty", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Ledger Transactions List
        item {
            Text(
                text = "AUDITED TRANSACTIONS LEDGER (${bookings.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateMuted
            )
        }

        items(bookings) { b ->
            AdminLedgerRow(booking = b)
        }
    }
}

@Composable
fun AdminLedgerRow(booking: Booking) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth().testTag("admin_ledger_${booking.reference}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
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
                    Text(
                        text = booking.reference,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SlateSurfaceVariant
                    ) {
                        Text(
                            text = booking.serviceType.displayName,
                            fontSize = 9.sp,
                            color = SlateSubtext,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (booking.status == BookingStatus.COMPLETED) EmeraldContainer else SlateSurfaceVariant
                ) {
                    Text(
                        text = booking.status.displayName,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (booking.status == BookingStatus.COMPLETED) EmeraldLight else GoldAccent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "${booking.originAddress} ➔ ${booking.destinationAddress}",
                fontSize = 11.sp,
                color = SlateSubtext,
                maxLines = 1
            )

            // 12% vs 88% split audit
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SlateSurfaceVariant)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Gross: ₱${String.format("%.2f", booking.grossFare)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Easy Move 12%: ₱${String.format("%.2f", booking.surcharge12Pct)}",
                    fontSize = 11.sp,
                    color = EmeraldLight,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Driver 88%: ₱${String.format("%.2f", booking.driverNetEarnings)}",
                    fontSize = 11.sp,
                    color = BlueAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
