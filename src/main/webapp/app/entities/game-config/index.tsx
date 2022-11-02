import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GameConfig from './game-config';
import GameConfigDetail from './game-config-detail';
import GameConfigUpdate from './game-config-update';
import GameConfigDeleteDialog from './game-config-delete-dialog';

const GameConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GameConfig />} />
    <Route path="new" element={<GameConfigUpdate />} />
    <Route path=":id">
      <Route index element={<GameConfigDetail />} />
      <Route path="edit" element={<GameConfigUpdate />} />
      <Route path="delete" element={<GameConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GameConfigRoutes;
