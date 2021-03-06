import {Injectable} from '@angular/core';
import {Apollo, gql} from 'apollo-angular';
import {IFuelInfo, IRouteInfo, IUltimapRouteResponse} from '../types/UltimapGraphQL';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UltimapService {
  private routeInfoBs = new BehaviorSubject<IRouteInfo | undefined>(undefined);
  public routeInfo = this.routeInfoBs.asObservable();

  constructor(private apollo: Apollo) {
  }

  public async queryRouteInfo(start: string, destination: string, departure?: Date, fuel?: IFuelInfo): Promise<void> {
    let fuelInput;

    if (fuel && fuel.consumption != null && fuel.typ) {
      fuelInput = `fuel:{consumption: ${fuel.consumption},typ:${fuel.typ}`;
    }

    try {
      const response = await this.apollo.query<IUltimapRouteResponse>({
        query: gql`
          {
            routeInfo(input: { geopoints: {start: "${start}", destination: "${destination}"}
                             ${departure ? ',departure:' + Math.round(departure.getTime() / 1000) : ''}
                             ${fuelInput ? ',' + fuelInput : ''}
            })
            {
              route {distance, duration, waypoints {lat, lon}}
              costs {totalConsumption, fuelCosts, wearFlatrate}
              weather {min, max, avg, rain}
            }
          }
        `,
      }).toPromise();

      this.routeInfoBs.next(response.data.routeInfo);
    } catch (e) {
      console.error('Error while fetching route info.', e);
    }
  }
}
