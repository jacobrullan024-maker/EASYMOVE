package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.math.roundToInt

class EasyMoveRepository {

    companion object {
        val PRESET_ROUTES = listOf(
            PresetRoute(
                label = "Ortigas ➔ BGC High Street",
                origin = "Ortigas Center, Pasig City",
                destination = "BGC High Street, Taguig City",
                distanceKm = 6.8
            ),
            PresetRoute(
                label = "Makati CBD ➔ Greenhills",
                origin = "Ayala Ave, Makati CBD",
                destination = "Greenhills Shopping Center, San Juan",
                distanceKm = 8.5
            ),
            PresetRoute(
                label = "Warehouse 4B ➔ Greenhills (Cargo)",
                origin = "Warehouse 4B, Taytay, Rizal",
                destination = "Greenhills Commercial Hub, San Juan",
                distanceKm = 14.5
            ),
            PresetRoute(
                label = "MOA Pasay ➔ QC Memorial Circle",
                origin = "SM Mall of Asia, Pasay City",
                destination = "Quezon Memorial Circle, Quezon City",
                distanceKm = 18.2
            )
        )

        fun computeFare(vehicleType: VehicleType, distanceKm: Double): FareCalculation {
            val grossFare = vehicleType.baseFare + (distanceKm * vehicleType.perKmRate)
            val roundedGross = (grossFare * 100).roundToInt() / 100.0
            val surcharge12Pct = ((roundedGross * 0.12) * 100).roundToInt() / 100.0
            val driverNet88Pct = ((roundedGross - surcharge12Pct) * 100).roundToInt() / 100.0
            return FareCalculation(
                grossFare = roundedGross,
                surcharge12Pct = surcharge12Pct,
                driverNet88Pct = driverNet88Pct,
                distanceKm = (distanceKm * 10).roundToInt() / 10.0
            )
        }
    }

    // Default Seed Bookings matching FastAPI backend
    private val initialBookings = listOf(
        Booking(
            id = "bk_seed_1",
            reference = "EM-892101",
            customerName = "Juan Dela Cruz",
            driverName = "Kardo Dalisay (Rider)",
            driverPhone = "+639178889999",
            driverPlate = "NXX-982",
            serviceType = ServiceType.RIDE,
            vehicleType = VehicleType.MOTORCYCLE,
            status = BookingStatus.COMPLETED,
            paymentMethod = PaymentMethod.EASYMOVE_WALLET,
            originAddress = "Ortigas Center, Pasig City",
            destinationAddress = "BGC High Street, Taguig City",
            distanceKm = 6.8,
            grossFare = 113.0,
            surcharge12Pct = 13.56,
            driverNetEarnings = 99.44,
            itemDescription = "Passenger Express Ride",
            createdAt = System.currentTimeMillis() - 7200000
        ),
        Booking(
            id = "bk_seed_2",
            reference = "EM-892102",
            customerName = "Juan Dela Cruz",
            driverName = "Kardo Dalisay (Rider)",
            driverPhone = "+639178889999",
            driverPlate = "NXX-982",
            serviceType = ServiceType.FOOD,
            vehicleType = VehicleType.MOTORCYCLE,
            status = BookingStatus.COOKING,
            paymentMethod = PaymentMethod.CASH,
            originAddress = "Mang Inasal & Grills Express, Pasig",
            destinationAddress = "Emerald Tower, Ortigas, Pasig",
            distanceKm = 3.2,
            grossFare = 480.0,
            surcharge12Pct = 57.60,
            driverNetEarnings = 422.40,
            itemDescription = "2x PM1 Paa + 1x Halo-Halo Supreme",
            createdAt = System.currentTimeMillis() - 1800000
        ),
        Booking(
            id = "bk_seed_3",
            reference = "EM-892103",
            customerName = "Jacob Rullan",
            driverName = "Kardo Dalisay (Rider)",
            driverPhone = "+639178889999",
            driverPlate = "NXX-982",
            serviceType = ServiceType.CARGO,
            vehicleType = VehicleType.EASYMOVE_VAN,
            status = BookingStatus.COMPLETED,
            paymentMethod = PaymentMethod.EASYMOVE_WALLET,
            originAddress = "Warehouse 4B, Taytay, Rizal",
            destinationAddress = "Greenhills Shopping Center, San Juan",
            distanceKm = 14.5,
            grossFare = 685.0,
            surcharge12Pct = 82.20,
            driverNetEarnings = 602.80,
            itemDescription = "10x Retail Boxes & Merchandise",
            createdAt = System.currentTimeMillis() - 14400000
        )
    )

