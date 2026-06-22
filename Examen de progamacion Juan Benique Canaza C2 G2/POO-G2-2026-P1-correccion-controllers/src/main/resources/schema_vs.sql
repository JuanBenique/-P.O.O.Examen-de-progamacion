CREATE TABLE IF NOT EXISTS upeu_usuario (
                                            id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                                            usuario VARCHAR(50) UNIQUE NOT NULL,
    clave VARCHAR(50) NOT NULL,
    perfil VARCHAR(50),
    estado VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS categoria (
                                         idCateg VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS producto (
                                        idProducto VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    preUnitario DOUBLE,
    porceUtil DOUBLE,
    talla VARCHAR(20),
    color VARCHAR(50),
    stock INT,
    idCateg VARCHAR(20),
    FOREIGN KEY (idCateg) REFERENCES categoria(idCateg)
    );

CREATE TABLE IF NOT EXISTS cliente (
                                       dni VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(200),
    celular VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS venta (
                                     idVenta VARCHAR(20) PRIMARY KEY,
    dniCliente VARCHAR(15),
    fechaVenta DATE,
    netoTotal DOUBLE,
    igv DOUBLE,
    precioTotal DOUBLE,
    idUsuario INT,
    FOREIGN KEY (dniCliente) REFERENCES cliente(dni),
    FOREIGN KEY (idUsuario) REFERENCES upeu_usuario(id_usuario)
    );

CREATE TABLE IF NOT EXISTS ventadetalle (
                                            idVentaDetalle VARCHAR(20) PRIMARY KEY,
    idVenta VARCHAR(20),
    idProducto VARCHAR(20),
    precioUni DOUBLE,
    porceUtil DOUBLE,
    cantidad INT,
    netoTotal DOUBLE,
    FOREIGN KEY (idVenta) REFERENCES venta(idVenta),
    FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
    );

-- Insertar usuario inicial
INSERT INTO upeu_usuario (usuario, clave, perfil, estado)
VALUES ('juan123', 'juan123456', 'Administrador', 'Activo');

-- Insertar categorías por defecto
INSERT INTO categoria (idCateg, nombre) VALUES ('C01', 'Polos Cuello V');
INSERT INTO categoria (idCateg, nombre) VALUES ('C02', 'Polos Manga Larga');
