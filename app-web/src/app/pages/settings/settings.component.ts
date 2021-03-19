import {Component} from '@angular/core';
import {FuelType, ICar} from '../../../types/costs';
import {SettingsService} from '../../services/settings.service';
import {ICarSetting, ISetting, SettingsKey} from '../../../types/settings';
import {UltimapService} from '../../services/ultimap.service';
import {take} from 'rxjs/operators';
import {UtilityService} from '../../services/utility.service';

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

  constructor(private settingsService: SettingsService, private ultimap: UltimapService, private utility: UtilityService) {
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
    if (this.carSetting.isConsumption && (this.carSetting.value == null || !this.carSetting.type)) {
      await this.utility.showToast('Bitte fülle alle Felder aus.');
      return;
    }
    if (!this.carSetting.isConsumption) delete this.carSetting.type;

    const saved = await this.settingsService.set({key: SettingsKey.CAR, value: this.carSetting});
    if (!saved) {
      await this.utility.showToast('Beim Speichern der Einstellungen ist ein Fehler aufgetreten.');
      return;
    }

    await this.utility.showToast('Einstellungen gespeichert.');

    this.ultimap.routeInfo.pipe(take(1)).subscribe(async routeInfo => {
      if (routeInfo) {
        // query currently active route to update car information
        const fetched = await this.ultimap.queryRouteInfo({start: routeInfo.route.start, destination: routeInfo.route.destination});
        if (fetched) await this.utility.showToast('Route wurde neu berechnet.');
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
