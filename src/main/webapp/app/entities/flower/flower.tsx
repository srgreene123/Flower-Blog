import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFlower } from 'app/shared/model/flower.model';
import { getEntities } from './flower.reducer';

export const Flower = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const flowerList = useAppSelector<IFlower[]>(state => state.flower.entities);
  const loading = useAppSelector(state => state.flower.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="flower-heading" data-cy="FlowerHeading">
        <Translate contentKey="blogApp.flower.home.title">Flowers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="blogApp.flower.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/flower/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="blogApp.flower.home.createLabel">Create new Flower</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {flowerList && flowerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="blogApp.flower.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.flower.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.flower.season">Season</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.flower.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.flower.imageLink">Image Link</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.flower.location">Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {flowerList.map((flower, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/flower/${flower.id}`} color="link" size="sm">
                      {flower.id}
                    </Button>
                  </td>
                  <td>{flower.name}</td>
                  <td>{flower.season}</td>
                  <td>{flower.description}</td>
                  <td>{flower.imageLink}</td>
                  <td>
                    {flower.locations
                      ? flower.locations.map((val, j) => (
                          <span key={j}>
                            <Link to={`/location/${val.id}`}>{val.city}</Link>
                            {j === flower.locations.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/flower/${flower.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/flower/${flower.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/flower/${flower.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="blogApp.flower.home.notFound">No Flowers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Flower;
