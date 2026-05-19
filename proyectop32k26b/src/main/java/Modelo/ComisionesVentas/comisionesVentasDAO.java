/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.ComisionesVentas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Controlador.ComisionesVentas.clsComisionVentas;
import Modelo.Conexion;
/**
 *
 * @author giron
 */
public class comisionesVentasDAO {


// Método corregido para buscar directamente en la tabla Vendedores
public String obtenerNombreEmpleado(int idEmpleado) {
    String nombre = "";
    // CORRECCIÓN: Buscamos en Vendedores usando la columna Venid que ya usamos en los otros métodos
    String sql = "SELECT Vennombre FROM Vendedores WHERE Venid = ?";
    
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
         
        ps.setInt(1, idEmpleado);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            nombre = rs.getString("Vennombre"); // Captura el nombre real (ej: Angel Roquel)
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener nombre del vendedor: " + e.getMessage());
    }
    return nombre;
}


// Método único que obtiene comisiones de vendedores, usado cuando se selecciona el radio button de vendedores
public List<clsComisionVentas> obtenerDatosComisiones(int idEmpleado, String tipofiltro, int idFiltro) {
    // Inicializa una lista vacía para almacenar los registros recuperados
    List<clsComisionVentas> lista = new ArrayList<>();
    // Base del SQL con todos los JOINs necesarios
    String sql = "SELECT cv.Comid, cv.Venid, cv.Commontoventas, cv.Commeta, cv.Comventasadicionales, cv.Comcomision, " +
                 "v.Vennombre, p.Prodnombre, m.marnombre, l.linnombre, l.lincomision, cp.Cppcodigo " +
                 "FROM ComisionesVendedores cv " +
                 "LEFT JOIN Vendedores v ON cv.Venid = v.Venid " +
                 "LEFT JOIN productos p ON cv.Venid = p.Prodid " +
                 "LEFT JOIN marcas m ON p.Prodid = m.marcaid " +
                 "LEFT JOIN lineas l ON m.marcaid = l.lineaid " +
                 "LEFT JOIN CuentasPorPagar cp ON cv.Venid = cp.Cppcodigo " +
                 "WHERE cv.Venid = ?";
    // Agrega el filtro adicional al SQL según el tipo seleccionado
    if (tipofiltro.equals("linea"))    sql += " AND l.lineaid = ?";
    if (tipofiltro.equals("marca"))    sql += " AND m.marcaid = ?";
    if (tipofiltro.equals("producto")) sql += " AND p.Prodid = ?";
    // Abre la conexión y prepara la consulta con recursos de cierre automático
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        // Asigna el ID del empleado como primer parámetro siempre
        ps.setInt(1, idEmpleado);
        // Si hay filtro adicional, asigna el ID correspondiente como segundo parámetro
        if (!tipofiltro.equals("vendedor")) ps.setInt(2, idFiltro);
        ResultSet rs = ps.executeQuery();
        // Recorre cada fila devuelta por la base de datos
        while (rs.next()) {
            clsComisionVentas obj = new clsComisionVentas();
            obj.setId_comision(rs.getInt("Comid"));
            obj.setVenid(rs.getInt("Venid"));
            obj.setId_empleado(rs.getInt("Venid"));
            obj.setMonto_ventas(rs.getDouble("Commontoventas"));
            obj.setMeta(rs.getDouble("Commeta"));
            obj.setVentas_adicionales(rs.getDouble("Comventasadicionales"));
            obj.setComision(rs.getDouble("Comcomision"));
            obj.setVennombre(rs.getString("Vennombre"));
            obj.setProdnombre(rs.getString("Prodnombre"));
            obj.setMarnombre(rs.getString("marnombre"));
            obj.setLinnombre(rs.getString("linnombre"));
            obj.setLincomision(rs.getDouble("lincomision"));
            // Extrae el código de cuentas por pagar obtenido mediante el JOIN con la tabla CuentasPorPagar
            obj.setCppcodigo(rs.getString("Cppcodigo"));
            lista.add(obj);
        }
    } catch (SQLException e) {
        System.out.println("Error en el DAO al obtener datos completos: " + e.getMessage());
    }
    return lista;
}


