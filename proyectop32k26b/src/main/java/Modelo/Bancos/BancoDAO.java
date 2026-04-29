/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Bancos;

import Controlador.Bancos.clsBanco;
import Controlador.clsUsuarioConectado;
import Modelo.BitacoraDAO;
import Modelo.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Proyecto Final - Sistema Bancario
 */
public class BancoDAO {

    private static final int APL_CODIGO = 20001; // Código app Banco en tabla aplicaciones

    public List<clsBanco> listar() {
        List<clsBanco> lista = new ArrayList<>();
        String sql = "SELECT * FROM Banco";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsBanco b = new clsBanco();
                b.setBanid(rs.getInt("Banid"));
                b.setBannombre(rs.getString("Bannombre"));
                b.setBandireccion(rs.getString("Bandireccion"));
                b.setBantelefono(rs.getString("Bantelefono"));
                b.setBancorreo(rs.getString("Bancorreo"));
                lista.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void insert(clsBanco banco) {
        String sql = "INSERT INTO Banco (nombre_banco, direccion, telefono, correo) VALUES (?,?,?,?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, banco.getBannombre());
            ps.setString(2, banco.getBandireccion());
            ps.setString(3, banco.getBantelefono());
            ps.setString(4, banco.getBancorreo());
            ps.executeUpdate();

            new BitacoraDAO().insert(clsUsuarioConectado.getUsuId(), APL_CODIGO, "INSERT");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar banco", e);
        }
    }

    public void update(clsBanco banco) {
        String sql = "UPDATE Banco SET nombre_banco=?, direccion=?, telefono=?, correo=? WHERE id_banco=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, banco.getBannombre());
            ps.setString(2, banco.getBandireccion());
            ps.setString(3, banco.getBantelefono());
            ps.setString(4, banco.getBancorreo());
            ps.setInt(5, banco.getBanid());
            int rows = ps.executeUpdate();

            if (rows == 0) throw new RuntimeException("No se encontró el banco para actualizar");

            new BitacoraDAO().insert(clsUsuarioConectado.getUsuId(), APL_CODIGO, "UPDATE");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar banco", e);
        }
    }

    public void delete(int idBanco) {
        String sql = "DELETE FROM Banco WHERE id_banco=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBanco);
            int rows = ps.executeUpdate();

            if (rows == 0) throw new RuntimeException("No se encontró el banco para eliminar");

            new BitacoraDAO().insert(clsUsuarioConectado.getUsuId(), APL_CODIGO, "DELETE");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar banco", e);
        }
    }

    public clsBanco query(int idBanco) {
        clsBanco banco = null;
        String sql = "SELECT * FROM Banco WHERE id_banco=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBanco);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    banco = new clsBanco();
                    banco.setBanid(rs.getInt("Banid"));
                    banco.setBannombre(rs.getString("Bannombre"));
                    banco.setBandireccion(rs.getString("Bandireccion"));
                    banco.setBantelefono(rs.getString("Bantelefono"));
                    banco.setBancorreo(rs.getString("Bancorreo"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar banco", e);
        }
        return banco;
    }
}
