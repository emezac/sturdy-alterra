import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Profile from './profile';
import Player from './player';
import Pack from './pack';
import Card from './card';
import Game from './game';
import GameConfig from './game-config';
import Map from './map';
import Dungeon from './dungeon';
import Floor from './floor';
import FloorConfig from './floor-config';
import Prize from './prize';
import RoomConfig from './room-config';
import Room from './room';
import Challenge from './challenge';
import Door from './door';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="profile/*" element={<Profile />} />
        <Route path="player/*" element={<Player />} />
        <Route path="pack/*" element={<Pack />} />
        <Route path="card/*" element={<Card />} />
        <Route path="game/*" element={<Game />} />
        <Route path="game-config/*" element={<GameConfig />} />
        <Route path="map/*" element={<Map />} />
        <Route path="dungeon/*" element={<Dungeon />} />
        <Route path="floor/*" element={<Floor />} />
        <Route path="floor-config/*" element={<FloorConfig />} />
        <Route path="prize/*" element={<Prize />} />
        <Route path="room-config/*" element={<RoomConfig />} />
        <Route path="room/*" element={<Room />} />
        <Route path="challenge/*" element={<Challenge />} />
        <Route path="door/*" element={<Door />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
