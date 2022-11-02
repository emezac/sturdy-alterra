import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dungeon from './dungeon';
import DungeonDetail from './dungeon-detail';
import DungeonUpdate from './dungeon-update';
import DungeonDeleteDialog from './dungeon-delete-dialog';

const DungeonRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Dungeon />} />
    <Route path="new" element={<DungeonUpdate />} />
    <Route path=":id">
      <Route index element={<DungeonDetail />} />
      <Route path="edit" element={<DungeonUpdate />} />
      <Route path="delete" element={<DungeonDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DungeonRoutes;
