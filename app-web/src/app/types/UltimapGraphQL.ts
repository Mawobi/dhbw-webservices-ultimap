export interface IFuelInfo {
  consumption: number;
  typ: FuelType;
}

export enum FuelType {
  BENZOL = 'BENZOL',
  DIESEl = 'DIESEL',
}

/** Object that is returned by the Ultimap API. */
export interface IUltimapRouteResponse {
  routeInfo: IRouteInfo;
}

/** Actual relevant route info data from the Ultimap API. */
export interface IRouteInfo {
  route: IRoute;
  costs?: IRouteCosts;
  weather?: IRouteWeather;
}

export interface IRoute {
  /** Duration in minutes. */
  duration: number;

  /** Distance in meters. */
  distance: number;
  waypoints?: IWaypoint[];
}

export interface IRouteCosts {
  totalConsumption: number;
  fuelCosts: number;
  wearFlatrate: number;
}

export interface IRouteWeather {
  min: number;
  max: number;
  avg: number;
  rain: number;
}

export interface IWaypoint {
  /** "Breitengrad" */
  lat: number;

  /** "Längengrad" */
  lon: number;
}
