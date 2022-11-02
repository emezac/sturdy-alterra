import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGameConfig } from 'app/shared/model/game-config.model';
import { getEntities as getGameConfigs } from 'app/entities/game-config/game-config.reducer';
import { IPlayer } from 'app/shared/model/player.model';
import { getEntities as getPlayers } from 'app/entities/player/player.reducer';
import { IGame } from 'app/shared/model/game.model';
import { getEntity, updateEntity, createEntity, reset } from './game.reducer';

export const GameUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const gameConfigs = useAppSelector(state => state.gameConfig.entities);
  const players = useAppSelector(state => state.player.entities);
  const gameEntity = useAppSelector(state => state.game.entity);
  const loading = useAppSelector(state => state.game.loading);
  const updating = useAppSelector(state => state.game.updating);
  const updateSuccess = useAppSelector(state => state.game.updateSuccess);

  const handleClose = () => {
    navigate('/game');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGameConfigs({}));
    dispatch(getPlayers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...gameEntity,
      ...values,
      location: gameConfigs.find(it => it.id.toString() === values.location.toString()),
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
          ...gameEntity,
          location: gameEntity?.location?.id,
          player: gameEntity?.player?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sturdyalterraApp.game.home.createOrEditLabel" data-cy="GameCreateUpdateHeading">
            Create or edit a Game
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="game-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Game Name"
                id="game-gameName"
                name="gameName"
                data-cy="gameName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="game-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Moves" id="game-moves" name="moves" data-cy="moves" type="text" />
              <ValidatedField id="game-location" name="location" data-cy="location" label="Location" type="select">
                <option value="" key="0" />
                {gameConfigs
                  ? gameConfigs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="game-player" name="player" data-cy="player" label="Player" type="select">
                <option value="" key="0" />
                {players
                  ? players.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game" replace color="info">
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

export default GameUpdate;
