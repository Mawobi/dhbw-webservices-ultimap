/**
 * The cost information for the current route.
 */
export interface IRouteCosts {
  /** Total fuel consumption in liter. */
  totalConsumption: number;

  /** Total fuel costs in euro. */
  fuelCosts: number;

  /** Total for wear flatrate ("Verschleiß-Pauschale") in euro. */
  wearFlatrate: number;
}

/**
 * What kind of fuel the car consumes.
 */
export enum FuelType {
  BENZOL = 'BENZOL',
  DIESEl = 'DIESEL',
}

/**
 * Information about the car.
 */
export interface ICar {
  id: number;
  name?: string;
  typ?: FuelType;

  /** fuel consumption in liter per 100 km */
  consumption?: number;
}
