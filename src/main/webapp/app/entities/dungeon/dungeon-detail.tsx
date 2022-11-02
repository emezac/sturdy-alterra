import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dungeon.reducer';

export const DungeonDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dungeonEntity = useAppSelector(state => state.dungeon.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dungeonDetailsHeading">Dungeon</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{dungeonEntity.id}</dd>
          <dt>
            <span id="dungeonName">Dungeon Name</span>
          </dt>
          <dd>{dungeonEntity.dungeonName}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>{dungeonEntity.startDate ? <TextFormat value={dungeonEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>{dungeonEntity.endDate ? <TextFormat value={dungeonEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/dungeon" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dungeon/${dungeonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DungeonDetail;
