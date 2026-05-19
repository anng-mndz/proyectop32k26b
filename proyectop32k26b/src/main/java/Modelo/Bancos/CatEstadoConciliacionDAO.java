//Karina Alejandra Arriaza Ortiz 9959-24-14190
//Documentación
package Modelo.Bancos;

import Controlador.Bancos.clsCatEstadoConciliacion;
import Controlador.clsUsuarioConectado;
import Modelo.BitacoraDAO;
import Modelo.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión del catálogo
 * de Estados de Conciliación (CatEstadoConciliacion).
 *
 * Permite realizar operaciones CRUD sobre la base de datos:
 * - Listar todos los registros
 * - Insertar nuevos estados
 * - Actualizar estados existentes
 * - Eliminar registros
 * - Consultar por ID
 *
 * Cada operación intenta registrarse en la bitácora del sistema.
 * Si la bitácora falla, la operación principal NO se cancela.
 *
 * @author Proyecto Final
 */
public class CatEstadoConciliacionDAO {

    // Código de la aplicación para registro en bitácora
    private static final int APL_CODIGO = 5200;

    /**
     * Obtiene todos los estados de conciliación.
     *
     * @return Lista de objetos clsCatEstadoConciliacion
     */
    public List<clsCatEstadoConciliacion> listar() {
        List<clsCatEstadoConciliacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM CatEstadoConciliacion";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new clsCatEstadoConciliacion(
                    rs.getInt("Catesid"),
                    rs.getString("Catesnombreestado")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Inserta un nuevo estado de conciliación.
     * La bitácora se registra en un bloque separado para que
     * un fallo en ella no cancele el INSERT principal.
     *
     * @param cates Objeto a insertar
     */
    public void insert(clsCatEstadoConciliacion cates) {
        String sql = "INSERT INTO CatEstadoConciliacion (Catesnombreestado) VALUES (?)";

        // ── Operación principal ──────────────────────────────────
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cates.getCatesnombreestado());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar CatEstadoConciliacion", e);
        }

        // ── Bitácora (fallo aquí NO afecta el INSERT) ────────────
        try {
            new BitacoraDAO().insert(clsUsuarioConectado.getUsuId(), APL_CODIGO, "INSERT");
        } catch (Exception e) {
            System.err.println("Advertencia: no se pudo registrar en bitácora - " + e.getMessage());
        }
    }

    /**
     * Actualiza un estado de conciliación existente.
     * La bitácora se registra en un bloque separado.
     *
     * @param cates Objeto con datos actualizados
     */
    public void update(clsCatEstadoConciliacion cates) {
        String sql = "UPDATE CatEstadoConciliacion SET Catesnombreestado=? WHERE Catesid=?";

        // ── Operación principal ──────────────────────────────────
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cates.getCatesnombreestado());
            ps.setInt(2, cates.getCatesid());

            int rows = ps.executeUpdate();

            if (rows == 0)
                throw new RuntimeException("No se encontró el EstadoConciliacion para actualizar");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar CatEstadoConciliacion", e);
        }

        // ── Bitácora (fallo aquí NO afecta el UPDATE) ────────────
        try {
            new BitacoraDAO().insert(clsUsuarioConectado.getUsuId(), APL_CODIGO, "UPDATE");
        } catch (Exception e) {
            System.err.println("Advertencia: no se pudo registrar en bitácora - " + e.getMessage());
        }
    }

    /**
     * Elimina un estado de conciliación por su ID.
     * La bitácora se registra en un bloque separado.
     *
     * @param id Identificador del estado
     */
    public void delete(int id) {
        String sql = "DELETE FROM CatEstadoConciliacion WHERE Catesid=?";

        // ── Operación principal ──────────────────────────────────
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows == 0)
                throw new RuntimeException("No se encontró el EstadoConciliacion para eliminar");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar CatEstadoConciliacion", e);
        }

        // ── Bitácora (fallo aquí NO afecta el DELETE) ────────────
        try {
            new BitacoraDAO().insert(clsUsuarioConectado.getUsuId(), APL_CODIGO, "DELETE");
        } catch (Exception e) {
            System.err.println("Advertencia: no se pudo registrar en bitácora - " + e.getMessage());
        }
    }

    /**
     * Consulta un estado de conciliación por su ID.
     *
     * @param id Identificador del estado
     * @return Objeto encontrado o null si no existe
     */
    public clsCatEstadoConciliacion query(int id) {
        clsCatEstadoConciliacion cates = null;
        String sql = "SELECT * FROM CatEstadoConciliacion WHERE Catesid=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cates = new clsCatEstadoConciliacion(
                        rs.getInt("Catesid"),
                        rs.getString("Catesnombreestado")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar CatEstadoConciliacion", e);
        }
        return cates;
    }
}