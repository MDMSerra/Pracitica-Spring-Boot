package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/consultas")
public class ConsultaController {
    @Autowired
    private AgendaDeConsultaService agendaDeConsultaService;
    @PostMapping
    @Transactional
     public ResponseEntity agendar(@RequestBody @Valid DatosAgendarConsulta datosAgendarConsulta){
        agendaDeConsultaService.agendar(datosAgendarConsulta);
        return ResponseEntity.ok(new DatosDetalleConsulta(null,datosAgendarConsulta.idPaciente(), datosAgendarConsulta.idMedico(), datosAgendarConsulta.fecha()));
     }
}
