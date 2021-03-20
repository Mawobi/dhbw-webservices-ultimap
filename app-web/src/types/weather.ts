/**
 * Weather information about the current route.
 */
export interface IRouteWeather {
  /** Minimal temperature in °C. */
  min: number;

  /** Maximal temperature in °C. */
  max: number;

  /** Average temperature in °C. */
  avg: number;

  /** Percentage of the route that is covered by rain. */
  rain: number;
}
