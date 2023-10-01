package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.stereotype.Service;


public interface ValidadorDeConsultas {

    public void validar(DatosAgendarConsulta datosAgendarConsulta);
}
