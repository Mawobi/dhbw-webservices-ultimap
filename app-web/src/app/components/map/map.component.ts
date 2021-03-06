import {Component, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {UltimapService} from '../../services/ultimap.service';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import View from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {Coordinate} from 'ol/coordinate';
import {IWaypoint} from '../../types/UltimapGraphQL';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import {Feature} from 'ol';
import {Point} from 'ol/geom';
import {Icon, Style} from 'ol/style';
import IconAnchorUnits from 'ol/style/IconAnchorUnits';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  map: Map;

  constructor(private ultimap: UltimapService) {
    this.map = new Map({});
  }

  private static transformCoordinates(lat: number, lon: number): Coordinate {
    return fromLonLat([lon, lat]);
  }

  private static getMap(): Map {
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
        center: MapComponent.transformCoordinates(49.354432818804625, 9.150116082904281),
        zoom: 14,
      })
    });
  }

  ngOnInit(): void {
    this.map = MapComponent.getMap();

    // set map center to user location if permisison granted
    this.getUserGeolocation().then(coordinates => {
      this.setCenter(coordinates.lat, coordinates.lon);
      this.addMarker(coordinates);
    }).catch(() => {
      console.log('User declined geolocation permission.');
    });
  }

  private setCenter(lat: number, lon: number): void {
    const view = this.map.getView();
    view.setCenter(MapComponent.transformCoordinates(lat, lon));
    view.setZoom(12);
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

  private addMarker(waypoint: IWaypoint): void {
    const marker = new Feature({
      geometry: new Point(fromLonLat([waypoint.lon, waypoint.lat])),
    });

    const iconStyle = new Style({
      image: new Icon({
        anchor: [0.5, 46],
        anchorXUnits: IconAnchorUnits.FRACTION,
        anchorYUnits: IconAnchorUnits.PIXELS,
        src: 'assets/marker.png',
      }),
    });

    marker.setStyle(iconStyle);

    const source = new VectorSource({features: [marker]});
    const layer = new VectorLayer({source});
    this.map.addLayer(layer);

    source.addFeature(marker);
  }
}
