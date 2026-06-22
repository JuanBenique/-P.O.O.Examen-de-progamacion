package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    private String idUsuario;
    private String usuario;
    private String clave;
    private String perfil;
    private String estado;
}