import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Map from './map';
import MapDetail from './map-detail';
import MapUpdate from './map-update';
import MapDeleteDialog from './map-delete-dialog';

const MapRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Map />} />
    <Route path="new" element={<MapUpdate />} />
    <Route path=":id">
      <Route index element={<MapDetail />} />
      <Route path="edit" element={<MapUpdate />} />
      <Route path="delete" element={<MapDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MapRoutes;
