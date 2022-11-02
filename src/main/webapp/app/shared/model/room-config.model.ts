export interface IRoomConfig {
  id?: string;
  setup?: string | null;
  numOfDoors?: number | null;
  numOfPrizes?: number | null;
}

export const defaultValue: Readonly<IRoomConfig> = {};
