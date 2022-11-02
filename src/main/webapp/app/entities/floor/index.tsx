import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Floor from './floor';
import FloorDetail from './floor-detail';
import FloorUpdate from './floor-update';
import FloorDeleteDialog from './floor-delete-dialog';

const FloorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Floor />} />
    <Route path="new" element={<FloorUpdate />} />
    <Route path=":id">
      <Route index element={<FloorDetail />} />
      <Route path="edit" element={<FloorUpdate />} />
      <Route path="delete" element={<FloorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FloorRoutes;
