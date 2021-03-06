import {Component, OnInit} from '@angular/core';
import {UltimapService} from './services/ultimap.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(private ultimap: UltimapService) {
    // TODO: remove
    setTimeout(() => {
      ultimap.queryRouteInfo('Dhbw Mosbach', 'Heilbronn');
    }, 1000);
  }

  ngOnInit(): void {
  }
}
