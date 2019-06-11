import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/event">
      Event
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guest">
      Guest
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/invitation">
      Invitation
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
