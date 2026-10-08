package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FareCalculation
import com.example.ui.theme.*

@Composable
fun FareAuditCard(
    fare: FareCalculation,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("fare_audit_card")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Fair Fee",
                        tint = EmeraldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Fair Worker Pricing Audit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SlateSurfaceVariant
                ) {
                    Text(
                        text = "${fare.distanceKm} km",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Fare Rows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gross Total Fare:",
                    fontSize = 13.sp,
                    color = SlateSubtext
                )
                Text(
                    text = "₱${String.format("%.2f", fare.grossFare)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Divider(color = SlateBorder.copy(alpha = 0.6f))

            // 88% Net Payout and 12% Surcharge Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(EmeraldContainer)
                    .border(1.dp, EmeraldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Rider Net Payout (88%)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldLight
                            )
                            Text(
                                text = "Guaranteed direct to driver's wallet",
                                fontSize = 10.sp,
                                color = EmeraldLight.copy(alpha = 0.75f)
                            )
                        }
                        Text(
                            text = "₱${String.format("%.2f", fare.driverNet88Pct)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldLight
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Easy Move Platform Surcharge (12%):",
                            fontSize = 11.sp,
                            color = EmeraldLight.copy(alpha = 0.85f)
                        )
                        Text(
                            text = "₱${String.format("%.2f", fare.surcharge12Pct)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }
}
