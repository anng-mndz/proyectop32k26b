/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador.Bancos;

import java.util.Date;

/**
 *
 * @author Proyecto Final - Sistema Bancario
 */
public class clsBanco {
    private int Banid;
    private String Bannombre;
    private String Bandireccion;
    private String Bantelefono;
    private String Bancorreo;
    private Date Banfecharegistro;
    
    
public clsBanco(int id, String nombre, String direccion, String telefono, String correo, Date fecha) {
        this.Banid = id;
        this.Bannombre = nombre;
        this.Bandireccion = direccion;
        this.Bantelefono = telefono;
        this.Bancorreo = correo;
        this.Banfecharegistro = fecha;
    }

    public clsBanco() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getBanid() { return Banid; }
    public void setBanid(int Banid) { this.Banid = Banid; }

    public String getBannombre() { return Bannombre; }
    public void setBannombre(String Bannombre) { this.Bannombre = Bannombre; }

    public String getBandireccion() { return Bandireccion; }
    public void setBandireccion(String Bandireccion) { this.Bandireccion = Bandireccion; }

    public String getBantelefono() { return Bantelefono; }
    public void setBantelefono(String Bantelefono) { this.Bantelefono = Bantelefono; }

    public String getBancorreo() { return Bancorreo; }
    public void setBancorreo(String Bancorreo) { this.Bancorreo = Bancorreo; }
    
    public Date getBanfecharegistro() { return Banfecharegistro; }
    public void setBanfecharegistro(Date Banfecharegistro) { this.Banfecharegistro = Banfecharegistro; }

    @Override
    public String toString() {
        return "Banco{ idBanco=" + Banid + ", nombreBanco=" + Bannombre + " }";
    }
}

