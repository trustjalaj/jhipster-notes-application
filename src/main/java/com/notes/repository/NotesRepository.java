package com.notes.repository;

import com.notes.domain.Notes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Notes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotesRepository extends MongoRepository<Notes, String> {

}
