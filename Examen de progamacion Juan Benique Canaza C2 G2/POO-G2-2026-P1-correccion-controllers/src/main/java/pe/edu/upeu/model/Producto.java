package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    private String idProducto;
    private String nombre;
    private Double preUnitario;
    private Double porceUtil;
    private String talla;
    private String color;
    private Integer stock;
    private Categoria idCateg;
}
