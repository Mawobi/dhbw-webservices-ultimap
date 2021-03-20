import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {ICarSetting, ISetting, SettingsKey, StorageKey} from '../../types/settings';
import {Plugins} from '@capacitor/core';

const {Storage} = Plugins;

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  private settingsBs = new BehaviorSubject<ISetting[]>([]);
  public settings = this.settingsBs.asObservable();

  constructor() {
    Storage.get({key: StorageKey.SETTINGS}).then(stored => {
      if (stored.value) {
        const unmappedSettings: ISetting[] = JSON.parse(stored.value);

        unmappedSettings.forEach(setting => {
          if (setting.key === SettingsKey.CAR) {
            const temp = setting.value as ICarSetting;
            if (temp.value) temp.value = Number.parseFloat(String(temp.value));
          }
        });

        this.settingsBs.next(unmappedSettings);
      }
    });
  }

  public async set(setting: ISetting): Promise<boolean> {
    let currentSettings: ISetting[];

    currentSettings = this.settingsBs.value.filter(s => {
      return s.key !== setting.key;
    });

    currentSettings.push(setting);

    try {
      await Storage.set({
        key: StorageKey.SETTINGS,
        value: JSON.stringify(currentSettings)
      });

      this.settingsBs.next(currentSettings);
      return true;
    } catch (e) {
      console.error(e);
      return false;
    }
  }

  public get(key: SettingsKey): ISetting | undefined {
    return this.settingsBs.value.find(setting => {
      return setting.key === key;
    });
  }
}
