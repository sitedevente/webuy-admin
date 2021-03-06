import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './shop.reducer';
import { IShop } from 'app/shared/model/shop.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Shop = (props: IShopProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { shopList, match, loading } = props;
  return (
    <div>
      <h2 id="shop-heading">
        Shops
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Shop
        </Link>
      </h2>
      <div className="table-responsive">
        {shopList && shopList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop Group</th>
                <th>Address</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shopList.map((shop, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shop.id}`} color="link" size="sm">
                      {shop.id}
                    </Button>
                  </td>
                  <td>{shop.shopGroup ? <Link to={`shop-group/${shop.shopGroup.id}`}>{shop.shopGroup.name}</Link> : ''}</td>
                  <td>{shop.address ? <Link to={`address/${shop.address.id}`}>{shop.address.city}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shop.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shop.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shop.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Shops found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ shop }: IRootState) => ({
  shopList: shop.entities,
  loading: shop.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Shop);
