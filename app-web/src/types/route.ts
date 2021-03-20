import {IRouteCosts} from './costs';
import {IRouteWeather} from './weather';

/**
 * The information from the Ultimap API about the queried route, costs and weather.
 */
export interface IRouteInfo {
  route: IRoute;
  costs?: IRouteCosts;
  weather?: IRouteWeather;
}

/**
 * The information about the queried route
 */
export interface IRoute {
  /** Either human readable address or coordinates like (59.0,14.9); Format like (lat,lon) of the start address.  */
  start: string;

  /** Either human readable address or coordinates like (59.0,14.9); Format like (lat,lon) of the destination address.  */
  destination: string;

  /** Duration in minutes. */
  duration: number;

  /** Distance in meters. */
  distance: number;
  waypoints?: IWaypoint[];
}

/**
 * Single waypoint / coordinates.
 */
export interface IWaypoint {
  /** "Breitengrad" */
  lat: number;

  /** "Längengrad" */
  lon: number;
}
