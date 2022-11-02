import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGameConfig } from 'app/shared/model/game-config.model';
import { getEntity, updateEntity, createEntity, reset } from './game-config.reducer';

export const GameConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const gameConfigEntity = useAppSelector(state => state.gameConfig.entity);
  const loading = useAppSelector(state => state.gameConfig.loading);
  const updating = useAppSelector(state => state.gameConfig.updating);
  const updateSuccess = useAppSelector(state => state.gameConfig.updateSuccess);

  const handleClose = () => {
    navigate('/game-config');
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
    values.setupDate = convertDateTimeToServer(values.setupDate);
    values.dateInit = convertDateTimeToServer(values.dateInit);
    values.dateEnd = convertDateTimeToServer(values.dateEnd);

    const entity = {
      ...gameConfigEntity,
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
      ? {
          setupDate: displayDefaultDateTime(),
          dateInit: displayDefaultDateTime(),
          dateEnd: displayDefaultDateTime(),
        }
      : {
          ...gameConfigEntity,
          setupDate: convertDateTimeFromServer(gameConfigEntity.setupDate),
          dateInit: convertDateTimeFromServer(gameConfigEntity.dateInit),
          dateEnd: convertDateTimeFromServer(gameConfigEntity.dateEnd),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sturdyalterraApp.gameConfig.home.createOrEditLabel" data-cy="GameConfigCreateUpdateHeading">
            Create or edit a Game Config
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="game-config-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Setup Date"
                id="game-config-setupDate"
                name="setupDate"
                data-cy="setupDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Floor Config" id="game-config-floorConfig" name="floorConfig" data-cy="floorConfig" type="text" />
              <ValidatedField label="Room Config" id="game-config-roomConfig" name="roomConfig" data-cy="roomConfig" type="text" />
              <ValidatedField
                label="Date Init"
                id="game-config-dateInit"
                name="dateInit"
                data-cy="dateInit"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Date End"
                id="game-config-dateEnd"
                name="dateEnd"
                data-cy="dateEnd"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game-config" replace color="info">
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

export default GameConfigUpdate;
