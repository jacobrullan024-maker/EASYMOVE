package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UserRole
import com.example.ui.components.EasyMoveTopBar
import com.example.ui.components.RiderChatDialog
import com.example.ui.components.WalletDialog
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.EasyMoveViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                EasyMoveApp()
            }
        }
    }
}

@Composable
fun EasyMoveApp(
    viewModel: EasyMoveViewModel = viewModel()
) {
    val currentRole by viewModel.selectedRole.collectAsState()
    val customerWallet by viewModel.customerWallet.collectAsState()
    val driverProfile by viewModel.driverProfile.collectAsState()
    val notificationMessage by viewModel.notificationMessage.collectAsState()
    val isChatOpen by viewModel.isChatOpen.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    var showWalletModal by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(notificationMessage) {
        notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissNotification()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SlateBg,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = EmeraldPrimary,
                    contentColor = OnEmerald,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = data.visuals.message,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = OnEmerald
                    )
                }
            }
        },
        topBar = {
            EasyMoveTopBar(
                currentRole = currentRole,
                customerWalletBalance = customerWallet,
                driverWalletBalance = driverProfile.walletBalance,
                onRoleSelected = { viewModel.selectRole(it) },
                onOpenWallet = { showWalletModal = true },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SlateSurface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = currentRole == UserRole.CUSTOMER,
                    onClick = { viewModel.selectRole(UserRole.CUSTOMER) },
                    icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Customer") },
                    label = { Text("Ride & Cargo", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnEmerald,
                        selectedTextColor = EmeraldLight,
                        indicatorColor = EmeraldPrimary,
                        unselectedIconColor = SlateSubtext,
                        unselectedTextColor = SlateSubtext
                    ),
                    modifier = Modifier.testTag("nav_item_customer")
                )

                NavigationBarItem(
                    selected = currentRole == UserRole.DRIVER,
                    onClick = { viewModel.selectRole(UserRole.DRIVER) },
                    icon = { Icon(Icons.Default.TwoWheeler, contentDescription = "Driver") },
                    label = { Text("Driver (88%)", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnEmerald,
                        selectedTextColor = EmeraldLight,
                        indicatorColor = EmeraldPrimary,
                        unselectedIconColor = SlateSubtext,
                        unselectedTextColor = SlateSubtext
                    ),
                    modifier = Modifier.testTag("nav_item_driver")
                )

                NavigationBarItem(
                    selected = currentRole == UserRole.RESTAURANT,
                    onClick = { viewModel.selectRole(UserRole.RESTAURANT) },
                    icon = { Icon(Icons.Default.Restaurant, contentDescription = "Food Delivery") },
                    label = { Text("Food & KDS", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnEmerald,
                        selectedTextColor = EmeraldLight,
                        indicatorColor = EmeraldPrimary,
                        unselectedIconColor = SlateSubtext,
                        unselectedTextColor = SlateSubtext
                    ),
                    modifier = Modifier.testTag("nav_item_food")
                )

                NavigationBarItem(
                    selected = currentRole == UserRole.AI_STUDIO,
                    onClick = { viewModel.selectRole(UserRole.AI_STUDIO) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Studio") },
                    label = { Text("AI Studio", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnEmerald,
                        selectedTextColor = EmeraldLight,
                        indicatorColor = EmeraldPrimary,
                        unselectedIconColor = SlateSubtext,
                        unselectedTextColor = SlateSubtext
                    ),
                    modifier = Modifier.testTag("nav_item_ai_studio")
                )

                NavigationBarItem(
                    selected = currentRole == UserRole.ADMIN,
                    onClick = { viewModel.selectRole(UserRole.ADMIN) },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Admin") },
                    label = { Text("Analytics", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnEmerald,
                        selectedTextColor = EmeraldLight,
                        indicatorColor = EmeraldPrimary,
                        unselectedIconColor = SlateSubtext,
                        unselectedTextColor = SlateSubtext
                    ),
                    modifier = Modifier.testTag("nav_item_admin")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.CUSTOMER -> {
                    CustomerRideScreen(
                        viewModel = viewModel,
                        onNavigateToAiStudio = { viewModel.selectRole(UserRole.AI_STUDIO) }
                    )
                }
                UserRole.DRIVER -> {
                    DriverConsoleScreen(
                        viewModel = viewModel,
                        onOpenWallet = { showWalletModal = true },
                        onNavigateToAiStudio = { viewModel.selectRole(UserRole.AI_STUDIO) }
                    )
                }
                UserRole.RESTAURANT -> {
                    FoodDeliveryScreen(viewModel = viewModel)
                }
                UserRole.AI_STUDIO -> {
                    AiStudioScreen(viewModel = viewModel)
                }
                UserRole.ADMIN -> {
                    AdminDashboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    if (showWalletModal) {
        val isDriver = currentRole == UserRole.DRIVER
        WalletDialog(
            currentBalance = if (isDriver) driverProfile.walletBalance else customerWallet,
            isDriver = isDriver,
            onDismiss = { showWalletModal = false },
            onTopUp = { amt ->
                if (isDriver) {
                    viewModel.topUpDriverWallet(amt)
                } else {
                    viewModel.topUpCustomerWallet(amt)
                }
            }
        )
    }

    if (isChatOpen) {
        RiderChatDialog(
            messages = chatMessages,
            riderName = driverProfile.fullName,
            onDismiss = { viewModel.closeChat() },
            onSendMessage = { text -> viewModel.sendChatMessage(text) }
        )
    }
}
