package com.example.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiImageService
import com.example.ai.ImageAiResult
import com.example.data.EasyMoveRepository
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class EasyMoveViewModel(
    private val repository: EasyMoveRepository = EasyMoveRepository(),
    private val geminiAiService: GeminiImageService = GeminiImageService()
) : ViewModel() {

    // Active Navigation / Role Tab
    private val _selectedRole = MutableStateFlow(UserRole.CUSTOMER)
    val selectedRole: StateFlow<UserRole> = _selectedRole.asStateFlow()

    fun selectRole(role: UserRole) {
        _selectedRole.value = role
    }

    // Customer Ride / Delivery Booking State
    private val _selectedVehicle = MutableStateFlow(VehicleType.MOTORCYCLE)
    val selectedVehicle: StateFlow<VehicleType> = _selectedVehicle.asStateFlow()

    private val _selectedService = MutableStateFlow(ServiceType.RIDE)
    val selectedService: StateFlow<ServiceType> = _selectedService.asStateFlow()

    private val _originInput = MutableStateFlow("Ortigas Center, Pasig City")
    val originInput: StateFlow<String> = _originInput.asStateFlow()

    private val _destinationInput = MutableStateFlow("BGC High Street, Taguig City")
    val destinationInput: StateFlow<String> = _destinationInput.asStateFlow()

    private val _distanceKm = MutableStateFlow(6.8)
    val distanceKm: StateFlow<Double> = _distanceKm.asStateFlow()

    private val _paymentMethod = MutableStateFlow(PaymentMethod.EASYMOVE_WALLET)
    val paymentMethod: StateFlow<PaymentMethod> = _paymentMethod.asStateFlow()

    private val _itemDescription = MutableStateFlow("")
    val itemDescription: StateFlow<String> = _itemDescription.asStateFlow()

    // Real-time computed fare quote
    val fareQuote: StateFlow<FareCalculation> = combine(
        _selectedVehicle,
        _distanceKm
    ) { vehicle, distance ->
        EasyMoveRepository.computeFare(vehicle, distance)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EasyMoveRepository.computeFare(VehicleType.MOTORCYCLE, 6.8)
    )

    // Data from Repository
    val bookings: StateFlow<List<Booking>> = repository.bookings
    val activeBooking: StateFlow<Booking?> = repository.activeBooking
    val restaurants: StateFlow<List<Restaurant>> = repository.restaurants
    val menuItems: StateFlow<List<MenuItem>> = repository.menuItems
    val cart: StateFlow<List<CartItem>> = repository.cart
    val driverProfile: StateFlow<DriverProfile> = repository.driverProfile
    val customerWallet: StateFlow<Double> = repository.customerWalletBalance

    // Selected Restaurant for Food screen
    private val _selectedRestaurant = MutableStateFlow<Restaurant?>(null)
    val selectedRestaurant: StateFlow<Restaurant?> = _selectedRestaurant.asStateFlow()

    private val _selectedFoodCategory = MutableStateFlow("All")
    val selectedFoodCategory: StateFlow<String> = _selectedFoodCategory.asStateFlow()

    // Booking Feedback Banner
    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage: StateFlow<String?> = _notificationMessage.asStateFlow()

    // --- AI IMAGE CREATION & EDITING STATE (gemini-nano-banana-2.1) ---
    private val _aiPromptInput = MutableStateFlow("Emerald green residential gate with yellow bougainvillea flowers for delivery landmark")
    val aiPromptInput: StateFlow<String> = _aiPromptInput.asStateFlow()

    private val _aiAspectRatio = MutableStateFlow("1:1")
    val aiAspectRatio: StateFlow<String> = _aiAspectRatio.asStateFlow()

    private val _aiResolution = MutableStateFlow("1K")
    val aiResolution: StateFlow<String> = _aiResolution.asStateFlow()

    private val _isGeneratingAiImage = MutableStateFlow(false)
    val isGeneratingAiImage: StateFlow<Boolean> = _isGeneratingAiImage.asStateFlow()

    private val _aiImageResult = MutableStateFlow<ImageAiResult.Success?>(null)
    val aiImageResult: StateFlow<ImageAiResult.Success?> = _aiImageResult.asStateFlow()

    private val _inputBitmapToEdit = MutableStateFlow<Bitmap?>(null)
    val inputBitmapToEdit: StateFlow<Bitmap?> = _inputBitmapToEdit.asStateFlow()

    private val _aiStudioTab = MutableStateFlow("CREATE") // "CREATE" or "EDIT"
    val aiStudioTab: StateFlow<String> = _aiStudioTab.asStateFlow()

    // --- RIDER CHAT SIMULATION STATE ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "msg_1",
                senderName = "Kardo Dalisay",
                text = "Magandang araw boss! On the way na po ako sa pickup location.",
                isFromCustomer = false,
                timestamp = System.currentTimeMillis() - 120000
            ),
            ChatMessage(
                id = "msg_2",
                senderName = "Juan Dela Cruz",
                text = "Salamat Kuya! Sa tapat po ako ng lobby.",
                isFromCustomer = true,
                timestamp = System.currentTimeMillis() - 60000
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatOpen = MutableStateFlow(false)
    val isChatOpen: StateFlow<Boolean> = _isChatOpen.asStateFlow()

    // Customer Actions
    fun setVehicle(vehicle: VehicleType) {
        _selectedVehicle.value = vehicle
        if (vehicle == VehicleType.EASYMOVE_VAN) {
            _selectedService.value = ServiceType.CARGO
        }
    }

    fun setServiceType(service: ServiceType) {
        _selectedService.value = service
        if (service == ServiceType.CARGO) {
            _selectedVehicle.value = VehicleType.EASYMOVE_VAN
        }
    }

    fun setOrigin(origin: String) {
        _originInput.value = origin
    }

    fun setDestination(destination: String) {
        _destinationInput.value = destination
    }

    fun setDistance(distance: Double) {
        _distanceKm.value = distance.coerceIn(0.5, 100.0)
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _paymentMethod.value = method
    }

    fun setItemDescription(desc: String) {
        _itemDescription.value = desc
    }

    fun applyPresetRoute(preset: PresetRoute) {
        _originInput.value = preset.origin
        _destinationInput.value = preset.destination
        _distanceKm.value = preset.distanceKm
        if (preset.label.contains("Cargo")) {
            setVehicle(VehicleType.EASYMOVE_VAN)
        }
    }

    fun confirmBooking() {
        val booking = repository.createRideBooking(
            vehicleType = _selectedVehicle.value,
            serviceType = _selectedService.value,
            origin = _originInput.value,
            destination = _destinationInput.value,
            distanceKm = _distanceKm.value,
            paymentMethod = _paymentMethod.value,
            itemDesc = _itemDescription.value.ifBlank { null }
        )
        _notificationMessage.value = "Booking Confirmed! Ref: ${booking.reference} • Fare: ₱${String.format("%.2f", booking.grossFare)}"
    }

    fun dismissNotification() {
        _notificationMessage.value = null
    }

    // Driver Console Actions
    fun toggleDriverDuty() {
        repository.toggleDriverDuty()
        val isOn = repository.driverProfile.value.isOnDuty
        _notificationMessage.value = if (isOn) "Driver duty status: ONLINE ON DUTY" else "Driver duty status: OFFLINE"
    }

    fun acceptDispatch(bookingId: String) {
        repository.acceptBookingByDriver(bookingId)
        _notificationMessage.value = "Trip Accepted! Navigating to customer pickup location."
    }

    fun updateBookingStatus(bookingId: String, newStatus: BookingStatus) {
        repository.advanceBookingStatus(bookingId, newStatus)
        if (newStatus == BookingStatus.COMPLETED) {
            _notificationMessage.value = "Trip Completed! Rider received 88% net earnings."
        } else {
            _notificationMessage.value = "Status updated to ${newStatus.displayName}"
        }
    }

    // Food & KDS Actions
    fun selectRestaurant(restaurant: Restaurant?) {
        _selectedRestaurant.value = restaurant
    }

    fun setFoodCategory(category: String) {
        _selectedFoodCategory.value = category
    }

    fun addToCart(item: MenuItem) {
        repository.addToCart(item)
    }

    fun decreaseCartItem(item: MenuItem) {
        repository.decreaseCartItem(item)
    }

    fun clearCart() {
        repository.clearCart()
    }

    fun checkoutFoodOrder(destinationAddress: String) {
        val currentRest = _selectedRestaurant.value ?: repository.restaurants.value.first()
        val currentCart = repository.cart.value
        if (currentCart.isEmpty()) return

        val order = repository.placeFoodOrder(
            restaurant = currentRest,
            paymentMethod = _paymentMethod.value,
            destinationAddress = destinationAddress,
            items = currentCart
        )
        _notificationMessage.value = "Food Order Placed! Ref: ${order.reference} • Sent to Kitchen KDS"
    }

    // Wallet Actions
    fun topUpCustomerWallet(amount: Double) {
        repository.topUpCustomerWallet(amount)
        _notificationMessage.value = "Wallet Top-up Successful: +₱${String.format("%.2f", amount)}"
    }

    fun topUpDriverWallet(amount: Double) {
        repository.topUpDriverWallet(amount)
        _notificationMessage.value = "Driver Balance Top-up: +₱${String.format("%.2f", amount)}"
    }

    fun getAdminMetrics(): AdminMetrics {
        return repository.getAdminMetrics()
    }

    // --- AI STUDIO (Create & Edit Images using gemini-nano-banana-2.1) ---
    fun setAiPrompt(prompt: String) {
        _aiPromptInput.value = prompt
    }

    fun setAiAspectRatio(ratio: String) {
        _aiAspectRatio.value = ratio
    }

    fun setAiResolution(res: String) {
        _aiResolution.value = res
    }

    fun setAiStudioTab(tab: String) {
        _aiStudioTab.value = tab
    }

    fun setInputBitmapToEdit(bitmap: Bitmap?) {
        _inputBitmapToEdit.value = bitmap
    }

    fun generateOrEditAiImage() {
        val prompt = _aiPromptInput.value.trim()
        if (prompt.isBlank()) {
            _notificationMessage.value = "Please enter a descriptive prompt for gemini-nano-banana-2.1"
            return
        }

        _isGeneratingAiImage.value = true
        viewModelScope.launch {
            val result = geminiAiService.generateOrEditImage(
                prompt = prompt,
                inputBitmap = if (_aiStudioTab.value == "EDIT") _inputBitmapToEdit.value else null,
                aspectRatio = _aiAspectRatio.value,
                imageSize = _aiResolution.value
            )

            _isGeneratingAiImage.value = false
            when (result) {
                is ImageAiResult.Success -> {
                    _aiImageResult.value = result
                    _notificationMessage.value = "✨ Image successfully created with gemini-nano-banana-2.1!"
                }
                is ImageAiResult.Error -> {
                    _notificationMessage.value = "AI Notice: ${result.message}"
                }
            }
        }
    }

    fun clearAiImage() {
        _aiImageResult.value = null
        _inputBitmapToEdit.value = null
    }

    // --- CHAT WITH RIDER ---
    fun openChat() {
        _isChatOpen.value = true
    }

    fun closeChat() {
        _isChatOpen.value = false
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val current = _chatMessages.value.toMutableList()
        val customerMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderName = "Juan Dela Cruz",
            text = text,
            isFromCustomer = true
        )
        current.add(customerMsg)
        _chatMessages.value = current

        // Simulate friendly response from rider Kuya Kardo
        viewModelScope.launch {
            delay(1200)
            val riderReply = when {
                text.contains("tagal", true) || text.contains("traffic", true) ->
                    "Opo boss, medyo mabigat po traffic sa may flyover pero malapit na po ako! 🛵"
                text.contains("san", true) || text.contains("where", true) ->
                    "Nasa Ortigas Ave na po ako, 2 minutes na lang po! 📍"
                text.contains("ingat", true) || text.contains("salamat", true) ->
                    "Maraming salamat po boss! Drive safe lagi tayo 👍"
                else ->
                    "Copy that boss! Ingatan ko po ang delivery nyo. Nandyan na po ako saglit lang po! 🌟"
            }
            val replyMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderName = "Kardo Dalisay (Rider)",
                text = riderReply,
                isFromCustomer = false
            )
            _chatMessages.value = _chatMessages.value + replyMsg
        }
    }

    fun triggerSafetySos() {
        _notificationMessage.value = "🛡️ Easy Move Safety Alert Triggered: Emergency contacts & Live GPS shared with 24/7 Security Desk!"
    }
}
