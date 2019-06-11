package com.myapp.eventmanager.service.impl;

import com.myapp.eventmanager.domain.Guest;
import com.myapp.eventmanager.domain.Invitation;
import com.myapp.eventmanager.domain.enumeration.InvitationStatus;
import com.myapp.eventmanager.repository.GuestRepository;
import com.myapp.eventmanager.repository.InvitationRepository;
import com.myapp.eventmanager.service.GuestService;
import com.myapp.eventmanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Guest}.
 */
@Service
@Transactional
public class GuestServiceImpl implements GuestService {

    private final Logger log = LoggerFactory.getLogger(GuestServiceImpl.class);

    private final GuestRepository guestRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    /**
     * Save a guest.
     *
     * @param guest the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Guest save(Guest guest) {
        log.debug("Request to save Guest : {}", guest);
        return guestRepository.save(guest);
    }

    /**
     * Get all the guests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Guest> findAll(Pageable pageable) {
        log.debug("Request to get all Guests");
        return guestRepository.findAll(pageable);
    }


    /**
     * Get one guest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Guest> findOne(Long id) {
        log.debug("Request to get Guest : {}", id);
        return guestRepository.findById(id);
    }

    /**
     * Delete the guest by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Guest : {}", id);
        guestRepository.deleteById(id);
    }

    @Override
    public void acceptInvitation(Long id, Long invitationId) {
        Invitation invitation;
        Optional<Invitation> savedInvitation = invitationRepository.findById(invitationId);
        if(!savedInvitation.isPresent()){
            throw new BadRequestAlertException("Bad Request", "Invalid Invitation", "Invitation with "+invitationId+ " does not exists. ");
        } else {
            invitation = savedInvitation.get();
            if(invitation.getGuest().getId() != id){
                throw new BadRequestAlertException("Bad Request", "Can not accept not invited event", "Guest with id = "+id+ " is not invited to the requested event. ");
            }

        }
        invitation.setInvitationStatus(InvitationStatus.APPROVED);
        invitationRepository.save(invitation);

    }

    @Override
    public List<Guest> getAllGuestsForEvent(Long id) {
        List<Guest> guestList = new ArrayList<>();
        List<Invitation> invitationList = invitationRepository.findAllByEvent_IdAndInvitationStatus(id, InvitationStatus.APPROVED);
        invitationList.forEach(invitation -> {
            if(invitation.getGuest() != null){
                guestList.add(invitation.getGuest());
            }
        });
        return guestList;
    }
}
