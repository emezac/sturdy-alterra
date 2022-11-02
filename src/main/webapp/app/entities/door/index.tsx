import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Door from './door';
import DoorDetail from './door-detail';
import DoorUpdate from './door-update';
import DoorDeleteDialog from './door-delete-dialog';

const DoorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Door />} />
    <Route path="new" element={<DoorUpdate />} />
    <Route path=":id">
      <Route index element={<DoorDetail />} />
      <Route path="edit" element={<DoorUpdate />} />
      <Route path="delete" element={<DoorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DoorRoutes;
