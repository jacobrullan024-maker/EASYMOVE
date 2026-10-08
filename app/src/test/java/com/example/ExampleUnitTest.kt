package com.example

import com.example.data.EasyMoveRepository
import com.example.model.PaymentMethod
import com.example.model.ServiceType
import com.example.model.VehicleType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testFareCalculation_motorcycle() {
        // Motorcycle: Base 45.0, 10.0/km, 6.8km
        // Gross: 45 + 68 = 113.0
        // 12% surcharge: 13.56
        // 88% net: 99.44
        val fare = EasyMoveRepository.computeFare(VehicleType.MOTORCYCLE, 6.8)
        assertEquals(113.0, fare.grossFare, 0.01)
        assertEquals(13.56, fare.surcharge12Pct, 0.01)
        assertEquals(99.44, fare.driverNet88Pct, 0.01)
    }

    @Test
    fun testFareCalculation_cargoVan() {
        // Cargo Van: Base 250.0, 30.0/km, 14.5km
        // Gross: 250 + 435 = 685.0
        // 12% surcharge: 82.20
        // 88% net: 602.80
        val fare = EasyMoveRepository.computeFare(VehicleType.EASYMOVE_VAN, 14.5)
        assertEquals(685.0, fare.grossFare, 0.01)
        assertEquals(82.20, fare.surcharge12Pct, 0.01)
        assertEquals(602.80, fare.driverNet88Pct, 0.01)
    }

    @Test
    fun testCreateRideBooking_updatesState() {
        val repo = EasyMoveRepository()
        val booking = repo.createRideBooking(
            vehicleType = VehicleType.MOTORCYCLE,
            serviceType = ServiceType.RIDE,
            origin = "Ortigas Center, Pasig",
            destination = "BGC High Street, Taguig",
            distanceKm = 6.8,
            paymentMethod = PaymentMethod.CASH
        )

        assertNotNull(booking.id)
        assertEquals(113.0, booking.grossFare, 0.01)
        assertEquals(13.56, booking.surcharge12Pct, 0.01)
        assertEquals(99.44, booking.driverNetEarnings, 0.01)
    }
}
