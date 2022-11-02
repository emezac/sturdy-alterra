import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDungeon } from 'app/shared/model/dungeon.model';
import { getEntities as getDungeons } from 'app/entities/dungeon/dungeon.reducer';
import { IGame } from 'app/shared/model/game.model';
import { getEntities as getGames } from 'app/entities/game/game.reducer';
import { IMap } from 'app/shared/model/map.model';
import { getEntity, updateEntity, createEntity, reset } from './map.reducer';

export const MapUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dungeons = useAppSelector(state => state.dungeon.entities);
  const games = useAppSelector(state => state.game.entities);
  const mapEntity = useAppSelector(state => state.map.entity);
  const loading = useAppSelector(state => state.map.loading);
  const updating = useAppSelector(state => state.map.updating);
  const updateSuccess = useAppSelector(state => state.map.updateSuccess);

  const handleClose = () => {
    navigate('/map');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDungeons({}));
    dispatch(getGames({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...mapEntity,
      ...values,
      tasks: mapIdList(values.tasks),
      game: games.find(it => it.id.toString() === values.game.toString()),
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
          ...mapEntity,
          tasks: mapEntity?.tasks?.map(e => e.id.toString()),
          game: mapEntity?.game?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sturdyalterraApp.map.home.createOrEditLabel" data-cy="MapCreateUpdateHeading">
            Create or edit a Map
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="map-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Map Name"
                id="map-mapName"
                name="mapName"
                data-cy="mapName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="map-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Task" id="map-task" data-cy="task" type="select" multiple name="tasks">
                <option value="" key="0" />
                {dungeons
                  ? dungeons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="map-game" name="game" data-cy="game" label="Game" type="select">
                <option value="" key="0" />
                {games
                  ? games.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/map" replace color="info">
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

export default MapUpdate;
