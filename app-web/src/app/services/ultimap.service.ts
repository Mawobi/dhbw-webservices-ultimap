import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {IRouteInfo} from '../../types/route';
import {Apollo, gql} from 'apollo-angular';
import {IUltimapCarInfoResponse, IUltimapCarModelResponse, IUltimapRequest, IUltimapRouteResponse} from '../../types/graphQL';
import {ICar} from '../../types/costs';
import {SettingsService} from './settings.service';
import {ICarSetting, SettingsKey} from '../../types/settings';

@Injectable({
  providedIn: 'root'
})
export class UltimapService {
  /** Holds the currently active routeInfo. */
  private routeInfoBs = new BehaviorSubject<IRouteInfo | undefined>(undefined);

  /** The Observable of the currently active routeInfo. */
  public routeInfo = this.routeInfoBs.asObservable();

  constructor(private apollo: Apollo, private settingsService: SettingsService) {
  }

  /**
   * Queries the given request from the ultimap GraphQL API with all available data and updates the routeInfoBs BehaviourSubject.
   * @param request The request to pass to the query as input.
   * @returns Promise that returns true of route could be fetched, false otherwise.
   */
  public async queryRouteInfo(request: IUltimapRequest): Promise<boolean> {
    const fuelInput = await this.queryCarTypeAndConsumption();

    try {
      const response = await this.apollo.query<IUltimapRouteResponse>({
        query: gql`
          {
            routeInfo(input: { geopoints: {start: "${request.start}", destination: "${request.destination}"}
                             ${request.departure ? ',departure:' + Math.round(request.departure.getTime() / 1000) : ''}
                             ${fuelInput && fuelInput.typ && fuelInput.consumption ? ',fuel: {consumption:' + fuelInput.consumption + ', typ:' + fuelInput.typ + '}' : ''}
            })
            {
              route {start, destination, distance, duration, waypoints {lat, lon}}
              costs {totalConsumption, fuelCosts, wearFlatrate}
              weather {min, max, avg, rain}
            }
          }
        `,
      }).toPromise();

      this.routeInfoBs.next(response.data.routeInfo);
      return true;
    } catch (e) {
      console.error('Error while fetching route info.', e);
      return false;
    }
  }

  /**
   * Removes the current routeInfo if its currently defined.
   */
  public removeRoute(): void {
    if (this.routeInfoBs.value) this.routeInfoBs.next(undefined);
  }

  public async queryCars(): Promise<ICar[]> {
    try {
      const response = await this.apollo.query<IUltimapCarModelResponse>({
        query: gql`
            {
              carModels {id, name, consumption, typ}
            }
        `,
      }).toPromise();

      return response.data.carModels;
    } catch (e) {
      console.error('Error while fetching car models.', e);
      return [];
    }
  }

  private async queryCarTypeAndConsumption(): Promise<ICar | undefined> {
    const setting = this.settingsService.get(SettingsKey.CAR);
    if (!setting || setting.value == null) return undefined;

    const carSetting = setting.value as ICarSetting;
    if (carSetting.isConsumption) return {consumption: carSetting.value, typ: carSetting.type, id: -1};
    if (carSetting.value == null) return undefined;

    try {
      const response = await this.apollo.query<IUltimapCarInfoResponse>({
        query: gql`
          {
            carInfo(carId: ${carSetting.value}) {
              id, consumption, typ
            }
          }
        `,
      }).toPromise();

      return response.data.carInfo;
    } catch (e) {
      console.error('Error while fetching car request information.', e);
      return undefined;
    }
  }
}
