package com.example.Testing.Service;

import com.example.Testing.DTOS.Builders.ScheduleBuilder;
import com.example.Testing.DTOS.ScheduleDTO;
import com.example.Testing.Entity.Clasa;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.Schedule;
import com.example.Testing.Entity.User;
import com.example.Testing.Repository.ClasaRepository;
import com.example.Testing.Repository.MateriaRepository;
import com.example.Testing.Repository.ScheduleRepository;
import com.example.Testing.Repository.UserRepository;
import com.example.Testing.validators.ScheduleValidator;
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
 * Service class for managing Schedule entities.
 */
@Service
public class ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);
    private final ScheduleRepository scheduleRepo;
    private final MateriaRepository subjectRepo;
    private final ClasaRepository clasaRepo;
    private final UserRepository userRepo;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, MateriaRepository subjectRepo, ClasaRepository clasaRepo, UserRepository userRepo) {
        this.scheduleRepo = scheduleRepository;
        this.subjectRepo = subjectRepo;
        this.clasaRepo = clasaRepo;
        this.userRepo = userRepo;
    }

    /**
     * Retrieve all schedules.
     *
     * @return List of ScheduleDTO objects representing schedules.
     */
    public List<ScheduleDTO> findSchedules() {
        List<Schedule> scheduleList = scheduleRepo.findAll();
        return scheduleList.stream()
                .map(ScheduleBuilder::toScheduleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a schedule by ID.
     *
     * @param id The ID of the schedule to retrieve.
     * @return ScheduleDTO object representing the schedule.
     * @throws ResourceNotFoundException if the schedule is not found.
     */
    public ScheduleDTO findScheduleById(UUID id) {
        ScheduleValidator.validateScheduleId(id,scheduleRepo);

        Optional<Schedule> scheduleOptional = scheduleRepo.findById(id);

        return ScheduleBuilder.toScheduleDTO(scheduleOptional.get());
    }

    /**
     * Insert a new schedule.
     *
     * @param scheduleDTO The ScheduleDTO object representing the schedule to insert.
     * @return The ID of the inserted schedule.
     */
    public UUID insertSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = ScheduleBuilder.toEntity(scheduleDTO);
        schedule = scheduleRepo.save(schedule);
        LOGGER.debug("Schedule with id {} was inserted in db", schedule.getId());
        return schedule.getId();
    }

    /**
     * Modify an existing schedule.
     *
     * @param scheduleDTO The ScheduleDTO object representing the updated schedule.
     * @param id          The ID of the schedule to modify.
     * @return The updated Schedule entity.
     * @throws ResourceNotFoundException if the schedule is not found.
     */
    public Schedule modifySchedule(ScheduleDTO scheduleDTO, UUID id) {
        ScheduleValidator.validateScheduleId(id,scheduleRepo);
        Optional<Schedule> scheduleOptional = scheduleRepo.findById(id);

        Schedule schedule = scheduleOptional.get(); // Extract the entity from Optional

        // Update schedule fields with data from scheduleDTO
        schedule.setDay(scheduleDTO.getDay());
        schedule.setTime(scheduleDTO.getTime());

        schedule = scheduleRepo.save(schedule); // Save the updated entity

        LOGGER.debug("Schedule with id {} has been modified in db", schedule.getId());
        return schedule;
    }

    /**
     * Delete a schedule by ID.
     *
     * @param id The ID of the schedule to delete.
     * @throws ResourceNotFoundException if the schedule is not found.
     */
    public void deleteSchedule(UUID id) {
        ScheduleValidator.validateScheduleId(id,scheduleRepo);
        ScheduleDTO scheduleDTO = findScheduleById(id);
        Schedule schedule = ScheduleBuilder.toEntity(scheduleDTO);
        schedule.setMateria(null);
        schedule.setUser(null);
        schedule.setClasa(null);
        scheduleRepo.save(schedule);
        scheduleRepo.deleteById(id);
    }

    /**
     * Add a subject to a schedule.
     *
     * @param scheduleID The ID of the schedule.
     * @param subjectID  The ID of the subject to add.
     * @return The ID of the updated schedule.
     * @throws ResourceNotFoundException if the schedule or subject is not found.
     */

    public UUID addSubject(UUID scheduleID, UUID subjectID)
    {
        if(scheduleRepo.existsById(scheduleID))
        {
            if(subjectRepo.existsById(subjectID))
            {
                Schedule schedule = scheduleRepo.findById(scheduleID).get();
                Materia subject = subjectRepo.findById(subjectID).get();
                schedule.setMateria(subject);

                scheduleRepo.save(schedule);

                return schedule.getId();
            }
            else{
                LOGGER.error("Subject with id {} was not found in db", subjectID);
                throw new ResourceNotFoundException(Materia.class.getSimpleName() + " with id: " + subjectID);
            }
        }
        else{
            LOGGER.error("Schedule with id {} was not found in db", scheduleID);
            throw new ResourceNotFoundException(Schedule.class.getSimpleName() + " with id: " + scheduleID);
        }
    }

    /**
     * Add a class to a schedule.
     *
     * @param scheduleID The ID of the schedule.
     * @param classID    The ID of the class to add.
     * @return The ID of the updated schedule.
     * @throws ResourceNotFoundException if the schedule or class is not found.
     */

    public UUID addClass(UUID scheduleID, UUID classID)
    {
        if(scheduleRepo.existsById(scheduleID))
        {
            if(clasaRepo.existsById(classID))
            {
                Schedule schedule = scheduleRepo.findById(scheduleID).get();
                Clasa clasa = clasaRepo.findById(classID).get();
                schedule.setClasa(clasa);

                scheduleRepo.save(schedule);

                return schedule.getId();
            }
            else{
                LOGGER.error("Class with id {} was not found in db", classID);
                throw new ResourceNotFoundException(Clasa.class.getSimpleName() + " with id: " + classID);
            }
        }
        else{
            LOGGER.error("Schedule with id {} was not found in db", scheduleID);
            throw new ResourceNotFoundException(Schedule.class.getSimpleName() + " with id: " + scheduleID);
        }
    }

    /**
     * Add a professor to a schedule.
     *
     * @param professorID The ID of the professor.
     * @param scheduleID  The ID of the schedule.
     * @return The ID of the updated schedule.
     * @throws ResourceNotFoundException if the professor or schedule is not found.
     */

    public UUID addProfessor(UUID professorID, UUID scheduleID)
    {
        if(userRepo.existsById(professorID))
        {
            if(scheduleRepo.existsById(scheduleID))
            {
                User professor = userRepo.findById(professorID).get();
                Schedule schedule = scheduleRepo.findById(scheduleID).get();

                schedule.setUser(professor);

                scheduleRepo.save(schedule);

                return schedule.getId();
            }
            else{
                LOGGER.error("Schedule with id {} was not found in db", scheduleID);
                throw new ResourceNotFoundException(Schedule.class.getSimpleName() + " with id: " + scheduleID);
            }
        }
        else{
            LOGGER.error("Professor with id {} was not found in db", professorID);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + professorID);
        }
    }
}
