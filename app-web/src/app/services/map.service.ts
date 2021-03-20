import {Injectable} from '@angular/core';
import Map from 'ol/Map';
import TileLayer from 'ol/layer/Tile';
import {OSM} from 'ol/source';
import {Feature, View} from 'ol';
import {fromLonLat} from 'ol/proj';
import {IWaypoint} from '../../types/route';
import {Coordinate} from 'ol/coordinate';
import {LineString, Point} from 'ol/geom';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import {Icon, Stroke, Style} from 'ol/style';
import IconAnchorUnits from 'ol/style/IconAnchorUnits';
import {Plugins} from '@capacitor/core';

const {Geolocation} = Plugins;

@Injectable({
  providedIn: 'root'
})
export class MapService {
  private static readonly ROUTE_LAYER_NAME = 'route-path';
  private static readonly MARKER_LAYER_NAME = 'user-geolocation';
  private static readonly ZOOM_DEFAULT = 16;
  private readonly map: Map;

  constructor() {
    // initialize map (does not display the map)
    this.map = new Map({
      layers: [
        new TileLayer({
          source: new OSM({
            url: 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
          })
        })
      ],
      view: new View({
        center: fromLonLat([9.150116082904281, 49.354432818804625]),
        zoom: MapService.ZOOM_DEFAULT
      })
    });
  }

  public display(target: HTMLDivElement): void {
    this.map.setTarget(target);

    // add observer to change map light/dark mode depending on current theme
    this.map.once('postrender', this.updateMapTheme);

    const observer = new MutationObserver(() => {
      this.updateMapTheme();
    });

    observer.observe(document.documentElement, {
      attributes: true,
      attributeFilter: ['theme'],
      attributeOldValue: false,
      subtree: false
    });
  }

  /**
   * Sets the map center to the given coordinates and sets zoom level to the default value.
   * @param waypoint The waypoint to set the center to.
   */
  public setCenter(waypoint: IWaypoint): void {
    const view = this.map.getView();
    view.setCenter(fromLonLat([waypoint.lon, waypoint.lat]));
    view.setZoom(MapService.ZOOM_DEFAULT);
  }

  /**
   * Draws a route with the given waypoints onto the map and centers the map to the route destination.
   * Removes the current route if one exists.
   * @param waypoints The waypoints of the route.
   */
  public displayRoute(waypoints: IWaypoint[]): void {
    this.removeRoute();
    if (waypoints.length === 0) return;

    const path: Coordinate[] = waypoints.map(waypoint => {
      return fromLonLat([waypoint.lon, waypoint.lat]);
    });

    const routePath = new LineString(path);

    const feature = new Feature({
      geometry: routePath
    });

    const source = new VectorSource({features: [feature]});

    const primaryColor = getComputedStyle(document.body).getPropertyValue('--um-color-primary');

    const routeLayer = new VectorLayer({
      source,
      className: MapService.ROUTE_LAYER_NAME,
      zIndex: 1,
      style: new Style({
        stroke: new Stroke({
          color: primaryColor ? primaryColor : 'red',
          width: 4
        })
      })
    });

    this.map.addLayer(routeLayer);
    this.setCenter(waypoints[waypoints.length - 1]);
  }

  /**
   * Displays a marker on the map at the given waypoint.
   * @param waypoint The waypoint to position the marker to.
   * @private
   */
  public addMarker(waypoint: IWaypoint): void {
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
    const markerLayer = new VectorLayer({source, className: MapService.MARKER_LAYER_NAME, zIndex: 2});

    this.removeMarker();
    this.map.addLayer(markerLayer);
  }

  /**
   * Gets the coordinates of the users current location if geolocation permission is granted.
   * @throws Error when permission is not granted or not supported by the device.
   * @returns Promise with the waypoint of the users location.
   */
  public async getUserLocation(): Promise<IWaypoint> {
    const coordinates = await Geolocation.getCurrentPosition();
    return {lon: coordinates.coords.longitude, lat: coordinates.coords.latitude};
  }

  /**
   * Centers the map to the users current location if available.
   * @returns The waypoint of the users location or undefined if location could not be fetched.
   */
  public async setCenterToUser(): Promise<IWaypoint | undefined> {
    try {
      const location = await this.getUserLocation();
      this.setCenter(location);
      return location;
    } catch (e) {
      console.error(e);
      return undefined;
    }
  }

  /**
   * Removes the currently displayed marker from the map if one exists.
   */
  public removeMarker(): void {
    for (const layer of this.map.getLayers().getArray()) {
      if (layer.getClassName() === MapService.MARKER_LAYER_NAME) {
        this.map.removeLayer(layer);
        break;
      }
    }
  }

  /**
   * Updates the map to either light or dark mode depending on teh current theme.
   */
  private updateMapTheme(): void {
    if (!this.map) return;

    const isDark = document.documentElement.getAttribute('theme') === 'dark';
    const target = this.map.getTargetElement();
    if (!target) return;

    const canvas = target.querySelector('canvas') as HTMLCanvasElement;
    if (canvas) canvas.style.filter = isDark ? 'invert(90%)' : '';
  }

  /**
   * Removes the currently drawn route if it exists.
   */
  private removeRoute(): void {
    for (const layer of this.map.getLayers().getArray()) {
      if (layer.getClassName() === MapService.ROUTE_LAYER_NAME) {
        this.map.removeLayer(layer);
        break;
      }
    }
  }
}
