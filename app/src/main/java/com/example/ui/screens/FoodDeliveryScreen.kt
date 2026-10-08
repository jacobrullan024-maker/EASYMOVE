package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.EasyMoveViewModel

@Composable
fun FoodDeliveryScreen(
    viewModel: EasyMoveViewModel,
    modifier: Modifier = Modifier
) {
    val restaurants by viewModel.restaurants.collectAsState()
    val menuItems by viewModel.menuItems.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val selectedRestaurant by viewModel.selectedRestaurant.collectAsState()
    val selectedCategory by viewModel.selectedFoodCategory.collectAsState()

    var showKitchenKdsMode by remember { mutableStateOf(false) }
    var deliveryAddress by remember { mutableStateOf("Emerald Tower, Ortigas Center, Pasig City") }
    var showCartDialog by remember { mutableStateOf(false) }

    val activeRest = selectedRestaurant ?: restaurants.firstOrNull() ?: return

    val currentRestItems = menuItems.filter { it.restaurantId == activeRest.id }
    val categories = listOf("All") + currentRestItems.map { it.category }.distinct()

    val filteredItems = if (selectedCategory == "All") {
        currentRestItems
    } else {
        currentRestItems.filter { it.category == selectedCategory }
    }

    val cartTotal = cart.sumOf { it.menuItem.price * it.quantity }
    val cartCount = cart.sumOf { it.quantity }

    if (showKitchenKdsMode) {
        KitchenKdsScreen(
            viewModel = viewModel,
            onBackToFood = { showKitchenKdsMode = false }
        )
        return
    }

    Box(modifier = modifier.fillMaxSize().background(SlateBg)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar Switcher between Customer Ordering & Kitchen KDS Portal
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FOOD DELIVERY & RESTAURANTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldLight
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SlateSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { showKitchenKdsMode = true }
                            .testTag("switch_to_kds_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🍳", fontSize = 12.sp)
                            Text(
                                text = "Merchant KDS Portal",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldLight
                            )
                        }
                    }
                }
            }

            // Partner Restaurant Cards Carousel
            item {
                Text(
                    text = "PARTNER MERCHANTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateMuted
                )
                val restScroll = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(restScroll),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    restaurants.forEach { rest ->
                        val isSelected = rest.id == activeRest.id
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) EmeraldContainer else SlateSurface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) EmeraldPrimary else SlateBorder
                            ),
                            modifier = Modifier
                                .width(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { viewModel.selectRestaurant(rest) }
                                .testTag("select_rest_${rest.id}")
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
                                        text = rest.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = rest.category,
                                    fontSize = 10.sp,
                                    color = SlateSubtext,
                                    maxLines = 1
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(12.dp))
                                    Text("${rest.rating} (${rest.reviewCount})", fontSize = 10.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                                    Text("• ${rest.deliveryTimeEst}", fontSize = 10.sp, color = SlateMuted)
                                }
                            }
                        }
                    }
                }
            }

            // Active Restaurant Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
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
                            Column {
                                Text(
                                    text = activeRest.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = activeRest.address,
                                    fontSize = 11.sp,
                                    color = SlateSubtext
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EmeraldContainer,
                                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "OPEN NOW",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldLight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Category Filter Pills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) EmeraldPrimary else SlateSurfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setFoodCategory(cat) }
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) OnEmerald else SlateText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Menu Items List
            items(filteredItems) { item ->
                FoodMenuItemCard(
                    item = item,
                    cartQuantity = cart.find { it.menuItem.id == item.id }?.quantity ?: 0,
                    onAdd = { viewModel.addToCart(item) },
                    onDecrease = { viewModel.decreaseCartItem(item) }
                )
            }
        }

        // Floating Cart Bar if cart has items
        if (cartCount > 0) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EmeraldPrimary,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { showCartDialog = true }
                    .testTag("floating_cart_bar")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(OnEmerald),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$cartCount",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = EmeraldLight
                            )
                        }
                        Column {
                            Text(
                                text = "View Cart & Checkout",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = OnEmerald
                            )
                            Text(
                                text = "12% Fair Fee applied to delivery",
                                fontSize = 10.sp,
                                color = OnEmerald.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Text(
                        text = "₱${String.format("%.2f", cartTotal)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = OnEmerald
                    )
                }
            }
        }

        // Cart & Checkout Dialog Sheet
        if (showCartDialog) {
            FoodCartSheet(
                restaurant = activeRest,
                cart = cart,
                deliveryAddress = deliveryAddress,
                onAddressChange = { deliveryAddress = it },
                onAdd = { viewModel.addToCart(it) },
                onDecrease = { viewModel.decreaseCartItem(it) },
                onDismiss = { showCartDialog = false },
                onCheckout = {
                    viewModel.checkoutFoodOrder(deliveryAddress)
                    showCartDialog = false
                }
            )
        }
    }
}

