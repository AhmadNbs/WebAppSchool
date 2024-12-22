package com.example.Testing.Controller;

import com.example.Testing.DTOS.*;
import com.example.Testing.DTOS.Builders.UserBuilder;
import com.example.Testing.Entity.*;
import com.example.Testing.Service.MateriaService;
import com.example.Testing.Service.ScheduleService;
import com.example.Testing.Service.UserService;
import com.example.Testing.config.RabbitSender;
import com.example.Testing.config.RestTemplateConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * Controller class for handling HTTP requests related to users.
 */
@Controller
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final MateriaService materiaService;
    private final ScheduleService scheduleService;
    private final RestTemplateConfig restTemplateConfig;
    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, MateriaService materiaService, ScheduleService scheduleService, RestTemplateConfig restTemplateConfig, RestTemplate restTemplate) {
        this.userService = userService;
        this.materiaService = materiaService;
        this.scheduleService = scheduleService;
        this.restTemplateConfig = restTemplateConfig;
        this.restTemplate = restTemplate;
    }

    /**
     * Welcome message for the user controller.
     *
     * @return A welcome message.
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to Spring Boot Application";
    }

    /**
     * Get all users.
     *
     * @return ModelAndView containing a list of UserDtos.
     */
    @GetMapping("/getAllUsers")
    public ModelAndView getUsers() {
        List<UserDtos> DTOS = userService.findUsers();
        List<ScheduleDTO> schedulesDTO = scheduleService.findSchedules();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", DTOS);
        modelAndView.addObject("schedules", schedulesDTO);
        modelAndView.setViewName("users"); // Assuming "user-list" is your view name
        return modelAndView;
    }

    /**
     * Insert a new user.
     *
     * @param userDTO The UserDtos object representing the new user.
     * @return ResponseEntity containing the ID of the inserted user.
     */
    @PostMapping(path = "/save-user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ModelAndView insertUser(UserDtos userDTO) {
        UUID userID = userService.insertUser(userDTO);
        // Create NotificationRequest object
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setEmail(userDTO.getEmail());
        notificationRequest.setSubject("Account Created");
        notificationRequest.setName(userDTO.getFirstName());
        notificationRequest.setBody("Dear "+ notificationRequest.getName() + " your account with the password " + userDTO.getPassword() + " and ID: " + userID + " has been created.");
        //send email
        restTemplateConfig.sendEmail(notificationRequest, restTemplate);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userID", userID);
        modelAndView.setViewName("redirect:/users/getAllUsers"); // Assuming "user-created" is your view name
        return modelAndView;
    }
    /**
     * Update an existing user.
     *
     * @param userDTO The UserDtos object representing the updated user.
     * @param userId  The ID of the user to be updated.
     * @return ModelAndView redirecting to the user list view.
     */
    @PostMapping("/update-user/{id}")
    public ModelAndView updateUser(UserDtos userDTO, @PathVariable("id") UUID userId) {
        User updatedUser = userService.modifyUser(userDTO, userId); // Call service method to modify user
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userID", userId);
        modelAndView.setViewName("redirect:/users/getAllUsers"); // Assuming "user-created" is your view name
        return modelAndView;
    }

    /**
     * Delete a user by ID.
     *
     * @param userId The ID of the user to delete.
     * @return ResponseEntity with a deletion message.
     */
    @PostMapping("/delete-user/{id}")
    public ModelAndView deleteUser(@PathVariable("id") UUID userId) {
        // Delete the user entity
        userService.deleteUser(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userID", userId);
        modelAndView.setViewName("redirect:/users/getAllUsers"); // Assuming "user-created" is your view name
        return modelAndView;
    }

    /**
     * Show admin login page.
     *
     * @return ModelAndView with the admin login view.
     */
    @GetMapping("/admin-login")
    public ModelAndView showAdminLoginForm() {
        ModelAndView adminView = new ModelAndView();
        adminView.setViewName("admin-login");
        return adminView; // Renders admin-login.html template
    }

    /**
     * Show professor login page.
     *
     * @return ModelAndView with the professor login view.
     */
    @GetMapping("/professor-login")
    public ModelAndView showProfessorLoginForm() {
        ModelAndView professorView = new ModelAndView();
        professorView.setViewName("professor-login");
        return professorView; // Renders professor-login.html template
    }

    /**
     * Show student login page.
     *
     * @return ModelAndView with the student login view.
     */
    @GetMapping("/student-login")
    public ModelAndView showStudentLoginForm() {
        ModelAndView studentView = new ModelAndView();
        studentView.setViewName("student-login");
        return studentView; // Renders professor-login.html template
    }

    /**
     * Handle admin login.
     *
     * @param email       The email of the admin.
     * @param password    The password of the admin.
     * @param request     The HTTP request.
     * @param attributes  Redirect attributes to store messages.
     * @return ModelAndView redirecting to the appropriate view based on the admin's role.
     */

    @PostMapping("/admin-login")
    public ModelAndView adminLogin(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   HttpServletRequest request,
                                   RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();

        // Authenticate the user
        User user = userService.checkUser(email, password);
        if (user != null) {
            String role = user.getRole();

            // Create a new session or retrieve the existing one
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("username", user.getFirstName()); // Assuming User has getFirstName() method
            session.setAttribute("role", role);

            // Redirect based on user role
            if ("admin".equals(role)) {
                modelAndView.setViewName("redirect:/users/admin");
            } else if ("professor".equals(role)) {
                modelAndView.setViewName("redirect:/users/professor-login");
            } else if ("student".equals(role)) {
                modelAndView.setViewName("redirect:/users/student-login");
            } else {
                // Handle unexpected roles
                attributes.addFlashAttribute("error", "Unauthorized role");
                modelAndView.setViewName("redirect:/users/admin-login");
            }
        } else {
            // Handle invalid login
            attributes.addFlashAttribute("error", "Invalid email or password");
            modelAndView.setViewName("redirect:/users/admin-login");
        }

        return modelAndView;
    }

    /**
     * Handle student login.
     *
     * @param email       The email of the student.
     * @param password    The password of the student.
     * @param request     The HTTP request.
     * @param attributes  Redirect attributes to store messages.
     * @return ModelAndView redirecting to the appropriate view based on the student's role.
     */

    @PostMapping("/student-login")
    public ModelAndView studentLogin(@RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     HttpServletRequest request,
                                     RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();

        // Authenticate the user
        User user = userService.checkUser(email, password);  // Assuming this method checks the user and returns a User object
        if (user != null) {
            String role = user.getRole();

            // Create a new session or retrieve the existing one
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("username", user.getFirstName()); // Assuming User has getFirstName() method
            session.setAttribute("role", role);

            // Redirect based on user role
            if ("student".equals(role)) {
                modelAndView.setViewName("redirect:/users/student/" + user.getId());
            } else if ("admin".equals(role)) {
                modelAndView.setViewName("redirect:/users/admin-login");
            } else if ("professor".equals(role)) {
                modelAndView.setViewName("redirect:/users/professor-login");
            } else {
                // Handle unexpected roles
                attributes.addFlashAttribute("error", "Unauthorized role");
                modelAndView.setViewName("redirect:/users/student-login");
            }
        } else {
            // Handle invalid login
            attributes.addFlashAttribute("error", "Invalid email or password");
            modelAndView.setViewName("redirect:/users/student-login");
        }

        return modelAndView;
    }

    /**
     * Handle professor login.
     *
     * @param email       The email of the professor.
     * @param password    The password of the professor.
     * @param request     The HTTP request.
     * @param attributes  Redirect attributes to store messages.
     * @return ModelAndView redirecting to the appropriate view based on the professor's role.
     */

    @PostMapping("/professor-login")
    public ModelAndView professorLogin(@RequestParam("email") String email,
                                       @RequestParam("password") String password,
                                       HttpServletRequest request,
                                       RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();

        // Authenticate the user
        User user = userService.checkUser(email, password);  // Assuming this method checks the user and returns a User object
        if (user != null) {
            String role = user.getRole();

            // Create a new session or retrieve the existing one
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("username", user.getFirstName()); // Assuming User has getFirstName() method
            session.setAttribute("role", role);

            // Redirect based on user role
            if ("professor".equals(role)) {
                modelAndView.setViewName("redirect:/users/professor/" + user.getId());
            } else if ("admin".equals(role)) {
                modelAndView.setViewName("redirect:/users/admin-login");
            } else if ("student".equals(role)) {
                modelAndView.setViewName("redirect:/users/student-login");
            } else {
                // Handle unexpected roles
                attributes.addFlashAttribute("error", "Unauthorized role");
                modelAndView.setViewName("redirect:/users/professor-login");
            }
        } else {
            // Handle invalid login
            attributes.addFlashAttribute("error", "Invalid email or password");
            modelAndView.setViewName("redirect:/users/professor-login");
        }

        return modelAndView;
    }

    /**
     * Displays the admin page.
     *
     * @param request The HTTP request to get the session details.
     * @return ModelAndView with the admin view or a redirect to the admin login if not authenticated.
     */


    @GetMapping("/admin")
    public ModelAndView showAdminPage(HttpServletRequest request) {
        ModelAndView adminView = new ModelAndView();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("email") == null) {
            return new ModelAndView("redirect:/users/admin-login");
        }

        String email = (String) session.getAttribute("email");
        String role = userService.getRole(email);
        if (!"admin".equals(role)) {
            return new ModelAndView("redirect:/");
        }

        adminView.setViewName("admin");
        return adminView; // Renders admin.html template
    }

    /**
     * Checks the user credentials and returns the role if authenticated.
     *
     * @param loginData A map containing user login data, including email and password.
     * @return ResponseEntity with the user's role if authenticated, or a 404 status if the user is not found.
     */

    @PostMapping("/checkuser")
    public ResponseEntity<String> checkUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        User user = userService.checkUser(email, password);
        if (user != null) {
            return ResponseEntity.ok().body(user.getRole());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    /**
     * Displays the student page.
     *
     * @param studentId The UUID of the student.
     * @param request The HTTP request to get the session details.
     * @return ModelAndView with the student view or a redirect to the student login if not authenticated.
     */

    @GetMapping("/student/{studentId}")
    public ModelAndView showStudentPage(@PathVariable UUID studentId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession(false);

        // Check if session exists and if email attribute is present
        if (session == null || session.getAttribute("email") == null) {
            return new ModelAndView("redirect:/users/student-login");
        }

        // Get the email and role from the session
        String email = (String) session.getAttribute("email");
        String role = userService.getRole(email);

        // Verify if the role is student
        if (!"student".equals(role)) {
            return new ModelAndView("redirect:/");
        }

        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        UserDtos dto = userService.findUserById(studentId);
        List<MateriaDTO> materials = materiaService.findSubjects();
        User user = UserBuilder.toEntity(dto);
        List<Clasa> userClasses = user.getClase();
        Clasa userClass = userClasses.get(0);
        List<Schedule> userSchedules = userClass.getSchedules();
        List<String> days = new ArrayList<>();
        List<String> times = new ArrayList<>();
        for (Schedule schedule : userSchedules)
        {
            if(!days.contains(schedule.getDay()))
            {
                days.add(schedule.getDay());
            }
            if(!times.contains(schedule.getTime()))
            {
                times.add(schedule.getTime());
            }
        }
        Collections.sort(times);
        days.sort(Comparator.comparingInt(day -> Arrays.asList(daysOfWeek).indexOf(day)));
        modelAndView.addObject("daysSchedule",days);
        modelAndView.addObject("timesSchedule",times);
        modelAndView.addObject("class", userClass);
        modelAndView.addObject("student", user);
        modelAndView.addObject("subjects", materials);
        modelAndView.addObject("schedules", userSchedules);
        modelAndView.setViewName("student");
        return modelAndView;
    }

/**
 * Displays the professor page.
 *
 * @param professorId The UUID of the professor
 * @param request The HTTP request to get the session details.
 * @return ModelAndView with the professor view or a redirect to the professor login if not authenticated.
 * */

    @GetMapping("/professor/{professorId}")
    public ModelAndView showProfessorPage(@PathVariable UUID professorId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession(false);

        // Check if session exists and if email attribute is present
        if (session == null || session.getAttribute("email") == null) {
            return new ModelAndView("redirect:/users/professor-login");
        }

        // Get the email and role from the session
        String email = (String) session.getAttribute("email");
        String role = userService.getRole(email);

        // Verify if the role is professor
        if (!"professor".equals(role)) {
            return new ModelAndView("redirect:/");
        }

        UserDtos dto = userService.findUserById(professorId);
        User user = UserBuilder.toEntity(dto);
        List<Schedule> schedules = new ArrayList<>();
        List<Materia> materials = new ArrayList<>();
        for(Schedule s: user.getSchedules())
        {
            if(!materials.contains(s.getMateria()))
            {
                schedules.add(s);
                materials.add(s.getMateria());
            }
        }
        modelAndView.addObject("prof",user);
        modelAndView.addObject("schedules",schedules);
        modelAndView.setViewName("professor");
        return modelAndView;
    }

    /**

     Generates a PDF for the user and sends it.
     @param userId The UUID of the user for whom the PDF is to be generated.
     @return ResponseEntity with a success message if the PDF is generated successfully.
     */

    @PostMapping("/generateAndSendPdf")
    public ResponseEntity<String> generatePdf(@RequestParam UUID userId) {
        userService.generatePdf(userId);
        return ResponseEntity.ok("PDF file has been generated successfully");
    }

    /**

     Generates a TXT file for the user and sends it.
     @param userId The UUID of the user for whom the TXT file is to be generated.
     @return ResponseEntity with a success message if the TXT file is generated successfully.
     */

    @PostMapping("/generateAndSendTxt")
    public ResponseEntity<String> generateTxt(@RequestParam UUID userId) {
        userService.generateTxt(userId);
        return ResponseEntity.ok("TXT file has been generated successfully");
    }

    /**

     Generates a CSV file for the user and sends it.
     @param userId The UUID of the user for whom the CSV file is to be generated.
     @return ResponseEntity with a success message if the CSV file is generated successfully.
     */

    @PostMapping("/generateAndSendCsv")
    public ResponseEntity<String> generateCsv(@RequestParam UUID userId) {
        userService.generateCsv(userId);
        return ResponseEntity.ok("CSV file has been generated successfully");
    }

}
