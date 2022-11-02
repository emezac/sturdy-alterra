import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPrize } from 'app/shared/model/prize.model';
import { getEntities } from './prize.reducer';

export const Prize = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const prizeList = useAppSelector(state => state.prize.entities);
  const loading = useAppSelector(state => state.prize.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="prize-heading" data-cy="PrizeHeading">
        Prizes
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/prize/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Prize
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {prizeList && prizeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Id</th>
                <th>Prize Name</th>
                <th>Pips</th>
                <th>Expire Date</th>
                <th>Name</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {prizeList.map((prize, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/prize/${prize.id}`} color="link" size="sm">
                      {prize.id}
                    </Button>
                  </td>
                  <td>{prize.prizeName}</td>
                  <td>{prize.pips}</td>
                  <td>{prize.expireDate ? <TextFormat type="date" value={prize.expireDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{prize.name ? <Link to={`/door/${prize.name.id}`}>{prize.name.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/prize/${prize.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/prize/${prize.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/prize/${prize.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Prizes found</div>
        )}
      </div>
    </div>
  );
};

export default Prize;
