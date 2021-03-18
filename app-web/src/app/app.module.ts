import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SharedModule} from './modules/shared.module';
import {registerLocaleData} from '@angular/common';
import locale_de from '@angular/common/locales/de';

import {HttpClientModule} from '@angular/common/http';
import {APOLLO_OPTIONS} from 'apollo-angular';
import {HttpLink} from 'apollo-angular/http';
import {InMemoryCache} from '@apollo/client/core';

registerLocaleData(locale_de);

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    SharedModule,
  ],
  providers: [{
    provide: APOLLO_OPTIONS,
    useFactory: (httpLink: HttpLink) => {
      return {
        cache: new InMemoryCache(),
        link: httpLink.create({
          uri: 'http://server.menkalian.de/graphql',
        }),
      };
    },
    deps: [HttpLink],
  },
    {provide: LOCALE_ID, useValue: 'de'},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
