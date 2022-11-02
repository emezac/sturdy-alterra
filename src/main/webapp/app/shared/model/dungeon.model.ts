import dayjs from 'dayjs';
import { IFloor } from 'app/shared/model/floor.model';
import { IMap } from 'app/shared/model/map.model';

export interface IDungeon {
  id?: string;
  dungeonName?: string;
  startDate?: string | null;
  endDate?: string | null;
  floors?: IFloor[] | null;
  jobs?: IMap[] | null;
}

export const defaultValue: Readonly<IDungeon> = {};
