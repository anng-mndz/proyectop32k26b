package Controlador.Bancos;
import java.util.Date;

public class clsConciliacionBancaria {
    private int Conbid;
    private Date conbfecha;
    private double Conbsaldosistema;
    private double Conbsaldobanco;
    private double Conbdiferencia;
    private int CBANid;
    private int Catesid;

public clsConciliacionBancaria(int id, Date fecha, double sistema, double banco, double diferencia, int cuenta, int estado) {
        this.Conbid = id;
        this.conbfecha = fecha;
        this.Conbsaldosistema = sistema;
        this.Conbsaldobanco = banco;
        this.Conbdiferencia = diferencia;
        this.CBANid = cuenta;
        this.Catesid = estado;
    } 

@Override
public String toString() {
    return "Conciliacion{ id=" + Conbid + ", saldoSistema=" + Conbsaldosistema + ", saldoBanco=" + Conbsaldobanco + " }";
}
}
