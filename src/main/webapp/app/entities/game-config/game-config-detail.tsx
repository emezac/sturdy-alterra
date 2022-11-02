import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './game-config.reducer';

export const GameConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gameConfigEntity = useAppSelector(state => state.gameConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameConfigDetailsHeading">Game Config</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{gameConfigEntity.id}</dd>
          <dt>
            <span id="setupDate">Setup Date</span>
          </dt>
          <dd>
            {gameConfigEntity.setupDate ? <TextFormat value={gameConfigEntity.setupDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="floorConfig">Floor Config</span>
          </dt>
          <dd>{gameConfigEntity.floorConfig}</dd>
          <dt>
            <span id="roomConfig">Room Config</span>
          </dt>
          <dd>{gameConfigEntity.roomConfig}</dd>
          <dt>
            <span id="dateInit">Date Init</span>
          </dt>
          <dd>
            {gameConfigEntity.dateInit ? <TextFormat value={gameConfigEntity.dateInit} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateEnd">Date End</span>
          </dt>
          <dd>{gameConfigEntity.dateEnd ? <TextFormat value={gameConfigEntity.dateEnd} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/game-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game-config/${gameConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GameConfigDetail;
