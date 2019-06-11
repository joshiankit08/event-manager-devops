import { Moment } from 'moment';
import { IInvitation } from 'app/shared/model/invitation.model';

export const enum EventStatus {
  CREATED = 'CREATED',
  LIVE = 'LIVE',
  CLOSED = 'CLOSED',
  CANCELLED = 'CANCELLED'
}

export interface IEvent {
  id?: number;
  name?: string;
  description?: string;
  startDate?: Moment;
  endDate?: Moment;
  location?: string;
  eventStatus?: EventStatus;
  acceptedInvitations?: IInvitation[];
}

export const defaultValue: Readonly<IEvent> = {};
