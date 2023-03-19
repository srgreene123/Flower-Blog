import { IPost } from 'app/shared/model/post.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IFlower {
  id?: number;
  name?: string | null;
  season?: string | null;
  description?: string | null;
  imageLink?: string | null;
  post?: IPost | null;
  locations?: ILocation[] | null;
}

export const defaultValue: Readonly<IFlower> = {};
