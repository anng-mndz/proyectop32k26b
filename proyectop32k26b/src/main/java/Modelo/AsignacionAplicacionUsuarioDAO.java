package Modelo;

import Controlador.clsAsignacionAplicacionUsuario;
import Controlador.clsAplicaciones;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.Conexion;

/**
 * ============================================================
 * AUTOR: Angoly Camila Araujo Mayen
 * CARNÉ: 9959-24-17623
 * ============================================================
 * 
 * Clase DAO encargada de gestionar las operaciones CRUD
 * relacionadas con la asignación de aplicaciones a usuarios.
 * 
 * Permite:
 * - Insertar asignaciones
 * - Actualizar permisos
 * - Eliminar asignaciones
 * - Consultar aplicaciones asignadas y disponibles
 * - Obtener permisos específicos de un usuario
 * 
 * Utiliza conexión a base de datos mediante la clase Conexion.
 */
public class AsignacionAplicacionUsuarioDAO {

    /**
     * INSERTAR una nueva asignación de aplicación a usuario
     * 
     * @param asignacion Objeto con los datos de la asignación
     * @return número de filas afectadas
     */
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

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

        return resultado;
    }
    
    /**
     * ACTUALIZAR permisos de una asignación existente
     * 
     * @param asig Objeto con los nuevos permisos
     * @return número de filas afectadas
     */
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * ELIMINAR una asignación de aplicación a usuario
     * 
     * @param asignacion Objeto con los identificadores
     * @return número de filas afectadas
     */
    public int borrarAsignacion(clsAsignacionAplicacionUsuario asignacion) {

        int resultado = 0;

        String sql = "DELETE FROM asignacionaplicacionusuarios "
                   + "WHERE Aplcodigo=? AND UsuId=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, asignacion.getAplcodigo());
            stmt.setInt(2, asignacion.getUsuId());

            resultado = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

        return resultado;
    }

    /**
     * OBTENER todas las aplicaciones asignadas a un usuario
     * 
     * @param usuId ID del usuario
     * @return lista de aplicaciones asignadas
     */
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

    /**
     * OBTENER los permisos específicos de una asignación
     * 
     * @param usuId ID del usuario
     * @param aplCodigo código de la aplicación
     * @return objeto con permisos o null si no existe
     */
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

                asig.setUsuId(rs.getInt("UsuId"));
                asig.setAplcodigo(rs.getInt("Aplcodigo"));

                asig.setAPLUins(rs.getString("APLUins"));
                asig.setAPLUsel(rs.getString("APLUsel"));
                asig.setAPLUupd(rs.getString("APLUupd"));
                asig.setAPLUdel(rs.getString("APLUdel"));
                asig.setAPLUrep(rs.getString("APLUrep"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return asig;
    }

    /**
     * OBTENER aplicaciones disponibles (no asignadas a un usuario)
     * 
     * @param usuId ID del usuario
     * @return lista de aplicaciones disponibles
     */
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
              + "AND APLESTADO = 1"; // Solo aplicaciones activas

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