package Modelo;

import Controlador.clsPerfil;
import Controlador.clsBitacora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerfilDAO {

    private static final String SQL_SELECT =
            "SELECT percodigo, pernombre, perestado FROM perfiles";

    private static final String SQL_INSERT =
            "INSERT INTO perfiles (pernombre, perestado) VALUES(?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE perfiles SET pernombre=?, perestado=? WHERE percodigo=?";

    private static final String SQL_DELETE =
            "DELETE FROM perfiles WHERE percodigo=?";

    private static final String SQL_SELECT_ID =
            "SELECT percodigo, pernombre, perestado FROM perfiles WHERE percodigo=?";


    private static final String SQL_INSERT_BITACORA =
            "INSERT INTO bitacora(usuid, aplcodigo, bitfecha, bitip, bitequipo, bitaccion) VALUES(?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BITACORA =
            "SELECT bitcodigo, usuid, aplcodigo, bitfecha, bitip, bitequipo, bitaccion FROM bitacora";

    private static final String SQL_UPDATE_BITACORA =
            "UPDATE bitacora SET usuid=?, aplcodigo=?, bitfecha=?, bitip=?, bitequipo=?, bitaccion=? WHERE bitcodigo=?";

    private static final String SQL_DELETE_BITACORA =
            "DELETE FROM bitacora WHERE bitcodigo=?";


    
    public List<clsPerfil> obtenerPerfiles(clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<clsPerfil> lista = new ArrayList<>();

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()) {
                clsPerfil p = new clsPerfil();
                p.setPercodigo(rs.getInt("percodigo"));
                p.setPernombre(rs.getString("pernombre"));
                p.setPerestado(rs.getString("perestado"));
                lista.add(p);
            }

            //bitacora.setBitaccion("SELECT perfiles");
            //insertarBitacora(bitacora);

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return lista;
    }

    public int insertarPerfil(clsPerfil perfil, clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);

            stmt.setString(1, perfil.getPernombre());
            stmt.setString(2, perfil.getPerestado());

            rows = stmt.executeUpdate();

            bitacora.setBitaccion("INSERT perfil " + perfil.getPernombre());
            insertarBitacora(bitacora);

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return rows;
    }

    public int actualizarPerfil(clsPerfil perfil, clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);

            stmt.setString(1, perfil.getPernombre());
            stmt.setString(2, perfil.getPerestado());
            stmt.setInt(3, perfil.getPercodigo());

            rows = stmt.executeUpdate();

            bitacora.setBitaccion("UPDATE perfil " + perfil.getPercodigo());
            insertarBitacora(bitacora);

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return rows;
    }

    public int eliminarPerfil(clsPerfil perfil, clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);

            stmt.setInt(1, perfil.getPercodigo());

            rows = stmt.executeUpdate();

            bitacora.setBitaccion("DELETE perfil " + perfil.getPercodigo());
            insertarBitacora(bitacora);

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return rows;
    }

    public clsPerfil obtenerPerfilPorId(int id, clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        clsPerfil perfil = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                perfil = new clsPerfil();
                perfil.setPercodigo(rs.getInt("percodigo"));
                perfil.setPernombre(rs.getString("pernombre"));
                perfil.setPerestado(rs.getString("perestado"));
            }

            //bitacora.setBitaccion("SELECT perfil ID " + id);
            //insertarBitacora(bitacora);

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return perfil;
    }


    
    public int insertarBitacora(clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_BITACORA);

            stmt.setInt(1, bitacora.getUsucodigo());
            stmt.setInt(2, bitacora.getAplcodigo());
            stmt.setString(3, bitacora.getBitfecha());
            stmt.setString(4, bitacora.getBitip());
            stmt.setString(5, bitacora.getBitequipo());
            stmt.setString(6, bitacora.getBitaccion());

            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return rows;
    }

    public List<clsBitacora> obtenerBitacora() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<clsBitacora> lista = new ArrayList<>();

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BITACORA);
            rs = stmt.executeQuery();

            while (rs.next()) {
                clsBitacora b = new clsBitacora();
                b.setBitcodigo(rs.getInt("bitcodigo"));
                b.setUsucodigo(rs.getInt("usucodigo"));
                b.setAplcodigo(rs.getInt("aplcodigo"));
                b.setBitfecha(rs.getString("bitfecha"));
                b.setBitip(rs.getString("bitip"));
                b.setBitequipo(rs.getString("bitequipo"));
                b.setBitaccion(rs.getString("bitaccion"));
                lista.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return lista;
    }

    public int actualizarBitacora(clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_BITACORA);

            stmt.setInt(1, bitacora.getUsucodigo());
            stmt.setInt(2, bitacora.getAplcodigo());
            stmt.setString(3, bitacora.getBitfecha());
            stmt.setString(4, bitacora.getBitip());
            stmt.setString(5, bitacora.getBitequipo());
            stmt.setString(6, bitacora.getBitaccion());
            stmt.setInt(7, bitacora.getBitcodigo());

            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return rows;
    }

    public int eliminarBitacora(clsBitacora bitacora) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_BITACORA);

            stmt.setInt(1, bitacora.getBitcodigo());

            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return rows;
    }
}