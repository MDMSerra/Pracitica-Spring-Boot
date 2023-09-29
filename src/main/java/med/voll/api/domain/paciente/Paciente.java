package med.voll.api.domain.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Entity(name = "Paciente")
@Table(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private boolean activo;
    private String documento;
    @Embedded
    private Direccion direccion;

    public Paciente() {
    }

    public  Paciente(DatosRegistroPaciente datosRegistroPaciente){
        this.nombre = datosRegistroPaciente.nombre();
        this.email = datosRegistroPaciente.email();
        this.telefono = datosRegistroPaciente.telefono();
        this.documento = datosRegistroPaciente.documento();
        this.direccion = new Direccion(datosRegistroPaciente.datosDireccion());
        this.activo = true;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumento() {
        return documento;
    }

    public Long getId() { return id; }

    public String getTelefono() { return telefono; }

    public boolean isActivo() { return activo; }

    public Direccion getDireccion() { return this.direccion;  }

    public void setActivo(boolean b) {
        this.activo = b;
    }

    public void actualizarInformacion(DatosActualizacionPaciente datos) {
        if (datos.nombre() != null) {
            this.nombre = datos.nombre();
        }
        if (datos.documento() != null) {
            this.documento = datos.documento();
        }
        if (datos.direccion() != null) {
            this.direccion = direccion.actualizarDireccion(datos.direccion());
        }
        if (datos.activo() != null) {
            this.activo = datos.activo();
        }
    }
    public void inactivar () {
            this.activo = false;
        }
    }
