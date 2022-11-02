import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDoor } from 'app/shared/model/door.model';
import { getEntities as getDoors } from 'app/entities/door/door.reducer';
import { IPrize } from 'app/shared/model/prize.model';
import { getEntity, updateEntity, createEntity, reset } from './prize.reducer';

export const PrizeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doors = useAppSelector(state => state.door.entities);
  const prizeEntity = useAppSelector(state => state.prize.entity);
  const loading = useAppSelector(state => state.prize.loading);
  const updating = useAppSelector(state => state.prize.updating);
  const updateSuccess = useAppSelector(state => state.prize.updateSuccess);

  const handleClose = () => {
    navigate('/prize');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDoors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.expireDate = convertDateTimeToServer(values.expireDate);

    const entity = {
      ...prizeEntity,
      ...values,
      name: doors.find(it => it.id.toString() === values.name.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          expireDate: displayDefaultDateTime(),
        }
      : {
          ...prizeEntity,
          expireDate: convertDateTimeFromServer(prizeEntity.expireDate),
          name: prizeEntity?.name?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sturdyalterraApp.prize.home.createOrEditLabel" data-cy="PrizeCreateUpdateHeading">
            Create or edit a Prize
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="prize-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Prize Name"
                id="prize-prizeName"
                name="prizeName"
                data-cy="prizeName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Pips" id="prize-pips" name="pips" data-cy="pips" type="text" />
              <ValidatedField
                label="Expire Date"
                id="prize-expireDate"
                name="expireDate"
                data-cy="expireDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="prize-name" name="name" data-cy="name" label="Name" type="select">
                <option value="" key="0" />
                {doors
                  ? doors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/prize" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PrizeUpdate;
