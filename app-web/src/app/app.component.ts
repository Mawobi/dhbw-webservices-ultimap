import {Component, OnInit} from '@angular/core';
import {UltimapService} from './services/ultimap.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  constructor(private ultimap: UltimapService) {
  }

  ngOnInit(): void {
    this.ultimap.queryRouteInfo('DHBW Mosbach', 'Heilbronn', new Date()).then((routeInfo => {
      console.log(routeInfo);
    }));
  }
}
