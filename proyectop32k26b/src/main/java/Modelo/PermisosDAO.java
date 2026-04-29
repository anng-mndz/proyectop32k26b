/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author Angel R
 * @author Astrid R
 */

//Codigo realizado por Angel roquel para los metodos generales. 
//Inician los permisos generales 
//Astrid hizo correcciones de: bug SQL faltaba FROM y separar las fuentes (perfil y usuario)
//nombre campo usuid es UsuId en la bd

public class PermisosDAO {
     // 1. Busca el perfil del usuario
public List<Integer> obtenerAplicacionesPermitidas(int usuId) {
   System.out.println("->Intentando buscar permisos para el ID Usuario: " + usuId);

        // Set para evitar duplicados al combinar las dos fuentes
        Set<Integer> appsPermitidas = new HashSet<>();

        // Fuente 1: apps permitidas por el perfil asignado al usuario
        int perfilAsignado = buscarPerfilDeUsuario(usuId);
        System.out.println("-> Perfil encontrado (-1 = ninguno): " + perfilAsignado);
        if (perfilAsignado != -1) {
            appsPermitidas.addAll(obtenerAppsPorPerfil(perfilAsignado));
        }

        // Fuente 2: apps asignadas directamente al usuario
        appsPermitidas.addAll(obtenerAppsPorUsuarioDirecto(usuId));

        System.out.println("-> Total apps permitidas: " + appsPermitidas.size());
        return new ArrayList<>(appsPermitidas);
    }

 
    public int buscarPerfilDeUsuario(int usuId) {
        String sql = "SELECT Percodigo FROM asignacionperfilusuario WHERE UsuId = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Percodigo");
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return -1;
    }

    // 2. Carga aplicaciones asignadas al perfil
    public List<Integer> obtenerAppsPorPerfil(int perCodigo) {
        List<Integer> listaApps = new ArrayList<>();
        String sql = "SELECT Aplcodigo FROM asignacionaplicacionperfil WHERE Percodigo = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, perCodigo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listaApps.add(rs.getInt("Aplcodigo"));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return listaApps;
    }

    // 3. Carga aplicaciones directas (AsignacionAplicacionUsuario)
    public List<Integer> obtenerAppsPorUsuarioDirecto(int usuId) {
        List<Integer> listaApps = new ArrayList<>();
        String sql = "SELECT Aplcodigo FROM asignacionaplicacionusuarios WHERE UsuId = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listaApps.add(rs.getInt("Aplcodigo"));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return listaApps;
    }

 // Metodos especificos para el crud realizados por Astrid Ruíz 
    // Todos combinan perfil + usuario directo (OR).
    //   10=Mant.Usuarios, 11=Mant.Aplicaciones, 10007=Mant.Perfil
    //   10010=AsigPerfilUsuario, 10011=AsigAplicacionPerfil, 10005=Bitacora

    /**
     * ¿Puede INSERTAR en este módulo?
     * Habilita btnRegistrar / btnInsertar
     *  El método revisa las dos fuentes de permisos:
    * 1. Permisos heredados desde un PERFIL
    *    (tabla asignacionaplicacionperfil)

    * 2. Permisos asignados DIRECTAMENTE al usuario
    *    (tabla asignacionaplicacionusuarios)

    * Se utiliza OR: Si cualquiera de las dos fuentes tiene valor "1", entonces el usuario sí puede insertar.
     */
    public boolean puedeInsertar(int usuId, int aplCodigo) {
        // Fuente 1:  Verificar permisos heredados desde el PERFIL
        int perfilAsignado = buscarPerfilDeUsuario(usuId); //buscar si el usuario tiene un perfil asignado
        if (perfilAsignado != -1) { //SI tiene perfil
            String sql = "SELECT APLPins FROM asignacionaplicacionperfil " +
                         "WHERE Aplcodigo = ? AND Percodigo = ?";
            try (Connection conn = Conexion.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, aplCodigo);
                stmt.setInt(2, perfilAsignado);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && "1".equals(rs.getString("APLPins"))) return true;
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
        // Fuente 2:Verificar permisos DIRECTOS del usuario
        String sql = "SELECT APLUins FROM asignacionaplicacionusuarios " +
                     "WHERE Aplcodigo = ? AND UsuId = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aplCodigo);
            stmt.setInt(2, usuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && "1".equals(rs.getString("APLUins"))) return true;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /**
     * ¿Puede BUSCAR en este módulo?
     * Habilita btnBuscar
     */
    public boolean puedeBuscar(int usuId, int aplCodigo) {
        // Fuente 1: desde el perfil
        int perfilAsignado = buscarPerfilDeUsuario(usuId);
        if (perfilAsignado != -1) {
            String sql = "SELECT APLPsel FROM asignacionaplicacionperfil " +
                         "WHERE Aplcodigo = ? AND Percodigo = ?";
            try (Connection conn = Conexion.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, aplCodigo);
                stmt.setInt(2, perfilAsignado);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && "1".equals(rs.getString("APLPsel"))) return true;
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
        // Fuente 2: directo al usuario
        String sql = "SELECT APLUsel FROM asignacionaplicacionusuarios " +
                     "WHERE Aplcodigo = ? AND UsuId = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aplCodigo);
            stmt.setInt(2, usuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && "1".equals(rs.getString("APLUsel"))) return true;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /**
     * ¿Puede MODIFICAR en este módulo?
     * Habilita btnModificar
     */
    public boolean puedeModificar(int usuId, int aplCodigo) {
        // Fuente 1: desde el perfil
        int perfilAsignado = buscarPerfilDeUsuario(usuId);
        if (perfilAsignado != -1) {
            String sql = "SELECT APLPupd FROM asignacionaplicacionperfil " +
                         "WHERE Aplcodigo = ? AND Percodigo = ?";
            try (Connection conn = Conexion.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, aplCodigo);
                stmt.setInt(2, perfilAsignado);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && "1".equals(rs.getString("APLPupd"))) return true;
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
        // Fuente 2: directo al usuario
        String sql = "SELECT APLUupd FROM asignacionaplicacionusuarios " +
                     "WHERE Aplcodigo = ? AND UsuId = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aplCodigo);
            stmt.setInt(2, usuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && "1".equals(rs.getString("APLUupd"))) return true;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /**
     * ¿Puede ELIMINAR en este módulo?
     * Habilita btnEliminar
     */
    public boolean puedeEliminar(int usuId, int aplCodigo) {
        // Fuente 1: desde el perfil
        int perfilAsignado = buscarPerfilDeUsuario(usuId);
        if (perfilAsignado != -1) {
            String sql = "SELECT APLPdel FROM asignacionaplicacionperfil " +
                         "WHERE Aplcodigo = ? AND Percodigo = ?";
            try (Connection conn = Conexion.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, aplCodigo);
                stmt.setInt(2, perfilAsignado);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && "1".equals(rs.getString("APLPdel"))) return true;
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
        // Fuente 2: directo al usuario
        String sql = "SELECT APLUdel FROM asignacionaplicacionusuarios " +
                     "WHERE Aplcodigo = ? AND UsuId = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aplCodigo);
            stmt.setInt(2, usuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && "1".equals(rs.getString("APLUdel"))) return true;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /**
     * ¿Puede ver REPORTES de este módulo?
     * Habilita btnReportes
     */
    public boolean puedeReportar(int usuId, int aplCodigo) {
        // Fuente 1: desde el perfil
        int perfilAsignado = buscarPerfilDeUsuario(usuId);
        if (perfilAsignado != -1) {
            String sql = "SELECT APLPrep FROM asignacionaplicacionperfil " +
                         "WHERE Aplcodigo = ? AND Percodigo = ?";
            try (Connection conn = Conexion.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, aplCodigo);
                stmt.setInt(2, perfilAsignado);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && "1".equals(rs.getString("APLPrep"))) return true;
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
        // Fuente 2: directo al usuario
        String sql = "SELECT APLUrep FROM asignacionaplicacionusuarios " +
                     "WHERE Aplcodigo = ? AND UsuId = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aplCodigo);
            stmt.setInt(2, usuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && "1".equals(rs.getString("APLUrep"))) return true;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }
}
