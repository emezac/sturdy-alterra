import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './floor.reducer';

export const FloorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const floorEntity = useAppSelector(state => state.floor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="floorDetailsHeading">Floor</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{floorEntity.id}</dd>
          <dt>
            <span id="floorName">Floor Name</span>
          </dt>
          <dd>{floorEntity.floorName}</dd>
          <dt>Location</dt>
          <dd>{floorEntity.location ? floorEntity.location.id : ''}</dd>
          <dt>Name</dt>
          <dd>{floorEntity.name ? floorEntity.name.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/floor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/floor/${floorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FloorDetail;
