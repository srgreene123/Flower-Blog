import { IUser } from 'app/shared/model/user.model';
import { IFlower } from 'app/shared/model/flower.model';

export interface IPost {
  id?: number;
  name?: string | null;
  date?: string | null;
  user?: IUser | null;
  flower?: IFlower | null;
}

export const defaultValue: Readonly<IPost> = {};
