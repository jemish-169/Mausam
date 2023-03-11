package com.client.mausam.Models

data class ForecastDetails(
    val current: CurrentX,
    val forecast: Forecast,
    val location: LocationX
)