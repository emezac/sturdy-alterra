import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './floor-config.reducer';

export const FloorConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const floorConfigEntity = useAppSelector(state => state.floorConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="floorConfigDetailsHeading">Floor Config</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{floorConfigEntity.id}</dd>
          <dt>
            <span id="setup">Setup</span>
          </dt>
          <dd>{floorConfigEntity.setup}</dd>
          <dt>
            <span id="numOfRooms">Num Of Rooms</span>
          </dt>
          <dd>{floorConfigEntity.numOfRooms}</dd>
        </dl>
        <Button tag={Link} to="/floor-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/floor-config/${floorConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FloorConfigDetail;
