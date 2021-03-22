import {Component} from '@angular/core';
import {IRouteInfo, IWaypoint} from '../../../types/route';
import {UltimapService} from '../../services/ultimap.service';
import {MapService} from '../../services/map.service';

@Component({
  selector: 'app-map-summary',
  templateUrl: './map-summary.component.html',
  styleUrls: ['./map-summary.component.scss']
})
export class MapSummaryComponent {
  routeInfo: IRouteInfo | undefined;

  constructor(private ultimap: UltimapService, private map: MapService) {
    ultimap.routeInfo.subscribe(routeInfo => {
      this.routeInfo = routeInfo;
    });
  }

  public setCenterToDestination(): void {
    if (!this.routeInfo) return;

    const waypoints: IWaypoint[] | undefined = this.routeInfo.route.waypoints;
    if (!waypoints || waypoints.length === 0) return;

    this.map.setCenter(waypoints[waypoints.length - 1]);
  }

  public formatDuration(minutes: number): string {
    const mins = Math.floor(minutes % 60);
    const hours = Math.floor(minutes / 60);

    const minsString = mins < 10 ? `0${mins}` : mins.toString();
    const hoursString = hours < 10 ? `0${hours}` : hours.toString();

    return `${hoursString}:${minsString} h`;
  }
}
