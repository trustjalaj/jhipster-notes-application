package com.notes.service;

import com.notes.domain.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Notes.
 */
public interface NotesService {

    /**
     * Save a notes.
     *
     * @param notes the entity to save
     * @return the persisted entity
     */
    Notes save(Notes notes);

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Notes> findAll(Pageable pageable);


    /**
     * Get the "id" notes.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Notes> findOne(String id);

    /**
     * Delete the "id" notes.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}
