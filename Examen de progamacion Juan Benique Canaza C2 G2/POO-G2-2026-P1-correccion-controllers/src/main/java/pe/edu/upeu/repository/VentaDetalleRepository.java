package pe.edu.upeu.repository;

import pe.edu.upeu.model.Producto;
import pe.edu.upeu.model.Venta;
import pe.edu.upeu.model.VentaDetalle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VentaDetalleRepository extends AbstractJpaRepository<VentaDetalle, String> {

    @Override
    protected String getTableName() { return "ventadetalle"; }

    @Override
    protected String getPkColumn() { return "idVentaDetalle"; }

    public List<VentaDetalle> findByIdVenta(String idVenta) {
        return executeQuery(
                "SELECT vd.*, p.nombre AS prod_nombre FROM ventadetalle vd " +
                        "JOIN producto p ON vd.idProducto = p.idProducto " +
                        "WHERE vd.idVenta = ?", idVenta
        );
    }

    @Override
    protected VentaDetalle insert(Connection conn, VentaDetalle vd) throws SQLException {
        executeUpdate(conn,
                "INSERT INTO ventadetalle(idVentaDetalle, idVenta, idProducto, precioUni, porceUtil, cantidad, netoTotal) VALUES (?,?,?,?,?,?,?)",
                vd.getIdVentaDetalle(),
                vd.getIdVenta().getIdVenta(),
                vd.getIdProducto().getIdProducto(),
                vd.getPrecioUni(),
                vd.getPorceUtil(),
                vd.getCantidad(),
                vd.getNetoTotal()
        );
        return vd;
    }

    @Override
    protected VentaDetalle updateRow(Connection conn, VentaDetalle vd) throws SQLException {
        executeUpdate(conn,
                "UPDATE ventadetalle SET idVenta=?, idProducto=?, precioUni=?, porceUtil=?, cantidad=?, netoTotal=? WHERE idVentaDetalle=?",
                vd.getIdVenta().getIdVenta(),
                vd.getIdProducto().getIdProducto(),
                vd.getPrecioUni(),
                vd.getPorceUtil(),
                vd.getCantidad(),
                vd.getNetoTotal(),
                vd.getIdVentaDetalle()
        );
        return vd;
    }

    @Override
    protected VentaDetalle mapRow(ResultSet rs) throws SQLException {
        return VentaDetalle.builder()
                .idVentaDetalle(rs.getString("idVentaDetalle"))
                .idVenta(Venta.builder()
                        .idVenta(rs.getString("idVenta"))
                        .build())
                .idProducto(Producto.builder()
                        .idProducto(rs.getString("idProducto"))
                        .nombre(rs.getString("prod_nombre"))
                        .build())
                .precioUni(rs.getDouble("precioUni"))
                .porceUtil(rs.getDouble("porceUtil"))
                .cantidad(rs.getInt("cantidad"))
                .netoTotal(rs.getDouble("netoTotal"))
                .build();
    }
}
