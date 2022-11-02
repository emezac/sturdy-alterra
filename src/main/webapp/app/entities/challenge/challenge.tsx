import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChallenge } from 'app/shared/model/challenge.model';
import { getEntities } from './challenge.reducer';

export const Challenge = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const challengeList = useAppSelector(state => state.challenge.entities);
  const loading = useAppSelector(state => state.challenge.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="challenge-heading" data-cy="ChallengeHeading">
        Challenges
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/challenge/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Challenge
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {challengeList && challengeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Id</th>
                <th>Intro Text</th>
                <th>Challenge Name</th>
                <th>Difficulty</th>
                <th>Name</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {challengeList.map((challenge, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/challenge/${challenge.id}`} color="link" size="sm">
                      {challenge.id}
                    </Button>
                  </td>
                  <td>{challenge.introText}</td>
                  <td>{challenge.challengeName}</td>
                  <td>{challenge.difficulty}</td>
                  <td>{challenge.name ? <Link to={`/room/${challenge.name.id}`}>{challenge.name.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/challenge/${challenge.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/challenge/${challenge.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/challenge/${challenge.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Challenges found</div>
        )}
      </div>
    </div>
  );
};

export default Challenge;
