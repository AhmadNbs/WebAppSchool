package com.example.Testing.Service;

import com.example.Testing.DTOS.Builders.ClasaBuilder;
import com.example.Testing.DTOS.ClasaDTO;
import com.example.Testing.Entity.*;
import com.example.Testing.Repository.*;
import com.example.Testing.validators.ClasaValidator;
import com.example.Testing.validators.UserValidator;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling operations related to Clasa entities.
 */
@Service
public class ClasaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClasaService.class);
    private final ClasaRepository clasaRepo;
    private final UserRepository userRepo;
    private final NoteRepository noteRepo;
    private final MateriaRepository materiaRepo;
    private final ScheduleRepository scheduleRepo;

    /**
     * Constructs a new instance of ClasaService.
     * @param clasaRepository The repository for interacting with Clasa entities.
     * @param userRepo The repository for interacting with User entities.
     * @param noteRepo The repository for interacting with Note entities.
     * @param materiaRepo The repository for interacting with Materia entities.
     * @param scheduleRepo The repository for interacting with Schedule entities.
     */
    @Autowired
    public ClasaService(ClasaRepository clasaRepository, UserRepository userRepo, NoteRepository noteRepo, MateriaRepository materiaRepo, ScheduleRepository scheduleRepo) {
        this.clasaRepo = clasaRepository;
        this.userRepo = userRepo;
        this.noteRepo = noteRepo;
        this.materiaRepo = materiaRepo;
        this.scheduleRepo = scheduleRepo;
    }

    /**
     * Retrieves a list of all Clasa entities.
     * @return A list of ClasaDTO representing all Clasa entities.
     */
    public List<ClasaDTO> findClase() {
        List<Clasa> clasaList = clasaRepo.findAll();
        return clasaList.stream()
                .map(ClasaBuilder::toClasaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a Clasa entity by ID.
     * @param id The ID of the Clasa entity to retrieve.
     * @return The ClasaDTO representing the Clasa entity with the given ID.
     * @throws ResourceNotFoundException if the Clasa entity with the given ID does not exist.
     */
    public ClasaDTO findClasaById(UUID id) {
        Optional<Clasa> clasaOptional = clasaRepo.findById(id);
        if (clasaOptional.isEmpty()) {
            LOGGER.error("Clasa with id {} was not found in db", id);
            throw new ResourceNotFoundException(Clasa.class.getSimpleName() + " with id: " + id);
        }
        return ClasaBuilder.toClasaDTO(clasaOptional.get());
    }

    /**
     * Adds a student to a class.
     * @param classID The ID of the class to add the student to.
     * @param studentID The ID of the student to be added.
     * @return The ID of the class.
     * @throws ResourceNotFoundException if the class or student does not exist, or if the student is already in a class.
     */

    public UUID addStudent(UUID classID, UUID studentID)
    {
        if(clasaRepo.existsById(classID))
        {
            if(userRepo.existsById(studentID))
            {
                Clasa clasa = clasaRepo.findById(classID).get();
                User student = userRepo.findById(studentID).get();
                if(!student.getClase().isEmpty()) {
                    clasa.getUsers().add(student);
                    clasaRepo.save(clasa);

                    return clasa.getId();
                }
                else{
                    LOGGER.error("Student with id {}  has already a class", studentID);
                    throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + studentID);
                }
            }
            else{
                LOGGER.error("Student with id {} was not found in db", studentID);
                throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + studentID);
            }
        }
        else{
            LOGGER.error("Clasa with id {} was not found in db", classID);
            throw new ResourceNotFoundException(Clasa.class.getSimpleName() + " with id: " + classID);
        }
    }

    /**
     * Inserts a new Clasa entity.
     * @param clasaDTO The ClasaDTO representing the Clasa entity to be inserted.
     * @return The ID of the newly inserted Clasa entity.
     */
    public UUID insertClasa(ClasaDTO clasaDTO) {
        Clasa clasa = ClasaBuilder.toEntity(clasaDTO);
        clasa = clasaRepo.save(clasa);
        LOGGER.debug("Clasa with id {} has been inserted in DB", clasa.getId());
        return clasa.getId();
    }

    /**
     * Modifies an existing Clasa entity.
     * @param clasaDTO The ClasaDTO representing the updated information of the Clasa entity.
     * @return The modified Clasa entity.
     * @throws ResourceNotFoundException if the Clasa entity with the given ID does not exist.
     */
    public Clasa modifyClasa(ClasaDTO clasaDTO,UUID id) {
        ClasaValidator.validateClasaId(id,clasaRepo);
        Optional<Clasa> clasaOptional = clasaRepo.findById(id);

        Clasa clasa = clasaOptional.get();

        // Update Clasa fields with data from clasaDTO
        clasa.setGrade(clasaDTO.getGrade());
        clasa.setNumber(clasaDTO.getNumber());
        clasa.setUsers(clasaDTO.getUsers());
        clasa.setSchedules(clasaDTO.getSchedules());

        clasa = clasaRepo.save(clasa);

        LOGGER.debug("Clasa with id {} has been modified in db", clasa.getId());
        return clasa;
    }

    /**
     * Deletes a Clasa entity by ID.
     * @param id The ID of the Clasa entity to delete.
     * @throws ResourceNotFoundException if the Clasa entity with the given ID does not exist.
     */
    public void deleteClasa(UUID id) {
        ClasaValidator.validateClasaId(id,clasaRepo);
        ClasaDTO dto = findClasaById(id);
        Clasa clasa = ClasaBuilder.toEntity(dto);
        for(User user: clasa.getUsers())
        {
            user.getClase().remove(clasa);
            userRepo.save(user);
        }
        for(Schedule s: clasa.getSchedules())
        {
            s.setClasa(null);
            scheduleRepo.save(s);
        }
        clasaRepo.deleteById(id);
    }
}
