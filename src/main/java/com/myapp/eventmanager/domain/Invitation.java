package com.myapp.eventmanager.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.myapp.eventmanager.domain.enumeration.InvitationStatus;

/**
 * A Invitation.
 */
@Entity
@Table(name = "invitation")
public class Invitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status")
    private InvitationStatus invitationStatus;

    @ManyToOne
    @JsonIgnoreProperties("invitations")
    private Event event;

    @ManyToOne
    @JsonIgnoreProperties("invitations")
    private Guest guest;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Invitation subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public Invitation invitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
        return this;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public Event getEvent() {
        return event;
    }

    public Invitation event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Guest getGuest() {
        return guest;
    }

    public Invitation guest(Guest guest) {
        this.guest = guest;
        return this;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invitation)) {
            return false;
        }
        return id != null && id.equals(((Invitation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Invitation{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", invitationStatus='" + getInvitationStatus() + "'" +
            "}";
    }
}
