import {CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './pages/home/home.component';
import {SettingsComponent} from './pages/settings/settings.component';
import {GraphQLModule} from './graphql.module';
import {HttpClientModule} from '@angular/common/http';
import {MapComponent} from './components/map/map.component';
import {MapSearchbarComponent} from './components/map-searchbar/map-searchbar.component';
import {FormsModule} from '@angular/forms';
import {MapSummaryComponent} from './components/map-summary/map-summary.component';
import {IconCardComponent} from './components/icon-card/icon-card.component';
import {registerLocaleData} from '@angular/common';
import locale_de from '@angular/common/locales/de';

registerLocaleData(locale_de);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SettingsComponent,
    MapComponent,
    MapSearchbarComponent,
    MapSummaryComponent,
    IconCardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    GraphQLModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [{provide: LOCALE_ID, useValue: 'de'}],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
