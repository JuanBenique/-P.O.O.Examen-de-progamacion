package pe.edu.upeu.repository;

import pe.edu.upeu.model.Cliente;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.model.Venta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentaRepository extends AbstractJpaRepository<Venta, String> {

    @Override
    protected String getTableName() { return "venta"; }

    @Override
    protected String getPkColumn() { return "idVenta"; }

    private static final String SELECT_JOIN =
            "SELECT v.*, c.nombre AS cli_nombre, c.direccion, c.celular, " +
                    "u.usuario, u.perfil, u.estado " +
                    "FROM venta v " +
                    "JOIN cliente c ON v.dniCliente = c.dni " +
                    "JOIN upeu_usuario u ON v.idUsuario = u.id_usuario ";

    @Override
    public java.util.List<Venta> findAll() {
        return executeQuery(SELECT_JOIN);
    }

    @Override
    public java.util.Optional<Venta> findById(String id) {
        return executeQueryOne(SELECT_JOIN + "WHERE v.idVenta = ?", id);
    }

    @Override
    protected Venta insert(Connection conn, Venta v) throws SQLException {
        executeUpdate(conn,
                "INSERT INTO venta(idVenta, dniCliente, fechaVenta, netoTotal, igv, precioTotal, idUsuario) VALUES (?,?,?,?,?,?,?)",
                v.getIdVenta(),
                v.getDniCliente().getDni(),
                v.getFechaVenta(),
                v.getNetoTotal(),
                v.getIgv(),
                v.getPrecioTotal(),
                v.getIdUsuario().getIdUsuario()
        );
        return v;
    }

    @Override
    protected Venta updateRow(Connection conn, Venta v) throws SQLException {
        executeUpdate(conn,
                "UPDATE venta SET dniCliente=?, fechaVenta=?, netoTotal=?, igv=?, precioTotal=?, idUsuario=? WHERE idVenta=?",
                v.getDniCliente().getDni(),
                v.getFechaVenta(),
                v.getNetoTotal(),
                v.getIgv(),
                v.getPrecioTotal(),
                v.getIdUsuario().getIdUsuario(),
                v.getIdVenta()
        );
        return v;
    }

    @Override
    protected Venta mapRow(ResultSet rs) throws SQLException {
        return Venta.builder()
                .idVenta(rs.getString("idVenta"))
                .fechaVenta(rs.getDate("fechaVenta").toLocalDate())
                .netoTotal(rs.getDouble("netoTotal"))
                .igv(rs.getDouble("igv"))
                .precioTotal(rs.getDouble("precioTotal"))
                .dniCliente(Cliente.builder()
                        .dni(rs.getString("dniCliente"))
                        .nombre(rs.getString("cli_nombre"))
                        .direccion(rs.getString("direccion"))
                        .celular(rs.getString("celular"))
                        .build())
                .idUsuario(Usuario.builder()
                        .idUsuario(rs.getString("idUsuario"))
                        .usuario(rs.getString("usuario"))
                        .perfil(rs.getString("perfil"))
                        .estado(rs.getString("estado"))
                        .build())
                .build();
    }
}