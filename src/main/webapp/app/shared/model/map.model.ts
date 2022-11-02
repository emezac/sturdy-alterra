import { IDungeon } from 'app/shared/model/dungeon.model';
import { IGame } from 'app/shared/model/game.model';

export interface IMap {
  id?: string;
  mapName?: string;
  description?: string | null;
  tasks?: IDungeon[] | null;
  game?: IGame | null;
}

export const defaultValue: Readonly<IMap> = {};
