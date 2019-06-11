package com.myapp.eventmanager.service;

import com.myapp.eventmanager.domain.Guest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Guest}.
 */
public interface GuestService {

    /**
     * Save a guest.
     *
     * @param guest the entity to save.
     * @return the persisted entity.
     */
    Guest save(Guest guest);

    /**
     * Get all the guests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Guest> findAll(Pageable pageable);


    /**
     * Get the "id" guest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Guest> findOne(Long id);

    /**
     * Delete the "id" guest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void acceptInvitation(Long id, Long invitationId);

    List<Guest> getAllGuestsForEvent(Long id);
}
