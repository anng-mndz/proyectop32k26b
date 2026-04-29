package Controlador.Bancos;

public class clsTipoTransaccion {
    private int TTid;
    private String TTnombretipo;
    private String TTdescripcion;
    
public clsTipoTransaccion(int id, String nombre, String descripcion) {
        this.TTid = id;
        this.TTnombretipo = nombre;
        this.TTdescripcion = descripcion;
    }

    public int getTTid() { return TTid; }
    public void setTTid(int TTid) { this.TTid = TTid; }

    public String getTTnombretipo() { return TTnombretipo; }
    public void setTTnombretipo(String TTnombretipo) { this.TTnombretipo = TTnombretipo; }

    public String getTTdescripcion() { return TTdescripcion; }
    public void setTTdescripcion(String TTdescripcion) { this.TTdescripcion = TTdescripcion; }

@Override
public String toString() {
    return "TipoTransaccion{ id=" + TTid + ", nombre=" + TTnombretipo + " }";
}

}
