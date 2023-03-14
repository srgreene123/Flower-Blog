import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    xxxx
    <Row>
      <Col md="12">
        <p>
          <Translate contentKey="global.footer">Your footer</Translate>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
