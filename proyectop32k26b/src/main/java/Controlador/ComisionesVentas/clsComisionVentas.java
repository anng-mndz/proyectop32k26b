/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador.ComisionesVentas;

/**
 *
 * @author giron
 */
public class clsComisionVentas {
    //atributos comisiones ventas
    
    private int id_comision;
    private int id_empleado; 
    private double monto_ventas;
    private double meta;
    private double ventas_adicionales;
    private double comision;
    private String cppcodigo;
    //atributos vendedores
    private int venid;
    private String vennombre; 
    private String ventelefono;
    private String vencorreo;
    //atributos productos
    private int proid;
    private String prodnombre;
    private double prodprecioventa;
    private double procomision;
    //atributos marca
    private String marnombre;
    private double marcomision;
    //atributos linea
    private String linnombre;
    private double lincomision;
    //atributos cuentas por pagar

    //constructores
    public clsComisionVentas(int id_comision, int id_empleado, double monto_ventas, double meta, double ventas_adicionales, double comision, int venid, String vennombre, String ventelefono, String vencorreo, int proid, String prodnombre, double prodprecioventa, String marnombre,double marcomision, String linnombre, double lincomision, String cppcodigo) {
        this.id_comision = id_comision;
        this.id_empleado = id_empleado;
        this.monto_ventas = monto_ventas;
        this.meta = meta;
        this.ventas_adicionales = ventas_adicionales;
        this.comision = comision;
        this.cppcodigo = cppcodigo;
        this.venid = venid;
        this.vennombre = vennombre;
        this.ventelefono = ventelefono;
        this.vencorreo = vencorreo;
        this.proid = proid;
        this.prodnombre = prodnombre;
        this.prodprecioventa = prodprecioventa;
        this.marnombre = marnombre;
        this.marcomision = marcomision;
        this.linnombre = linnombre;
        this.lincomision = lincomision;
    }

    //getter y setter comisiones ventas
    public int getId_comision() {
        return id_comision;
    }

    public void setId_comision(int id_comision) {
        this.id_comision = id_comision;
    }
    public double getMarcomision(){
        return marcomision;
    }

    public void setMarcomision(double marcomision) {
        this.marcomision = marcomision;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public double getMonto_ventas() {
        return monto_ventas;
    }

    public void setMonto_ventas(double monto_ventas) {
        this.monto_ventas = monto_ventas;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public double getVentas_adicionales() {
        return ventas_adicionales;
    }

    public void setVentas_adicionales(double ventas_adicionales) {
        this.ventas_adicionales = ventas_adicionales;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }
    
    public String getCppcodigo() {
        return cppcodigo;
    }

    public void setCppcodigo(String cppcodigo) {
        this.cppcodigo = cppcodigo;
    }

    //getter y setter vendedores
    public int getVenid() {
        return venid;
    }

    public void setVenid(int venid) {
        this.venid = venid;
    }

    public String getVennombre() {
        return vennombre;
    }

    public void setVennombre(String vennombre) {
        this.vennombre = vennombre;
    }

    public String getVentelefono() {
        return ventelefono;
    }

    public void setVentelefono(String ventelefono) {
        this.ventelefono = ventelefono;
    }

    public String getVencorreo() {
        return vencorreo;
    }

    public void setVencorreo(String vencorreo) {
        this.vencorreo = vencorreo;
    }

    //getter y setter productos
    public int getProid() {
        return proid;
    }

    public void setProid(int proid) {
        this.proid = proid;
    }

    public String getProdnombre() {
        return prodnombre;
    }

    public void setProdnombre(String prodnombre) {
        this.prodnombre = prodnombre;
    }

    public double getProdprecioventa() {
        return prodprecioventa;
    }

    public void setProdprecioventa(double prodprecioventa) {
        this.prodprecioventa = prodprecioventa;
    }
    
    //getter y setter marca
    public String getMarnombre() {
        return marnombre;
    }

    public void setMarnombre(String marnombre) {
        this.marnombre = marnombre;
    }

    //getter y setter linea
    public String getLinnombre() {
        return linnombre;
    }

    public void setLinnombre(String linnombre) {
        this.linnombre = linnombre;
    }

    public double getLincomision() {
        return lincomision;
    }

    public void setLincomision(double lincomision) {
        this.lincomision = lincomision;
    }
       public double getProcomision() {
        return procomision;
    }

    public void setProcomision(double procomision) {
        this.procomision = procomision;
    }

    //constructor vacio
    public clsComisionVentas() {
    }

    //tostring
    @Override
    public String toString() {
        return "clsComisionVentas{" + "id_comision=" + id_comision + ", id_empleado=" + id_empleado + ", monto_ventas=" + monto_ventas + ", meta=" + meta + ", ventas_adicionales=" + ventas_adicionales + ", comision=" + comision + ", venid=" + venid + ", vennombre=" + vennombre + ", ventelefono=" + ventelefono + ", vencorreo=" + vencorreo + ", proid=" + proid + ", prodnombre=" + prodnombre + ", prodprecioventa=" + prodprecioventa + ", marnombre=" + marnombre + ", linnombre=" + linnombre + ", lincomision=" + lincomision + ", cppcodigo=" + cppcodigo + '}';
    }
}