// Método para obtener todos los vendedores y cargarlos en el combobox
public List<clsComisionVentas> obtenerTodosVendedores() {
    // Inicializa una lista vacía para almacenar los vendedores
    List<clsComisionVentas> lista = new ArrayList<>();
    // Consulta todos los vendedores registrados en la tabla
    String sql = "SELECT Venid, Vennombre FROM Vendedores";
    // Abre la conexión y prepara la consulta con recursos de cierre automático
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        // Recorre cada vendedor devuelto por la base de datos
        while (rs.next()) {
            clsComisionVentas obj = new clsComisionVentas();
            // Obtiene el ID y nombre del vendedor
            obj.setVenid(rs.getInt("Venid"));
            obj.setVennombre(rs.getString("Vennombre"));
            lista.add(obj);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener todos los vendedores: " + e.getMessage());
    }
    return lista;
}
public List<clsComisionVentas> obtenerHistorialCompleto() {
    List<clsComisionVentas> lista = new ArrayList<>();
    // Trae todas las comisiones calculadas junto al nombre de su vendedor
    String sql = "SELECT cv.Comid, cv.Venid, cv.Commontoventas, cv.Commeta, cv.Comventasadicionales, cv.Comcomision, cv.Commarca, v.Vennombre " +
                 "FROM comisionesvendedores cv " +
                 "INNER JOIN Vendedores v ON cv.Venid = v.Venid " +
                 "ORDER BY cv.Comid DESC"; // Ordena mostrando lo más nuevo al principio
                 
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            clsComisionVentas obj = new clsComisionVentas();
            obj.setId_comision(rs.getInt("Comid"));
            obj.setVenid(rs.getInt("Venid"));
            obj.setMonto_ventas(rs.getDouble("Commontoventas"));
            obj.setMeta(rs.getDouble("Commeta"));
            obj.setVentas_adicionales(rs.getDouble("Comventasadicionales"));
            obj.setComision(rs.getDouble("Comcomision"));
            obj.setVennombre(rs.getString("Vennombre"));
            
            // Evaluamos la marca de la BD, si viene nula o vacía ponemos "General"
            String marca = rs.getString("Commarca");
            obj.setMarnombre((marca == null || marca.trim().isEmpty()) ? "General" : marca);
            
            obj.setLinnombre("No aplica");
            obj.setProdnombre("No aplica");
            obj.setCppcodigo("No aplica");
            
            lista.add(obj);
        }
    } catch (SQLException e) {
        System.out.println("Error en el DAO al obtener historial: " + e.getMessage());
    }
    return lista;
}


// 1. Método para cargar las ventas brutas del vendedor a la Vista Previa
public List<clsComisionVentas> obtenerDatosPorVendedor(int idEmpleado) {
    List<clsComisionVentas> lista = new ArrayList<>();
    String sql = "SELECT cv.Comid, cv.Venid, cv.Commontoventas, cv.Commeta, cv.Comventasadicionales, cv.Comcomision, v.Vennombre " +
                 "FROM comisionesvendedores cv " +
                 "INNER JOIN Vendedores v ON cv.Venid = v.Venid " +
                 "WHERE cv.Venid = ?";
                 
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idEmpleado);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            clsComisionVentas obj = new clsComisionVentas();
            obj.setId_comision(rs.getInt("Comid"));
            obj.setVenid(rs.getInt("Venid"));
            obj.setId_empleado(rs.getInt("Venid"));
            obj.setMonto_ventas(rs.getDouble("Commontoventas"));
            obj.setMeta(rs.getDouble("Commeta"));
            obj.setVentas_adicionales(rs.getDouble("Comventasadicionales"));
            obj.setComision(rs.getDouble("Comcomision"));
            obj.setVennombre(rs.getString("Vennombre"));
            
            // Valores por defecto para mantener limpia la estructura
            obj.setProdnombre("No aplica");
            obj.setMarnombre("No aplica");
            obj.setLinnombre("No aplica");
            obj.setLincomision(0.0);
            obj.setCppcodigo("No aplica");
            
            lista.add(obj);
        }
    } catch (SQLException e) {
        System.out.println("Error en el DAO al obtener datos: " + e.getMessage());
    }
    return lista;
}

