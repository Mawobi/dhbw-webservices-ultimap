import {ICar} from './costs';
import {IRouteInfo} from './route';

/**
 * A request object that is passed by the route searchbar that is used as input for the GraphQL API.
 */
export interface IUltimapRequest {
  start: string;
  destination: string;
  departure?: Date;
}

/**
 * Object that is returned by the Ultimap API for the routeInfo query.
 */
export interface IUltimapRouteResponse {
  routeInfo: IRouteInfo;
}

/**
 * Object that is returned by the Ultimap API for the carModel query.
 */
export interface IUltimapCarModelResponse {
  carModels: ICar[];
}

/**
 * Object that is returned by the Ultimap API for the carInfo query.
 */
export interface IUltimapCarInfoResponse {
  carInfo: ICar;
}
