package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico,Long> {
    Page<Medico> findByActivoTrue(Pageable paginacion);

    /*
    @Query("""
            SELECT m FROM Medico m WHERE m.activo = 1 AND m.especialidad=:especialidad AND
            m.id NOT IN(
            SELECT c.medico.id FROM Consulta c WHERE c.fecha=:fecha
            )
            ORDER BY RAND()
            LIMIT 1""")
     */
    @Query("""
            select m from Medico m
            where m.activo= 1 
            and
            m.especialidad=:especialidad 
            and
            m.id not in(  
                select c.medico.id from Consulta c
                where
                c.fecha=:fecha
            )
            order by rand()
            limit 1
            """)
    Medico seleccionarMedicoConEspecialidadEnFecha(Especialidad especialidad, LocalDateTime fecha);

    List<Medico> findByNombre(String nombre);

    @Query("""
            SELECT m.activo FROM Medico m WHERE m.id=:idMedico
            """)
    Boolean findActivoById(Long idMedico);

}
