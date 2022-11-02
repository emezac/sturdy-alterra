import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRoomConfig } from 'app/shared/model/room-config.model';
import { getEntities } from './room-config.reducer';

export const RoomConfig = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const roomConfigList = useAppSelector(state => state.roomConfig.entities);
  const loading = useAppSelector(state => state.roomConfig.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="room-config-heading" data-cy="RoomConfigHeading">
        Room Configs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/room-config/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Room Config
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {roomConfigList && roomConfigList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Id</th>
                <th>Setup</th>
                <th>Num Of Doors</th>
                <th>Num Of Prizes</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {roomConfigList.map((roomConfig, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/room-config/${roomConfig.id}`} color="link" size="sm">
                      {roomConfig.id}
                    </Button>
                  </td>
                  <td>{roomConfig.setup}</td>
                  <td>{roomConfig.numOfDoors}</td>
                  <td>{roomConfig.numOfPrizes}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/room-config/${roomConfig.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/room-config/${roomConfig.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/room-config/${roomConfig.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Room Configs found</div>
        )}
      </div>
    </div>
  );
};

export default RoomConfig;