// 2. Método profesional para insertar permanentemente el cálculo final en MySQL
public int registrarComision(int idEmpleado, double ventas, double meta, String nombreMarca, double adicionales, double comision) {
    Connection conn = null;
    PreparedStatement stmt = null;
    int rows = 0;
    
    String sql = "INSERT INTO comisionesvendedores (Venid, Commontoventas, Commeta, Commarca, Comventasadicionales, Comcomision, Cppcodigo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try {
        conn = Conexion.getConnection();
        stmt = conn.prepareStatement(sql);
        
        stmt.setInt(1, idEmpleado);
        stmt.setDouble(2, ventas);
        stmt.setDouble(3, meta);
        stmt.setString(4, nombreMarca);          // Guarda "General" en Commarca (Varchar)
        stmt.setDouble(5, adicionales);
        stmt.setDouble(6, comision);
        stmt.setNull(7, java.sql.Types.INTEGER); // Guarda NULL en Cppcodigo de forma segura
        
        System.out.println("Ejecutando registro en BD...");
        rows = stmt.executeUpdate();
        
    } catch (SQLException ex) {
        javax.swing.JOptionPane.showMessageDialog(null, "Error SQL Real: " + ex.getMessage());
        ex.printStackTrace();
    } finally {
        Conexion.close(stmt);
        Conexion.close(conn);
    }
    return rows;
}

// Método para obtener información directamente de la tabla marcas por su ID
public List<clsComisionVentas> obtenerDatosPorMarca(int idEmpleado, int idMarca) {
    List<clsComisionVentas> lista = new ArrayList<>();
    
    // Consulta SQL que busca los artículos vendidos por el empleado que pertenezcan a la marca filtrada
    String sql = "SELECT cv.Comid, cv.Venid, cv.Commeta, cv.Comventasadicionales, " +
                 "v.Vennombre, m.marnombre, m.marcomision, " +
                 "SUM(cv.Commontoventas) AS VentaEspecificaMarca " + // Sumamos solo lo vendido de esta marca
                 "FROM comisionesvendedores cv " +
                 "INNER JOIN Vendedores v ON cv.Venid = v.Venid " +
                 "INNER JOIN productos p ON cv.Venid = p.prodid " + // Enlace a tus productos
                 "INNER JOIN marcas m ON p.marcaid = m.marcaid " +
                 "WHERE cv.Venid = ? AND m.marcaid = ? " +
                 "GROUP BY cv.Comid, cv.Venid, v.Vennombre, m.marnombre, m.marcomision";
                 
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
         
        ps.setInt(1, idEmpleado);
        ps.setInt(2, idMarca);
        
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            clsComisionVentas obj = new clsComisionVentas();
            
            obj.setId_comision(rs.getInt("Comid"));
            obj.setVenid(rs.getInt("Venid"));
            obj.setVennombre(rs.getString("Vennombre"));
            obj.setMeta(rs.getDouble("Commeta"));
            obj.setVentas_adicionales(rs.getDouble("Comventasadicionales"));
            
            // ASIGNACIÓN CLAVE: En monto_ventas guardamos SOLO lo vendido de esa marca
            obj.setMonto_ventas(rs.getDouble("VentaEspecificaMarca")); 
            
            obj.setMarnombre(rs.getString("marnombre"));    
            obj.setMarcomision(rs.getDouble("marcomision")); 
            
            obj.setLinnombre("No aplica");
            obj.setProdnombre("No aplica");
            obj.setComision(0.0);
            obj.setCppcodigo("No aplica");
            
            lista.add(obj);
        }
    } catch (SQLException e) {
        System.out.println("Error en el DAO por marca: " + e.getMessage());
    }
    return lista;
}

// 1. Buscar Producto Maestro por su ID
public clsComisionVentas buscarProductoMaestro(int idProducto) {
    clsComisionVentas obj = null;
    
    // CORRECCIÓN: Cambiamos p.procomision por p.prodcomision (o el nombre exacto que tenga en tu phpMyAdmin)
    // También nos aseguramos de que el ID sea p.prodid en lugar de p.prodid si daba error
    String sql = "SELECT p.prodid, p.prodnombre, p.prodcomision, m.marnombre, l.linnombre " +
                 "FROM productos p " +
                 "LEFT JOIN marcas m ON p.marcaid = m.marcaid " +
                 "LEFT JOIN lineas l ON p.lineaid = l.lineaid " +
                 "WHERE p.prodid = ?";
                 
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idProducto);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                obj = new clsComisionVentas();
                obj.setProid(rs.getInt("prodid"));
                obj.setProdnombre(rs.getString("prodnombre"));
                
                // Mapeo corregido hacia tu clase entidad
                obj.setProcomision(rs.getDouble("prodcomision")); 
                obj.setMarnombre(rs.getString("marnombre"));
                obj.setLinnombre(rs.getString("linnombre"));
                obj.setLincomision(0.0);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "MySQL ejecutó el Query, pero el ID " + idProducto + " NO existe en la tabla productos.");
            }
        }
    } catch (SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(null, "Error de Sintaxis SQL Real: " + e.getMessage());
        e.printStackTrace();
    }
    return obj;
}

