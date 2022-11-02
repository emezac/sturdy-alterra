import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlayer } from 'app/shared/model/player.model';
import { getEntities as getPlayers } from 'app/entities/player/player.reducer';
import { IPack } from 'app/shared/model/pack.model';
import { getEntity, updateEntity, createEntity, reset } from './pack.reducer';

export const PackUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const players = useAppSelector(state => state.player.entities);
  const packEntity = useAppSelector(state => state.pack.entity);
  const loading = useAppSelector(state => state.pack.loading);
  const updating = useAppSelector(state => state.pack.updating);
  const updateSuccess = useAppSelector(state => state.pack.updateSuccess);

  const handleClose = () => {
    navigate('/pack');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPlayers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...packEntity,
      ...values,
      player: players.find(it => it.id.toString() === values.player.toString()),
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
          ...packEntity,
          player: packEntity?.player?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sturdyalterraApp.pack.home.createOrEditLabel" data-cy="PackCreateUpdateHeading">
            Create or edit a Pack
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="pack-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Pack Name"
                id="pack-packName"
                name="packName"
                data-cy="packName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Deck Name" id="pack-deckName" name="deckName" data-cy="deckName" type="text" />
              <ValidatedField label="Config Setup" id="pack-configSetup" name="configSetup" data-cy="configSetup" type="text" />
              <ValidatedField id="pack-player" name="player" data-cy="player" label="Player" type="select">
                <option value="" key="0" />
                {players
                  ? players.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pack" replace color="info">
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

export default PackUpdate;
