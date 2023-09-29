package med.voll.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroPaciente(
        @NotBlank(message = "{nombre.obligatorio}")
        String nombre,
        @NotBlank(message = "{email.obligatorio}")//estos mensajes de validacion se configuran en ValidationMessages.properties de la carpeta resources
        @Email(message = "{email.invalido}")
        String email,
        @NotBlank(message = "Teléfono es obligatorio")//ese mensaje de validacion no necesita un properties en resources ya que se configura aqui
        String telefono,
        @NotBlank
        @Pattern(regexp = "\\d{6}")
        String documento,
        @NotNull(message = "Datos de dirección son obligatorios")
        @Valid
        DatosDireccion datosDireccion) {
}
