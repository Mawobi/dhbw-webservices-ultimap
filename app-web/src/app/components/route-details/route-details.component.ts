import {Component, OnInit} from '@angular/core';
import {UltimapService} from '../../services/ultimap.service';
import {IRouteInfo} from '../../types/UltimapGraphQL';

@Component({
  selector: 'app-route-details',
  templateUrl: './route-details.component.html',
  styleUrls: ['./route-details.component.scss']
})
export class RouteDetailsComponent implements OnInit {
  routeInfo: IRouteInfo | undefined;

  constructor(private ultimap: UltimapService) {
    ultimap.routeInfo.subscribe(info => {
      this.routeInfo = info;
    });
  }

  ngOnInit(): void {
  }

}
