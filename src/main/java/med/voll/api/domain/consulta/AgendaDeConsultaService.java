package med.voll.api.domain.consulta;


import med.voll.api.domain.consulta.validaciones.ValidadorCancelamientoDeConsulta;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgendaDeConsultaService {
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    List<ValidadorDeConsultas> validadorDeConsultasList;

    @Autowired
    List<ValidadorCancelamientoDeConsulta> validadorCancelamientoDeConsultaList;
    public DatosDetalleConsulta agendar(DatosAgendarConsulta datosAgendarConsulta){

        if (!pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el paciente no fue encontrado");
        }
        if (datosAgendarConsulta.idMedico()!=null && !medicoRepository.existsById(datosAgendarConsulta.idMedico())){
            throw new ValidacionDeIntegridad("Este id para el medico no fue encontrado");
        }
        validadorDeConsultasList.forEach(v -> v.validar(datosAgendarConsulta));

        var paciente  = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();
        var medico = seleccionarMedico(datosAgendarConsulta);
        if(medico==null){
            throw new ValidacionDeIntegridad("no existen medicos disponibles para este horario y especialidad");
        }
        var consulta = new Consulta (medico, paciente, datosAgendarConsulta.fecha());

        consultaRepository.save(consulta);
        return new DatosDetalleConsulta(consulta);
    }

    public void cancelar(DatosCancelamientoConsulta datosCancelamientoConsulta){
        if(!consultaRepository.existsById(datosCancelamientoConsulta.idConsulta())){
            throw new ValidacionDeIntegridad("Id de la consulta informado no Existe!");
        }

        validadorCancelamientoDeConsultaList.forEach(v -> v.validar(datosCancelamientoConsulta));

        var consulta = consultaRepository.getReferenceById(datosCancelamientoConsulta.idConsulta());
        consulta.cancelar(datosCancelamientoConsulta.motivo());

    }

    private Medico seleccionarMedico(DatosAgendarConsulta datosAgendarConsulta) {
        if(datosAgendarConsulta.idMedico()!=null){
            return medicoRepository.getReferenceById(datosAgendarConsulta.idMedico());
        }
        if(datosAgendarConsulta.especialidad()==null){
            throw  new ValidacionDeIntegridad("debe seleccionar una especialidad para el medico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datosAgendarConsulta.especialidad(), datosAgendarConsulta.fecha());
    }
}