    private val initialRestaurants = listOf(
        Restaurant(
            id = "rest_1",
            name = "Mang Inasal & Grills Express",
            category = "Filipino BBQ & Fast Food",
            rating = 4.8,
            reviewCount = 1240,
            address = "Corner Julia Vargas & Ortigas Ave, Pasig City",
            isOpen = true,
            deliveryTimeEst = "20-30 mins",
            deliveryFee = 49.0
        ),
        Restaurant(
            id = "rest_2",
            name = "Jollibee BGC High Street",
            category = "Burgers, Fried Chicken & Spaghetti",
            rating = 4.9,
            reviewCount = 2850,
            address = "Bonifacio High Street, Taguig City",
            isOpen = true,
            deliveryTimeEst = "15-25 mins",
            deliveryFee = 45.0
        ),
        Restaurant(
            id = "rest_3",
            name = "Army Navy Burger + Burrito",
            category = "Gourmet Burgers & Mexican",
            rating = 4.7,
            reviewCount = 890,
            address = "Emerald Ave, Ortigas Center, Pasig",
            isOpen = true,
            deliveryTimeEst = "25-40 mins",
            deliveryFee = 55.0
        )
    )

    private val initialMenuItems = listOf(
        MenuItem(
            id = "m1",
            restaurantId = "rest_1",
            name = "PM1 Paa Large with Rice",
            category = "Chicken Inasal",
            price = 185.00,
            description = "Char-broiled chicken leg & thigh with special chicken oil & hot steamed rice.",
            isPopular = true
        ),
        MenuItem(
            id = "m2",
            restaurantId = "rest_1",
            name = "PM2 Pecho Large with Rice",
            category = "Chicken Inasal",
            price = 195.00,
            description = "Char-broiled chicken breast & wing seasoned with secret citrus-annatto marinade.",
            isPopular = true
        ),
        MenuItem(
            id = "m3",
            restaurantId = "rest_1",
            name = "2-Pcs Pork Barbecue Meal",
            category = "Pork Barbecue",
            price = 165.00,
            description = "Tender grilled pork skewers basted in savory sweet garlic barbecue glaze.",
            isPopular = false
        ),
        MenuItem(
            id = "m4",
            restaurantId = "rest_1",
            name = "Extra Creamy Halo-Halo Supreme",
            category = "Drinks & Desserts",
            price = 110.00,
            description = "Finely shaved ice with sweetened bananas, jackfruit, ube halaya, leche flan & ice cream.",
            isPopular = true
        ),
        MenuItem(
            id = "m5",
            restaurantId = "rest_1",
            name = "Cold Sago't Gulaman (Large)",
            category = "Drinks & Desserts",
            price = 65.00,
            description = "Refreshing traditional brown sugar syrup drink with tapioca pearls & chewy gelatin.",
            isPopular = false
        ),
        // Jollibee items
        MenuItem(
            id = "m6",
            restaurantId = "rest_2",
            name = "2-pc Chickenjoy with Rice & Drink",
            category = "Chickenjoy",
            price = 210.00,
            description = "Crispylicious, juicylicious fried chicken with rich savory gravy.",
            isPopular = true
        ),
        MenuItem(
            id = "m7",
            restaurantId = "rest_2",
            name = "Jolly Spaghetti with Yumburger",
            category = "Value Combos",
            price = 169.00,
            description = "Sweet-style spaghetti topped with sliced hotdogs and grated cheese paired with beef patty burger.",
            isPopular = true
        ),
        // Army Navy items
        MenuItem(
            id = "m8",
            restaurantId = "rest_3",
            name = "Classic Bully Burger with Fries",
            category = "Burgers",
            price = 285.00,
            description = "100% pure quarter-pound beef patty with fresh lettuce, onions, and tomato on toasted sesame bun.",
            isPopular = true
        ),
        MenuItem(
            id = "m9",
            restaurantId = "rest_3",
            name = "Carnitas Pork Burrito",
            category = "Burritos",
            price = 265.00,
            description = "Slow-cooked savory pork rolled with Spanish rice, refried beans, onions, and cilantro.",
            isPopular = false
        )
    )

