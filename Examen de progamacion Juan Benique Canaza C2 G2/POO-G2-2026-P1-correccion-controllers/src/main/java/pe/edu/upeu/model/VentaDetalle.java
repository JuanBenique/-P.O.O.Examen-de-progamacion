package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalle {
    private String idVentaDetalle;
    private Venta idVenta;
    private Producto idProducto;
    private Double precioUni;
    private Double porceUtil;
    private Integer cantidad;
    private Double netoTotal;
}
