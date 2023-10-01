package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamiento;

public interface ValidadorCancelamientoDeConsulta {
    public void validar(DatosCancelamientoConsulta datosCancelamientoConsulta);

    public @NotNull MotivoCancelamiento motivo(DatosCancelamientoConsulta datosCancelamientoConsulta);
}
