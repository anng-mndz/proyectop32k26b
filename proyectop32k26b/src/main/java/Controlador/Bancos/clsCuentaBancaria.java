package Controlador.Bancos;
import java.util.Date;

public class clsCuentaBancaria {
    private int CBANid;
    private String CBANnumerocuenta;
    private double CBANsaldoactual;
    private Date CBANfechaapertura;
    private int Banid;
    private int Cliid;
    private int TCidcuenta;

public clsCuentaBancaria(int id, String numero, double saldo, Date fecha, int banco, int cliente, int tipo) {
        this.CBANid = id;
        this.CBANnumerocuenta = numero;
        this.CBANsaldoactual = saldo;
        this.CBANfechaapertura = fecha;
        this.Banid = banco;
        this.Cliid = cliente;
        this.TCidcuenta = tipo;
    }

    public int getCBANid() { return CBANid; }
    public void setCBANid(int CBANid) { this.CBANid = CBANid; }

    public String getCBANnumerocuenta() { return CBANnumerocuenta; }
    public void setCBANnumerocuenta(String CBANnumerocuenta) { this.CBANnumerocuenta = CBANnumerocuenta; }

    public double getCBANsaldoactual() { return CBANsaldoactual; }
    public void setCBANsaldoactual(double CBANsaldoactual) { this.CBANsaldoactual = CBANsaldoactual; }

    public Date getCBANfechaapertura() { return CBANfechaapertura; }
    public void setCBANfechaapertura(Date CBANfechaapertura) { this.CBANfechaapertura = CBANfechaapertura; }

    public int getBanid() { return Banid; }
    public void setBanid(int Banid) { this.Banid = Banid; }

    public int getCliid() { return Cliid; }
    public void setCliid(int Cliid) { this.Cliid = Cliid; }

    public int getTCidcuenta() { return TCidcuenta; }
    public void setTCidcuenta(int TCidcuenta) { this.TCidcuenta = TCidcuenta; }

@Override
public String toString() {
    return "CuentaBancaria{ id=" + CBANid + ", numero=" + CBANnumerocuenta + ", saldo=" + CBANsaldoactual + " }";
}

}
