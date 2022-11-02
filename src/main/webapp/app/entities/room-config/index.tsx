import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RoomConfig from './room-config';
import RoomConfigDetail from './room-config-detail';
import RoomConfigUpdate from './room-config-update';
import RoomConfigDeleteDialog from './room-config-delete-dialog';

const RoomConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RoomConfig />} />
    <Route path="new" element={<RoomConfigUpdate />} />
    <Route path=":id">
      <Route index element={<RoomConfigDetail />} />
      <Route path="edit" element={<RoomConfigUpdate />} />
      <Route path="delete" element={<RoomConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RoomConfigRoutes;