// 2. Buscar Marca Maestra por su ID
public clsComisionVentas buscarMarcaMaestra(int idMarca) {
    clsComisionVentas obj = null;
    String sql = "SELECT marcaid, marnombre, marcomision FROM marcas WHERE marcaid = ?";
    try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idMarca);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            obj = new clsComisionVentas();
            obj.setMarnombre(rs.getString("marnombre"));
            obj.setMarcomision(rs.getDouble("marcomision")); 
        }
    } catch (SQLException e) { System.out.println("Error Marca: " + e.getMessage()); }
    return obj;
}

// 3. Buscar Línea Maestra por su ID
public clsComisionVentas buscarLineaMaestra(int idLinea) {
    clsComisionVentas obj = null;
    String sql = "SELECT lineaid, linnombre, lincomision FROM lineas WHERE lineaid = ?";
    try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idLinea);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            obj = new clsComisionVentas();
            obj.setLinnombre(rs.getString("linnombre"));
            obj.setLincomision(rs.getDouble("lincomision")); 
        }
    } catch (SQLException e) { System.out.println("Error Línea: " + e.getMessage()); }
    return obj;
}

// Método para obtener información directamente de la tabla lineas por su ID
public List<clsComisionVentas> obtenerDatosPorLinea(int idEmpleado, int idLinea) {
    // Inicializa una lista vacía para almacenar los registros recuperados
    List<clsComisionVentas> lista = new ArrayList<>();
    // Consulta directo la tabla lineas sin depender de ComisionesVendedores
    String sql = "SELECT v.Venid, v.Vennombre, l.linnombre, l.lincomision " +
                 "FROM lineas l " +
                 "JOIN Vendedores v ON v.Venid = ? " +
                 "WHERE l.lineaid = ?";
    // Abre la conexión y prepara la consulta con recursos de cierre automático
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        // Asigna el ID del empleado como primer parámetro
        ps.setInt(1, idEmpleado);
        // Asigna el ID de la línea como segundo parámetro
        ps.setInt(2, idLinea);
        ResultSet rs = ps.executeQuery();
        // Recorre cada fila devuelta por la base de datos
        while (rs.next()) {
            clsComisionVentas obj = new clsComisionVentas();
            // Obtiene el ID y nombre del vendedor
            obj.setVenid(rs.getInt("Venid"));
            obj.setVennombre(rs.getString("Vennombre"));
            // Obtiene el nombre de la línea y su comisión
            obj.setLinnombre(rs.getString("linnombre"));
            obj.setLincomision(rs.getDouble("lincomision"));
            lista.add(obj);
        }
    } catch (SQLException e) {
        System.out.println("Error en el DAO al obtener datos por línea: " + e.getMessage());
    }
    return lista;
}

// Método para obtener información directamente de la tabla productos por su ID
public List<clsComisionVentas> obtenerDatosPorProducto(int idEmpleado, int idProducto) {
    // Inicializa una lista vacía para almacenar los registros recuperados
    List<clsComisionVentas> lista = new ArrayList<>();
    // Consulta directo la tabla productos sin depender de ComisionesVendedores
    String sql = "SELECT v.Venid, v.Vennombre, p.Prodnombre, p.prodcomision " +
                 "FROM productos p " +
                 "JOIN Vendedores v ON v.Venid = ? " +
                 "WHERE p.Prodid = ?";
    // Abre la conexión y prepara la consulta con recursos de cierre automático
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        // Asigna el ID del empleado como primer parámetro
        ps.setInt(1, idEmpleado);
        // Asigna el ID del producto como segundo parámetro
        ps.setInt(2, idProducto);
        ResultSet rs = ps.executeQuery();
        // Recorre cada fila devuelta por la base de datos
        while (rs.next()) {
            clsComisionVentas obj = new clsComisionVentas();
            // Obtiene el ID y nombre del vendedor
            obj.setVenid(rs.getInt("Venid"));
            obj.setVennombre(rs.getString("Vennombre"));
            // Obtiene el nombre del producto y su comisión
            obj.setProdnombre(rs.getString("Prodnombre"));
            obj.setProdprecioventa(rs.getDouble("prodcomision"));
            lista.add(obj);
        }
    } catch (SQLException e) {
        System.out.println("Error en el DAO al obtener datos por producto: " + e.getMessage());
    }
    return lista;
}
}

