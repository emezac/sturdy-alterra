import { IFloorConfig } from 'app/shared/model/floor-config.model';
import { IRoom } from 'app/shared/model/room.model';
import { IDungeon } from 'app/shared/model/dungeon.model';

export interface IFloor {
  id?: string;
  floorName?: string;
  location?: IFloorConfig | null;
  rooms?: IRoom[] | null;
  name?: IDungeon | null;
}

export const defaultValue: Readonly<IFloor> = {};
