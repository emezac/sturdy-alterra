import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDungeon } from 'app/shared/model/dungeon.model';
import { getEntities } from './dungeon.reducer';

export const Dungeon = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const dungeonList = useAppSelector(state => state.dungeon.entities);
  const loading = useAppSelector(state => state.dungeon.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="dungeon-heading" data-cy="DungeonHeading">
        Dungeons
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/dungeon/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Dungeon
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {dungeonList && dungeonList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Id</th>
                <th>Dungeon Name</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dungeonList.map((dungeon, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/dungeon/${dungeon.id}`} color="link" size="sm">
                      {dungeon.id}
                    </Button>
                  </td>
                  <td>{dungeon.dungeonName}</td>
                  <td>{dungeon.startDate ? <TextFormat type="date" value={dungeon.startDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{dungeon.endDate ? <TextFormat type="date" value={dungeon.endDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/dungeon/${dungeon.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/dungeon/${dungeon.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/dungeon/${dungeon.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Dungeons found</div>
        )}
      </div>
    </div>
  );
};

export default Dungeon;
