package com.example.Testing.Controller;

import com.example.Testing.DTOS.Builders.MateriaBuilder;
import com.example.Testing.DTOS.MateriaDTO;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for handling HTTP requests related to subjects (materias).
 */
@Controller
@CrossOrigin
@RequestMapping("/subjects")
public class MateriaController {

    private final MateriaService materiaService;

    @Autowired
    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    /**
     * Get a welcome message for the subjects controller.
     *
     * @return A welcome message.
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot Application";
    }

    /**
     * Get all subjects.
     *
     * @return ModelAndView containing a list of MateriaDTOs.
     */
    @GetMapping("/getAllSubjects")
    public ModelAndView getMaterias() {
        List<MateriaDTO> DTOS = materiaService.findSubjects();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("subjects", DTOS);
        modelAndView.setViewName("subjects");
        return modelAndView;
    }

    /**
     * Get a specific subject by ID.
     *
     * @param materiaId The ID of the subject to retrieve.
     * @return ResponseEntity containing the MateriaDTO object.
     */
    @GetMapping("/get-subject/{id}")
    public ResponseEntity<MateriaDTO> getSubject(@PathVariable("id") UUID materiaId) {
        MateriaDTO dto = materiaService.findSubjectById(materiaId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Insert a new subject.
     *
     * @param materiaDTO The MateriaDTO object representing the new subject.
     * @return ModelAndView containing the ID of the inserted subject.
     */
    @PostMapping("/save-subject")
    public ModelAndView insertSubject(MateriaDTO materiaDTO) {
        UUID materiaID = materiaService.insertSubject(materiaDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("materiaID", materiaID);
        modelAndView.setViewName("redirect:/subjects/getAllSubjects");
        return modelAndView;
    }

    /**
     * Update an existing subject.
     *
     * @param materiaDTO The MateriaDTO object representing the updated subject.
     * @return ModelAndView containing the ID of the updated subject.
     */
    @PostMapping("/update-subject/{id}")
    public ModelAndView updateSubject(MateriaDTO materiaDTO, @PathVariable("id") UUID materiaId) {
        Materia updatedMateria = materiaService.modifySubject(materiaDTO, materiaId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("materiaId", materiaId);
        modelAndView.setViewName("redirect:/subjects/getAllSubjects");
        return modelAndView;
    }

    /**
     * Delete a subject by ID.
     *
     * @param subjectId The ID of the subject to delete.
     * @return ModelAndView with a deletion message.
     */
    @PostMapping("/delete-subject/{id}")
    public ModelAndView deleteSubject(@PathVariable("id") UUID subjectId) {
        materiaService.deleteSubject(subjectId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("subjectId", subjectId);
        modelAndView.setViewName("redirect:/subjects/getAllSubjects");
        return modelAndView;
    }
}
