package com.myapp.eventmanager.repository;

import com.myapp.eventmanager.domain.Invitation;
import com.myapp.eventmanager.domain.enumeration.InvitationStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Invitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findAllByEvent_IdAndInvitationStatus(Long id, InvitationStatus approved);
}
