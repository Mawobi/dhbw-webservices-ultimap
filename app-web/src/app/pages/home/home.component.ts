import {Component} from '@angular/core';
import {MapService} from '../../services/map.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  constructor(private map: MapService) {
  }

  public async setCenterToUser(): Promise<void> {
    await this.map.setCenterToUser();
  }
}
