import { IInvitation } from 'app/shared/model/invitation.model';

export interface IGuest {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  acceptedInvitations?: IInvitation[];
}

export const defaultValue: Readonly<IGuest> = {};
