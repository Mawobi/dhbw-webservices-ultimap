import {Injectable} from '@angular/core';
import {Apollo, gql} from 'apollo-angular';
import {IFuelInfo, IRouteInfo, IUltimapRouteResponse} from '../types/UltimapGraphQL';

@Injectable({
  providedIn: 'root'
})
export class UltimapService {

  constructor(private apollo: Apollo) {
  }

  public async queryRouteInfo(start: string, destination: string, departure?: Date, fuel?: IFuelInfo): Promise<IRouteInfo | null> {
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

      return response.data.routeInfo;
    } catch (e) {
      console.error('Error while fetching route info.', e);
      return null;
    }
  }
}
