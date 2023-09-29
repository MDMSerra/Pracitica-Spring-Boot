package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroMedico(
        @NotBlank(message = "{nombre.obligatorio}")//estos mensajes de validacion se configuran en ValidationMessages.properties de la carpeta resources
        String nombre,
        @NotBlank(message = "{email.obligatorio}")//estos mensajes de validacion se configuran en ValidationMessages.properties de la carpeta resources
        @Email(message = "{email.invalido}")//estos mensajes de validacion se configuran en ValidationMessages.properties de la carpeta resources
        String email,
        @NotBlank(message = "Teléfono es obligatorio")//ese mensaje de validacion no necesita un properties en resources ya que se configura aqui
        String telefono,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String documento,
        @NotNull(message = "{especialidad.obligatorio}")//estos mensajes de validacion se configuran en ValidationMessages.properties de la carpeta resources
        Especialidad especialidad,
        @NotNull(message = "Datos de dirección son obligatorios")
        @Valid
        DatosDireccion direccion) {
}
