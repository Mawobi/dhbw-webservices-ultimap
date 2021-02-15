package de.dhbw.mosbach.webservices.ultimap.util

import de.dhbw.mosbach.webservices.ultimap.client.routing.types.CoordinateInput
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType

// This file adds ConvenienceMethods to convert between the generated Classes with similar properties

fun CoordinateType.toRoutingCoordinate() =
    de.dhbw.mosbach.webservices.ultimap.client.routing.types.CoordinateType.newBuilder()
        .lat(lat)
        .lon(lon)
        .build()!!

fun de.dhbw.mosbach.webservices.ultimap.client.routing.types.CoordinateType.toUltimapCoordinate() =
    CoordinateType.newBuilder()
        .lat(lat)
        .lon(lon)
        .build()

fun CoordinateType.toRoutingInput(): CoordinateInput =
    CoordinateInput.newBuilder()
        .lat(lat)
        .lon(lon)
        .build()

fun CoordinateType.toWeatherInput() =
    de.dhbw.mosbach.webservices.ultimap.client.weather.types.CoordinateInput.newBuilder()
        .lat(lat)
        .lon(lon)
        .build()!!