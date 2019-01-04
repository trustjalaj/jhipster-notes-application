package com.notes.service.impl;

import com.notes.domain.Notes;
import com.notes.repository.NotesRepository;
import com.notes.service.NotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing Notes.
 */
@Service
public class NotesServiceImpl implements NotesService {

    private final Logger log = LoggerFactory.getLogger(NotesServiceImpl.class);

    private final NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    /**
     * Save a notes.
     *
     * @param notes the entity to save
     * @return the persisted entity
     */
    @Override
    public Notes save(Notes notes) {
        log.debug("Request to save Notes : {}", notes);
        return notesRepository.save(notes);
    }

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Notes> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return notesRepository.findAll(pageable);
    }


    /**
     * Get one notes by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Notes> findOne(String id) {
        log.debug("Request to get Notes : {}", id);
        return notesRepository.findById(id);
    }

    /**
     * Delete the notes by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Notes : {}", id);
        notesRepository.deleteById(id);
    }
}
