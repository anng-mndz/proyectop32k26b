/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador.ComisionesVentas;
import java.time.LocalDateTime;


/**
 *
 * @author Jorge Reyes
 */
public class clsBitacoraComisionesVenta {
 
    private int Bitcodigo;
    private int Usucodigo;
    private int Aplcodigo;
    private String Bitfecha; //cambiar fecha a string
    private String Bitip;
    private String Bitequipo;
    private String Bitaccion;

    // Constructor vacío
    public clsBitacoraComisionesVenta() {
    }

    // Constructor con parámetros
    public clsBitacoraComisionesVenta(int Usucodigo, int Aplcodigo, String Bitfecha,String Bitip, String Bitequipo, String Bitaccion) {
        this.Usucodigo = Usucodigo;
        this.Aplcodigo = Aplcodigo;
        this.Bitfecha  = Bitfecha;
        this.Bitip     = Bitip;
        this.Bitequipo = Bitequipo;
        this.Bitaccion = Bitaccion;
    }

    // Getters y Setters
    public int getBitcodigo() { return Bitcodigo; }
    public void setBitcodigo(int Bitcodigo) { this.Bitcodigo = Bitcodigo; }

    public int getUsucodigo() { return Usucodigo; }
    public void setUsucodigo(int Usucodigo) { this.Usucodigo = Usucodigo; }

    public int getAplcodigo() { return Aplcodigo; }
    public void setAplcodigo(int Aplcodigo) { this.Aplcodigo = Aplcodigo; }

    public String getBitfecha() { return Bitfecha; }
    public void setBitfecha(String Bitfecha) { this.Bitfecha = Bitfecha; }
    
    public String getBitip() { return Bitip; }
    public void setBitip(String Bitip) { this.Bitip = Bitip; }

    public String getBitequipo() { return Bitequipo; }
    public void setBitequipo(String Bitequipo) { this.Bitequipo = Bitequipo; }

    public String getBitaccion() { return Bitaccion; }
    public void setBitaccion(String Bitaccion) { this.Bitaccion = Bitaccion; }

    @Override
    public String toString() {
        return "Bitacora{" + "Bitcodigo=" + Bitcodigo + ", Usucodigo=" + Usucodigo + ", Aplcodigo=" + Aplcodigo + ", Bitfecha=" + Bitfecha + ", Bitip='" + Bitip + ", Bitequipo='" + Bitequipo + ", Bitaccion='" + Bitaccion + '}';
    }
    
}
