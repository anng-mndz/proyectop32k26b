package Controlador.Bancos;
import java.util.Date;

public class clsBitacoraBancaria {
    private int BBid;
    private int BBusuarioaccion;
    private String BBaccion;
    private String BBtabla;
    private int BBregistroid;
    private String BBvaloranterior;
    private String BBvalornuevo;
    private Date BBfechaaccion;
    private String BBdescripcion;

public clsBitacoraBancaria(int id, int usuario, String accion, String tabla, int registro, String anterior, String nuevo, Date fecha, String descripcion) {
        this.BBid = id;
        this.BBusuarioaccion = usuario;
        this.BBaccion = accion;
        this.BBtabla = tabla;
        this.BBregistroid = registro;
        this.BBvaloranterior = anterior;
        this.BBvalornuevo = nuevo;
        this.BBfechaaccion = fecha;
        this.BBdescripcion = descripcion;
    } 

@Override
public String toString() {
    return "Bitacora{ id=" + BBid + ", accion=" + BBaccion + ", tabla=" + BBtabla + " }";
}
}
