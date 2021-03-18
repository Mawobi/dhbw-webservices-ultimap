import {Component} from '@angular/core';
import {UltimapService} from './services/ultimap.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private ultimap: UltimapService) {
  }
}
