package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun EasyMoveTopBar(
    currentRole: UserRole,
    customerWalletBalance: Double,
    driverWalletBalance: Double,
    onRoleSelected: (UserRole) -> Unit,
    onOpenWallet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SlateBg)
            .border(width = 1.dp, color = SlateBorder)
            .padding(top = 8.dp, bottom = 12.dp)
    ) {
        // Upper Brand Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Fair Fee Label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EM",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = OnEmerald
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Easy Move",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = EmeraldPrimary.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "12% FAIR FEE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Rides • Logistics • Food Delivery",
                        fontSize = 11.sp,
                        color = SlateSubtext
                    )
                }
            }

            // Wallet Quick Pill
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SlateSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onOpenWallet() }
                    .testTag("topbar_wallet_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = EmeraldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    val activeBalance = if (currentRole == UserRole.DRIVER) driverWalletBalance else customerWalletBalance
                    Text(
                        text = "₱${String.format("%.2f", activeBalance)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Role Switcher Tabs (Horizontal Scrollable)
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoleTabItem(
                title = "📱 Customer",
                subtitle = "Ride & Cargo",
                isSelected = currentRole == UserRole.CUSTOMER,
                testTag = "role_tab_customer",
                onClick = { onRoleSelected(UserRole.CUSTOMER) }
            )
            RoleTabItem(
                title = "🛵 Driver Console",
                subtitle = "88% Net Payout",
                isSelected = currentRole == UserRole.DRIVER,
                testTag = "role_tab_driver",
                onClick = { onRoleSelected(UserRole.DRIVER) }
            )
            RoleTabItem(
                title = "🍔 Food Delivery",
                subtitle = "Restaurants & Eats",
                isSelected = currentRole == UserRole.RESTAURANT,
                testTag = "role_tab_food",
                onClick = { onRoleSelected(UserRole.RESTAURANT) }
            )
            RoleTabItem(
                title = "🎨 AI Studio",
                subtitle = "gemini-nano-banana",
                isSelected = currentRole == UserRole.AI_STUDIO,
                testTag = "role_tab_ai_studio",
                onClick = { onRoleSelected(UserRole.AI_STUDIO) }
            )
            RoleTabItem(
                title = "📊 Admin Analytics",
                subtitle = "GMV & 12% Revenue",
                isSelected = currentRole == UserRole.ADMIN,
                testTag = "role_tab_admin",
                onClick = { onRoleSelected(UserRole.ADMIN) }
            )
        }
    }
}

@Composable
private fun RoleTabItem(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) EmeraldPrimary.copy(alpha = 0.2f) else SlateSurfaceVariant,
        label = "tab_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) EmeraldPrimary else SlateBorder,
        label = "tab_border"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = if (isSelected) EmeraldLight else SlateText
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = if (isSelected) EmeraldLight.copy(alpha = 0.8f) else SlateMuted
            )
        }
    }
}
