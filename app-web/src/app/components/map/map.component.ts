import {Component, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {UltimapService} from '../../services/ultimap.service';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import View from 'ol/View';
import {fromLonLat} from 'ol/proj';
import {Coordinate} from 'ol/coordinate';
import {IRouteInfo, IWaypoint} from '../../types/UltimapGraphQL';
import {Feature} from 'ol';
import {LineString, Point} from 'ol/geom';
import {Icon, Stroke, Style} from 'ol/style';
import IconAnchorUnits from 'ol/style/IconAnchorUnits';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  private map: Map;
  private routeInfo: IRouteInfo | undefined;

  constructor(private ultimap: UltimapService) {
    this.map = new Map({});

    this.ultimap.routeInfo.subscribe(info => {
      this.routeInfo = info;

      if (info && info.route.waypoints) {
        this.displayRoute(info.route.waypoints);
      }
    });
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
            url: 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          }),
        })
      ],
      view: new View({
        center: MapComponent.transformCoordinates(49.354432818804625, 9.150116082904281),
        zoom: 14,
      }),
    });
  }

  private static updateMapTheme(): void {
    const isDark = document.documentElement.getAttribute('theme') === 'dark';
    const canvas = document.querySelector('#map canvas') as HTMLCanvasElement;

    if (canvas) {
      canvas.style.filter = isDark ? 'invert(90%)' : '';
    }
  }

  ngOnInit(): void {
    this.map = MapComponent.getMap();
    this.map.once('postrender', MapComponent.updateMapTheme);

    const observer = new MutationObserver(() => {
      MapComponent.updateMapTheme();
    });

    observer.observe(document.documentElement, {
      attributes: true,
      attributeFilter: ['theme'],
      attributeOldValue: false,
      subtree: false
    });

    // set map center to user location if permission granted
    this.getUserGeolocation().then(coordinates => {
      this.setCenter(coordinates.lat, coordinates.lon);
      this.addMarker(coordinates);
    }).catch((e: Error) => {
      console.warn(e.message);
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
          reject(new Error('User declined geolocation permission.'));
        });
      } else {
        reject(new Error('Geolocation is not supported on this device.'));
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
    const markerLayer = new VectorLayer({source, className: 'user-geolocation', zIndex: 2});

    // check if marker already exists and remove if yes
    this.map.getLayers().forEach(layer => {
      if (layer.getClassName() === 'user-geolocation') {
        this.map.removeLayer(layer);
      }
    });

    this.map.addLayer(markerLayer);
  }

  private displayRoute(waypoints: IWaypoint[]): void {
    // check if route already exists and remove if yes
    this.map.getLayers().forEach(layer => {
      if (layer.getClassName() === 'route-path') {
        this.map.removeLayer(layer);
      }
    });

    const path = waypoints.map(waypoint => {
      return MapComponent.transformCoordinates(waypoint.lat, waypoint.lon);
    });

    const routePath = new LineString(path);

    const feature = new Feature({
      geometry: routePath
    });

    const source = new VectorSource();
    source.addFeature(feature);

    const primaryColor = getComputedStyle(document.body).getPropertyValue('--um-color-primary');

    const routeLayer = new VectorLayer({
      source,
      className: 'route-path',
      zIndex: 1,
      style: new Style({
        stroke: new Stroke({
          color: primaryColor ? primaryColor : 'red',
          width: 4
        })
      })
    });

    this.map.addLayer(routeLayer);

    if (waypoints.length > 0) {
      const destination = waypoints[waypoints.length - 1];
      this.setCenter(destination.lat, destination.lon);
    }
  }
}
