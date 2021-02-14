package de.dhbw.mosbach.webservices.ultimap.util

import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType

// This file adds ConvenienceMethods to convert between the generated Classes with similar properties

fun CoordinateType.toRoutingCoordinate() =
    de.dhbw.mosbach.webservices.ultimap.client.routing.types.CoordinateType.newBuilder()
        .lat(lat)
        .lon(lon)
        .build()

fun de.dhbw.mosbach.webservices.ultimap.client.routing.types.CoordinateType.toUltimapCoordinate() =
    CoordinateType.newBuilder()
        .lat(lat)
        .lon(lon)
        .build()