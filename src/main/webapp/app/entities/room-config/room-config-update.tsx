import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRoomConfig } from 'app/shared/model/room-config.model';
import { getEntity, updateEntity, createEntity, reset } from './room-config.reducer';

export const RoomConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const roomConfigEntity = useAppSelector(state => state.roomConfig.entity);
  const loading = useAppSelector(state => state.roomConfig.loading);
  const updating = useAppSelector(state => state.roomConfig.updating);
  const updateSuccess = useAppSelector(state => state.roomConfig.updateSuccess);

  const handleClose = () => {
    navigate('/room-config');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...roomConfigEntity,
      ...values,
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
          ...roomConfigEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sturdyalterraApp.roomConfig.home.createOrEditLabel" data-cy="RoomConfigCreateUpdateHeading">
            Create or edit a Room Config
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="room-config-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField label="Setup" id="room-config-setup" name="setup" data-cy="setup" type="text" />
              <ValidatedField label="Num Of Doors" id="room-config-numOfDoors" name="numOfDoors" data-cy="numOfDoors" type="text" />
              <ValidatedField label="Num Of Prizes" id="room-config-numOfPrizes" name="numOfPrizes" data-cy="numOfPrizes" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/room-config" replace color="info">
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

export default RoomConfigUpdate;
