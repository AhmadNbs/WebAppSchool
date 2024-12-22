package com.example.Testing.Controller;

import com.example.Testing.DTOS.*;
import com.example.Testing.DTOS.Builders.ClasaBuilder;
import com.example.Testing.DTOS.Builders.UserBuilder;
import com.example.Testing.Entity.*;
import com.example.Testing.Service.ClasaService;
import com.example.Testing.Service.MateriaService;
import com.example.Testing.Service.NoteService;
import com.example.Testing.Service.UserService;
import com.example.Testing.config.RabbitSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller class for handling HTTP requests related to classes (Clasa).
 */
@RestController
@CrossOrigin
@RequestMapping("/classes")
public class ClasaController {

    private final ClasaService clasaService;
    private final UserService userService;

    private final MateriaService materiaService;
    private final NoteService notaService;

    private final RabbitSender rabbitSender;

    @Autowired
    public ClasaController(ClasaService clasaService, UserService userService, MateriaService materiaService, NoteService notaService, RabbitSender rabbitSender) {
        this.clasaService = clasaService;
        this.userService = userService;
        this.materiaService = materiaService;
        this.notaService = notaService;
        this.rabbitSender = rabbitSender;
    }

    /**
     * Welcome message for the class controller.
     *
     * @return A welcome message.
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot Application";
    }

    /**
     * Get all classes.
     *
     * @return ResponseEntity containing a list of ClasaDTO objects.
     */
    @GetMapping("/getAllClasses")
    public ModelAndView getClasses() {
        List<ClasaDTO> DTOS = clasaService.findClase();
        List<UserDtos> userDTO= userService.findUsers();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("classes", DTOS);
        modelAndView.addObject("students",userDTO);
        modelAndView.addObject("professors",userDTO);
        modelAndView.setViewName("classes");
        return modelAndView;
    }

    /**
     * Get a list of classes without additional details.
     *
     * @return ModelAndView containing a list of ClasaDTO objects.
     */

    @GetMapping("/getClasses")
    public ModelAndView getClasele() {
        List<ClasaDTO> DTOS = clasaService.findClase();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("classes", DTOS);
        modelAndView.setViewName("class");
        return modelAndView;
    }

    /**
     * Add a student to a class.
     *
     * @param classId   The ID of the class.
     * @param studentId The ID of the student to add.
     * @return ModelAndView redirecting to the list of all classes.
     */

    @PostMapping("/addStudent/{idClass}/{idStudent}")
    public ModelAndView addStudent(@PathVariable("idClass") UUID classId, @PathVariable("idStudent") UUID studentId)
    {
        ModelAndView modelAndView = new ModelAndView();
        clasaService.addStudent(classId,studentId);
        modelAndView.setViewName("redirect:/classes/getAllClasses");

        return modelAndView;
    }

    /**
     * Add a professor to a class.
     *
     * @param classId     The ID of the class.
     * @param professorId The ID of the professor to add.
     * @return ModelAndView redirecting to the list of all classes.
     */

    @PostMapping("/addProfessor/{idClass}/{idProfessor}")
    public ModelAndView addProfessor(@PathVariable("idClass") UUID classId, @PathVariable("idProfessor") UUID professorId)
    {
        ModelAndView modelAndView = new ModelAndView();
        clasaService.addStudent(classId,professorId);
        modelAndView.setViewName("redirect:/classes/getAllClasses");

        return modelAndView;
    }
    /**
     * Get a specific class by ID.
     *
     * @param classId The ID of the class to retrieve.
     * @return ResponseEntity containing the ClasaDTO object.
     */
    @GetMapping("/get-class/{classId}/{subjectId}")
    public ModelAndView getClass(@PathVariable("classId") UUID classId,@PathVariable("subjectId") UUID subjectId) {
        ClasaDTO dto = clasaService.findClasaById(classId);
        List<User> users= dto.getUsers();
        MateriaDTO subject = materiaService.findSubjectById(subjectId);
        List<NoteDTO> marks =  notaService.findNotes();
        marks.sort(Comparator.comparingDouble(NoteDTO::getNota));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("class", dto);
        modelAndView.addObject("students", users);
        modelAndView.addObject("subject", subject);
        modelAndView.addObject("marks", marks);
        modelAndView.setViewName("class-students");

        return modelAndView;
    }

