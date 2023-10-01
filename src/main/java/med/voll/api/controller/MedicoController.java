package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import med.voll.api.domain.direccion.DatosDireccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {
    @Autowired // se usa para usar el objeto ya creado, se le pide a spring que se lo inyecte asi no se necesite instanciar uno.
    // PERO dara problemas en las pruebas unitarias, no se recomienda en la practica, solo se usa para medios didacticos
    private MedicoRepository medicoRepository;
    @PostMapping
    public ResponseEntity registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentsBuilder){
       Medico medico =  medicoRepository.save(new Medico(datosRegistroMedico));
        //return 201 Created - debe devolver
        //debe retornar la url donde encontrar el recurso
        // GET http://localhost:8080
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(
                medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(), medico.getDireccion().getComplemento())
        );
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);
    }
    @GetMapping
    public ResponseEntity< Page<DatosListadoMedico>> ListadoMedico(@PageableDefault(size = 10 , sort = "nombre") Pageable paginacion){
        //return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        //return medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok( medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
    }
    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(
                medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(), medico.getDireccion().getComplemento())
        ));
    }

    @GetMapping("/{id}")
    //delete logico: elimina solo de la lista, queda en la base de datos
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedicos(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(
                medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(), medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }
    @DeleteMapping("/{id}")
    @Transactional
    //delete logico: elimina solo de la lista, queda en la base de datos
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();

        return ResponseEntity.noContent().build();
    }

    /* //elimina el medico de la base de datos
        public void eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    } */

}
