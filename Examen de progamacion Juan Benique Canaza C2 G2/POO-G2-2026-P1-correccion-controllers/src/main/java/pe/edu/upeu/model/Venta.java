package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
    private String idVenta;
    private Cliente dniCliente;
    private LocalDate fechaVenta;
    private Double netoTotal;
    private Double igv;
    private Double precioTotal;
    private Usuario idUsuario;
    private List<VentaDetalle> detalleVenta;
}
