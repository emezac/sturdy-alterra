import { IPrize } from 'app/shared/model/prize.model';
import { IRoom } from 'app/shared/model/room.model';

export interface IDoor {
  id?: string;
  doorName?: string;
  prizes?: IPrize[] | null;
  name?: IRoom | null;
}

export const defaultValue: Readonly<IDoor> = {};
