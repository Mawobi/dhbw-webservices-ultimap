import {Injectable} from '@angular/core';
import {Plugins} from '@capacitor/core';

const {Toast} = Plugins;

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  constructor() {
  }

  public async showToast(message: string): Promise<void> {
    await Toast.show({
      text: message,
      duration: 'long',
      position: 'bottom'
    });
  }
}
