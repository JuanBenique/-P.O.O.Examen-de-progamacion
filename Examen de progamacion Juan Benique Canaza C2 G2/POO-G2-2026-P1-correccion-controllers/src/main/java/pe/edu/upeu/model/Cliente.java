package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    private String dni;
    private String nombre;
    private String direccion;
    private String celular;
}
