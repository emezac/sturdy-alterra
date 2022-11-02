import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Challenge from './challenge';
import ChallengeDetail from './challenge-detail';
import ChallengeUpdate from './challenge-update';
import ChallengeDeleteDialog from './challenge-delete-dialog';

const ChallengeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Challenge />} />
    <Route path="new" element={<ChallengeUpdate />} />
    <Route path=":id">
      <Route index element={<ChallengeDetail />} />
      <Route path="edit" element={<ChallengeUpdate />} />
      <Route path="delete" element={<ChallengeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChallengeRoutes;
