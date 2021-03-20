/**
 * Keys that are used to identify data stored in the users local storage.
 */
import {FuelType} from './costs';

export enum StorageKey {
    SETTINGS = 'car'
}

/**
 * An app setting.
 */
export interface ISetting {
    key: SettingsKey;
    value: any;
}

/**
 * Keys that are used to identify a specific app setting.
 */
export enum SettingsKey {
    CAR = 'car',
}

export interface ICarSetting {
    value?: number;
    isConsumption: boolean;
    type?: FuelType;
}
