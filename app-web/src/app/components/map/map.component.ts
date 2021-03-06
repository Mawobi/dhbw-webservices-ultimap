import {Component, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {UltimapService} from '../../services/ultimap.service';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import View from 'ol/View';
import {transform} from 'ol/proj';
import {Coordinate} from 'ol/coordinate';
import {IWaypoint} from '../../types/UltimapGraphQL';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  map: Map | undefined;

  constructor(private ultimap: UltimapService) {
  }

  ngOnInit(): void {
    this.map = this.getMap();

    // set map center to user location if permisison granted
    this.getUserGeolocation().then(coordinates => {
      this.setCenter(coordinates.lat, coordinates.lon);
    }).catch(e => {
      console.log('User declined geolocation permission.');
    });
  }

  private getMap(): Map {
    return new Map({
      target: 'map',
      layers: [
        new TileLayer({
          source: new OSM({
            url: 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
          })
        })
      ],
      view: new View({
        center: this.transformCoordinates(49.354432818804625, 9.150116082904281),
        zoom: 14,
      })
    });
  }

  private setCenter(lat: number, lon: number): void {
    if (!this.map) {
      return;
    }

    const view = this.map.getView();
    view.setCenter(this.transformCoordinates(lat, lon));
    view.setZoom(12);
  }

  private transformCoordinates(lat: number, lon: number): Coordinate {
    return transform([lon, lat], 'EPSG:4326', 'EPSG:3857');
  }

  private async getUserGeolocation(): Promise<IWaypoint> {
    return new Promise((resolve, reject) => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;

          resolve({lat, lon});
        }, () => {
          reject();
        });
      } else {
        reject();
      }
    });
  }
}
