package Controlador.Bancos;

public class clsEstadoConciliacion {
    private int Catesid;
    private String Catesnombreestado;

public clsEstadoConciliacion(int id, String nombre) {
        this.Catesid = id;
        this.Catesnombreestado = nombre;
    }

    public int getCatesid() { return Catesid; }
    public void setCatesid(int Catesid) { this.Catesid = Catesid; }

    public String getCatesnombreestado() { return Catesnombreestado; }
    public void setCatesnombreestado(String Catesnombreestado) { this.Catesnombreestado = Catesnombreestado; } 

@Override
public String toString() {
    return "EstadoConciliacion{ id=" + Catesid + ", estado=" + Catesnombreestado + " }";
}

}
