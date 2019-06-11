package com.myapp.eventmanager.service.impl;

import com.myapp.eventmanager.domain.Event;
import com.myapp.eventmanager.domain.enumeration.EventStatus;
import com.myapp.eventmanager.domain.enumeration.InvitationStatus;
import com.myapp.eventmanager.service.InvitationService;
import com.myapp.eventmanager.domain.Invitation;
import com.myapp.eventmanager.repository.InvitationRepository;
import com.myapp.eventmanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Invitation}.
 */
@Service
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private final Logger log = LoggerFactory.getLogger(InvitationServiceImpl.class);

    private final InvitationRepository invitationRepository;

    @Autowired
    private EventServiceImpl eventService;

    public InvitationServiceImpl(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    /**
     * Save a invitation.
     *
     * @param invitation the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Invitation save(Invitation invitation) {
        log.debug("Request to save Invitation : {}", invitation);
        return invitationRepository.save(invitation);
    }

    @Override
    public Invitation update(Invitation invitation) {
        log.debug("Request to update Invitation : {}", invitation);
        Event event = null;
        if(invitation.getEvent() != null){
            Optional<Event> savedEvent = eventService.findOne(invitation.getEvent().getId());
            if(savedEvent.isPresent()){
                event = savedEvent.get();
                if(event.getEventStatus().equals(EventStatus.CANCELLED) || event.getEventStatus().equals(EventStatus.CLOSED)){
                    throw new BadRequestAlertException("Event already " + event.getEventStatus(), "Invalid Invitation", "Can not send invitation to non-active event. ");
                }
            }

        }
        checkStatusBeforeSave(invitation);
        return invitationRepository.save(invitation);
    }

    private void checkStatusBeforeSave(Invitation invitation) {
        if(invitation.getGuest() != null){
            if(invitation.getEvent() == null){
                throw new BadRequestAlertException("OH snap!, you forgot to add an event ", "Invalid Invitation", "Can not send invitation to empty. ");
            }
            if(invitation.getGuest().getId() != null){
                invitation.setInvitationStatus(InvitationStatus.SENT);
            }
        } else {
            invitation.setInvitationStatus(InvitationStatus.CREATED);
        }
    }


    /**
     * Get all the invitations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Invitation> findAll(Pageable pageable) {
        log.debug("Request to get all Invitations");
        return invitationRepository.findAll(pageable);
    }


    /**
     * Get one invitation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Invitation> findOne(Long id) {
        log.debug("Request to get Invitation : {}", id);
        return invitationRepository.findById(id);
    }

    /**
     * Delete the invitation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invitation : {}", id);
        invitationRepository.deleteById(id);
    }
}
