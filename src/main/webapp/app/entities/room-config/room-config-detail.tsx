import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './room-config.reducer';

export const RoomConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roomConfigEntity = useAppSelector(state => state.roomConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roomConfigDetailsHeading">Room Config</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{roomConfigEntity.id}</dd>
          <dt>
            <span id="setup">Setup</span>
          </dt>
          <dd>{roomConfigEntity.setup}</dd>
          <dt>
            <span id="numOfDoors">Num Of Doors</span>
          </dt>
          <dd>{roomConfigEntity.numOfDoors}</dd>
          <dt>
            <span id="numOfPrizes">Num Of Prizes</span>
          </dt>
          <dd>{roomConfigEntity.numOfPrizes}</dd>
        </dl>
        <Button tag={Link} to="/room-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/room-config/${roomConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoomConfigDetail;
