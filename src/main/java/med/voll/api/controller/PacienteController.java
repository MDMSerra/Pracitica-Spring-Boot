package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import med.voll.api.domain.paciente.*;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {
   @Autowired
    private PacienteRepository pacienteRepository;
    @PostMapping
    public ResponseEntity registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente, UriComponentsBuilder uriComponentsBuilder){
        Paciente paciente =  pacienteRepository.save(new Paciente(datosRegistroPaciente));
        DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(
                paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getTelefono(), paciente.getDocumento(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento())
        );
        URI url = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaPaciente);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listarPacientes(@PageableDefault(size = 2)Pageable paginacion){
        return ResponseEntity.ok( pacienteRepository.findByActivoTrue(paginacion).map(DatosListadoPaciente::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizacionPaciente datosActualizacionPaciente) {
        Paciente paciente = pacienteRepository.getReferenceById(datosActualizacionPaciente.id());
        paciente.actualizarInformacion(datosActualizacionPaciente);
        return ResponseEntity.ok(new DatosRespuestaPaciente(
                paciente.getId(), paciente.getNombre(), paciente.getDocumento(), paciente.getEmail(), paciente.getTelefono(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento())));
    }

    @GetMapping("/{id}")
    //delete logico: elimina solo de la lista, queda en la base de datos
    public ResponseEntity<DatosRespuestaPaciente> retornaDatosMedicos(@PathVariable Long id){
        Paciente paciente = pacienteRepository.getReferenceById(id);

        DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(
                paciente.getId(), paciente.getNombre(), paciente.getDocumento(), paciente.getEmail(), paciente.getTelefono(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento())
        );

        return ResponseEntity.ok(datosRespuestaPaciente);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.inactivar();
        return  ResponseEntity.noContent().build();
    }

    @Transactional
    public void inactivarPacientes() {
        //List<Paciente> pacientesSinVisita = pacienteRepository.findPacientesSinVisita(LocalDate.now().minusYears(1));
        List<Paciente> pacientesFallecidos = pacienteRepository.findPacientesFallecidos();

        //for (Paciente paciente : pacientesSinVisita) {
        //    paciente.setActivo(false);
        //}

        for (Paciente paciente : pacientesFallecidos) {
            paciente.setActivo(false);
        }
    }
}
