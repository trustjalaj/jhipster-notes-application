package com.notes.web.rest;

import com.notes.NotesApplicationApp;
import com.notes.domain.Notes;
import com.notes.domain.enumeration.AssociatedResource;
import com.notes.repository.NotesRepository;
import com.notes.service.NotesService;
import com.notes.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.List;

import static com.notes.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NotesResource REST controller.
 *
 * @see NotesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotesApplicationApp.class)
public class NotesResourceIntTest {

    private static final String DEFAULT_ASSOCIATED_RESOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATED_RESOURCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final AssociatedResource DEFAULT_ASSOCIATED_RESOURCE = AssociatedResource.FLEET;
    private static final AssociatedResource UPDATED_ASSOCIATED_RESOURCE = AssociatedResource.POLICY;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private NotesService notesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restNotesMockMvc;

    private Notes notes;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notes createEntity() {
        Notes notes = new Notes()
            .associatedResourceId(DEFAULT_ASSOCIATED_RESOURCE_ID)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .associatedResource(DEFAULT_ASSOCIATED_RESOURCE);
        return notes;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotesResource notesResource = new NotesResource(notesService);
        this.restNotesMockMvc = MockMvcBuilders.standaloneSetup(notesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @Before
    public void initTest() {
        notesRepository.deleteAll();
        notes = createEntity();
    }

    @Test
    public void createNotes() throws Exception {
        int databaseSizeBeforeCreate = notesRepository.findAll().size();

        // Create the Notes
        restNotesMockMvc.perform(post("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notes)))
            .andExpect(status().isCreated());

        // Validate the Notes in the database
        List<Notes> notesList = notesRepository.findAll();
        assertThat(notesList).hasSize(databaseSizeBeforeCreate + 1);
        Notes testNotes = notesList.get(notesList.size() - 1);
        assertThat(testNotes.getAssociatedResourceId()).isEqualTo(DEFAULT_ASSOCIATED_RESOURCE_ID);
        assertThat(testNotes.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotes.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testNotes.getAssociatedResource()).isEqualTo(DEFAULT_ASSOCIATED_RESOURCE);
    }

    @Test
    public void createNotesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notesRepository.findAll().size();

        // Create the Notes with an existing ID
        notes.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotesMockMvc.perform(post("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notes)))
            .andExpect(status().isBadRequest());

        // Validate the Notes in the database
        List<Notes> notesList = notesRepository.findAll();
        assertThat(notesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllNotes() throws Exception {
        // Initialize the database
        notesRepository.save(notes);

        // Get all the notesList
        restNotesMockMvc.perform(get("/api/notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notes.getId())))
            .andExpect(jsonPath("$.[*].associatedResourceId").value(hasItem(DEFAULT_ASSOCIATED_RESOURCE_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].associatedResource").value(hasItem(DEFAULT_ASSOCIATED_RESOURCE.toString())));
    }

    @Test
    public void getNotes() throws Exception {
        // Initialize the database
        notesRepository.save(notes);

        // Get the notes
        restNotesMockMvc.perform(get("/api/notes/{id}", notes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notes.getId()))
            .andExpect(jsonPath("$.associatedResourceId").value(DEFAULT_ASSOCIATED_RESOURCE_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.associatedResource").value(DEFAULT_ASSOCIATED_RESOURCE.toString()));
    }

    @Test
    public void getNonExistingNotes() throws Exception {
        // Get the notes
        restNotesMockMvc.perform(get("/api/notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateNotes() throws Exception {
        // Initialize the database
        notesService.save(notes);

        int databaseSizeBeforeUpdate = notesRepository.findAll().size();

        // Update the notes
        Notes updatedNotes = notesRepository.findById(notes.getId()).get();
        updatedNotes
            .associatedResourceId(UPDATED_ASSOCIATED_RESOURCE_ID)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .associatedResource(UPDATED_ASSOCIATED_RESOURCE);

        restNotesMockMvc.perform(put("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotes)))
            .andExpect(status().isOk());

        // Validate the Notes in the database
        List<Notes> notesList = notesRepository.findAll();
        assertThat(notesList).hasSize(databaseSizeBeforeUpdate);
        Notes testNotes = notesList.get(notesList.size() - 1);
        assertThat(testNotes.getAssociatedResourceId()).isEqualTo(UPDATED_ASSOCIATED_RESOURCE_ID);
        assertThat(testNotes.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotes.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotes.getAssociatedResource()).isEqualTo(UPDATED_ASSOCIATED_RESOURCE);
    }

    @Test
    public void updateNonExistingNotes() throws Exception {
        int databaseSizeBeforeUpdate = notesRepository.findAll().size();

        // Create the Notes

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotesMockMvc.perform(put("/api/notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notes)))
            .andExpect(status().isBadRequest());

        // Validate the Notes in the database
        List<Notes> notesList = notesRepository.findAll();
        assertThat(notesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteNotes() throws Exception {
        // Initialize the database
        notesService.save(notes);

        int databaseSizeBeforeDelete = notesRepository.findAll().size();

        // Get the notes
        restNotesMockMvc.perform(delete("/api/notes/{id}", notes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Notes> notesList = notesRepository.findAll();
        assertThat(notesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notes.class);
        Notes notes1 = new Notes();
        notes1.setId("id1");
        Notes notes2 = new Notes();
        notes2.setId(notes1.getId());
        assertThat(notes1).isEqualTo(notes2);
        notes2.setId("id2");
        assertThat(notes1).isNotEqualTo(notes2);
        notes1.setId(null);
        assertThat(notes1).isNotEqualTo(notes2);
    }
}
