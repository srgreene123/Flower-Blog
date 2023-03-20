import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './flower.reducer';

export const FlowerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const flowerEntity = useAppSelector(state => state.flower.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flowerDetailsHeading">
          <Translate contentKey="blogApp.flower.detail.title">Flower</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{flowerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="blogApp.flower.name">Name</Translate>
            </span>
          </dt>
          <dd>{flowerEntity.name}</dd>
          <dt>
            <span id="season">
              <Translate contentKey="blogApp.flower.season">Season</Translate>
            </span>
          </dt>
          <dd>{flowerEntity.season}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="blogApp.flower.description">Description</Translate>
            </span>
          </dt>
          <dd>{flowerEntity.description}</dd>
          <dt>
            <span id="imageLink">
              <Translate contentKey="blogApp.flower.imageLink">Image Link</Translate>
            </span>
          </dt>
          <dd>{flowerEntity.imageLink}</dd>
          <dt>
            <Translate contentKey="blogApp.flower.location">Location</Translate>
          </dt>
          <dd>
            {flowerEntity.locations
              ? flowerEntity.locations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {flowerEntity.locations && i === flowerEntity.locations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/flower" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flower/${flowerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FlowerDetail;