    // Reactive State
    private val _bookings = MutableStateFlow<List<Booking>>(initialBookings)
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    private val _activeBooking = MutableStateFlow<Booking?>(_bookings.value.find { it.status == BookingStatus.COOKING })
    val activeBooking: StateFlow<Booking?> = _activeBooking.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(initialRestaurants)
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()

    private val _menuItems = MutableStateFlow<List<MenuItem>>(initialMenuItems)
    val menuItems: StateFlow<List<MenuItem>> = _menuItems.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _driverProfile = MutableStateFlow(DriverProfile())
    val driverProfile: StateFlow<DriverProfile> = _driverProfile.asStateFlow()

    private val _customerWalletBalance = MutableStateFlow(2500.0)
    val customerWalletBalance: StateFlow<Double> = _customerWalletBalance.asStateFlow()

    // Create Ride / Cargo Booking
    fun createRideBooking(
        vehicleType: VehicleType,
        serviceType: ServiceType,
        origin: String,
        destination: String,
        distanceKm: Double,
        paymentMethod: PaymentMethod,
        itemDesc: String? = null
    ): Booking {
        val fare = computeFare(vehicleType, distanceKm)
        val refNum = "EM-${(100000..999999).random()}"
        val newBooking = Booking(
            id = UUID.randomUUID().toString(),
            reference = refNum,
            customerName = "Juan Dela Cruz",
            driverName = if (_driverProfile.value.isOnDuty) _driverProfile.value.fullName else null,
            driverPhone = if (_driverProfile.value.isOnDuty) "+639178889999" else null,
            driverPlate = if (_driverProfile.value.isOnDuty) _driverProfile.value.plateNumber else null,
            serviceType = serviceType,
            vehicleType = vehicleType,
            status = if (_driverProfile.value.isOnDuty) BookingStatus.ACCEPTED else BookingStatus.PENDING,
            paymentMethod = paymentMethod,
            originAddress = origin,
            destinationAddress = destination,
            distanceKm = fare.distanceKm,
            grossFare = fare.grossFare,
            surcharge12Pct = fare.surcharge12Pct,
            driverNetEarnings = fare.driverNet88Pct,
            itemDescription = itemDesc ?: "${vehicleType.displayName} Transfer",
            createdAt = System.currentTimeMillis()
        )

        // Deduct from customer wallet if wallet payment used
        if (paymentMethod == PaymentMethod.EASYMOVE_WALLET) {
            val updated = (_customerWalletBalance.value - fare.grossFare).coerceAtLeast(0.0)
            _customerWalletBalance.value = (updated * 100).roundToInt() / 100.0
        }

        val updatedList = listOf(newBooking) + _bookings.value
        _bookings.value = updatedList
        _activeBooking.value = newBooking

        return newBooking
    }

    // Food Order Placement
    fun placeFoodOrder(
        restaurant: Restaurant,
        paymentMethod: PaymentMethod,
        destinationAddress: String,
        items: List<CartItem>
    ): Booking {
        val foodTotal = items.sumOf { it.menuItem.price * it.quantity }
        val fare = computeFare(VehicleType.MOTORCYCLE, 3.5) // Standard 3.5km delivery distance
        val grossTotal = foodTotal + fare.grossFare
        val refNum = "EM-${(100000..999999).random()}"

        val itemSummary = items.joinToString(", ") { "${it.quantity}x ${it.menuItem.name}" }

        val newBooking = Booking(
            id = UUID.randomUUID().toString(),
            reference = refNum,
            customerName = "Juan Dela Cruz",
            driverName = if (_driverProfile.value.isOnDuty) _driverProfile.value.fullName else null,
            driverPhone = if (_driverProfile.value.isOnDuty) "+639178889999" else null,
            driverPlate = if (_driverProfile.value.isOnDuty) _driverProfile.value.plateNumber else null,
            serviceType = ServiceType.FOOD,
            vehicleType = VehicleType.MOTORCYCLE,
            status = BookingStatus.COOKING,
            paymentMethod = paymentMethod,
            originAddress = "${restaurant.name}, ${restaurant.address}",
            destinationAddress = destinationAddress,
            distanceKm = 3.5,
            grossFare = (grossTotal * 100).roundToInt() / 100.0,
            surcharge12Pct = fare.surcharge12Pct,
            driverNetEarnings = fare.driverNet88Pct,
            itemDescription = itemSummary,
            createdAt = System.currentTimeMillis()
        )

        if (paymentMethod == PaymentMethod.EASYMOVE_WALLET) {
            val updated = (_customerWalletBalance.value - grossTotal).coerceAtLeast(0.0)
            _customerWalletBalance.value = (updated * 100).roundToInt() / 100.0
        }

        _cart.value = emptyList()
        val updatedList = listOf(newBooking) + _bookings.value
        _bookings.value = updatedList
        _activeBooking.value = newBooking

        return newBooking
    }

