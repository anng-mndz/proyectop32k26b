/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.ComisionesVentas;
import Controlador.ComisionesVentas.clsBitacoraComisionesVenta;
import Modelo.Conexion;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; //manejo de fechas usado por la bd sql
import java.text.SimpleDateFormat;
import java.time.LocalDateTime; //para la fechas y horas en java
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jorge Reyes
 */
public class BitacoraComisionesDAO {
    private static final String SQL_SELECT = "SELECT BCVid, BCVusuarioaccion, BCVtabla, BCVfecha, BCVregistroid, BCVdescripcion, BCVaccion FROM bitacoracomisionventa";
    private static final String SQL_INSERT = "INSERT INTO bitacoracomisionventa(BCVusuarioaccion, BCVtabla, BCVfecha, BCVregistroid, BCVdescripcion, BCVaccion) VALUES(?, ?, NOW(), ?, ?, ?)";
    //Se agregaron varios querys para poder buscar por diferentes tipos en la bitacora (ya sea codigo, usuario, fecha,etc) 
    private static final String SQL_QUERY_POR_CODIGO = "SELECT BCVid, BCVusuarioaccion, BCVtabla, BCVfecha, BCVregistroid, BCVdescripcion, BCVaccion FROM bitacoracomisionventa WHERE BCVid=?";
    private static final String SQL_QUERY_POR_USUARIO = "SELECT BCVid, BCVusuarioaccion, BCVtabla, BCVfecha, BCVregistroid, BCVdescripcion, BCVaccion FROM bitacoracomisionventa WHERE BCVusuarioaccion=?";
    private static final String SQL_QUERY_POR_APLICACION = "SELECT BCVid, BCVusuarioaccion, BCVtabla, BCVfecha, BCVregistroid, BCVdescripcion, BCVaccion FROM bitacoracomisionventa WHERE BCVtabla=?";
    private static final String SQL_QUERY_POR_FECHAS = "SELECT BCVid, BCVusuarioaccion, BCVtabla, BCVfecha, BCVregistroid, BCVdescripcion, BCVaccion FROM bitacoracomisionventa WHERE BCVfecha BETWEEN ? AND ?"; //between y and para buscar desde el inicio d fecha que el usuario seleccione hasta el final (el intervalo d fechas)
    private static final String SQL_QUERY_POR_ACCION = "SELECT BCVid, BCVusuarioaccion, BCVtabla, BCVfecha, BCVregistroid, BCVdescripcion, BCVaccion FROM bitacoracomisionventa WHERE BCVaccion=?";
 //Bit

    // SELECT (trae todos los registros) 
        public String fechaActual() {

        java.util.Date fecha = new java.util.Date();
        //SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");

        return formatoFecha.format(fecha);

    }

    public static String horaActual() {

        java.util.Date fecha = new java.util.Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("hh:mm:ss");

        return formatoFecha.format(fecha);

    }
    
    private String obtenerNombrePc() throws UnknownHostException {
        // return System.getProperty("user.name");        
        return InetAddress.getLocalHost().getHostName();
    }
            
