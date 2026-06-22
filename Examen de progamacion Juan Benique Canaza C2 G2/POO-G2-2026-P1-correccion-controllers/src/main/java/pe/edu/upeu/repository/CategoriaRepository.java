package pe.edu.upeu.repository;

import pe.edu.upeu.model.Categoria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaRepository extends AbstractJpaRepository<Categoria, String> {

    @Override
    protected String getTableName() { return "categoria"; }

    @Override
    protected String getPkColumn() { return "idCateg"; }

    @Override
    protected Categoria insert(Connection conn, Categoria c) throws SQLException {
        executeUpdate(conn,
                "INSERT INTO categoria(idCateg, nombre) VALUES (?,?)",
                c.getIdCateg(),
                c.getNombre()
        );
        return c;
    }

    @Override
    protected Categoria updateRow(Connection conn, Categoria c) throws SQLException {
        executeUpdate(conn,
                "UPDATE categoria SET nombre=? WHERE idCateg=?",
                c.getNombre(),
                c.getIdCateg()
        );
        return c;
    }

    @Override
    protected Categoria mapRow(ResultSet rs) throws SQLException {
        return Categoria.builder()
                .idCateg(rs.getString("idCateg"))
                .nombre(rs.getString("nombre"))
                .build();
    }
}