package com.example.googlemapsgps

import android.location.Location
import java.sql.Timestamp

data class LocationModel(
    val location: Location,
    val timestamp: Long
)