    private String obtenerIP() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        return ip.getHostAddress();
    }        
    public List<clsBitacoraComisionesVenta> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clsBitacoraComisionesVenta bitacora = null;
        List<clsBitacoraComisionesVenta> bitacoras = new ArrayList<clsBitacoraComisionesVenta>(); //lista para almacenar todos los registros

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery(); //se ejecuta la consulta
            while (rs.next()) {
                //valores de cada columna del ResultSet
                
                 int Bitcodigo    = rs.getInt("BCVid");
                int Usuid    = rs.getInt("BCVusuarioaccion");
                int Aplcodigo    = rs.getInt("BCVtabla");
                String Bitfecha = rs.getString("BCVfecha");
                String Bitip     = rs.getString("BCVregistroid");
                String Bitequipo = rs.getString("BCVdescripcion");
                String Bitaccion = rs.getString("BCVaccion");

                bitacora = new clsBitacoraComisionesVenta();
                bitacora.setBitcodigo(Bitcodigo);
                bitacora.setUsucodigo(Usuid);
                bitacora.setAplcodigo(Aplcodigo);
                bitacora.setBitfecha(Bitfecha);
                bitacora.setBitip(Bitip);
                bitacora.setBitequipo(Bitequipo);
                bitacora.setBitaccion(Bitaccion);

                bitacoras.add(bitacora);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return bitacoras;
    }

    // Isert
    public int insert(int Usuid, int Aplcodigo, String Bitaccion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String ipAsignada;
        String nombrepcAsignada;
        ipAsignada = " ";
        nombrepcAsignada = " ";        
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            try {
                   ipAsignada= obtenerIP();
                   nombrepcAsignada= obtenerNombrePc();            
            } catch (UnknownHostException ex)
                {
                }                           
            //asignación d valores a los parametros
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, Usuid);
            stmt.setInt(2, Aplcodigo);
            stmt.setString(3, ipAsignada);
            stmt.setString(4, nombrepcAsignada);
            stmt.setString(5,  Bitaccion);

            System.out.println("Ejecutando query: " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    // Query por código 
    public clsBitacoraComisionesVenta queryPorCodigo(clsBitacoraComisionesVenta bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_QUERY_POR_CODIGO);
            stmt = conn.prepareStatement(SQL_QUERY_POR_CODIGO);
            // se pasa el código que se desea buscar
            stmt.setInt(1, bitacora.getBitcodigo());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int Bitcodigo    = rs.getInt("BCVid");
                int Usuid    = rs.getInt("BCVusuarioaccion");
                int Aplcodigo    = rs.getInt("BCVtabla");
                String Bitfecha = rs.getString("BCVfecha");
                String Bitip     = rs.getString("BCVregistroid");
                String Bitequipo = rs.getString("BCVdescripcion");
                String Bitaccion = rs.getString("BCVaccion");

                bitacora = new clsBitacoraComisionesVenta();
                bitacora.setBitcodigo(Bitcodigo);
                bitacora.setUsucodigo(Usuid);
                bitacora.setAplcodigo(Aplcodigo);
                bitacora.setBitfecha(Bitfecha); 
                bitacora.setBitip(Bitip);
                bitacora.setBitequipo(Bitequipo);
                bitacora.setBitaccion(Bitaccion);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return bitacora;
    }

    //Se hace el mismo proceso para los demás querys: ejecutar la consulta, recorrer el ResultSet, convertir cada registro en un objeto Bitacora y agregarlo a una lista.
    
    // Query por usuario
    public List<clsBitacoraComisionesVenta> queryPorUsuario(int Usuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clsBitacoraComisionesVenta bitacora = null;
        List<clsBitacoraComisionesVenta> bitacoras = new ArrayList<clsBitacoraComisionesVenta>();

        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_QUERY_POR_USUARIO);
            stmt = conn.prepareStatement(SQL_QUERY_POR_USUARIO);
            stmt.setInt(1, Usuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                 int Bitcodigo    = rs.getInt("BCVid");
                int Usu    = rs.getInt("BCVusuarioaccion");
                int Aplcodigo    = rs.getInt("BCVtabla");
                String Bitfecha = rs.getString("BCVfecha");
                String Bitip     = rs.getString("BCVregistroid");
                String Bitequipo = rs.getString("BCVdescripcion");
                String Bitaccion = rs.getString("BCVaccion");

                bitacora = new clsBitacoraComisionesVenta();
                bitacora.setBitcodigo(Bitcodigo);
                bitacora.setUsucodigo(Usuid);
                bitacora.setAplcodigo(Aplcodigo);
                bitacora.setBitfecha(Bitfecha);
                bitacora.setBitip(Bitip);
                bitacora.setBitequipo(Bitequipo);
                bitacora.setBitaccion(Bitaccion);

                bitacoras.add(bitacora);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return bitacoras;
    }

    // Query por aplicación
    public List<clsBitacoraComisionesVenta> queryPorAplicacion(int Aplcodigo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clsBitacoraComisionesVenta bitacora = null;
        List<clsBitacoraComisionesVenta> bitacoras = new ArrayList<clsBitacoraComisionesVenta>();

        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_QUERY_POR_APLICACION);
            stmt = conn.prepareStatement(SQL_QUERY_POR_APLICACION);
            stmt.setInt(1, Aplcodigo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                 int Bitcodigo    = rs.getInt("BCVid");
                int Usuid    = rs.getInt("BCVusuarioaccion");
                int Apl    = rs.getInt("BCVtabla");
                String Bitfecha = rs.getString("BCVfecha");
                String Bitip     = rs.getString("BCVregistroid");
                String Bitequipo = rs.getString("BCVdescripcion");
                String Bitaccion = rs.getString("BCVaccion");

                bitacora = new clsBitacoraComisionesVenta();
                bitacora.setBitcodigo(Bitcodigo);
                bitacora.setUsucodigo(Usuid);
                bitacora.setAplcodigo(Apl);
                bitacora.setBitfecha(Bitfecha);
                bitacora.setBitip(Bitip);
                bitacora.setBitequipo(Bitequipo);
                bitacora.setBitaccion(Bitaccion);

                bitacoras.add(bitacora);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return bitacoras;
    }

    // Query por rango de fechas
    public List<clsBitacoraComisionesVenta> queryPorFechas (String fechaInicio, String fechaFin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clsBitacoraComisionesVenta bitacora = null;
        List<clsBitacoraComisionesVenta> bitacoras = new ArrayList<clsBitacoraComisionesVenta>();

        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_QUERY_POR_FECHAS);
            stmt = conn.prepareStatement(SQL_QUERY_POR_FECHAS);
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                 int Bitcodigo    = rs.getInt("BCVid");
                int Usuid    = rs.getInt("BCVusuarioaccion");
                int Aplcodigo    = rs.getInt("BCVtabla");
                String Bitfecha = rs.getString("BCVfecha");
                String Bitip     = rs.getString("BCVregistroid");
                String Bitequipo = rs.getString("BCVdescripcion");
                String Bitaccion = rs.getString("BCVaccion");

                bitacora = new clsBitacoraComisionesVenta();
                bitacora.setBitcodigo(Bitcodigo);
                bitacora.setUsucodigo(Usuid);
                bitacora.setAplcodigo(Aplcodigo);
                bitacora.setBitfecha(Bitfecha);
                bitacora.setBitip(Bitip);
                bitacora.setBitequipo(Bitequipo);
                bitacora.setBitaccion(Bitaccion);

                bitacoras.add(bitacora);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return bitacoras;
    }

    // Query por acción
    public List<clsBitacoraComisionesVenta> queryPorAccion(String Bitaccion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clsBitacoraComisionesVenta bitacora = null;
        List<clsBitacoraComisionesVenta> bitacoras = new ArrayList<clsBitacoraComisionesVenta>();

        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query: " + SQL_QUERY_POR_ACCION);
            stmt = conn.prepareStatement(SQL_QUERY_POR_ACCION);
            stmt.setString(1, Bitaccion);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                 int Bitcodigo    = rs.getInt("BCVid");
                int Usuid    = rs.getInt("BCVusuarioaccion");
                int Aplcodigo    = rs.getInt("BCVtabla");
                String Bitfecha = rs.getString("BCVfecha");
                String Bitip     = rs.getString("BCVregistroid");
                String Bitequipo = rs.getString("BCVdescripcion");
                String Bitaccion2 = rs.getString("BCVaccion");

                bitacora = new clsBitacoraComisionesVenta();
                bitacora.setBitcodigo(Bitcodigo);
                bitacora.setUsucodigo(Usuid);
                bitacora.setAplcodigo(Aplcodigo);
                bitacora.setBitfecha(Bitfecha);
                bitacora.setBitip(Bitip);
                bitacora.setBitequipo(Bitequipo);
                bitacora.setBitaccion(Bitaccion2);

                bitacoras.add(bitacora);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return bitacoras;
    }
}
