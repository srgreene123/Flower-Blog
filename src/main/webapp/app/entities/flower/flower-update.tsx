import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPost } from 'app/shared/model/post.model';
import { getEntities as getPosts } from 'app/entities/post/post.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IFlower } from 'app/shared/model/flower.model';
import { getEntity, updateEntity, createEntity, reset } from './flower.reducer';

export const FlowerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const posts = useAppSelector(state => state.post.entities);
  const locations = useAppSelector(state => state.location.entities);
  const flowerEntity = useAppSelector(state => state.flower.entity);
  const loading = useAppSelector(state => state.flower.loading);
  const updating = useAppSelector(state => state.flower.updating);
  const updateSuccess = useAppSelector(state => state.flower.updateSuccess);

  const handleClose = () => {
    navigate('/flower');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPosts({}));
    dispatch(getLocations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...flowerEntity,
      ...values,
      locations: mapIdList(values.locations),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...flowerEntity,
          locations: flowerEntity?.locations?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blogApp.flower.home.createOrEditLabel" data-cy="FlowerCreateUpdateHeading">
            <Translate contentKey="blogApp.flower.home.createOrEditLabel">Create or edit a Flower</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="flower-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('blogApp.flower.name')} id="flower-name" name="name" data-cy="name" type="text" />
              <ValidatedField label={translate('blogApp.flower.season')} id="flower-season" name="season" data-cy="season" type="text" />
              <ValidatedField
                label={translate('blogApp.flower.description')}
                id="flower-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('blogApp.flower.imageLink')}
                id="flower-imageLink"
                name="imageLink"
                data-cy="imageLink"
                type="text"
              />
              <ValidatedField
                label={translate('blogApp.flower.location')}
                id="flower-location"
                data-cy="location"
                type="select"
                multiple
                name="locations"
              >
                <option value="" key="0" />
                {locations
                  ? locations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/flower" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FlowerUpdate;
