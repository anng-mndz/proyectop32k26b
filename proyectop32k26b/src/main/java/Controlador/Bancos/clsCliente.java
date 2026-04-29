package Controlador.Bancos;

public class clsCliente {
    private int Clid;
    private String Clinombre;
    private String Clinit;
    private String Clitelefono;
    private String Cliestado;
    private String Clidireccion;
    private String Clicorreo;
    
public clsCliente(int id, String nombre, String nit, String telefono, String estado, String direccion, String correo) {
        this.Clid = id;
        this.Clinombre = nombre;
        this.Clinit = nit;
        this.Clitelefono = telefono;
        this.Cliestado = estado;
        this.Clidireccion = direccion;
        this.Clicorreo = correo;
    }

    public int getClid() { return Clid; }
    public void setClid(int Clid) { this.Clid = Clid; }

    public String getClinombre() { return Clinombre; }
    public void setClinombre(String Clinombre) { this.Clinombre = Clinombre; }

    public String getClinit() { return Clinit; }
    public void setClinit(String Clinit) { this.Clinit = Clinit; }

    public String getClitelefono() { return Clitelefono; }
    public void setClitelefono(String Clitelefono) { this.Clitelefono = Clitelefono; }

    public String getCliestado() { return Cliestado; }
    public void setCliestado(String Cliestado) { this.Cliestado = Cliestado; }

    public String getClidireccion() { return Clidireccion; }
    public void setClidireccion(String Clidireccion) { this.Clidireccion = Clidireccion; }

    public String getClicorreo() { return Clicorreo; }
    public void setClicorreo(String Clicorreo) { this.Clicorreo = Clicorreo; }
    
@Override
public String toString() {
    return "Cliente{ id=" + Clid + ", nombre=" + Clinombre + ", NIT=" + Clinit + " }";
}
}
