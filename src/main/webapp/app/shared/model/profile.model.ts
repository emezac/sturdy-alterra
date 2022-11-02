export interface IProfile {
  id?: string;
  name?: string;
  auth0UserId?: string;
  socialNetwork?: string | null;
  aclSetup?: string | null;
}

export const defaultValue: Readonly<IProfile> = {};
