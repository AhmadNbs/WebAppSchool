package com.example.Testing.Service;

import com.example.Testing.DTOS.Builders.NoteBuilder;
import com.example.Testing.DTOS.NoteDTO;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.Note;
import com.example.Testing.Entity.User;
import com.example.Testing.Repository.MateriaRepository;
import com.example.Testing.Repository.NoteRepository;
import com.example.Testing.Repository.UserRepository;
import com.example.Testing.validators.NoteValidator;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.Testing.validators.NoteValidator.validateNoteId;

/**
 * Service class for managing Note entities.
 */
@Service
public class NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteService.class);

    private final NoteRepository noteRepo;
    private final UserRepository userRepo;
    private final MateriaRepository materiaRepo;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepo, MateriaRepository materiaRepo) {
        this.noteRepo = noteRepository;
        this.userRepo = userRepo;
        this.materiaRepo = materiaRepo;
    }

    /**
     * Retrieve all notes.
     *
     * @return List of NoteDTO objects representing notes.
     */
    public List<NoteDTO> findNotes() {
        List<Note> noteList = noteRepo.findAll();
        return noteList.stream()
                .map(NoteBuilder::toNoteDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a note by ID.
     *
     * @param id The ID of the note to retrieve.
     * @return NoteDTO object representing the note.
     * @throws ResourceNotFoundException if the note is not found.
     */
    public NoteDTO findNoteById(UUID id) {
        NoteValidator.validateNoteId(id,noteRepo);
        Optional<Note> noteOptional = noteRepo.findById(id);
        return NoteBuilder.toNoteDTO(noteOptional.get());
    }

    /**
     * Insert a new note.
     *
     * @param noteDTO The NoteDTO object representing the note to insert.
     * @return The ID of the inserted note.
     */
    public UUID insertNote(NoteDTO noteDTO) {
        Note note = NoteBuilder.toEntity(noteDTO);
        note = noteRepo.save(note);
        LOGGER.debug("Note with id {} was inserted in db", note.getId());
        return note.getId();
    }

    /**
     * Modify an existing note.
     *
     * @param noteDTO The NoteDTO object representing the updated note.
     * @param id      The ID of the note to modify.
     * @return The updated Note entity.
     * @throws ResourceNotFoundException if the note is not found.
     */
    public Note modifyNote(NoteDTO noteDTO, UUID id) {
        NoteValidator.validateNoteId(id,noteRepo);
        Optional<Note> noteOptional = noteRepo.findById(id);
        Note note = noteOptional.get();

        // Update note fields with data from noteDTO
        note.setNota(noteDTO.getNota());
        note.setSubjects(noteDTO.getSubjects());
        note.setUsers(noteDTO.getUsers());

        note = noteRepo.save(note);

        LOGGER.debug("Note with id {} has been modified in db", note.getId());
        return note;
    }

    /**
     * Delete a note by ID.
     *
     * @param id The ID of the note to delete.
     * @throws ResourceNotFoundException if the note is not found.
     */
    public void deleteNote(UUID id) {
        NoteValidator.validateNoteId(id,noteRepo);
        NoteDTO dto = findNoteById(id);
        Note mark = NoteBuilder.toEntity(dto);
        for(User user: mark.getUsers())
        {
            user.getMarks().remove(mark);
            userRepo.save(user);
        }
        for(Materia m : mark.getSubjects())
        {
            m.getMarks().remove(mark);
            materiaRepo.save(m);
        }
        noteRepo.deleteById(id);
    }
}
