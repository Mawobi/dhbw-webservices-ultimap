import {Component, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {UltimapService} from '../../services/ultimap.service';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import View from 'ol/View';
import {transform} from 'ol/proj';
import {Coordinate} from 'ol/coordinate';

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

    this.ultimap.queryRouteInfo('DHBW Mosbach', 'Heilbronn', new Date()).then((routeInfo => {
      if (routeInfo) {
        console.log(routeInfo);

        if (routeInfo.route.waypoints && routeInfo.route.waypoints.length > 0) {
          const lat = routeInfo.route.waypoints[0].lat;
          const lon = routeInfo.route.waypoints[0].lon;

          this.setCenter(lat, lon);
        }
      }
    }));
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
        center: this.transformCoordinates(0, 0),
        zoom: 5,
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
}
