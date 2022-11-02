import { IPack } from 'app/shared/model/pack.model';

export interface ICard {
  id?: string;
  cardName?: string | null;
  initialPip?: string | null;
  pack?: IPack | null;
}

export const defaultValue: Readonly<ICard> = {};
