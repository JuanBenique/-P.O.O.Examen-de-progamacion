package pe.edu.upeu.repository;

import pe.edu.upeu.repository.helper.SqlHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractJpaRepository<T,ID> extends SqlHelper<T> implements ICrudGenericoRepository<T,ID>{

    /** Nombre de la tabla en BD. Ej: "upeu_categoria" */
    protected abstract String getTableName();
    /** Nombre de la columna PK. Ej: "id_categoria" */
    protected abstract String getPkColumn();

    /** Inserta una fila nueva y devuelve la entidad con su PK asignada. */
    protected abstract T insert(Connection conn, T entity) throws SQLException;
    /** Actualiza una fila existente y devuelve la entidad. */
    protected abstract T updateRow(Connection conn, T entity) throws SQLException;

    @Override
    public T save(T entity) {
        try( Connection conn = openConnection() ) {
            conn.setAutoCommit(false);
                try {
                    T resultado=insert(conn, entity);
                    conn.commit();
                    return resultado;
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar: "+e.getMessage(), e);
        }
    }

    @Override
    public T update(T entity) {
        try( Connection conn = openConnection() ) {
            conn.setAutoCommit(false);
            try {
                T resultado=updateRow(conn, entity);
                conn.commit();
                return resultado;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar: "+e.getMessage(), e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        String sql = "SELECT * FROM "+getTableName()+" WHERE "+getPkColumn()+" = ?";
        return executeQueryOne(sql, id);
    }

    @Override
    public List<T> findAll() {
        String sql = "SELECT * FROM "+getTableName();
        return executeQuery(sql);
    }

    @Override
    public void deleteById(ID id) {
        String sql = "DELETE FROM "+getTableName()+" WHERE "+getPkColumn()+" = ?";
        executeUpdateStandalone(sql, id);
    }

    @Override
    public boolean existsById(ID id) {
        String sql = "SELECT 1 FROM "+getTableName()+" WHERE "+getPkColumn()+" = ?";
        return executeExists(sql, id);
    }

}
