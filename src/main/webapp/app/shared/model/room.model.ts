import { IRoomConfig } from 'app/shared/model/room-config.model';
import { IDoor } from 'app/shared/model/door.model';
import { IChallenge } from 'app/shared/model/challenge.model';
import { IFloor } from 'app/shared/model/floor.model';

export interface IRoom {
  id?: string;
  introText?: string | null;
  roomName?: string;
  location?: IRoomConfig | null;
  doors?: IDoor[] | null;
  challenges?: IChallenge[] | null;
  name?: IFloor | null;
}

export const defaultValue: Readonly<IRoom> = {};
