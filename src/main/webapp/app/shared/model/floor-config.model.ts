export interface IFloorConfig {
  id?: string;
  setup?: string;
  numOfRooms?: number | null;
}

export const defaultValue: Readonly<IFloorConfig> = {};
