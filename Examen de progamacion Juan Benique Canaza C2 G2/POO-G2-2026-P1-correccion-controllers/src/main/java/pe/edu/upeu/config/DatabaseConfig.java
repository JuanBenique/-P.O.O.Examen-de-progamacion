package pe.edu.upeu.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private static HikariDataSource dataSource;

    private DatabaseConfig() {}

    public static synchronized void init() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }
        try {
            // 1. Configuración de la base de datos H2 embebida (URL CORREGIDA)
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:file:./sysventas_db");
            config.setUsername("sa");
            config.setPassword("");
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(5);

            dataSource = new HikariDataSource(config);

            // 2. Creación automática de tablas y datos
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {

                String sql =
                        "CREATE TABLE IF NOT EXISTS upeu_usuario (" +
                                "id_usuario INT AUTO_INCREMENT PRIMARY KEY, " +
                                "usuario VARCHAR(50) UNIQUE NOT NULL, " +
                                "clave VARCHAR(50) NOT NULL, " +
                                "perfil VARCHAR(50), " +
                                "estado VARCHAR(20)); " +

                                "CREATE TABLE IF NOT EXISTS categoria (" +
                                "idCateg VARCHAR(20) PRIMARY KEY, " +
                                "nombre VARCHAR(100) NOT NULL); " +

                                "CREATE TABLE IF NOT EXISTS producto (" +
                                "idProducto VARCHAR(20) PRIMARY KEY, " +
                                "nombre VARCHAR(100) NOT NULL, " +
                                "preUnitario DOUBLE, " +
                                "porceUtil DOUBLE, " +
                                "talla VARCHAR(20), " +
                                "color VARCHAR(50), " +
                                "stock INT, " +
                                "idCateg VARCHAR(20), " +
                                "FOREIGN KEY (idCateg) REFERENCES categoria(idCateg)); " +

                                "CREATE TABLE IF NOT EXISTS cliente (" +
                                "dni VARCHAR(15) PRIMARY KEY, " +
                                "nombre VARCHAR(150) NOT NULL, " +
                                "direccion VARCHAR(200), " +
                                "celular VARCHAR(20)); " +

                                "CREATE TABLE IF NOT EXISTS venta (" +
                                "idVenta VARCHAR(20) PRIMARY KEY, " +
                                "dniCliente VARCHAR(15), " +
                                "fechaVenta DATE, " +
                                "netoTotal DOUBLE, " +
                                "igv DOUBLE, " +
                                "precioTotal DOUBLE, " +
                                "idUsuario INT, " +
                                "FOREIGN KEY (dniCliente) REFERENCES cliente(dni), " +
                                "FOREIGN KEY (idUsuario) REFERENCES upeu_usuario(id_usuario)); " +

                                "CREATE TABLE IF NOT EXISTS ventadetalle (" +
                                "idVentaDetalle VARCHAR(20) PRIMARY KEY, " +
                                "idVenta VARCHAR(20), " +
                                "idProducto VARCHAR(20), " +
                                "precioUni DOUBLE, " +
                                "porceUtil DOUBLE, " +
                                "cantidad INT, " +
                                "netoTotal DOUBLE, " +
                                "FOREIGN KEY (idVenta) REFERENCES venta(idVenta), " +
                                "FOREIGN KEY (idProducto) REFERENCES producto(idProducto)); " +

                                // Insertar tu usuario juan123 si no existe
                                "INSERT INTO upeu_usuario (usuario, clave, perfil, estado) " +
                                "SELECT 'juan123', 'juan123456', 'Administrador', 'Activo' " +
                                "WHERE NOT EXISTS (SELECT 1 FROM upeu_usuario WHERE usuario = 'juan123'); " +

                                // Insertar categorías por defecto si no existen
                                "INSERT INTO categoria (idCateg, nombre) " +
                                "SELECT 'C01', 'Polos Cuello V' WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE idCateg = 'C01'); " +
                                "INSERT INTO categoria (idCateg, nombre) " +
                                "SELECT 'C02', 'Polos Manga Larga' WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE idCateg = 'C02');";

                stmt.execute(sql);
                System.out.println("Base de datos y tablas creadas con éxito.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error inicializando base de datos", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            init();
        }
        return dataSource.getConnection();
    }
}