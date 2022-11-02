import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/profile">
        Profile
      </MenuItem>
      <MenuItem icon="asterisk" to="/player">
        Player
      </MenuItem>
      <MenuItem icon="asterisk" to="/pack">
        Pack
      </MenuItem>
      <MenuItem icon="asterisk" to="/card">
        Card
      </MenuItem>
      <MenuItem icon="asterisk" to="/game">
        Game
      </MenuItem>
      <MenuItem icon="asterisk" to="/game-config">
        Game Config
      </MenuItem>
      <MenuItem icon="asterisk" to="/map">
        Map
      </MenuItem>
      <MenuItem icon="asterisk" to="/dungeon">
        Dungeon
      </MenuItem>
      <MenuItem icon="asterisk" to="/floor">
        Floor
      </MenuItem>
      <MenuItem icon="asterisk" to="/floor-config">
        Floor Config
      </MenuItem>
      <MenuItem icon="asterisk" to="/prize">
        Prize
      </MenuItem>
      <MenuItem icon="asterisk" to="/room-config">
        Room Config
      </MenuItem>
      <MenuItem icon="asterisk" to="/room">
        Room
      </MenuItem>
      <MenuItem icon="asterisk" to="/challenge">
        Challenge
      </MenuItem>
      <MenuItem icon="asterisk" to="/door">
        Door
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
