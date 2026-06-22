package pe.edu.upeu.repository;

import pe.edu.upeu.model.Categoria;
import pe.edu.upeu.model.Producto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoRepository extends AbstractJpaRepository<Producto, String> {

    @Override
    protected String getTableName() { return "producto"; }

    @Override
    protected String getPkColumn() { return "idProducto"; }

    private static final String SELECT_JOIN =
            "SELECT p.*, c.nombre AS cat_nombre " +
                    "FROM producto p JOIN categoria c ON p.idCateg = c.idCateg ";

    @Override
    public java.util.List<Producto> findAll() {
        return executeQuery(SELECT_JOIN);
    }

    @Override
    public java.util.Optional<Producto> findById(String id) {
        return executeQueryOne(SELECT_JOIN + "WHERE p.idProducto = ?", id);
    }

    @Override
    protected Producto insert(Connection conn, Producto p) throws SQLException {
        executeUpdate(conn,
                "INSERT INTO producto(idProducto, nombre, preUnitario, porceUtil, talla, color, stock, idCateg) VALUES (?,?,?,?,?,?,?,?)",
                p.getIdProducto(),
                p.getNombre(),
                p.getPreUnitario(),
                p.getPorceUtil(),
                p.getTalla(),
                p.getColor(),
                p.getStock(),
                p.getIdCateg().getIdCateg()
        );
        return p;
    }

    @Override
    protected Producto updateRow(Connection conn, Producto p) throws SQLException {
        executeUpdate(conn,
                "UPDATE producto SET nombre=?, preUnitario=?, porceUtil=?, talla=?, color=?, stock=?, idCateg=? WHERE idProducto=?",
                p.getNombre(),
                p.getPreUnitario(),
                p.getPorceUtil(),
                p.getTalla(),
                p.getColor(),
                p.getStock(),
                p.getIdCateg().getIdCateg(),
                p.getIdProducto()
        );
        return p;
    }

    @Override
    protected Producto mapRow(ResultSet rs) throws SQLException {
        return Producto.builder()
                .idProducto(rs.getString("idProducto"))
                .nombre(rs.getString("nombre"))
                .preUnitario(rs.getDouble("preUnitario"))
                .porceUtil(rs.getDouble("porceUtil"))
                .talla(rs.getString("talla"))
                .color(rs.getString("color"))
                .stock(rs.getInt("stock"))
                .idCateg(Categoria.builder()
                        .idCateg(rs.getString("idCateg"))
                        .nombre(rs.getString("cat_nombre"))
                        .build())
                .build();
    }
}