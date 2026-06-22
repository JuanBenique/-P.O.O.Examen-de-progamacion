package pe.edu.upeu.repository;

import pe.edu.upeu.model.Cliente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteRepository extends AbstractJpaRepository<Cliente, String> {

    @Override
    protected String getTableName() { return "cliente"; }

    @Override
    protected String getPkColumn() { return "dni"; }

    @Override
    protected Cliente insert(Connection conn, Cliente c) throws SQLException {
        executeUpdate(conn,
                "INSERT INTO cliente(dni, nombre, direccion, celular) VALUES (?,?,?,?)",
                c.getDni(),
                c.getNombre(),
                c.getDireccion(),
                c.getCelular()
        );
        return c;
    }

    @Override
    protected Cliente updateRow(Connection conn, Cliente c) throws SQLException {
        executeUpdate(conn,
                "UPDATE cliente SET nombre=?, direccion=?, celular=? WHERE dni=?",
                c.getNombre(),
                c.getDireccion(),
                c.getCelular(),
                c.getDni()
        );
        return c;
    }

    @Override
    protected Cliente mapRow(ResultSet rs) throws SQLException {
        return Cliente.builder()
                .dni(rs.getString("dni"))
                .nombre(rs.getString("nombre"))
                .direccion(rs.getString("direccion"))
                .celular(rs.getString("celular"))
                .build();
    }
}
