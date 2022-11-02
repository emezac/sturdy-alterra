import dayjs from 'dayjs';

export interface IGameConfig {
  id?: string;
  setupDate?: string | null;
  floorConfig?: number | null;
  roomConfig?: number | null;
  dateInit?: string | null;
  dateEnd?: string | null;
}

export const defaultValue: Readonly<IGameConfig> = {};
