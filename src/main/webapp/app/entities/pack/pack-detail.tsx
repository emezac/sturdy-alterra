import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pack.reducer';

export const PackDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const packEntity = useAppSelector(state => state.pack.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="packDetailsHeading">Pack</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{packEntity.id}</dd>
          <dt>
            <span id="packName">Pack Name</span>
          </dt>
          <dd>{packEntity.packName}</dd>
          <dt>
            <span id="deckName">Deck Name</span>
          </dt>
          <dd>{packEntity.deckName}</dd>
          <dt>
            <span id="configSetup">Config Setup</span>
          </dt>
          <dd>{packEntity.configSetup}</dd>
          <dt>Player</dt>
          <dd>{packEntity.player ? packEntity.player.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pack" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pack/${packEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PackDetail;
