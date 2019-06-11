import { IEvent } from 'app/shared/model/event.model';
import { IGuest } from 'app/shared/model/guest.model';

export const enum InvitationStatus {
  CREATED = 'CREATED',
  SENT = 'SENT',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

export interface IInvitation {
  id?: number;
  subject?: string;
  invitationStatus?: InvitationStatus;
  event?: IEvent;
  guest?: IGuest;
}

export const defaultValue: Readonly<IInvitation> = {};
