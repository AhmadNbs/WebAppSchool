package com.example.Testing.Controller;

import com.example.Testing.DTOS.Builders.ScheduleBuilder;
import com.example.Testing.DTOS.ClasaDTO;
import com.example.Testing.DTOS.MateriaDTO;
import com.example.Testing.DTOS.ScheduleDTO;
import com.example.Testing.DTOS.UserDtos;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.Schedule;
import com.example.Testing.Service.ClasaService;
import com.example.Testing.Service.MateriaService;
import com.example.Testing.Service.ScheduleService;
import com.example.Testing.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for handling HTTP requests related to schedules.
 */
@Controller
@CrossOrigin
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final MateriaService materiaService;
    private final ClasaService clasaService;
    private final UserService userService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, MateriaService materiaService, ClasaService clasaService, UserService userService) {
        this.scheduleService = scheduleService;
        this.materiaService = materiaService;
        this.clasaService = clasaService;
        this.userService = userService;
    }

    /**
     * Get all schedules.
     *
     * @return ModelAndView containing a list of ScheduleDTOs.
     */
    @GetMapping("/getAllSchedules")
    public ModelAndView getSchedules() {
        List<ScheduleDTO> DTOS = scheduleService.findSchedules();
        List<MateriaDTO> materiaDTO = materiaService.findSubjects();
        List<ClasaDTO> clasaDTO = clasaService.findClase();
        List<UserDtos> professors =  userService.findProfessors();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("schedules", DTOS);
        modelAndView.addObject("subjects", materiaDTO);
        modelAndView.addObject("classes",clasaDTO);
        modelAndView.addObject("professors",professors);
        modelAndView.setViewName("schedules");
        return modelAndView;
    }

    @PostMapping("/addSubject/{idSchedule}/{idSubject}")
    public ModelAndView addSubject(@PathVariable("idSchedule") UUID scheduleId, @PathVariable("idSubject") UUID subjectId)
    {
        ModelAndView modelAndView = new ModelAndView();
        scheduleService.addSubject(scheduleId,subjectId);
        modelAndView.setViewName("redirect:/schedules/getAllSchedules");

        return modelAndView;
    }

    @PostMapping("/addClass/{idSchedule}/{idClass}")
    public ModelAndView addClass(@PathVariable("idSchedule") UUID scheduleId, @PathVariable("idClass") UUID classId)
    {
        ModelAndView modelAndView = new ModelAndView();
        scheduleService.addClass(scheduleId,classId);
        modelAndView.setViewName("redirect:/schedules/getAllSchedules");

        return modelAndView;
    }

    /**
     * Get a specific schedule by ID.
     *
     * @param scheduleId The ID of the schedule to retrieve.
     * @return ResponseEntity containing the ScheduleDTO object.
     */
    @GetMapping("/get-schedule/{id}")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable("id") UUID scheduleId) {
        ScheduleDTO dto = scheduleService.findScheduleById(scheduleId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Insert a new schedule.
     *
     * @param scheduleDTO The ScheduleDTO object representing the new schedule.
     * @return ModelAndView containing the ID of the inserted schedule.
     */
    @PostMapping(path = "/save-schedule")
    public ModelAndView insertSchedule(ScheduleDTO scheduleDTO) {
        UUID scheduleID = scheduleService.insertSchedule(scheduleDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("scheduleID", scheduleID);
        modelAndView.setViewName("redirect:/schedules/getAllSchedules");
        return modelAndView;
    }

    /**
     * Update an existing schedule.
     *
     * @param scheduleDTO The ScheduleDTO object representing the updated schedule.
     * @param scheduleId  The ID of the schedule to update.
     * @return ModelAndView containing the ID of the updated schedule.
     */
    @PostMapping("/update-schedule/{id}")
    public ModelAndView updateSchedule(ScheduleDTO scheduleDTO, @PathVariable("id") UUID scheduleId) {
        Schedule updatedSchedule = scheduleService.modifySchedule(scheduleDTO, scheduleId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("scheduleID", scheduleId);
        modelAndView.setViewName("redirect:/schedules/getAllSchedules");
        return modelAndView;
    }

    /**
     * Delete a schedule by ID.
     *
     * @param scheduleId The ID of the schedule to delete.
     * @return ModelAndView with a deletion message.
     */
    @PostMapping("/delete-schedule/{id}")
    public ModelAndView deleteSchedule(@PathVariable("id") UUID scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("scheduleId", scheduleId);
        modelAndView.setViewName("redirect:/schedules/getAllSchedules");
        return modelAndView;
    }

    /**
     * Add a professor to a schedule.
     *
     * @param professorId The ID of the professor to add.
     * @param scheduleId  The ID of the schedule to which the professor will be added.
     * @return ModelAndView redirecting to the getAllSchedules view.
     */

    @PostMapping("/addProfessor/{idSchedule}/{idUser}")
    public ModelAndView addProfessor(@PathVariable("idUser") UUID professorId, @PathVariable("idSchedule") UUID scheduleId)
    {
        ModelAndView modelAndView = new ModelAndView();
        scheduleService.addProfessor(professorId,scheduleId);
        modelAndView.setViewName("redirect:/schedules/getAllSchedules");

        return modelAndView;
    }
}
