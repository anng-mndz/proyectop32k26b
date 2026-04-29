package Controlador.Bancos;

public class clsTipoCuenta {
    private int TCidcuenta;
    private String TCnombretipo;
    private String TCdescripcion;
    
public clsTipoCuenta(int id, String nombre, String descripcion) {
        this.TCidcuenta = id;
        this.TCnombretipo = nombre;
        this.TCdescripcion = descripcion;
    }

    public int getTCidcuenta() { return TCidcuenta; }
    public void setTCidcuenta(int TCidcuenta) { this.TCidcuenta = TCidcuenta; }

    public String getTCnombretipo() { return TCnombretipo; }
    public void setTCnombretipo(String TCnombretipo) { this.TCnombretipo = TCnombretipo; }

    public String getTCdescripcion() { return TCdescripcion; }
    public void setTCdescripcion(String TCdescripcion) { this.TCdescripcion = TCdescripcion; }
    
@Override
public String toString() {
    return "TipoCuenta{ id=" + TCidcuenta + ", nombre=" + TCnombretipo + " }";
}
}