@Composable
fun FoodMenuItemCard(
    item: MenuItem,
    cartQuantity: Int,
    onAdd: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = Modifier.fillMaxWidth().testTag("menu_item_${item.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (item.isPopular) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GoldAccent.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "POPULAR CHOICE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    color = SlateSubtext,
                    maxLines = 2
                )
                Text(
                    text = "₱${String.format("%.2f", item.price)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldLight
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (cartQuantity == 0) {
                Button(
                    onClick = onAdd,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_item_${item.id}")
                ) {
                    Text("+ Add", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldLight)
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(EmeraldContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                        Text("-", color = EmeraldLight, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }
                    Text(
                        text = "$cartQuantity",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onAdd, modifier = Modifier.size(28.dp)) {
                        Text("+", color = EmeraldLight, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCartSheet(
    restaurant: Restaurant,
    cart: List<CartItem>,
    deliveryAddress: String,
    onAddressChange: (String) -> Unit,
    onAdd: (MenuItem) -> Unit,
    onDecrease: (MenuItem) -> Unit,
    onDismiss: () -> Unit,
    onCheckout: () -> Unit
) {
    val itemsTotal = cart.sumOf { it.menuItem.price * it.quantity }
    val deliveryFee = 75.0 // Motorcycle base 45 + 3km*10
    val platformSurcharge12 = (deliveryFee * 0.12)
    val driverNet = deliveryFee - platformSurcharge12
    val grandTotal = itemsTotal + deliveryFee

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onCheckout,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("cart_checkout_button")
            ) {
                Text(
                    text = "Place Food Order • ₱${String.format("%.2f", grandTotal)}",
                    fontWeight = FontWeight.Black,
                    color = OnEmerald
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SlateSubtext)
            }
        },
        title = {
            Text(
                text = "Order from ${restaurant.name}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = deliveryAddress,
                    onValueChange = onAddressChange,
                    label = { Text("Delivery Address") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = SlateBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(color = SlateBorder)

                // Items in Cart
                cart.forEach { c ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${c.quantity}x ${c.menuItem.name}",
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "₱${String.format("%.2f", c.menuItem.price * c.quantity)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight
                        )
                    }
                }

                Divider(color = SlateBorder)

                // Breakdown
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Food Items Subtotal:", fontSize = 11.sp, color = SlateSubtext)
                    Text("₱${String.format("%.2f", itemsTotal)}", fontSize = 11.sp, color = Color.White)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Express Rider Delivery:", fontSize = 11.sp, color = SlateSubtext)
                    Text("₱${String.format("%.2f", deliveryFee)}", fontSize = 11.sp, color = Color.White)
                }

                // 12% Fair Fee breakdown container
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "• Rider 88% Net Pay: ₱${String.format("%.2f", driverNet)}",
                            fontSize = 10.sp,
                            color = EmeraldLight,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "• Easy Move 12% Platform Fee: ₱${String.format("%.2f", platformSurcharge12)}",
                            fontSize = 10.sp,
                            color = EmeraldLight.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        },
        containerColor = SlateSurface
    )
}
