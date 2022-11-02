import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FloorConfig from './floor-config';
import FloorConfigDetail from './floor-config-detail';
import FloorConfigUpdate from './floor-config-update';
import FloorConfigDeleteDialog from './floor-config-delete-dialog';

const FloorConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FloorConfig />} />
    <Route path="new" element={<FloorConfigUpdate />} />
    <Route path=":id">
      <Route index element={<FloorConfigDetail />} />
      <Route path="edit" element={<FloorConfigUpdate />} />
      <Route path="delete" element={<FloorConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FloorConfigRoutes;
