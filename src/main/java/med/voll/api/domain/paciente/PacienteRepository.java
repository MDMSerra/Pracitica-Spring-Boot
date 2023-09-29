package med.voll.api.domain.paciente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente,Long> {

    Page<Paciente> findByActivoTrue(Pageable paginacion);
    //@Query("SELECT p FROM Paciente p WHERE p.ultimaVisita < :fecha")
    //List<Paciente> findPacientesSinVisita(@Param("fecha") LocalDate fecha);

    @Query("SELECT p FROM Paciente p WHERE p.activo = 0")
    List<Paciente> findPacientesFallecidos();
}
