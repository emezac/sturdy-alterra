import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './game.reducer';

export const GameDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gameEntity = useAppSelector(state => state.game.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameDetailsHeading">Game</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{gameEntity.id}</dd>
          <dt>
            <span id="gameName">Game Name</span>
          </dt>
          <dd>{gameEntity.gameName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{gameEntity.description}</dd>
          <dt>
            <span id="moves">Moves</span>
          </dt>
          <dd>{gameEntity.moves}</dd>
          <dt>Location</dt>
          <dd>{gameEntity.location ? gameEntity.location.id : ''}</dd>
          <dt>Player</dt>
          <dd>{gameEntity.player ? gameEntity.player.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/game" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game/${gameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GameDetail;
