import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavBarComponent} from '../components/nav-bar/nav-bar.component';
import {NavTabComponent} from '../components/nav-tab/nav-tab.component';
import {RouterModule} from '@angular/router';
import {MapComponent} from '../components/map/map.component';
import {RouteDetailsComponent} from '../components/route-details/route-details.component';
import {FormsModule} from '@angular/forms';
import {RouteSearchbarComponent} from '../components/route-searchbar/route-searchbar.component';

const components: any[] = [NavBarComponent, NavTabComponent, MapComponent, RouteDetailsComponent, RouteSearchbarComponent];

@NgModule({
  declarations: [...components],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
  ],
  exports: [...components],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SharedModule {
}
