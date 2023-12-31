package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Test
    @DisplayName("deberia retornar nulo cuando el medico se encuentre en consulta con otro paciente en ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario1() {

        var proximoLunes10H = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);

        var medico = registrarMedico("Jose","j@mail.com", "647361", Especialidad.GINECOLOGIA);

        var paciente = registrarPaciente ("Antonio", "a@mail.com", "654321");

        registrarConsulta(medico,paciente, proximoLunes10H);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.GINECOLOGIA,proximoLunes10H);

        assertThat(medicoLibre).isNull();
        //assertNull(medicoLibre);
    }

    @Test
    @DisplayName("deberia retornar un medico cuando realice la consulta en la base de datos para ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario2() {

        var proximoLunes10H = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);

        var medico = registrarMedico("Jose","j@mail.com", "647361", Especialidad.GINECOLOGIA);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.GINECOLOGIA,proximoLunes10H);

        assertThat(medicoLibre).isEqualTo(medico);
    }

    private  void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha){
        testEntityManager.persist(new Consulta(null, medico, paciente, fecha, null));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad){
        var medico = new Medico(datosMedico(nombre,email,documento,especialidad));
        testEntityManager.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento){
        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        testEntityManager.persist(paciente);
        return paciente;
    }
    private DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad especialidad){
        return  new DatosRegistroMedico(nombre, email, "123565122", documento, especialidad, datosDireccion());
    }
    private DatosRegistroPaciente datosPaciente(String nombre, String email, String documento){
        return  new DatosRegistroPaciente(nombre, email, "234576327", documento, datosDireccion());
    }
    private DatosDireccion datosDireccion(){
        return new DatosDireccion("loca","azul","acapulpo",321,"12");
    }
}