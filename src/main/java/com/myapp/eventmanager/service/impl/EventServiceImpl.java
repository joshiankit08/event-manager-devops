package com.myapp.eventmanager.service.impl;

import com.myapp.eventmanager.domain.enumeration.EventStatus;
import com.myapp.eventmanager.service.EventService;
import com.myapp.eventmanager.domain.Event;
import com.myapp.eventmanager.repository.EventRepository;
import com.myapp.eventmanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Save a event.
     *
     * @param event the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        log.debug("Request to update Event : {}", event);
        Event savedEvent = findOne(event.getId()).get();
        EventStatus currentStatus = savedEvent.getEventStatus();
        EventStatus newStatus = event.getEventStatus();
        if(currentStatus != newStatus){
            if(currentStatus.equals(EventStatus.LIVE)){
                if(!newStatus.equals(EventStatus.CLOSED))
                throw new BadRequestAlertException("Live event can only be closed", ENTITY_NAME, "Live event can only be closed. ");
            }
        }
        if(newStatus.equals(EventStatus.LIVE)){
            Instant now = Instant.now();
            if(!event.getStartDate().isBefore(now)){
                throw new BadRequestAlertException("Don't be TOO DESPERATE. Can not go live in future :)", ENTITY_NAME, "Event can not go live before time. ");
            }
            if(!event.getEndDate().isAfter(now)){
                throw new BadRequestAlertException("Can not go back in TIME !!!", ENTITY_NAME, "Event can not go live before time. ");
            }
        }
        return eventRepository.save(event);
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Event> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        return eventRepository.findAll(pageable);
    }


    /**
     * Get one event by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        if(!isActive(id)){
            eventRepository.deleteById(id);
        } else {
            throw new BadRequestAlertException("Live Event can not be deleted. ", ENTITY_NAME, "Can not cancel an active event.");
        }

    }

    private boolean isActive(Long id) {
        Event event = findOne(id).get();
        if(event.getEventStatus().equals(EventStatus.LIVE)){
            return true;
        }
        return false;
    }


}
