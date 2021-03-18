import {Component} from '@angular/core';
import {UltimapService} from '../../services/ultimap.service';

export interface IRequest {
  start: string;
  destination: string;
}


@Component({
  selector: 'app-route-searchbar',
  templateUrl: './route-searchbar.component.html',
  styleUrls: ['./route-searchbar.component.scss']
})
export class RouteSearchbarComponent {
  public request: IRequest = {
    start: '',
    destination: ''
  };

  constructor(private ultimap: UltimapService) {
  }

  public setStart(ev: KeyboardEvent): void {
    if (ev.key !== 'Enter') return;
    const input = (ev.target as HTMLInputElement);
    this.request.start = input.value;
    input.value = '';

    this.queryRoute();
  }

  public setDestination(ev: KeyboardEvent): void {
    if (ev.key !== 'Enter') return;
    const input = (ev.target as HTMLInputElement);
    this.request.destination = input.value;
    input.value = '';

    this.queryRoute();
  }

  public clearStart(): void {
    this.request.start = '';
    this.ultimap.removeRoute();
  }

  public clearDestination(): void {
    this.request.destination = '';
    this.ultimap.removeRoute();
  }

  public async queryRoute(): Promise<void> {
    if (this.request.start && this.request.destination) {
      console.log(`Query route for start ${this.request.start} and destination ${this.request.destination}`);

      try {
        await this.ultimap.queryRouteInfo(this.request.start, this.request.destination);
      } catch (e) {
        console.error(e);
      }
    }
  }

}
