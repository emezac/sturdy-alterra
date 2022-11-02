import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFloorConfig } from 'app/shared/model/floor-config.model';
import { getEntities as getFloorConfigs } from 'app/entities/floor-config/floor-config.reducer';
import { IDungeon } from 'app/shared/model/dungeon.model';
import { getEntities as getDungeons } from 'app/entities/dungeon/dungeon.reducer';
import { IFloor } from 'app/shared/model/floor.model';
import { getEntity, updateEntity, createEntity, reset } from './floor.reducer';

export const FloorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const floorConfigs = useAppSelector(state => state.floorConfig.entities);
  const dungeons = useAppSelector(state => state.dungeon.entities);
  const floorEntity = useAppSelector(state => state.floor.entity);
  const loading = useAppSelector(state => state.floor.loading);
  const updating = useAppSelector(state => state.floor.updating);
  const updateSuccess = useAppSelector(state => state.floor.updateSuccess);

  const handleClose = () => {
    navigate('/floor');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFloorConfigs({}));
    dispatch(getDungeons({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...floorEntity,
      ...values,
      location: floorConfigs.find(it => it.id.toString() === values.location.toString()),
      name: dungeons.find(it => it.id.toString() === values.name.toString()),
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
          ...floorEntity,
          location: floorEntity?.location?.id,
          name: floorEntity?.name?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sturdyalterraApp.floor.home.createOrEditLabel" data-cy="FloorCreateUpdateHeading">
            Create or edit a Floor
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="floor-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Floor Name"
                id="floor-floorName"
                name="floorName"
                data-cy="floorName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="floor-location" name="location" data-cy="location" label="Location" type="select">
                <option value="" key="0" />
                {floorConfigs
                  ? floorConfigs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="floor-name" name="name" data-cy="name" label="Name" type="select">
                <option value="" key="0" />
                {dungeons
                  ? dungeons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/floor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FloorUpdate;
