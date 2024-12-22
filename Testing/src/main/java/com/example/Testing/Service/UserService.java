package com.example.Testing.Service;

import com.example.Testing.DTOS.Builders.UserBuilder;
import com.example.Testing.DTOS.UserDtos;
import com.example.Testing.Entity.*;
import com.example.Testing.Repository.*;
import com.example.Testing.strategy.CsvGeneratorStrategy;
import com.example.Testing.strategy.PdfGeneratorStrategy;
import com.example.Testing.strategy.TxtGeneratorStrategy;
import com.example.Testing.strategy.UserFileGenerator;
import com.example.Testing.validators.UserValidator;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing User entities.
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepo;
    private final NoteRepository noteRepo;
    private final MateriaRepository materiaRepo;
    private final ClasaRepository clasaRepo;
    private final ScheduleRepository scheduleRepo;

    @Autowired
    public UserService(UserRepository userRepository, NoteRepository noteRepo, MateriaRepository materiaRepo, ScheduleRepository scheduleRepo, ClasaRepository clasaRepo, ScheduleRepository scheduleRepo1) {
        this.userRepo = userRepository;
        this.noteRepo = noteRepo;
        this.materiaRepo = materiaRepo;
        this.clasaRepo = clasaRepo;
        this.scheduleRepo = scheduleRepo1;
    }

    /**
     * Retrieve all users.
     *
     * @return List of UserDtos objects representing users.
     */
    public List<UserDtos> findUsers() {
        List<User> userList = userRepo.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all professors.
     *
     * @return List of UserDtos objects representing professors.
     */

    public List<UserDtos> findProfessors() {
        List<User> userList = userRepo.findAll();
        List<User> professors = new ArrayList<>();
        for(User prof: userList)
        {
            if(prof.getRole().equals("professor"))
            {
                professors.add(prof);
            }
        }
        return professors.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }


    /**
     * Find a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return UserDtos object representing the user.
     * @throws ResourceNotFoundException if the user is not found.
     */
    public UserDtos findUserById(UUID id) {
        UserValidator.validateUserId(id, userRepo);
        Optional<User> userOptional = userRepo.findById(id);
        return UserBuilder.toUserDTO(userOptional.get());
    }

    /**
     * Find a user by email.
     *
     * @param email The email of the user to retrieve.
     * @return UserDtos object representing the user.
     */

    public UserDtos findUserByEmail(String email)
    {
        UserValidator.validateUserEmail(email, userRepo);
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));
        return UserBuilder.toUserDTO(userOptional.get());
    }

    /**
     * Insert a new user.
     *
     * @param userDTO The UserDtos object representing the user to insert.
     * @return The ID of the inserted user.
     */
    public UUID insertUser(UserDtos userDTO) {
        User userv = userRepo.findByEmail(userDTO.getEmail());
        if(Objects.isNull(userv))
        {
            User user = UserBuilder.toEntity(userDTO);
            user = userRepo.save(user);
            LOGGER.debug("User with id {} was inserted in db", user.getId());
            return user.getId();
        }
        return userDTO.getId();
    }

    /**
     * Modify an existing user.
     *
     * @param userDTO The UserDtos object representing the updated user.
     * @param id      The ID of the user to modify.
     * @return The updated User entity.
     * @throws ResourceNotFoundException if the user is not found.
     */
    public User modifyUser(UserDtos userDTO, UUID id) {
        UserValidator.validateUserId(id, userRepo);

        Optional<User> userOptional = userRepo.findById(id);

        User user = userOptional.get(); // Extract the entity from Optional
        // Update user fields with data from userDTO
        user.setRole(userDTO.getRole());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setClase(userDTO.getClase());
        user.setSchedules(userDTO.getSchedules()); //Admin
        user.setMarks(userDTO.getMarks());

        user = userRepo.save(user); // Save the updated entity

        LOGGER.debug("User with id {} has been modified in db", user.getId());
        return user;
    }

    /**
     * Delete a user by ID.
     *
     * @param id The ID of the user to delete.
     * @throws ResourceNotFoundException if the user is not found.
     */
    public void deleteUser(UUID id) {
        UserValidator.validateUserId(id, userRepo); //clasa UserVlaidator are metode statice
        UserDtos userDTO = findUserById(id);
        User user = UserBuilder.toEntity(userDTO);
        for (Clasa clasa : user.getClase())
        {
            clasa.getUsers().remove(user);
            clasaRepo.save(clasa);
        }
        for(Note nota : user.getMarks().values())
        {
            nota.getUsers().remove(user);
            noteRepo.save(nota);
        }
        for(Materia subject: user.getMarks().keySet())
        {
            subject.getUsers().remove(user);
            materiaRepo.save(subject);
        }
        for(Schedule s: user.getSchedules())
        {
            s.setUser(null);
            scheduleRepo.save(s);
        }
        userRepo.deleteById(id);
    }

    /**
     * Check if a user exists with the given email and password combination.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return True if a user with the given email and password exists, otherwise false.
     */
    public boolean adminVerification(String email, String password) {
        User user = userRepo.findByEmail(email);
        if(user != null)
        {
            if(user.getRole().equals("admin"))
            {
                if(user.getPassword().equals(password))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verify if the user is a student with the given email and password combination.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return True if a user with the given email and password exists and is a student, otherwise false.
     */

    public boolean studentVerification(String email, String password) {
        User user = userRepo.findByEmail(email);
        if(user != null)
        {
            if(user.getRole().equals("student"))
            {
                if(user.getPassword().equals(password))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verify if the user is a professor with the given email and password combination.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return True if a user with the given email and password exists and is a professor, otherwise false.
     */

    public boolean professorVerification(String email, String password) {
        User user = userRepo.findByEmail(email);
        if(user != null)
        {
            if(user.getRole().equals("professor"))
            {
                if(user.getPassword().equals(password))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add marks for a user.
     *
     * @param userID    The ID of the user.
     * @param markID    The ID of the mark.
     * @param subjectID The ID of the subject.
     */

    public void addMarks(UUID userID, UUID markID, UUID subjectID)
    {
        UserValidator.validateUserId(userID,userRepo);
        User user = userRepo.findById(userID).get();
        if(noteRepo.existsById(markID))
        {
            Note nota = noteRepo.findById(markID).get();
            if(materiaRepo.existsById(subjectID))
            {
                Materia materia = materiaRepo.findById(subjectID).get();
                if(user.getMarks().containsKey(materia))
                {
                    user.getMarks().remove(materia);
                }
                user.getMarks().put(materia,nota);
                UserDtos dto = UserBuilder.toUserDTO(user);
                modifyUser(dto,user.getId());
            }
        }
    }

    /**
     * Check if a user exists with the given email and password combination.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The User entity if found, otherwise null.
     */

    public User checkUser(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password);
    }

    /**
     * Get the role of a user by email.
     *
     * @param email The email of the user.
     * @return The role of the user.
     * @throws IllegalStateException if the role is invalid.
     */

    public String getRole(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String role = user.getRole();

            // Validate role against the set of valid roles
            if (role.equals("admin") || role.equals("professor") || role.equals("student")) {
                return role;
            } else {
                throw new IllegalStateException("Invalid role: " + role);
            }
        }
        return null;
    }

    /**
     * Generate a TXT file for a user.
     *
     * @param id The ID of the user.
     * @throws ResourceNotFoundException if the user is not found.
     */

    public void generateTxt(UUID id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in the database", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        User user = userOptional.get();

        UserFileGenerator userFileGenerator = new UserFileGenerator();
        userFileGenerator.setFileGenerationStrategy(new TxtGeneratorStrategy());
        userFileGenerator.generateUserFile(Optional.of(user));
    }

    /**
     * Generate a CSV file for a user.
     *
     * @param id The ID of the user.
     * @throws ResourceNotFoundException if the user is not found.
     */

    public void generateCsv(UUID id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in the database", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        User user = userOptional.get();
        UserFileGenerator userFileGenerator = new UserFileGenerator();
        userFileGenerator.setFileGenerationStrategy(new CsvGeneratorStrategy());
        userFileGenerator.generateUserFile(Optional.of(user));
    }

    /**
     * Generate a PDF file for a user.
     *
     * @param id The ID of the user.
     * @throws ResourceNotFoundException if the user is not found.
     */

    public void generatePdf(UUID id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in the database", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        User user = userOptional.get();
        UserFileGenerator userFileGenerator = new UserFileGenerator();
        userFileGenerator.setFileGenerationStrategy(new PdfGeneratorStrategy());
        userFileGenerator.generateUserFile(Optional.of(user));
    }
}
