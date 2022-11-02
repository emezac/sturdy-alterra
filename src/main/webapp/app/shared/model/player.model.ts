import { IProfile } from 'app/shared/model/profile.model';
import { IGame } from 'app/shared/model/game.model';
import { IPack } from 'app/shared/model/pack.model';

export interface IPlayer {
  id?: string;
  nickName?: string;
  location?: IProfile | null;
  games?: IGame[] | null;
  packs?: IPack[] | null;
}

export const defaultValue: Readonly<IPlayer> = {};
