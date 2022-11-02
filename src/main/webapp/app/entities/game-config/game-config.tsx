import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGameConfig } from 'app/shared/model/game-config.model';
import { getEntities } from './game-config.reducer';

export const GameConfig = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const gameConfigList = useAppSelector(state => state.gameConfig.entities);
  const loading = useAppSelector(state => state.gameConfig.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="game-config-heading" data-cy="GameConfigHeading">
        Game Configs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/game-config/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Game Config
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gameConfigList && gameConfigList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Id</th>
                <th>Setup Date</th>
                <th>Floor Config</th>
                <th>Room Config</th>
                <th>Date Init</th>
                <th>Date End</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gameConfigList.map((gameConfig, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/game-config/${gameConfig.id}`} color="link" size="sm">
                      {gameConfig.id}
                    </Button>
                  </td>
                  <td>{gameConfig.setupDate ? <TextFormat type="date" value={gameConfig.setupDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{gameConfig.floorConfig}</td>
                  <td>{gameConfig.roomConfig}</td>
                  <td>{gameConfig.dateInit ? <TextFormat type="date" value={gameConfig.dateInit} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{gameConfig.dateEnd ? <TextFormat type="date" value={gameConfig.dateEnd} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/game-config/${gameConfig.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/game-config/${gameConfig.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/game-config/${gameConfig.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Game Configs found</div>
        )}
      </div>
    </div>
  );
};

export default GameConfig;
