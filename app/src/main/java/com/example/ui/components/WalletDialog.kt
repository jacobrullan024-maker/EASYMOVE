package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun WalletDialog(
    currentBalance: Double,
    isDriver: Boolean = false,
    onDismiss: () -> Unit,
    onTopUp: (Double) -> Unit
) {
    var selectedAmount by remember { mutableStateOf(500.0) }
    var selectedChannel by remember { mutableStateOf("GCash") }

    val amounts = listOf(200.0, 500.0, 1000.0, 2000.0)
    val channels = listOf("GCash", "Maya", "BDO / BPI", "Visa / Master")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("wallet_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
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
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Wallet",
                                tint = EmeraldLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isDriver) "Driver Payout Wallet" else "Easy Move Wallet",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Instant Top-Up & Payouts",
                                fontSize = 11.sp,
                                color = SlateSubtext
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_wallet_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SlateSubtext
                        )
                    }
                }

                // Balance Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(EmeraldContainer)
                        .border(1.dp, EmeraldPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "AVAILABLE BALANCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "₱${String.format("%.2f", currentBalance)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = if (isDriver) "Includes 88% net earnings from all trips" else "Accepted across rides, food & cargo",
                            fontSize = 11.sp,
                            color = EmeraldLight
                        )
                    }
                }

                // Amount Selection Chips
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SELECT AMOUNT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateMuted
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        amounts.forEach { amt ->
                            val isSelected = selectedAmount == amt
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) EmeraldPrimary.copy(alpha = 0.25f) else SlateSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldPrimary else SlateBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedAmount = amt }
                                    .testTag("topup_amount_${amt.toInt()}")
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+₱${amt.toInt()}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) EmeraldLight else SlateText
                                    )
                                }
                            }
                        }
                    }
                }

                // Payment Channel Selection
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        channels.forEach { ch ->
                            val isSelected = selectedChannel == ch
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) SlateSurfaceVariant else SlateBg,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldLight else SlateBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedChannel = ch }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ch,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else SlateSubtext
                                    )
                                }
                            }
                        }
                    }
                }

                // Submit Top Up Button
                Button(
                    onClick = {
                        onTopUp(selectedAmount)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_topup_button")
                ) {
                    Text(
                        text = "Top-Up ₱${String.format("%.2f", selectedAmount)} via $selectedChannel",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = OnEmerald
                    )
                }
            }
        }
    }
}
