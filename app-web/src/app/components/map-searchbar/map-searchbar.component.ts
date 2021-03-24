import {Component, ElementRef, ViewChild} from '@angular/core';
import {IUltimapRequest} from '../../../types/graphQL';
import {UltimapService} from '../../services/ultimap.service';
import {UtilityService} from '../../services/utility.service';

@Component({
  selector: 'app-map-searchbar',
  templateUrl: './map-searchbar.component.html',
  styleUrls: ['./map-searchbar.component.scss']
})
export class MapSearchbarComponent {
  @ViewChild('input') input: ElementRef<HTMLInputElement> | undefined;
  public request: IUltimapRequest = {
    start: '',
    destination: ''
  };

  constructor(private ultimap: UltimapService, private utility: UtilityService) {
    ultimap.routeInfo.subscribe(routeInfo => {
      if (routeInfo) {
        this.request.start = routeInfo.route.start;
        this.request.destination = routeInfo.route.destination;
      }
    });
  }

  /**
   * Queries the route for the current request if start and destination are defined
   */
  public async queryRoute(): Promise<void> {
    if (!this.request.start || !this.request.destination) return;
    if (this.request.start === this.request.destination) {
      await this.utility.showToast('Die Start- und Zieladresse sind identisch. Bitte gib unterschiedliche Adressen ein.');
      return;
    }

    await this.utility.showToast('Route wird berechnet...');
    console.log(`Query route for start ${this.request.start} and destination ${this.request.destination}`);

    const fetched = await this.ultimap.queryRouteInfo(this.request);

    if (fetched) {
      await this.utility.showToast('Route erfolgreich berechnet.');
    } else {
      await this.utility.showToast('Beim Berechnen der Route ist ein Fehler aufgetreten.');
    }

  }

  /**
   * Resets the request start and removes the current route if one exists.
   */
  public removeStart(): void {
    this.request.start = '';
    if (this.input) {
      const input = this.input.nativeElement;
      input.disabled = false;
      input.placeholder = 'Startadresse...';
    }
    this.ultimap.removeRoute();
  }

  /**
   * Resets the request destination and removes the current route if one exists.
   */
  public removeDestination(): void {
    this.request.destination = '';
    if (this.input) {
      const input = this.input.nativeElement;
      input.disabled = false;

      if (this.request.start) input.placeholder = 'Zieladresse...';
    }
    this.ultimap.removeRoute();
  }

  public inputData(ev?: KeyboardEvent): void {
    if (!this.input) return;
    if (ev && ev.key !== 'Enter') return;

    const input = this.input.nativeElement;

    const value = input.value;
    if (!this.request.start) {
      this.request.start = value;
      input.placeholder = 'Zieladresse...';
      input.focus();
    } else if (!this.request.destination) {
      this.request.destination = value;
      input.placeholder = '';
      input.disabled = true;
    } else {
      input.disabled = true;
    }

    input.value = '';
    this.queryRoute();
  }
}
