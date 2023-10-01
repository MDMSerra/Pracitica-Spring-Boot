package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class HorarioAntecedencia implements ValidadorCancelamientoDeConsulta{

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validar(DatosCancelamientoConsulta datosCancelamientoConsulta){
        var consulta = consultaRepository.getReferenceById(datosCancelamientoConsulta.idConsulta());
        var ahora = LocalDateTime.now();
        var diferenciaEnHoras = Duration.between(ahora, consulta.getFecha()).toHours();

        if (diferenciaEnHoras<24){
            throw new ValidationException("Consulta solamente puede ser cancelada con antecedencia minima de 24 horas");
        }
    }

    @Override
    public @NotNull MotivoCancelamiento motivo(DatosCancelamientoConsulta datosCancelamientoConsulta) {
        return datosCancelamientoConsulta.motivo();
    }
}
