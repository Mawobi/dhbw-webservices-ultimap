package de.dhbw.mosbach.webservices.ultimap

import de.dhbw.mosbach.webservices.ultimap.client.carinfo.types.FuelPriceType
import de.dhbw.mosbach.webservices.ultimap.client.routing.types.RouteType
import de.dhbw.mosbach.webservices.ultimap.client.weather.types.WeatherType
import de.dhbw.mosbach.webservices.ultimap.external.carinfo.ICarinfoProvider
import de.dhbw.mosbach.webservices.ultimap.external.routing.IRoutingProvider
import de.dhbw.mosbach.webservices.ultimap.external.weather.IWeatherProvider
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CarInfoType
import de.dhbw.mosbach.webservices.ultimap.graphql.types.CoordinateType
import de.dhbw.mosbach.webservices.ultimap.graphql.types.FuelType
import de.dhbw.mosbach.webservices.ultimap.util.toRoutingCoordinate
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@TestConfiguration
@Profile("mocked")
class TestConfiguration {
    @Bean
    fun getMockCarinfoProvider(): ICarinfoProvider =
        object : ICarinfoProvider {
            private val cars = listOf(
                CarInfoType(
                    0,
                    "Lamborghini Aventador S",
                    19.0,
                    FuelType.BENZOL
                ),
                CarInfoType(
                    1,
                    "BMW 3er M",
                    8.7,
                    FuelType.DIESEL
                )
            )

            override fun getCar(carId: Int) = cars[carId]

            override fun getFuel(typ: FuelType?): FuelPriceType =
                when (typ) {
                    FuelType.BENZOL -> FuelPriceType(1.439)
                    FuelType.DIESEL -> FuelPriceType(1.129)
                    else            -> FuelPriceType(9.999)
                }


            override fun getAllCars(): List<CarInfoType> = cars
        }

    @Bean
    fun getMockWeatherProvider() : IWeatherProvider =
        object : IWeatherProvider {
            override fun getWeather(inputCoordinate: CoordinateType,
                                    timestamp: Int
            ): WeatherType = WeatherType(12.4, 4.8)
        }

    @Bean
    fun getMockRoutingProvider() : IRoutingProvider =
        object : IRoutingProvider {
            override fun geocode(name: String?): CoordinateType =
                when(name) {
                    "DHBW Mosbach" -> CoordinateType(49.351978, 9.145870)
                    "DHBW Bad Mergentheim" -> CoordinateType(49.490200, 9.773150)
                    else -> CoordinateType(49.3533157, 9.1493341)
                }

            override fun getRoute(start: CoordinateType?, destination: CoordinateType?): RouteType =
                RouteType(
                    60,
                    50000,
                    listOf(
                        start!!.toRoutingCoordinate(),
                        destination!!.toRoutingCoordinate()
                    )
                )

        }
}