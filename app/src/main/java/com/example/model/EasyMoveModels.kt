package com.example.model

enum class UserRole {
    CUSTOMER,
    DRIVER,
    RESTAURANT,
    ADMIN,
    AI_STUDIO
}

enum class VehicleType(
    val displayName: String,
    val subtitle: String,
    val iconEmoji: String,
    val baseFare: Double,
    val perKmRate: Double
) {
    MOTORCYCLE(
        displayName = "Motorcycle",
        subtitle = "Fast & Express Ride / Parcel",
        iconEmoji = "🛵",
        baseFare = 45.0,
        perKmRate = 10.0
    ),
    SEDAN(
        displayName = "Sedan",
        subtitle = "Comfort 4-Seater Ride",
        iconEmoji = "🚗",
        baseFare = 80.0,
        perKmRate = 15.0
    ),
    EASYMOVE_VAN(
        displayName = "Cargo Van",
        subtitle = "Heavy Move & Bulk Logistics",
        iconEmoji = "🚚",
        baseFare = 250.0,
        perKmRate = 30.0
    )
}

enum class BookingStatus(val displayName: String, val stepIndex: Int) {
    PENDING("Finding Rider...", 0),
    ACCEPTED("Rider Assigned", 1),
    COOKING("Kitchen Preparing", 1),
    READY_FOR_PICKUP("Ready for Pickup", 2),
    IN_TRANSIT("In Transit to Destination", 3),
    COMPLETED("Completed", 4),
    CANCELLED("Cancelled", -1)
}

enum class PaymentMethod(val displayName: String, val icon: String) {
    CASH("Cash on Delivery", "💵"),
    EASYMOVE_WALLET("Easy Move Wallet", "💳"),
    GCASH("GCash E-Wallet", "📱"),
    MAYA("Maya Payment", "🟢")
}

enum class ServiceType(val displayName: String) {
    RIDE("Ride-Hailing"),
    PARCEL("Express Parcel"),
    FOOD("Food Delivery"),
    CARGO("Heavy Cargo")
}

data class FareCalculation(
    val grossFare: Double,
    val surcharge12Pct: Double,
    val driverNet88Pct: Double,
    val distanceKm: Double
)

data class Booking(
    val id: String,
    val reference: String,
    val customerName: String,
    val driverName: String? = null,
    val driverPhone: String? = null,
    val driverPlate: String? = null,
    val serviceType: ServiceType,
    val vehicleType: VehicleType,
    val status: BookingStatus,
    val paymentMethod: PaymentMethod,
    val originAddress: String,
    val destinationAddress: String,
    val distanceKm: Double,
    val grossFare: Double,
    val surcharge12Pct: Double,
    val driverNetEarnings: Double,
    val itemDescription: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val etaMinutes: Int = 12
)

data class Restaurant(
    val id: String,
    val name: String,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val address: String,
    val isOpen: Boolean = true,
    val deliveryTimeEst: String = "20-35 mins",
    val deliveryFee: Double = 49.0
)

data class MenuItem(
    val id: String,
    val restaurantId: String,
    val name: String,
    val category: String,
    val price: Double,
    val description: String,
    val isAvailable: Boolean = true,
    val isPopular: Boolean = false
)

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
)

data class DriverProfile(
    val id: String = "drv_kardo",
    val fullName: String = "Kardo Dalisay",
    val plateNumber: String = "NXX-982",
    val vehicleModel: String = "Yamaha NMAX 155cc / L300",
    val rating: Double = 4.95,
    val totalTrips: Int = 1842,
    val isOnDuty: Boolean = true,
    val walletBalance: Double = 1850.0,
    val todayNetEarnings: Double = 1850.0,
    val completedTripsToday: Int = 14
)

data class PresetRoute(
    val label: String,
    val origin: String,
    val destination: String,
    val distanceKm: Double
)

data class AdminMetrics(
    val grossMerchandiseValueGmv: Double,
    val easymove12PctRevenue: Double,
    val driverNetPayouts88Pct: Double,
    val totalCompletedTrips: Int,
    val activeOnDutyDrivers: Int
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val isFromCustomer: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class AiImagePreset(
    val label: String,
    val prompt: String,
    val category: String,
    val emoji: String
)

