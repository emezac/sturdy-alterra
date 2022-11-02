import { ICard } from 'app/shared/model/card.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IPack {
  id?: string;
  packName?: string;
  deckName?: string | null;
  configSetup?: string | null;
  cards?: ICard[] | null;
  player?: IPlayer | null;
}

export const defaultValue: Readonly<IPack> = {};
