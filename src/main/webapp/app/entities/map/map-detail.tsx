import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './map.reducer';

export const MapDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mapEntity = useAppSelector(state => state.map.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mapDetailsHeading">Map</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{mapEntity.id}</dd>
          <dt>
            <span id="mapName">Map Name</span>
          </dt>
          <dd>{mapEntity.mapName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{mapEntity.description}</dd>
          <dt>Task</dt>
          <dd>
            {mapEntity.tasks
              ? mapEntity.tasks.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {mapEntity.tasks && i === mapEntity.tasks.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Game</dt>
          <dd>{mapEntity.game ? mapEntity.game.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/map" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/map/${mapEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MapDetail;
