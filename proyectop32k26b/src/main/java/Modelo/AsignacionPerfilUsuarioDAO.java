/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


import java.sql.*;
import java.util.ArrayList; 
import java.util.List;

/**
 * @author Maria Celeste Mayen Ibarra 9959-23-3775
 */
public class AsignacionPerfilUsuarioDAO {

    // Aqui abri la conexion para traer toda la lista de perfiles desdde la DB
    private static final String SQL_SELECT = "SELECT UsuId, Percodigo FROM asignacionperfilusuario";
    private static final String SQL_INSERT = "INSERT INTO asignacionperfilusuario (UsuId, Percodigo) VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM asignacionperfilusuario WHERE UsuId = ? AND Percodigo = ?";
    private static final String SQL_QUERY = "SELECT UsuId, Percodigo FROM asignacionperfilusuario WHERE UsuId = ? AND Percodigo = ?";

    public List<AsignacionPerfilUsuarioDAO> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AsignacionPerfilUsuarioDAO> asignaciones = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Aquí se llenarían los objetos del controlador
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return asignaciones;
    }

    public int insertar(int UsuId, int Percodigo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, UsuId);
            stmt.setInt(2, Percodigo);
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    public int eliminar(int UsuId, int Percodigo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, UsuId);
            stmt.setInt(2, Percodigo);
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }
    
    // Método de búsqueda específico para la tabla 
    public boolean buscar(int UsuId, int Percodigo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_QUERY);
            stmt.setInt(1, UsuId);
            stmt.setInt(2, Percodigo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return false;
    }
}