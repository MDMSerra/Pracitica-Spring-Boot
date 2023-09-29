package med.voll.api.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.medico.Especialidad;

import java.time.LocalDateTime;

public record DatosAgendarConsulta(Long id, @NotNull Long idPaciente, Long idMedico, @NotNull @Future LocalDateTime fecha, Especialidad especialidad) {
    //si ponemos @NotNull en especialidad no vamos a conseguir mandar un mensaje al cliente con las validaciones del throw del error

}
