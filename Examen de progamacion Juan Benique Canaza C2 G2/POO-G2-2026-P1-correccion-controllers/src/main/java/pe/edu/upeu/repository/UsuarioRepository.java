package pe.edu.upeu.repository;

import pe.edu.upeu.model.Usuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository extends AbstractJpaRepository<Usuario, String> {

    @Override
    protected String getTableName() {
        return "upeu_usuario";
    }

    @Override
    protected String getPkColumn() {
        return "id_usuario";
    }

    private static final String SELECT_SIMPLE =
            "SELECT u.* FROM upeu_usuario u ";

    @Override
    public List<Usuario> findAll() {
        return executeQuery(SELECT_SIMPLE);
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return executeQueryOne(SELECT_SIMPLE + " WHERE u.id_usuario = ?", id);
    }

    public Optional<Usuario> findByUsuarioAndClave(String usuario, String clave) {
        return executeQueryOne(
                SELECT_SIMPLE + " WHERE u.usuario = ? AND u.clave = ?",
                usuario,
                clave
        );
    }

    @Override
    protected Usuario insert(Connection conn, Usuario u) throws SQLException {
        long id = executeInsertGetKey(conn,
                "INSERT INTO upeu_usuario(usuario,clave,perfil,estado) VALUES (?,?,?,?)",
                u.getUsuario(),
                u.getClave(),
                u.getPerfil(),
                u.getEstado()
        );
        u.setIdUsuario(String.valueOf(id));
        return u;
    }

    @Override
    protected Usuario updateRow(Connection conn, Usuario u) throws SQLException {
        executeUpdate(conn,
                "UPDATE upeu_usuario SET usuario=?,clave=?,perfil=?,estado=? WHERE id_usuario=?",
                u.getUsuario(),
                u.getClave(),
                u.getPerfil(),
                u.getEstado(),
                u.getIdUsuario()
        );
        return u;
    }

    @Override
    protected Usuario mapRow(ResultSet rs) throws SQLException {
        return Usuario.builder()
                .idUsuario(rs.getString("id_usuario"))
                .usuario(rs.getString("usuario"))
                .clave(rs.getString("clave"))
                .perfil(rs.getString("perfil"))
                .estado(rs.getString("estado"))
                .build();
    }
}