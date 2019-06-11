package com.myapp.eventmanager.web.rest;

import com.myapp.eventmanager.EventmanagerApp;
import com.myapp.eventmanager.domain.Guest;
import com.myapp.eventmanager.repository.GuestRepository;
import com.myapp.eventmanager.service.GuestService;
import com.myapp.eventmanager.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.myapp.eventmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link GuestResource} REST controller.
 */
@SpringBootTest(classes = EventmanagerApp.class)
public class GuestResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private GuestService guestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restGuestMockMvc;

    private Guest guest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuestResource guestResource = new GuestResource(guestService);
        this.restGuestMockMvc = MockMvcBuilders.standaloneSetup(guestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guest createEntity(EntityManager em) {
        Guest guest = new Guest()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL);
        return guest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guest createUpdatedEntity(EntityManager em) {
        Guest guest = new Guest()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);
        return guest;
    }

    @BeforeEach
    public void initTest() {
        guest = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuest() throws Exception {
        int databaseSizeBeforeCreate = guestRepository.findAll().size();

        // Create the Guest
        restGuestMockMvc.perform(post("/api/guests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guest)))
            .andExpect(status().isCreated());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeCreate + 1);
        Guest testGuest = guestList.get(guestList.size() - 1);
        assertThat(testGuest.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testGuest.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testGuest.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createGuestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guestRepository.findAll().size();

        // Create the Guest with an existing ID
        guest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuestMockMvc.perform(post("/api/guests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guest)))
            .andExpect(status().isBadRequest());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGuests() throws Exception {
        // Initialize the database
        guestRepository.saveAndFlush(guest);

        // Get all the guestList
        restGuestMockMvc.perform(get("/api/guests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guest.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getGuest() throws Exception {
        // Initialize the database
        guestRepository.saveAndFlush(guest);

        // Get the guest
        restGuestMockMvc.perform(get("/api/guests/{id}", guest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guest.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGuest() throws Exception {
        // Get the guest
        restGuestMockMvc.perform(get("/api/guests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuest() throws Exception {
        // Initialize the database
        guestService.save(guest);

        int databaseSizeBeforeUpdate = guestRepository.findAll().size();

        // Update the guest
        Guest updatedGuest = guestRepository.findById(guest.getId()).get();
        // Disconnect from session so that the updates on updatedGuest are not directly saved in db
        em.detach(updatedGuest);
        updatedGuest
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);

        restGuestMockMvc.perform(put("/api/guests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuest)))
            .andExpect(status().isOk());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeUpdate);
        Guest testGuest = guestList.get(guestList.size() - 1);
        assertThat(testGuest.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testGuest.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testGuest.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingGuest() throws Exception {
        int databaseSizeBeforeUpdate = guestRepository.findAll().size();

        // Create the Guest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuestMockMvc.perform(put("/api/guests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guest)))
            .andExpect(status().isBadRequest());

        // Validate the Guest in the database
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuest() throws Exception {
        // Initialize the database
        guestService.save(guest);

        int databaseSizeBeforeDelete = guestRepository.findAll().size();

        // Delete the guest
        restGuestMockMvc.perform(delete("/api/guests/{id}", guest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Guest> guestList = guestRepository.findAll();
        assertThat(guestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Guest.class);
        Guest guest1 = new Guest();
        guest1.setId(1L);
        Guest guest2 = new Guest();
        guest2.setId(guest1.getId());
        assertThat(guest1).isEqualTo(guest2);
        guest2.setId(2L);
        assertThat(guest1).isNotEqualTo(guest2);
        guest1.setId(null);
        assertThat(guest1).isNotEqualTo(guest2);
    }
}
