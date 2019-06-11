import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './invitation.reducer';

// tslint:disable-next-line:no-unused-variable

export interface IInvitationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InvitationDetail extends React.Component<IInvitationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { invitationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Invitation [<b>{invitationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="subject">Subject</span>
            </dt>
            <dd>{invitationEntity.subject}</dd>
            <dt>
              <span id="invitationStatus">Invitation Status</span>
            </dt>
            <dd>{invitationEntity.invitationStatus}</dd>
            <dt>Event</dt>
            <dd>{invitationEntity.event ? invitationEntity.event.id : ''}</dd>
            <dt>Guest</dt>
            <dd>{invitationEntity.guest ? invitationEntity.guest.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/invitation" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/invitation/${invitationEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ invitation }: IRootState) => ({
  invitationEntity: invitation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InvitationDetail);
