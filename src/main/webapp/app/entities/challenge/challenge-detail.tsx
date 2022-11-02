import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './challenge.reducer';

export const ChallengeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const challengeEntity = useAppSelector(state => state.challenge.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="challengeDetailsHeading">Challenge</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{challengeEntity.id}</dd>
          <dt>
            <span id="introText">Intro Text</span>
          </dt>
          <dd>{challengeEntity.introText}</dd>
          <dt>
            <span id="challengeName">Challenge Name</span>
          </dt>
          <dd>{challengeEntity.challengeName}</dd>
          <dt>
            <span id="difficulty">Difficulty</span>
          </dt>
          <dd>{challengeEntity.difficulty}</dd>
          <dt>Name</dt>
          <dd>{challengeEntity.name ? challengeEntity.name.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/challenge" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/challenge/${challengeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChallengeDetail;
