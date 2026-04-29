-- ============================================================
-- SCRIPT UNIFICADO SIG - ORDEN CORRECTO DE DEPENDENCIAS
-- ============================================================
-- Módulos: CC (Kevin), Bancos (Karina), Planilla (Meilyn),
--          Logística (Marcos), Ventas/Compras (Roli),
--          Comisiones Vendedores (Dulce)
-- ============================================================


USE sig;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET FOREIGN_KEY_CHECKS = 0; 
SET time_zone = "+00:00";

-- ============================================================
-- BLOQUE 1: CATÁLOGOS (sin dependencias)
-- ============================================================

CREATE TABLE IF NOT EXISTS `CattipoCuenta` (
  `TCidcuenta`   INT         NOT NULL AUTO_INCREMENT,
  `TCnombretipo` VARCHAR(50) NOT NULL UNIQUE,
  `TCdescripcion` VARCHAR(150),
  PRIMARY KEY (`TCidcuenta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Cattipotransaccion` (
  `TTid`          INT         NOT NULL AUTO_INCREMENT,
  `TTnombretipo`  VARCHAR(50) NOT NULL UNIQUE,
  `TTdescripcion` VARCHAR(150),
  PRIMARY KEY (`TTid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Catestadoconciliacion` (
  `Catesid`           INT         NOT NULL AUTO_INCREMENT,
  `Catesnombreestado` VARCHAR(50) NOT NULL UNIQUE,
  PRIMARY KEY (`Catesid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 2: TABLAS MAESTRAS INDEPENDIENTES
-- ============================================================

-- Bancos
CREATE TABLE IF NOT EXISTS `Banco` (
  `Banid`            INT          NOT NULL AUTO_INCREMENT,
  `Bannombre`        VARCHAR(100) NOT NULL,
  `Bandireccion`     VARCHAR(200),
  `Bantelefono`      VARCHAR(20),
  `Bancorreo`        VARCHAR(100),
  `Banfecharegistro` DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Banid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Planilla
CREATE TABLE IF NOT EXISTS departamentos (
  Depcodigo INT AUTO_INCREMENT PRIMARY KEY,
  Depnombre VARCHAR(50) NOT NULL,
  Depestado TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS puestos (
  Puecodigo      INT AUTO_INCREMENT PRIMARY KEY,
  Puenombre      VARCHAR(50)    NOT NULL,
  Puesalario_base DECIMAL(10,2) NOT NULL,
  Depcodigo      INT            NOT NULL,
  FOREIGN KEY (Depcodigo) REFERENCES departamentos(Depcodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS empleados (
  Empcodigo       INT AUTO_INCREMENT PRIMARY KEY,
  Empnombre       VARCHAR(100) NOT NULL,
  Empdpi          VARCHAR(20)  UNIQUE,
  Puecodigo       INT          NOT NULL,
  Empfecha_ingreso DATE,
  Empestado       TINYINT(1)   DEFAULT 1,
  FOREIGN KEY (Puecodigo) REFERENCES puestos(Puecodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CC: Maestros
CREATE TABLE IF NOT EXISTS `proveedores` (
  `Procodigo`       int(11)      NOT NULL AUTO_INCREMENT,
  `Pronombre`       varchar(100) NOT NULL,
  `Pronit`          varchar(20)  NOT NULL,
  `Procuentabancaria` varchar(50),
  `Proestado`       varchar(20)  DEFAULT NULL,
  `Procontacto`     varchar(100) DEFAULT NULL,
  `Prodepartamento` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Procodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `acreedores` (
  `Acrecodigo`       int(11)      NOT NULL AUTO_INCREMENT,
  `Acrenombre`       varchar(100) NOT NULL,
  `Acrenit`          varchar(20)  NOT NULL,
  `Acrecuentabancaria` varchar(50),
  `Acreestado`       char(1)      NOT NULL,
  PRIMARY KEY (`Acrecodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `clientes` (
  `Cliid`       int(11)      NOT NULL AUTO_INCREMENT,
  `Clinombre`   varchar(100) NOT NULL,
  `Clinit`      varchar(20)  NOT NULL,
  `Cliestado`   char(1)      NOT NULL,
  `Clitelefono` varchar(20)  DEFAULT NULL,
  `Clidireccion` varchar(200) NOT NULL,
  `Clicorreo`   varchar(100) NOT NULL,
  PRIMARY KEY (`Cliid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Ventas: Vendedores
CREATE TABLE `vendedores` (
  `Venid`       int(11)      NOT NULL AUTO_INCREMENT,
  `Empcodigo`   int(11)      DEFAULT NULL,
  `Vennombre`   varchar(100) DEFAULT NULL,
  `Ventelefono` varchar(20)  DEFAULT NULL,
  `Vendireccion` varchar(200) DEFAULT NULL,
  `Vencorreo`   varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Venid`),
  KEY `Empcodigo` (`Empcodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 3: BANCOS (depende de clientes, CattipoCuenta, Banco)
-- ============================================================

CREATE TABLE IF NOT EXISTS `CuentaBancaria` (
  `CBANid`            INT           NOT NULL AUTO_INCREMENT,
  `CBANnumerocuenta`  VARCHAR(50)   NOT NULL UNIQUE,
  `CBANsaldoactual`   DECIMAL(12,2) DEFAULT 0.00,
  `CBANfechaapertura` DATE          NOT NULL,
  `Banid`             INT           NOT NULL,
  `Cliid`             INT           NOT NULL,
  `TCidcuenta`        INT           NOT NULL,
  PRIMARY KEY (`CBANid`),
  FOREIGN KEY (`Banid`)      REFERENCES `Banco`(`Banid`),
  FOREIGN KEY (`Cliid`)      REFERENCES `clientes`(`Cliid`),
  FOREIGN KEY (`TCidcuenta`) REFERENCES `CattipoCuenta`(`TCidcuenta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `movimientobancario` (
  `Movbid`              INT           NOT NULL AUTO_INCREMENT,
  `Movbfechamovimiento` DATETIME      DEFAULT CURRENT_TIMESTAMP,
  `Movbmonto`           DECIMAL(12,2) NOT NULL,
  `Movdescripcion`      VARCHAR(255),
  `CBANid`              INT           NOT NULL,
  `TTid`                INT           NOT NULL,
  `Movbtipomov`         VARCHAR(20)   NOT NULL,
  `Movbreferencia`      VARCHAR(50),
  `Movbconciliado`      CHAR(1)       DEFAULT 'N',
  PRIMARY KEY (`Movbid`),
  FOREIGN KEY (`CBANid`) REFERENCES `CuentaBancaria`(`CBANid`),
  FOREIGN KEY (`TTid`)   REFERENCES `Cattipotransaccion`(`TTid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ConciliacionBancaria` (
  `Conbid`           INT           NOT NULL AUTO_INCREMENT,
  `conbfecha`        DATETIME      DEFAULT CURRENT_TIMESTAMP,
  `Conbsaldosistema` DECIMAL(12,2) NOT NULL,
  `Conbsaldobanco`   DECIMAL(12,2) NOT NULL,
  `Conbdiferencia`   DECIMAL(12,2) NOT NULL,
  `CBANid`           INT           NOT NULL,
  `Catesid`          INT           NOT NULL,
  PRIMARY KEY (`Conbid`),
  FOREIGN KEY (`CBANid`)   REFERENCES `CuentaBancaria`(`CBANid`),
  FOREIGN KEY (`Catesid`)  REFERENCES `Catestadoconciliacion`(`Catesid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- BitacoraBancaria: FK a usuario comentada hasta que llegue módulo seguridad
CREATE TABLE IF NOT EXISTS `BitacoraBancaria` (
  `BBid`            INT          NOT NULL AUTO_INCREMENT,
  `BBusuarioaccion` INT          NOT NULL,
  `BBaccion`        VARCHAR(50)  NOT NULL,
  `BBtabla`         VARCHAR(100) NOT NULL,
  `BBregistroid`    INT          DEFAULT NULL,
  `BBvaloranterior` TEXT,
  `BBvalornuevo`    TEXT,
  `BBfechaaccion`   DATETIME     DEFAULT CURRENT_TIMESTAMP,
  `BBdescripcion`   VARCHAR(255),
  PRIMARY KEY (`BBid`)
  -- FOREIGN KEY (`BBusuarioaccion`) REFERENCES `usuario`(`Usucodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 4: CC TRANSACCIONALES
-- (dependen de proveedores, acreedores, clientes,
--  vendedores, Cattipotransaccion, movimientobancario)
-- ============================================================

CREATE TABLE IF NOT EXISTS `cuentasporpagar` (
  `Cppcodigo`        int(11)       NOT NULL AUTO_INCREMENT,
  `Procodigo`        int(11)       DEFAULT NULL,
  `Acrecodigo`       int(11)       DEFAULT NULL,
  `Venid`            int(11)       DEFAULT NULL,
  `Cppfechaemision`  date          NOT NULL,
  `Cppmontototal`    decimal(12,2) NOT NULL,
  `Cppsaldopendiente` decimal(12,2) NOT NULL,
  `Cppestado`        char(1)       NOT NULL,
  `TTid`             int(11)       NOT NULL,
  `Cpporigenid`      int(11)       NULL,
  PRIMARY KEY (`Cppcodigo`),
  FOREIGN KEY (`Procodigo`)  REFERENCES `proveedores`(`Procodigo`),
  FOREIGN KEY (`Acrecodigo`) REFERENCES `acreedores`(`Acrecodigo`),
  FOREIGN KEY (`Venid`)      REFERENCES `vendedores`(`Venid`),
  FOREIGN KEY (`TTid`)       REFERENCES `Cattipotransaccion`(`TTid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `cuentasporcobrar` (
  `Cpccodigo` int(11)       NOT NULL AUTO_INCREMENT,
  `Cliid`     int(11)       NOT NULL,
  `Cpcfecha`  date          NOT NULL,
  `Cpcmonto`  decimal(12,2) NOT NULL,
  `Cpcsaldo`  decimal(12,2) NOT NULL,
  `Cpcestado` char(1)       NOT NULL,
  PRIMARY KEY (`Cpccodigo`),
  FOREIGN KEY (`Cliid`) REFERENCES `clientes`(`Cliid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `pagosemision` (
  `Pagemid`   int(11)       NOT NULL AUTO_INCREMENT,
  `Cppcodigo` int(11)       NOT NULL,
  `Movbid`    int(11)       NOT NULL,
  `Pagefecha` datetime      NOT NULL,
  `Pagemonto` decimal(12,2) NOT NULL,
  `Pagetipo`  varchar(20),
  PRIMARY KEY (`Pagemid`),
  FOREIGN KEY (`Cppcodigo`) REFERENCES `cuentasporpagar`(`Cppcodigo`),
  FOREIGN KEY (`Movbid`)    REFERENCES `movimientobancario`(`Movbid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `cobrosemision` (
  `Cobemid`   int(11)       NOT NULL AUTO_INCREMENT,
  `Cpccodigo` int(11)       NOT NULL,
  `Movbid`    int(11)       NOT NULL,
  `Cobfecha`  datetime      NOT NULL,
  `Cobmonto`  decimal(12,2) NOT NULL,
  `Cobtipo`   varchar(20),
  PRIMARY KEY (`Cobemid`),
  FOREIGN KEY (`Cpccodigo`) REFERENCES `cuentasporcobrar`(`Cpccodigo`),
  FOREIGN KEY (`Movbid`)    REFERENCES `movimientobancario`(`Movbid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 5: PLANILLA (depende de empleados, movimientobancario)
-- ============================================================

CREATE TABLE IF NOT EXISTS conceptosplanilla (
  Concodigo   INT AUTO_INCREMENT PRIMARY KEY,
  Connombre   VARCHAR(50) NOT NULL,
  Contipo     ENUM('PERCEPCION','DEDUCCION') NOT NULL,
  Conporcentaje DECIMAL(5,2),
  Conmonto    DECIMAL(10,2),
  Conaplica   ENUM('TODOS','INDIVIDUAL','EXCEPCION') DEFAULT 'TODOS',
  Conestado   TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS planillaencabezado (
  Placodigo INT AUTO_INCREMENT PRIMARY KEY,
  Plafecha  DATE          NOT NULL,
  Platotal  DECIMAL(10,2) DEFAULT 0,
  Plaestado TINYINT(1)    DEFAULT 1,
  Movbid    INT           NULL,
  FOREIGN KEY (Movbid) REFERENCES movimientobancario(Movbid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS planilladetalle (
  Detcodigo            INT AUTO_INCREMENT PRIMARY KEY,
  Placodigo            INT           NOT NULL,
  Empcodigo            INT           NOT NULL,
  Detsalario           DECIMAL(10,2) NOT NULL,
  Dettotalpercepciones DECIMAL(10,2) DEFAULT 0,
  Dettotaldeducciones  DECIMAL(10,2) DEFAULT 0,
  Detliquido           DECIMAL(10,2),
  FOREIGN KEY (Placodigo) REFERENCES planillaencabezado(Placodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (Empcodigo) REFERENCES empleados(Empcodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS detalleconceptosplanilla (
  Detconcodigo INT AUTO_INCREMENT PRIMARY KEY,
  Detcodigo    INT           NOT NULL,
  Concodigo    INT           NOT NULL,
  Monto        DECIMAL(10,2) NOT NULL,
  UNIQUE (Detcodigo, Concodigo),
  FOREIGN KEY (Detcodigo) REFERENCES planilladetalle(Detcodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (Concodigo) REFERENCES conceptosplanilla(Concodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS conceptosexcepcion (
  Exccodigo INT AUTO_INCREMENT PRIMARY KEY,
  Concodigo INT NOT NULL,
  Empcodigo INT NOT NULL,
  UNIQUE (Concodigo, Empcodigo),
  FOREIGN KEY (Concodigo) REFERENCES conceptosplanilla(Concodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (Empcodigo) REFERENCES empleados(Empcodigo)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 6: LOGÍSTICA (depende de proveedores, clientes,
--           empleados, productos, bodegas)
-- ============================================================

CREATE TABLE IF NOT EXISTS `bodegas` (
  `bodegaid`    int(11)      NOT NULL AUTO_INCREMENT,
  `Bodnombre`   varchar(100) DEFAULT NULL,
  `Bodubicacion` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`bodegaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `productos` (
  `Prodid`          int(11)       NOT NULL AUTO_INCREMENT,
  `Prodnombre`      varchar(100)  DEFAULT NULL,
  `Prodstockactual` int(11)       DEFAULT NULL,
  `Prodpuntoreorden` int(11)      DEFAULT NULL,
  `Prodprecioventa` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`Prodid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `transportistas` (
  `Tranid`          int(11)     NOT NULL AUTO_INCREMENT,
  `Trantipovehiculo` varchar(50) DEFAULT NULL,
  `Empcodigo`       int(11)     NOT NULL,
  PRIMARY KEY (`Tranid`),
  KEY `fk_transportista_empleado` (`Empcodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `arribosmercancia` (
  `Arriboid`              int(11)     NOT NULL AUTO_INCREMENT,
  `Procodigo`             int(11)     DEFAULT NULL,
  `Arrfechaarribo`        datetime    DEFAULT NULL,
  `Arrestadoverificacion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Arriboid`),
  KEY `Procodigo` (`Procodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `detallearribo` (
  `Detallearriboid`              int(11)       NOT NULL AUTO_INCREMENT,
  `Arriboid`                     int(11)       DEFAULT NULL,
  `Prodid`                       int(11)       DEFAULT NULL,
  `Detarribocantidad`            int(11)       NOT NULL,
  `Detarribopreciounitariocompra` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Detallearriboid`),
  KEY `Arriboid` (`Arriboid`),
  KEY `Prodid` (`Prodid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `pedidos` (
  `Pedid`     int(11)     NOT NULL AUTO_INCREMENT,
  `Cliid`     int(11)     DEFAULT NULL,
  `Pedfecha`  datetime    DEFAULT current_timestamp(),
  `Pedestado` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Pedid`),
  KEY `Cliid` (`Cliid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `detallepedidos` (
  `Detallepedidoid`       int(11) NOT NULL AUTO_INCREMENT,
  `Pedid`                 int(11) DEFAULT NULL,
  `Prodid`                int(11) DEFAULT NULL,
  `Detallepedidocantidad` int(11) NOT NULL,
  PRIMARY KEY (`Detallepedidoid`),
  KEY `Pedid` (`Pedid`),
  KEY `Prodid` (`Prodid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `envios` (
  `Envid`         int(11)      NOT NULL AUTO_INCREMENT,
  `Pedid`         int(11)      DEFAULT NULL,
  `Tranid`        int(11)      DEFAULT NULL,
  `Envfechasalida` datetime    DEFAULT NULL,
  `Envnumeroguia` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Envid`),
  KEY `Pedid` (`Pedid`),
  KEY `Tranid` (`Tranid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `existencias` (
  `Existenciaid` int(11) NOT NULL AUTO_INCREMENT,
  `Prodid`       int(11) NOT NULL,
  `bodegaid`     int(11) NOT NULL,
  `Existock`     int(11) DEFAULT 0,
  PRIMARY KEY (`Existenciaid`),
  UNIQUE KEY `uniqueproductobodega` (`Prodid`,`bodegaid`),
  KEY `bodegaid` (`bodegaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `movimientosinventario` (
  `Movimientoid`      int(11)   NOT NULL AUTO_INCREMENT,
  `Prodid`            int(11)   NOT NULL,
  `bodegaid`          int(11)   NOT NULL,
  `Movtipomovimiento` enum('entrada','salida') NOT NULL,
  `Movmotivo`         enum('compra','venta','merma','ajuste','devolucion') NOT NULL,
  `Movcantidad`       int(11)   NOT NULL,
  `Movfecha`          datetime  DEFAULT current_timestamp(),
  `Movtiporeferencia` enum('pedido','arribo','ajuste','manual','merma') DEFAULT NULL,
  `Movreferenciaid`   int(11)   DEFAULT NULL,
  `Movobservacion`    text      DEFAULT NULL,
  PRIMARY KEY (`Movimientoid`),
  KEY `Prodid` (`Prodid`),
  KEY `bodegaid` (`bodegaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 7: VENTAS/COMPRAS
-- (depende de clientes, vendedores, proveedores, productos)
-- ============================================================

CREATE TABLE IF NOT EXISTS `ordencompra` (
  `Ordid`     int(11)       NOT NULL AUTO_INCREMENT,
  `Procodigo` int(11)       DEFAULT NULL,
  `Ordfecha`  date          DEFAULT NULL,
  `Ordtotal`  decimal(10,2) DEFAULT NULL,
  `Ordestado` varchar(20)   DEFAULT NULL,
  PRIMARY KEY (`Ordid`),
  KEY `Procodigo` (`Procodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `detalleordencompra` (
  `Detordid`     int(11)       NOT NULL AUTO_INCREMENT,
  `Ordid`        int(11)       DEFAULT NULL,
  `Prodid`       int(11)       DEFAULT NULL,
  `Cantcompra`   int(11)       DEFAULT NULL,
  `Costounitario` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Detordid`),
  KEY `Prodid` (`Prodid`),
  KEY `Ordid` (`Ordid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `facturasventa` (
  `Facid`    int(11)       NOT NULL AUTO_INCREMENT,
  `Cliid`    int(11)       DEFAULT NULL,
  `Venid`    int(11)       DEFAULT NULL,
  `Facfecha` date          DEFAULT NULL,
  `Factotal` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Facid`),
  KEY `Cliid` (`Cliid`),
  KEY `Venid` (`Venid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `facturaventadetalle` (
  `Detfacid`      int(11)       NOT NULL AUTO_INCREMENT,
  `Facid`         int(11)       DEFAULT NULL,
  `Prodid`        int(11)       DEFAULT NULL,
  `Cantidad`      int(11)       DEFAULT NULL,
  `Preciounitario` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Detfacid`),
  KEY `Facid` (`Facid`),
  KEY `Prodid` (`Prodid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 8: COMISIONES Y REPORTES
-- (dependen de vendedores, cuentasporpagar, clientes)
-- ============================================================

CREATE TABLE IF NOT EXISTS `comisionesvendedores` (
  `Comid`               int(11)       NOT NULL AUTO_INCREMENT,
  `Venid`               int(11)       NOT NULL,
  `Commontoventas`      decimal(10,2) NOT NULL,
  `Commeta`             decimal(10,2) NULL,
  `Commarca`            varchar(100)  NULL,
  `Comventasadicionales` decimal(10,2) NULL,
  `Comcomision`         decimal(10,2) NOT NULL,
  `Cppcodigo`           int(11)       NULL,
  PRIMARY KEY (`Comid`),
  FOREIGN KEY (`Venid`)     REFERENCES `vendedores`(`Venid`),
  FOREIGN KEY (`Cppcodigo`) REFERENCES `cuentasporpagar`(`Cppcodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `reportes` (
  `Repid`               int(11)       NOT NULL AUTO_INCREMENT,
  `Repfecha`            date          NOT NULL,
  `Rephora`             time          NOT NULL,
  `Cliid`               int(11)       NOT NULL,
  `Venid`               int(11)       NOT NULL,
  `Repmontoventas`      decimal(10,2) NOT NULL,
  `Repmeta`             decimal(10,2) NOT NULL,
  `Repmarca`            varchar(100)  NULL,
  `Repventasadicionales` decimal(10,2) NOT NULL,
  `Repcomision`         decimal(10,2) NOT NULL,
  `Repdescripcion`      varchar(255)  NULL,
  PRIMARY KEY (`Repid`),
  FOREIGN KEY (`Cliid`) REFERENCES `clientes`(`Cliid`),
  FOREIGN KEY (`Venid`) REFERENCES `vendedores`(`Venid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BLOQUE 9: FKs ENTRE MÓDULOS (al final cuando todo existe)
-- ============================================================

ALTER TABLE `vendedores`
  ADD CONSTRAINT `vendedores_ibfk_1`
    FOREIGN KEY (`Empcodigo`) REFERENCES `empleados`(`Empcodigo`);

ALTER TABLE `transportistas`
  ADD CONSTRAINT `fk_transportista_empleado`
    FOREIGN KEY (`Empcodigo`) REFERENCES `empleados`(`Empcodigo`);

ALTER TABLE `arribosmercancia`
  ADD CONSTRAINT `arribosmercancia_fk`
    FOREIGN KEY (`Procodigo`) REFERENCES `proveedores`(`Procodigo`);

ALTER TABLE `detallearribo`
  ADD CONSTRAINT `fk_detallearribo_arribo`
    FOREIGN KEY (`Arriboid`) REFERENCES `arribosmercancia`(`Arriboid`),
  ADD CONSTRAINT `fk_detallearribo_producto`
    FOREIGN KEY (`Prodid`) REFERENCES `productos`(`Prodid`);

ALTER TABLE `detallepedidos`
  ADD CONSTRAINT `fk_detallepedidos_pedido`
    FOREIGN KEY (`Pedid`) REFERENCES `pedidos`(`Pedid`),
  ADD CONSTRAINT `fk_detallepedidos_producto`
    FOREIGN KEY (`Prodid`) REFERENCES `productos`(`Prodid`);

ALTER TABLE `envios`
  ADD CONSTRAINT `fk_envios_pedido`
    FOREIGN KEY (`Pedid`) REFERENCES `pedidos`(`Pedid`),
  ADD CONSTRAINT `fk_envios_transportista`
    FOREIGN KEY (`Tranid`) REFERENCES `transportistas`(`Tranid`);

ALTER TABLE `existencias`
  ADD CONSTRAINT `fk_existencias_bodega`
    FOREIGN KEY (`bodegaid`) REFERENCES `bodegas`(`bodegaid`),
  ADD CONSTRAINT `fk_existencias_producto`
    FOREIGN KEY (`Prodid`) REFERENCES `productos`(`Prodid`);

ALTER TABLE `movimientosinventario`
  ADD CONSTRAINT `fk_movimientosinventario_bodega`
    FOREIGN KEY (`bodegaid`) REFERENCES `bodegas`(`bodegaid`),
  ADD CONSTRAINT `fk_movimientosinventario_producto`
    FOREIGN KEY (`Prodid`) REFERENCES `productos`(`Prodid`);

ALTER TABLE `pedidos`
  ADD CONSTRAINT `fk_pedidos_cliente`
    FOREIGN KEY (`Cliid`) REFERENCES `clientes`(`Cliid`);

ALTER TABLE `ordencompra`
  ADD CONSTRAINT `ordencompra_ibfk_1`
    FOREIGN KEY (`Procodigo`) REFERENCES `proveedores`(`Procodigo`);

ALTER TABLE `detalleordencompra`
  ADD CONSTRAINT `detalleordencompra_ibfk_1`
    FOREIGN KEY (`Prodid`) REFERENCES `productos`(`Prodid`),
  ADD CONSTRAINT `detalleordencompra_ibfk_2`
    FOREIGN KEY (`Ordid`) REFERENCES `ordencompra`(`Ordid`);

ALTER TABLE `facturasventa`
  ADD CONSTRAINT `facturasventa_ibfk_1`
    FOREIGN KEY (`Cliid`) REFERENCES `clientes`(`Cliid`),
  ADD CONSTRAINT `facturasventa_ibfk_2`
    FOREIGN KEY (`Venid`) REFERENCES `vendedores`(`Venid`);

ALTER TABLE `facturaventadetalle`
  ADD CONSTRAINT `facturaventadetalle_ibfk_1`
    FOREIGN KEY (`Facid`) REFERENCES `facturasventa`(`Facid`),
  ADD CONSTRAINT `facturaventadetalle_ibfk_2`
    FOREIGN KEY (`Prodid`) REFERENCES `productos`(`Prodid`);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- BLOQUE 10: DATOS INICIALES
-- ============================================================

INSERT INTO `CattipoCuenta` (`TCnombretipo`, `TCdescripcion`) VALUES
  ('Monetaria', 'Cuenta de uso diario'),
  ('Ahorro',    'Cuenta de ahorro personal');

INSERT INTO `Cattipotransaccion` (`TTnombretipo`, `TTdescripcion`) VALUES
  ('Deposito',      'Ingreso de dinero'),
  ('Retiro',        'Salida de dinero'),
  ('Transferencia', 'Movimiento entre cuentas'),
  ('Pago',          'Pago realizado'),
  ('Cobro',         'Cobro recibido'),
  ('PLANILLA',      'Egreso por pago de nomina'),
  ('COMISION',      'Pago de comision a vendedor'),
  ('PROV',          'Pago a proveedor o acreedor');

INSERT INTO `Catestadoconciliacion` (`Catesnombreestado`) VALUES
  ('Conciliado'),
  ('Pendiente'),
  ('Con Diferencia');

INSERT INTO `proveedores` (`Pronombre`, `Pronit`, `Procuentabancaria`, `Proestado`) VALUES
  ('Distribuidora XYZ', '1234567-8', '1234-56789', 'A');

INSERT INTO `acreedores` (`Acrenombre`, `Acrenit`, `Acrecuentabancaria`, `Acreestado`) VALUES
  ('Arrendadora Central', '9988776-5', '9988-77665', 'A');

INSERT INTO `clientes` (`Clinombre`, `Clinit`, `Cliestado`, `Clitelefono`, `Clidireccion`, `Clicorreo`) VALUES
  ('Comercial El Sol', '3344556-7', 'A', '55512345', 'Zona 1, Ciudad de Guatemala', 'comercialelsol@gmail.com');

COMMIT;