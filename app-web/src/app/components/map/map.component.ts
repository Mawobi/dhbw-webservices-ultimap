import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UltimapService} from '../../services/ultimap.service';
import {IRouteInfo} from '../../../types/route';
import {MapService} from '../../services/map.service';


@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  @ViewChild('target') target: ElementRef<HTMLDivElement> | undefined;
  private routeInfo: IRouteInfo | undefined;

  constructor(private ultimap: UltimapService, private map: MapService) {
    ultimap.routeInfo.subscribe(routeInfo => {
      this.routeInfo = routeInfo;
      this.update();
    });
  }

  ngOnInit(): void {
    setTimeout(() => {
      if (!this.target) return;
      this.map.display(this.target.nativeElement);

      // center map to user location and add marker if no route is active
      if (!this.routeInfo) {
        this.map.setCenterToUser().then(userLocation => {
          if (userLocation) this.map.addMarker(userLocation);
        });
      }
    }, 0);
  }

  /**
   * Updates the map to the current routeInfo. Should be called when the routeInfo of the ultimap service changes.
   */
  private update(): void {
    const waypoints = this.routeInfo?.route.waypoints;
    this.map.displayRoute(waypoints ?? []);

    if (waypoints && waypoints.length > 0) {
      this.map.addMarker(waypoints[waypoints.length - 1]);
    } else {
      this.map.removeMarker();
    }
  }
}
