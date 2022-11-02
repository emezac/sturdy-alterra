import { IRoom } from 'app/shared/model/room.model';
import { TypesOfChanllenge } from 'app/shared/model/enumerations/types-of-chanllenge.model';
import { Difficulty } from 'app/shared/model/enumerations/difficulty.model';

export interface IChallenge {
  id?: string;
  introText?: string;
  challengeName?: TypesOfChanllenge | null;
  difficulty?: Difficulty | null;
  name?: IRoom | null;
}

export const defaultValue: Readonly<IChallenge> = {};
