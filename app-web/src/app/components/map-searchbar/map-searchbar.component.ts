import {Component} from '@angular/core';
import {IUltimapRequest} from '../../../types/graphQL';
import {UltimapService} from '../../services/ultimap.service';

@Component({
  selector: 'app-map-searchbar',
  templateUrl: './map-searchbar.component.html',
  styleUrls: ['./map-searchbar.component.scss']
})
export class MapSearchbarComponent {
  public request: IUltimapRequest = {
    start: '',
    destination: ''
  };

  constructor(private ultimap: UltimapService) {
    ultimap.routeInfo.subscribe(routeInfo => {
      if (routeInfo) {
        this.request.start = routeInfo.route.start;
        this.request.destination = routeInfo.route.destination;
      }
    });
  }

  /**
   * Sets the request start to the input value if enter was pressed.
   * @param ev The key press event.
   */
  public setStart(ev: KeyboardEvent): void {
    if (ev.key !== 'Enter') return;
    const input = (ev.target as HTMLInputElement);
    this.request.start = input.value;
    input.value = '';

    this.queryRoute();
  }

  /**
   * Sets the request destination to the input value if enter was pressed.
   * @param ev The key press event.
   */
  public setDestination(ev: KeyboardEvent): void {
    if (ev.key !== 'Enter') return;
    const input = (ev.target as HTMLInputElement);
    this.request.destination = input.value;
    input.value = '';

    this.queryRoute();
  }

  /**
   * Queries the route for the current request if start and destination are defined
   */
  public async queryRoute(): Promise<void> {
    if (!this.request.start || !this.request.destination) return;

    console.log(`Query route for start ${this.request.start} and destination ${this.request.destination}`);

    try {
      await this.ultimap.queryRouteInfo(this.request);
    } catch (e) {
      console.error(e);
    }

  }

  /**
   * Resets the request start and removes the current route if one exists.
   */
  public removeStart(): void {
    this.request.start = '';
    this.ultimap.removeRoute();
  }

  /**
   * Resets the request destination and removes the current route if one exists.
   */
  public removeDestination(): void {
    this.request.destination = '';
    this.ultimap.removeRoute();
  }
}
