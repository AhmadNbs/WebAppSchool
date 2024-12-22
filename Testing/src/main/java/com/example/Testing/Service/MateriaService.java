package com.example.Testing.Service;

import com.example.Testing.DTOS.Builders.MateriaBuilder;
import com.example.Testing.DTOS.MateriaDTO;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.Note;
import com.example.Testing.Entity.Schedule;
import com.example.Testing.Entity.User;
import com.example.Testing.Repository.MateriaRepository;
import com.example.Testing.Repository.NoteRepository;
import com.example.Testing.Repository.ScheduleRepository;
import com.example.Testing.Repository.UserRepository;
import com.example.Testing.validators.MateriaValidator;
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
 * Service class for managing Materia entities.
 */
@Service
public class MateriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MateriaService.class);

    private final MateriaRepository materiaRepo;
    private final ScheduleRepository scheduleRepo;
    private final NoteRepository notaRepo;
    private final UserRepository userRepo;

    @Autowired
    public MateriaService(MateriaRepository materiaRepository, ScheduleRepository scheduleRepo, NoteRepository notaRepo, UserRepository userRepo) {
        this.materiaRepo = materiaRepository;
        this.scheduleRepo = scheduleRepo;
        this.notaRepo = notaRepo;
        this.userRepo = userRepo;
    }

    /**
     * Retrieve all subjects.
     *
     * @return List of MateriaDTO objects representing subjects.
     */
    public List<MateriaDTO> findSubjects() {
        List<Materia> materiaList = materiaRepo.findAll();
        return materiaList.stream()
                .map(MateriaBuilder::toMateriaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a subject by ID.
     *
     * @param id The ID of the subject to retrieve.
     * @return MateriaDTO object representing the subject.
     * @throws ResourceNotFoundException if the subject is not found.
     */
    public MateriaDTO findSubjectById(UUID id) {
        MateriaValidator.validateMateriaId(id, materiaRepo);
        Optional<Materia> materiaOptional = materiaRepo.findById(id);
        return MateriaBuilder.toMateriaDTO(materiaOptional.get());
    }

    /**
     * Insert a new subject.
     *
     * @param materiaDTO The MateriaDTO object representing the subject to insert.
     * @return The ID of the inserted subject.
     */
    public UUID insertSubject(MateriaDTO materiaDTO) {
        Materia materia = MateriaBuilder.toEntity(materiaDTO);
        materia = materiaRepo.save(materia);
        LOGGER.debug("Subject with id {} was inserted in db", materia.getId());
        return materia.getId();
    }

    /**
     * Modify an existing subject.
     *
     * @param materiaDTO The MateriaDTO object representing the updated subject.
     * @param id         The ID of the subject to modify.
     * @return The updated Materia entity.
     * @throws ResourceNotFoundException if the subject is not found.
     */
    public Materia modifySubject(MateriaDTO materiaDTO, UUID id) {
        MateriaValidator.validateMateriaId(id, materiaRepo);
        Optional<Materia> materiaOptional = materiaRepo.findById(id);
        Materia materia = materiaOptional.get();

        // Update subject fields with data from materiaDTO
        materia.setName(materiaDTO.getName());

        materia = materiaRepo.save(materia);

        LOGGER.debug("Subject with id {} has been modified in db", materia.getId());
        return materia;
    }

    /**
     * Delete a subject by ID.
     *
     * @param id The ID of the subject to delete.
     * @throws ResourceNotFoundException if the subject is not found.
     */
    public void deleteSubject(UUID id) {
        MateriaValidator.validateMateriaId(id, materiaRepo);
        MateriaDTO dto = findSubjectById(id);
        Materia m = MateriaBuilder.toEntity(dto);
        for(Schedule s: m.getSchedules())
        {
            s.setMateria(null);
            scheduleRepo.save(s);
        }
        for (User user: m.getUsers())
        {
            user.getMarks().keySet().remove(m);
            userRepo.save(user);
        }
        for(Note nota: m.getMarks())
        {
            nota.getSubjects().remove(m);
            notaRepo.save(nota);
        }
        materiaRepo.deleteById(id);
    }
}
