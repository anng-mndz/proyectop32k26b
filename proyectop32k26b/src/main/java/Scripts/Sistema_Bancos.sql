--Karina Alejandra Arriaza Ortiz 9959-24-14190
CREATE DATABASE SistemaBancos;
USE SistemaBancos;

====================================================
-- TABLAS MAESTRAS
====================================================

-- Catálogo de tipos de cuenta
CREATE TABLE Cat_TipoCuenta (
    id_tipo_cuenta INT PRIMARY KEY AUTO_INCREMENT,
    nombre_tipo VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(150)
);

-- Catálogo de tipos de transacción
CREATE TABLE Cat_TipoTransaccion (
    id_tipo_transaccion INT PRIMARY KEY AUTO_INCREMENT,
    nombre_tipo VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(150)
);

-- Catálogo de estados de conciliación
CREATE TABLE Cat_EstadoConciliacion (
    id_estado INT PRIMARY KEY AUTO_INCREMENT,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE
);

====================================================
-- TABLA MAESTRA DE BANCOS
====================================================

CREATE TABLE Banco (
    id_banco INT PRIMARY KEY AUTO_INCREMENT,
    nombre_banco VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

====================================================
-- TABLA MAESTRA DE CLIENTES / PROPIETARIOS
====================================================

CREATE TABLE Cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(150) NOT NULL,
    dpi_nit VARCHAR(30) UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    correo VARCHAR(100)
);

====================================================
-- TABLA TRANSACCIONAL DE CUENTAS BANCARIAS
====================================================

CREATE TABLE Cuenta_Bancaria (
    id_cuenta INT PRIMARY KEY AUTO_INCREMENT,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    saldo_actual DECIMAL(12,2) DEFAULT 0.00,
    fecha_apertura DATE NOT NULL,
    
    id_banco INT NOT NULL,
    id_cliente INT NOT NULL,
    id_tipo_cuenta INT NOT NULL,

    FOREIGN KEY (id_banco) REFERENCES Banco(id_banco),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_tipo_cuenta) REFERENCES Cat_TipoCuenta(id_tipo_cuenta)
);

====================================================
-- TABLA TRANSACCIONAL DE MOVIMIENTOS BANCARIOS
====================================================

CREATE TABLE Movimiento_Bancario (
    id_movimiento INT PRIMARY KEY AUTO_INCREMENT,
    
    fecha_movimiento DATETIME DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(12,2) NOT NULL,
    descripcion VARCHAR(255),

    id_cuenta INT NOT NULL,
    id_tipo_transaccion INT NOT NULL,

    FOREIGN KEY (id_cuenta) REFERENCES Cuenta_Bancaria(id_cuenta),
    FOREIGN KEY (id_tipo_transaccion) REFERENCES Cat_TipoTransaccion(id_tipo_transaccion)
);

====================================================
-- TABLA TRANSACCIONAL DE CONCILIACIONES
====================================================

CREATE TABLE Conciliacion_Bancaria (
    id_conciliacion INT PRIMARY KEY AUTO_INCREMENT,

    fecha_conciliacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    saldo_sistema DECIMAL(12,2) NOT NULL,
    saldo_banco DECIMAL(12,2) NOT NULL,
    diferencia DECIMAL(12,2) NOT NULL,

    id_cuenta INT NOT NULL,
    id_estado INT NOT NULL,

    FOREIGN KEY (id_cuenta) REFERENCES Cuenta_Bancaria(id_cuenta),
    FOREIGN KEY (id_estado) REFERENCES Cat_EstadoConciliacion(id_estado)
);

====================================================
-- TABLA DE BITÁCORA / AUDITORÍA
====================================================

CREATE TABLE Bitacora_Bancaria (
    id_bitacora INT PRIMARY KEY AUTO_INCREMENT,

    usuario_accion VARCHAR(100),
    accion_realizada VARCHAR(200),
    tabla_afectada VARCHAR(100),

    fecha_accion DATETIME DEFAULT CURRENT_TIMESTAMP
);