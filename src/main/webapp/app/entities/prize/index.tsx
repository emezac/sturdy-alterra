import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Prize from './prize';
import PrizeDetail from './prize-detail';
import PrizeUpdate from './prize-update';
import PrizeDeleteDialog from './prize-delete-dialog';

const PrizeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Prize />} />
    <Route path="new" element={<PrizeUpdate />} />
    <Route path=":id">
      <Route index element={<PrizeDetail />} />
      <Route path="edit" element={<PrizeUpdate />} />
      <Route path="delete" element={<PrizeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PrizeRoutes;