    /**
     * Get a specific class by ID, along with its subjects.
     *
     * @param classId The ID of the class to retrieve.
     * @return ModelAndView containing the class details and subjects.
     */

    @GetMapping("/get-class/{id}")
    public ModelAndView getClass(@PathVariable("id") UUID classId) {
        ClasaDTO dto = clasaService.findClasaById(classId);
        List<MateriaDTO> subjects = materiaService.findSubjects();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("class", dto);
        modelAndView.addObject("subjects", subjects);
        modelAndView.setViewName("class-materials");

        return modelAndView;
    }

    /**
     * Insert a new class.
     *
     * @param clasaDTO The ClasaDTO object representing the new class.
     * @return ResponseEntity containing the ID of the inserted class.
     */
    @PostMapping("/save-class")
    public ModelAndView insertClasa(ClasaDTO clasaDTO) {
        UUID clasaID = clasaService.insertClasa(clasaDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("clasaID", clasaID);
        modelAndView.setViewName("redirect:/classes/getAllClasses");
        return modelAndView;
    }

    /**
     * Update an existing class.
     *
     * @param clasaDTO The ClasaDTO object representing the updated class.
     * @param clasaId  The ID of the class to update.
     * @return ModelAndView redirecting to the list of all classes.
     */
    @PostMapping("/update-class/{id}")
    public ModelAndView updateClasa(ClasaDTO clasaDTO, @PathVariable("id") UUID clasaId) {
        Clasa updatedClasa = clasaService.modifyClasa(clasaDTO, clasaId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("clasaId", clasaId);
        modelAndView.setViewName("redirect:/classes/getAllClasses");
        return modelAndView;
    }

    /**
     * Delete a class by ID.
     *
     * @param clasaId The ID of the class to delete.
     * @return ResponseEntity with a deletion message.
     */
    @PostMapping("/delete-class/{id}")
    public ModelAndView deleteClass(@PathVariable("id") UUID clasaId) {
        clasaService.deleteClasa(clasaId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("clasaId", clasaId);
        modelAndView.setViewName("redirect:/classes/getAllClasses");
        return modelAndView;
    }

    /**
     * Submit marks for a student in a specific subject.
     *
     * @param studentId The ID of the student.
     * @param markId    The ID of the mark.
     * @param subjectId The ID of the subject.
     * @param classId   The ID of the class.
     * @return ModelAndView redirecting to the class details with the subject.
     */

    @PostMapping("/submitMarks/{studentId}/{subjectId}/{markId}/{classId}")
    public ModelAndView submitMarks(@PathVariable("studentId") UUID studentId, @PathVariable("markId") UUID markId, @PathVariable("subjectId") UUID subjectId,
                                    @PathVariable("classId") UUID classId) {
        userService.addMarks(studentId,markId,subjectId);
        UserDtos userDTO = userService.findUserById(studentId);
        MateriaDTO subjectDTO = materiaService.findSubjectById(subjectId);
        NotificationRequestDTO notificationDTO = new NotificationRequestDTO();
        notificationDTO.setEmail(userDTO.getEmail());
        notificationDTO.setSubject("Mark Recorded");
        notificationDTO.setName(userDTO.getFirstName());
        notificationDTO.setBody("Dear "+ notificationDTO.getName() + " your exam mark for " + subjectDTO.getName() + " has been recorded");
        //send email
        rabbitSender.send(notificationDTO);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/classes/get-class/" + classId + "/" + subjectId);
        return modelAndView;
    }
}
