export type UUID = `${string}-${string}-${string}-${string}-${string}`;

export interface Babee {
  id?: UUID;
  firstName: string;
  lastName: string;
  birthDate: Date;
  photo: string;
}

export type BabeeList = Babee[];

export interface BaseItem {
  id?: UUID;
  babeeId: UUID;
  eventDate: Date;
}

export interface Activity extends BaseItem {
  name: string;
  activityType?: number;
  comment?: string;
}

export type ActivityList = Activity[];

export interface HealthAct extends BaseItem {
  actHour: string;
  healthActType: number;
  temperature?: number;
  medecine?: number;
  dosage?: number;
}

export type HealthActList = HealthAct[];

export interface Sleeping extends BaseItem {
  beginHour: string;
  endHour: string;
  duration?: number;
}

export type SleepingList = Sleeping[];

export interface CareAct extends BaseItem {
  careActType: number;
  careActDetail?: number;
  comment?: string;
}

export type CareActList = CareAct[];

export interface Feeding extends BaseItem {
  feedingInformations: string[];
}

export type FeedingList = Feeding[];

export interface Informations extends BaseItem {
  comment?: string;
}

export type InformationList = Informations[];

export interface User {
  role: string;
  bebeId?: number;
}

export const CARE_ACT_TYPE_LIST = [
  {
    id: 1,
    label: 'Urine',
  },
  {
    id: 2,
    label: 'Selle',
  },
  {
    id: 3,
    label: 'Yeux',
  },
  { id: 4, label: 'Nez' },
];

export const CARE_ACT_DETAIL_LIST = [
  { id: 1, label: 'Couche' },
  { id: 2, label: 'Pot' },
  { id: 3, label: 'Toilettes' },
];

export const HEALTH_ACT_TYPE_LIST = [
  { id: 1, label: 'Température' },
  { id: 2, label: 'Médicaments' },
];
