package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TwoWheeler
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
import com.example.model.ChatMessage
import com.example.ui.theme.*

@Composable
fun RiderChatDialog(
    messages: List<ChatMessage>,
    riderName: String = "Kardo Dalisay",
    onDismiss: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }

    val quickReplies = listOf(
        "Nandito na po ako sa lobby!",
        "Ingat po sa biyahe Kuya! 🛵",
        "Pakiiwan nalang po sa guard.",
        "Salamat po marami!"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .testTag("rider_chat_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
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
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(EmeraldContainer)
                                .border(1.dp, EmeraldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(
                                text = riderName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldLight))
                                Text("Online • Yamaha NMAX", fontSize = 10.sp, color = EmeraldLight)
                            }
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateSubtext)
                    }
                }

                Divider(color = SlateBorder, modifier = Modifier.padding(vertical = 8.dp))

                // Messages List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages) { msg ->
                        ChatBubbleItem(message = msg)
                    }
                }

                // Quick Replies Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickReplies.take(2).forEach { qr ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SlateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSendMessage(qr) }
                        ) {
                            Text(
                                text = qr,
                                fontSize = 10.sp,
                                color = EmeraldLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Input Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Mensahe kay Kuya Rider...", fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = SlateSurfaceVariant,
                            unfocusedContainerColor = SlateSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                onSendMessage(textInput)
                                textInput = ""
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        contentPadding = PaddingValues(12.dp),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = OnEmerald, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage) {
    val isCustomer = message.isFromCustomer
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCustomer) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 240.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isCustomer) 14.dp else 2.dp,
                        bottomEnd = if (isCustomer) 2.dp else 14.dp
                    )
                )
                .background(if (isCustomer) EmeraldContainer else SlateSurfaceVariant)
                .border(
                    1.dp,
                    if (isCustomer) EmeraldPrimary.copy(alpha = 0.5f) else SlateBorder,
                    RoundedCornerShape(14.dp)
                )
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = message.senderName,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCustomer) EmeraldLight else GoldAccent
                )
                Text(
                    text = message.text,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}
