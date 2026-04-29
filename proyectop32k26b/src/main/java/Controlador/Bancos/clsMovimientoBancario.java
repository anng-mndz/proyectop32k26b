package Controlador.Bancos;
import java.util.Date;

public class clsMovimientoBancario {
    private int Movbid;
    private Date Movbfechamovimiento;
    private double Movibmonto;
    private String Movdescripcion;
    private int CBANid;
    private int TTid;
    private String Movbtipomov;
    private String Movbreferencia;
    private String Movbconciliado;

public clsMovimientoBancario(int id, Date fecha, double monto, String descripcion, int cuenta, int tipo, String tipomov, String referencia, String conciliado) {
        this.Movbid = id;
        this.Movbfechamovimiento = fecha;
        this.Movibmonto = monto;
        this.Movdescripcion = descripcion;
        this.CBANid = cuenta;
        this.TTid = tipo;
        this.Movbtipomov = tipomov;
        this.Movbreferencia = referencia;
        this.Movbconciliado = conciliado;
    }    
@Override
public String toString() {
    return "Movimiento{ id=" + Movbid + ", monto=" + Movibmonto + ", tipo=" + Movbtipomov + " }";
}

}
