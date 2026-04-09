package Modelo;

/**
 * Data Access Object para la gestión de Asignaciones de Aplicaciones a Usuarios
 * 
 * Proporciona métodos para realizar operaciones CRUD sobre las asignaciones
 * de aplicaciones a usuarios, incluyendo la gestión automática de registros
 * en la bitácora del sistema.
 * 
 * Funcionalidades principales:
 * - Insertar nuevas asignaciones de aplicaciones a usuarios
 * - Actualizar permisos de acceso (Insert, Select, Update, Delete, Report)
 * - Eliminar asignaciones de aplicaciones
 * - Recuperar aplicaciones asignadas a un usuario
 * - Recuperar aplicaciones disponibles para un usuario
 * - Obtener permisos específicos de una asignación
 * - Registro automático de operaciones en bitácora
 * 
 * Todas las operaciones generan automáticamente registros en la tabla bitacora
 * con información de usuario, aplicación, acción y datos de conexión.
 * 
 * @author Camila Araujo
 * @carnet 9959-24-17623
 * @author Angel Méndez (Implementación de bitácora automática)
 * @carnet 9959-24-6845
 * @since 2026-04-08
 * 
 * Modificaciones realizadas por Angel:
 * - Agregado registro automático en bitácora para operaciones CRUD
 * - Mejora en textos de acciones para optimizar espacio en BD
 * - Implementación de selección múltiple de aplicaciones
 * - Validaciones mejoradas en consultas
 */

import Controlador.clsAsignacionAplicacionUsuario;
import Controlador.clsAplicaciones;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.Conexion;

public class AsignacionAplicacionUsuarioDAO {

    // INSERTAR asignación
    public int ingresaAsignacion(clsAsignacionAplicacionUsuario asignacion) {
        int resultado = 0;

        String sql = "INSERT INTO asignacionaplicacionusuarios "
                + "(Aplcodigo, UsuId, APLUins, APLUsel, APLUupd, APLUdel, APLUrep) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, asignacion.getAplcodigo());
            stmt.setInt(2, asignacion.getUsuId());
            stmt.setString(3, asignacion.getAPLUins());
            stmt.setString(4, asignacion.getAPLUsel());
            stmt.setString(5, asignacion.getAPLUupd());
            stmt.setString(6, asignacion.getAPLUdel());
            stmt.setString(7, asignacion.getAPLUrep());

            resultado = stmt.executeUpdate();

            // ✅ REGISTRAR EN BITÁCORA (texto corto)
            if (resultado > 0) {
                String accion = "Asignación App";
                new BitacoraDAO().insert(asignacion.getUsuId(), asignacion.getAplcodigo(), accion);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

        return resultado;
    }
    
    public int actualizaAsignacion(clsAsignacionAplicacionUsuario asig) {

        int resultado = 0;

        String sql = "UPDATE asignacionaplicacionusuarios "
                + "SET APLUins=?, APLUsel=?, APLUupd=?, APLUdel=?, APLUrep=? "
                + "WHERE Aplcodigo=? AND UsuId=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, asig.getAPLUins());
            ps.setString(2, asig.getAPLUsel());
            ps.setString(3, asig.getAPLUupd());
            ps.setString(4, asig.getAPLUdel());
            ps.setString(5, asig.getAPLUrep());

            ps.setInt(6, asig.getAplcodigo());
            ps.setInt(7, asig.getUsuId());

            resultado = ps.executeUpdate();

            // ✅ REGISTRAR EN BITÁCORA (texto corto)
            if (resultado > 0) {
                String accion = "Mod Perm: I:" + asig.getAPLUins() + 
                              ",S:" + asig.getAPLUsel() + 
                              ",U:" + asig.getAPLUupd() + 
                              ",D:" + asig.getAPLUdel() + 
                              ",R:" + asig.getAPLUrep();
                new BitacoraDAO().insert(asig.getUsuId(), asig.getAplcodigo(), accion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    // ELIMINAR asignación
    public int borrarAsignacion(clsAsignacionAplicacionUsuario asignacion) {

        int resultado = 0;

        String sql = "DELETE FROM asignacionaplicacionusuarios "
                   + "WHERE Aplcodigo=? AND UsuId=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, asignacion.getAplcodigo());
            stmt.setInt(2, asignacion.getUsuId());

            resultado = stmt.executeUpdate();

            // ✅ REGISTRAR EN BITÁCORA (texto corto)
            if (resultado > 0) {
                String accion = "Eliminar App";
                new BitacoraDAO().insert(asignacion.getUsuId(), asignacion.getAplcodigo(), accion);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

        return resultado;
    }


    // APLICACIONES YA ASIGNADAS
    public List<clsAplicaciones> getAplicacionesAsignadas(int usuId) {

        List<clsAplicaciones> lista = new ArrayList<>();

        String sql =
                "SELECT a.APLCODIGO, a.APLNOMBRE, a.APLESTADO "
              + "FROM Aplicaciones a "
              + "INNER JOIN asignacionaplicacionusuarios au "
              + "ON a.APLCODIGO = au.Aplcodigo "
              + "WHERE au.UsuId = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                clsAplicaciones app = new clsAplicaciones();

                app.setAplcodigo(rs.getInt("APLCODIGO"));
                app.setAplnombre(rs.getString("APLNOMBRE"));
                app.setAplestado(rs.getString("APLESTADO"));

                lista.add(app);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

        return lista;
    }

    // OBTENER PERMISOS DE UNA ASIGNACION
    public clsAsignacionAplicacionUsuario getPermisos(int usuId, int aplCodigo) {

        clsAsignacionAplicacionUsuario asig = null;

        String sql = "SELECT * FROM asignacionaplicacionusuarios "
                + "WHERE UsuId=? AND Aplcodigo=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuId);
            ps.setInt(2, aplCodigo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                asig = new clsAsignacionAplicacionUsuario();

                asig.setUsuId(rs.getInt("usuid"));
                asig.setAplcodigo(rs.getInt("aplcodigo"));

                asig.setAPLUins(rs.getString("apluins"));
                asig.setAPLUsel(rs.getString("aplusel"));
                asig.setAPLUupd(rs.getString("apluupd"));
                asig.setAPLUdel(rs.getString("apludel"));
                asig.setAPLUrep(rs.getString("aplurep"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return asig;
    }

    // APLICACIONES DISPONIBLES
    public List<clsAplicaciones> getAplicacionesDisponibles(int usuId) {

        List<clsAplicaciones> lista = new ArrayList<>();

        String sql =
                "SELECT APLCODIGO, APLNOMBRE, APLESTADO "
              + "FROM Aplicaciones "
              + "WHERE APLCODIGO NOT IN ("
              + "  SELECT Aplcodigo "
              + "  FROM asignacionaplicacionusuarios "
              + "  WHERE UsuId = ?"
              + ") "
              + "AND APLESTADO = 1";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                clsAplicaciones app = new clsAplicaciones();

                app.setAplcodigo(rs.getInt("APLCODIGO"));
                app.setAplnombre(rs.getString("APLNOMBRE"));
                app.setAplestado(rs.getString("APLESTADO"));

                lista.add(app);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

        return lista;
    }

}