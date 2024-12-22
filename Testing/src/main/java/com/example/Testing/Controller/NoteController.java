package com.example.Testing.Controller;

import com.example.Testing.DTOS.Builders.NoteBuilder;
import com.example.Testing.DTOS.ClasaDTO;
import com.example.Testing.DTOS.MateriaDTO;
import com.example.Testing.DTOS.NoteDTO;
import com.example.Testing.Entity.Clasa;
import com.example.Testing.Entity.Note;
import com.example.Testing.Service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for handling HTTP requests related to marks (notes).
 */
@Controller
@CrossOrigin
@RequestMapping("/marks")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Get a welcome message for the marks controller.
     *
     * @return A welcome message.
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot Application";
    }

    /**
     * Get all marks.
     *
     * @return ResponseEntity containing a list of NoteDTOs.
     */
    @GetMapping("/getAllMarks")
    public ModelAndView getMarks() {
        List<NoteDTO> DTOS = noteService.findNotes();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("marks", DTOS);
        modelAndView.setViewName("note");
        return modelAndView;
    }

    /**
     * Get a specific mark by ID.
     *
     * @param noteId The ID of the mark to retrieve.
     * @return ResponseEntity containing the NoteDTO object.
     */
    @GetMapping("/get-mark/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable("id") UUID noteId) {
        NoteDTO dto = noteService.findNoteById(noteId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Insert a new mark.
     *
     * @param notaDTO The NoteDTO object representing the new mark.
     * @return ResponseEntity containing the ID of the inserted mark.
     */
    @PostMapping("/save-mark")
    public ModelAndView insertMark(NoteDTO notaDTO) {
        UUID notaID = noteService.insertNote(notaDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("notaID", notaID);
        modelAndView.setViewName("redirect:/marks/getAllMarks");
        return modelAndView;
    }

    /**
     * Update an existing mark.
     *
     * @param notaDTO The NoteDTO object representing the updated mark.
     * @return ResponseEntity containing the ID of the updated mark.
     */
    @PostMapping("/update-mark/{id}")
    public ModelAndView updateMark(NoteDTO notaDTO, @PathVariable("id") UUID notaId) {
        Note updatedNota = noteService.modifyNote(notaDTO, notaId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("notaId", notaId);
        modelAndView.setViewName("redirect:/marks/getAllMarks");
        return modelAndView;
    }

    /**
     * Delete a mark by ID.
     *
     * @param markId The ID of the mark to delete.
     * @return ResponseEntity with a deletion message.
     */
    @PostMapping("/delete-mark/{id}")
    public ModelAndView deleteMark(@PathVariable("id") UUID markId) {
        noteService.deleteNote(markId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("markId", markId);
        modelAndView.setViewName("redirect:/marks/getAllMarks");
        return modelAndView;
    }
}
