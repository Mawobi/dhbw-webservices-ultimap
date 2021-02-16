export interface UltimapRouteRequest {
  geopoints: {
    start: string;
    destination: string;
  };
  departure: number;
  fuel: {
    consumption: number;
    typ: FuelType;
  };
}

export enum FuelType {
  BENZOL = 'BENZOL',
  DIESEl = 'DIESEL',
}

export interface UltimapRouteResponse {
  data: { routeInfo: RouteInfo };
}

export interface RouteInfo {
  route: Route;
  costs: RouteCosts;
  weather: RouteWeather;
}

export interface Route {
  duration: number;
  distance: number;
  waypoints: Waypoint[];
}

export interface RouteCosts {
  totalConsumption: number;
  fuelCosts: number;
  wearFlatrate: number;
}

export interface RouteWeather {
  min: number;
  max: number;
  avg: number;
  rain: number;
}

export interface Waypoint {
  /** "Breitengrad" */
  lat: number;

  /** "Längengrad" */
  lon: number;
}
