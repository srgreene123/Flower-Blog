import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Flower from './flower';
import FlowerDetail from './flower-detail';
import FlowerUpdate from './flower-update';
import FlowerDeleteDialog from './flower-delete-dialog';

const FlowerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Flower />} />
    <Route path="new" element={<FlowerUpdate />} />
    <Route path=":id">
      <Route index element={<FlowerDetail />} />
      <Route path="edit" element={<FlowerUpdate />} />
      <Route path="delete" element={<FlowerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FlowerRoutes;
