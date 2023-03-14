import { IFlower } from 'app/shared/model/flower.model';

export interface ILocation {
  id?: number;
  city?: string | null;
  countryName?: string | null;
  flowers?: IFlower[] | null;
}

export const defaultValue: Readonly<ILocation> = {};
