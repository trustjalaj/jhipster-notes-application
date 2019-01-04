package com.notes.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.notes.domain.Notes;
import com.notes.service.NotesService;
import com.notes.web.rest.errors.BadRequestAlertException;
import com.notes.web.rest.util.HeaderUtil;
import com.notes.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notes.
 */
@RestController
@RequestMapping("/api")
public class NotesResource {

    private static final String ENTITY_NAME = "notesApplicationNotes";
    private final Logger log = LoggerFactory.getLogger(NotesResource.class);
    private final NotesService notesService;

    public NotesResource(NotesService notesService) {
        this.notesService = notesService;
    }

    /**
     * POST  /notes : Create a new notes.
     *
     * @param notes the notes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notes, or with status 400 (Bad Request) if the notes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notes")
    @Timed
    public ResponseEntity<Notes> createNotes(@RequestBody Notes notes) throws URISyntaxException {
        log.debug("REST request to save Notes : {}", notes);
        if (notes.getId() != null) {
            throw new BadRequestAlertException("A new notes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notes result = notesService.save(notes);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * PUT  /notes : Updates an existing notes.
     *
     * @param notes the notes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notes,
     * or with status 400 (Bad Request) if the notes is not valid,
     * or with status 500 (Internal Server Error) if the notes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notes")
    @Timed
    public ResponseEntity<Notes> updateNotes(@RequestBody Notes notes) throws URISyntaxException {
        log.debug("REST request to update Notes : {}", notes);
        if (notes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Notes result = notesService.save(notes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notes.getId()))
            .body(result);
    }

    /**
     * GET  /notes : get all the notes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notes in body
     */
    @GetMapping("/notes")
    @Timed
    public ResponseEntity<List<Notes>> getAllNotes(Pageable pageable) {
        log.debug("REST request to get a page of Notes");
        Page<Notes> page = notesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /notes/:id : get the "id" notes.
     *
     * @param id the id of the notes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notes, or with status 404 (Not Found)
     */
    @GetMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Notes> getNotes(@PathVariable String id) {
        log.debug("REST request to get Notes : {}", id);
        Optional<Notes> notes = notesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notes);
    }

    /**
     * DELETE  /notes/:id : delete the "id" notes.
     *
     * @param id the id of the notes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotes(@PathVariable String id) {
        log.debug("REST request to delete Notes : {}", id);
        notesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
