export interface UltimapRouteRequest {
	start: Coordinates | string;
	end: Coordinates | string;
	departure?: Date;

	/** Fuel consumption per liter/100 km of the users car. */
	fuel_consumption?: number;

	fuel_type?: 'gasoline' | 'diesel';
}

export interface UltimapRouteResponse {
	estimated_time: number;
	distance: number;
	route: Coordinates[];

	/** Fuel consumption for whole route in liters. */
	fuel_consumption: string;

	/** Fuel costs for the whole route in €. */
	fuel_costs: number;

	/** Pauschale. */
	car_wear_flatrate: number;

	weather: WeatherInfo;
}

export interface WeatherInfo {
	min: number;
	max: number;
	avg: number;

	/** Rain probability. */
	rain: number;
}

export interface Coordinates {
	/** "Längengrad" */
	lon: number;

	/** "Breitengrad" */
	lat: number;
}
