/*
 * ============================================================
 * Autor: Anthony Suc
 * Fecha: 28/03/2026
 * Clase: AplicacionesDAO
 * Descripción:
 * Esta clase implementa el patrón DAO (Data Access Object)
 * para gestionar las operaciones CRUD sobre la tabla "aplicaciones".
 * 
 * Funcionalidades principales:
 * - Listar aplicaciones
 * - Insertar nuevas aplicaciones
 * - Actualizar aplicaciones existentes
 * - Eliminar aplicaciones
 * - Consultar aplicaciones por ID
 * 
 * Además, integra un sistema de bitácora para auditoría de acciones,
 * permitiendo registrar eventos importantes realizados por el usuario.
 * 
 * Notas:
 * - Utiliza conexiones a base de datos mediante la clase Conexion.
 * - Implementa manejo de excepciones para garantizar estabilidad.
 * - La bitácora no interrumpe el flujo principal en caso de fallo.
 * ============================================================
 */

package Modelo;

import Controlador.clsAplicaciones;
import Controlador.clsUsuarioConectado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AplicacionesDAO {

    /**
     * Método que obtiene todas las aplicaciones registradas en la base de datos.
     * 
     * @return Lista de objetos clsAplicaciones
     */
    public List<clsAplicaciones> listar() {
        List<clsAplicaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM aplicaciones";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Recorrido de resultados y mapeo a objetos
            while (rs.next()) {
                clsAplicaciones app = new clsAplicaciones();
                app.setAplcodigo(rs.getInt("Aplcodigo"));
                app.setAplnombre(rs.getString("Aplnombre"));
                app.setAplestado(rs.getString("Aplestado"));
                lista.add(app);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar aplicaciones", e);
        }

        return lista;
    }

    /**
     * Inserta una nueva aplicación en la base de datos.
     * También registra la acción en la bitácora.
     * 
     * @param app Objeto clsAplicaciones con los datos a insertar
     */
    public void insert(clsAplicaciones app) {

    String sql = "INSERT INTO aplicaciones (Aplcodigo, Aplnombre, Aplestado) VALUES (?, ?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // Inserción manual del código (según cambios en la BD)
        ps.setInt(1, app.getAplcodigo());
        ps.setString(2, app.getAplnombre());
        ps.setString(3, app.getAplestado());

        int rows = ps.executeUpdate();

        // Validación de inserción exitosa
        if (rows > 0) {

            // Registro en bitácora
            try {
                registrarBitacora("INSERT aplicación ID: " + app.getAplcodigo() +
                                  " Nombre: " + app.getAplnombre());
            } catch (Exception ex) {
                System.out.println("Error en bitácora (no crítico): " + ex.getMessage());
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al insertar aplicación", e);
    }
}

    /**
     * Actualiza los datos de una aplicación existente.
     * Registra la operación en la bitácora.
     * 
     * @param app Objeto clsAplicaciones con los datos actualizados
     */
    public void update(clsAplicaciones app) {

        String sql = "UPDATE aplicaciones SET Aplnombre=?, Aplestado=? WHERE Aplcodigo=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, app.getAplnombre());
            ps.setString(2, app.getAplestado());
            ps.setInt(3, app.getAplcodigo());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                // Registro en bitácora
                try {
                    registrarBitacora("UPDATE aplicación ID: " + app.getAplcodigo() +
                                      " Nombre: " + app.getAplnombre());
                } catch (Exception ex) {
                    System.out.println("Error en bitácora (no crítico): " + ex.getMessage());
                }

            } else {
                throw new RuntimeException("No se encontró la aplicación para actualizar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar aplicación", e);
        }
    }

    /**
     * Elimina una aplicación de la base de datos según su ID.
     * Registra la acción en la bitácora.
     * 
     * @param codigo ID de la aplicación a eliminar
     */
    public void delete(int codigo) {

        String sql = "DELETE FROM aplicaciones WHERE Aplcodigo=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);
            int rows = ps.executeUpdate();

            if (rows > 0) {

                // Registro en bitácora
                try {
                    registrarBitacora("DELETE aplicación ID: " + codigo);
                } catch (Exception ex) {
                    System.out.println("Error en bitácora (no crítico): " + ex.getMessage());
                }

            } else {
                throw new RuntimeException("No se encontró la aplicación para eliminar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar aplicación", e);
        }
    }

    /**
     * Consulta una aplicación específica por su ID.
     * 
     * @param codigo ID de la aplicación
     * @return Objeto clsAplicaciones o null si no existe
     */
    public clsAplicaciones query(int codigo) {

        clsAplicaciones app = null;
        String sql = "SELECT * FROM aplicaciones WHERE Aplcodigo=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    app = new clsAplicaciones();
                    app.setAplcodigo(rs.getInt("Aplcodigo"));
                    app.setAplnombre(rs.getString("Aplnombre"));
                    app.setAplestado(rs.getString("Aplestado"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar aplicación", e);
        }

        return app;
    }

    /**
     * Registra una acción en la bitácora del sistema.
     * 
     * @param accion Descripción de la acción realizada
     */
    private void registrarBitacora(String accion) {

        int usuario = clsUsuarioConectado.getUsuId();

        // Validación de usuario autenticado
        if (usuario == 0) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        BitacoraDAO bitacora = new BitacoraDAO();

        // ID de aplicación para bitácora (debe existir en la BD)
        int aplCodigoBitacora = 1;

        bitacora.insert(usuario, aplCodigoBitacora, accion);
    }
}