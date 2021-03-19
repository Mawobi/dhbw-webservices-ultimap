import {Component} from '@angular/core';
import {FuelType, ICar} from '../../../types/costs';
import {SettingsService} from '../../services/settings.service';
import {ICarSetting, ISetting, SettingsKey} from '../../../types/settings';
import {UltimapService} from '../../services/ultimap.service';
import {take} from 'rxjs/operators';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {
  public carSetting: ICarSetting = {value: undefined, isConsumption: false};
  public availableCars: ICar[] = [];
  public fuelTypes: FuelType[] = [FuelType.DIESEl, FuelType.BENZOL];
  private settings: ISetting[] = [];

  constructor(private settingsService: SettingsService, private ultimap: UltimapService) {
    settingsService.settings.subscribe(settings => {
      this.settings = settings;
      const carSetting = settingsService.get(SettingsKey.CAR);
      if (carSetting && carSetting.value != null) this.carSetting = carSetting.value;
    });

    ultimap.queryCars().then(cars => {
      this.availableCars = cars;
    });
  }

  public async save(): Promise<void> {
    if (this.carSetting == null) return;
    if (this.carSetting.isConsumption && (this.carSetting.value == null || !this.carSetting.type)) return;
    if (!this.carSetting.isConsumption) delete this.carSetting.type;

    await this.settingsService.set({key: SettingsKey.CAR, value: this.carSetting});

    await this.ultimap.routeInfo.pipe(take(1)).subscribe(routeInfo => {
      if (routeInfo) {
        // query currently active route to update car information
        this.ultimap.queryRouteInfo({start: routeInfo.route.start, destination: routeInfo.route.destination});
      }
    });
  }

  onToggleChange(): void {
    setTimeout(() => {
      delete this.carSetting.value;

      if (!this.carSetting.isConsumption) delete this.carSetting.type;
    }, 0);
  }
}
