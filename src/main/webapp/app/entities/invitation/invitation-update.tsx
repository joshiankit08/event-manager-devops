import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Label, Row } from 'reactstrap';
import { AvField, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import { getEntities as getEvents } from 'app/entities/event/event.reducer';
import { getEntities as getGuests } from 'app/entities/guest/guest.reducer';
import { createEntity, getEntity, reset, updateEntity } from './invitation.reducer';

// tslint:disable-next-line:no-unused-variable

export interface IInvitationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IInvitationUpdateState {
  isNew: boolean;
  eventId: string;
  guestId: string;
}

export class InvitationUpdate extends React.Component<IInvitationUpdateProps, IInvitationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      eventId: '0',
      guestId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getEvents();
    this.props.getGuests();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { invitationEntity } = this.props;
      const entity = {
        ...invitationEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/invitation');
  };

  render() {
    const { invitationEntity, events, guests, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="eventmanagerApp.invitation.home.createOrEditLabel">Create or edit a Invitation</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : invitationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="invitation-id">ID</Label>
                    <AvInput id="invitation-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="subjectLabel" for="invitation-subject">
                    Subject
                  </Label>
                  <AvField
                    id="invitation-subject"
                    type="text"
                    name="subject"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="invitationStatusLabel" for="invitation-invitationStatus">
                    Invitation Status
                  </Label>
                  <AvInput
                    id="invitation-invitationStatus"
                    type="select"
                    className="form-control"
                    name="invitationStatus"
                    value={(!isNew && invitationEntity.invitationStatus) || 'CREATED'}
                  >
                    <option value="CREATED">CREATED</option>
                    <option value="SENT">SENT</option>
                    <option value="APPROVED">APPROVED</option>
                    <option value="REJECTED">REJECTED</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="invitation-event">Event</Label>
                  <AvInput id="invitation-event" type="select" className="form-control" name="event.id">
                    <option value="" key="0" />
                    {events
                      ? events.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="invitation-guest">Guest</Label>
                  <AvInput id="invitation-guest" type="select" className="form-control" name="guest.id">
                    <option value="" key="0" />
                    {guests
                      ? guests.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/invitation" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  events: storeState.event.entities,
  guests: storeState.guest.entities,
  invitationEntity: storeState.invitation.entity,
  loading: storeState.invitation.loading,
  updating: storeState.invitation.updating,
  updateSuccess: storeState.invitation.updateSuccess
});

const mapDispatchToProps = {
  getEvents,
  getGuests,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InvitationUpdate);
