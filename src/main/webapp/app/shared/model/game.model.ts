import { IGameConfig } from 'app/shared/model/game-config.model';
import { IMap } from 'app/shared/model/map.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IGame {
  id?: string;
  gameName?: string;
  description?: string | null;
  moves?: string | null;
  location?: IGameConfig | null;
  maps?: IMap[] | null;
  player?: IPlayer | null;
}

export const defaultValue: Readonly<IGame> = {};