    // Driver Operations
    fun toggleDriverDuty() {
        val current = _driverProfile.value
        _driverProfile.value = current.copy(isOnDuty = !current.isOnDuty)
    }

    fun acceptBookingByDriver(bookingId: String) {
        val drv = _driverProfile.value
        val updatedList = _bookings.value.map { b ->
            if (b.id == bookingId) {
                b.copy(
                    status = BookingStatus.ACCEPTED,
                    driverName = drv.fullName,
                    driverPhone = "+639178889999",
                    driverPlate = drv.plateNumber
                )
            } else b
        }
        _bookings.value = updatedList
        if (_activeBooking.value?.id == bookingId) {
            _activeBooking.value = updatedList.find { it.id == bookingId }
        }
    }

    fun advanceBookingStatus(bookingId: String, newStatus: BookingStatus) {
        val updatedList = _bookings.value.map { b ->
            if (b.id == bookingId) {
                b.copy(status = newStatus)
            } else b
        }
        _bookings.value = updatedList
        if (_activeBooking.value?.id == bookingId) {
            _activeBooking.value = updatedList.find { it.id == bookingId }
        }

        // If completed, disburse net earnings to driver profile & wallet
        if (newStatus == BookingStatus.COMPLETED) {
            val completed = updatedList.find { it.id == bookingId }
            if (completed != null) {
                val drv = _driverProfile.value
                val newEarnings = ((drv.todayNetEarnings + completed.driverNetEarnings) * 100).roundToInt() / 100.0
                val newWallet = ((drv.walletBalance + completed.driverNetEarnings) * 100).roundToInt() / 100.0
                _driverProfile.value = drv.copy(
                    todayNetEarnings = newEarnings,
                    walletBalance = newWallet,
                    completedTripsToday = drv.completedTripsToday + 1,
                    totalTrips = drv.totalTrips + 1
                )
            }
        }
    }

    // Cart Operations
    fun addToCart(item: MenuItem) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.menuItem.id == item.id }
        if (index >= 0) {
            current[index] = current[index].copy(quantity = current[index].quantity + 1)
        } else {
            current.add(CartItem(menuItem = item, quantity = 1))
        }
        _cart.value = current
    }

    fun decreaseCartItem(item: MenuItem) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.menuItem.id == item.id }
        if (index >= 0) {
            val q = current[index].quantity
            if (q > 1) {
                current[index] = current[index].copy(quantity = q - 1)
            } else {
                current.removeAt(index)
            }
        }
        _cart.value = current
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun topUpCustomerWallet(amount: Double) {
        val updated = ((_customerWalletBalance.value + amount) * 100).roundToInt() / 100.0
        _customerWalletBalance.value = updated
    }

    fun topUpDriverWallet(amount: Double) {
        val drv = _driverProfile.value
        val updated = ((drv.walletBalance + amount) * 100).roundToInt() / 100.0
        _driverProfile.value = drv.copy(walletBalance = updated)
    }

    fun getAdminMetrics(): AdminMetrics {
        val all = _bookings.value
        val gmv = all.sumOf { it.grossFare }
        val surcharge = all.sumOf { it.surcharge12Pct }
        val netPayout = all.sumOf { it.driverNetEarnings }
        val completedCount = all.count { it.status == BookingStatus.COMPLETED }
        val activeDrivers = if (_driverProfile.value.isOnDuty) 1 else 0

        return AdminMetrics(
            grossMerchandiseValueGmv = (gmv * 100).roundToInt() / 100.0,
            easymove12PctRevenue = (surcharge * 100).roundToInt() / 100.0,
            driverNetPayouts88Pct = (netPayout * 100).roundToInt() / 100.0,
            totalCompletedTrips = completedCount,
            activeOnDutyDrivers = activeDrivers
        )
    }
}
