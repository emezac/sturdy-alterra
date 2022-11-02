import dayjs from 'dayjs';
import { IDoor } from 'app/shared/model/door.model';

export interface IPrize {
  id?: string;
  prizeName?: string;
  pips?: number | null;
  expireDate?: string | null;
  name?: IDoor | null;
}

export const defaultValue: Readonly<IPrize> = {};
