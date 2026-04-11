/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
public class clsAsignacionAplicacionPerfil {
    int Aplcodigo, Percodigo;
    private String APLPins, APLPsel, APLPupd, APLPdel, APLPrep;

    public clsAsignacionAplicacionPerfil(String APLPins, String APLPsel, String APLPupd, String APLPdel, String APLPrep) {
        this.APLPins = APLPins;
        this.APLPsel = APLPsel;
        this.APLPupd = APLPupd;
        this.APLPdel = APLPdel;
        this.APLPrep = APLPrep;
    }

    public void setAplcodigo(int Aplcodigo) {
        this.Aplcodigo = Aplcodigo;
    }

    public int getAplcodigo() {
        return Aplcodigo;
    }

    public void setPercodigo(int Percodigo) {
        this.Percodigo = Percodigo;
    }

    public int getPercodigo() {
        return Percodigo;
    }

    public String getAPLPins() {
        return APLPins;
    }

    public void setAPLPins(String APLPins) {
        this.APLPins = APLPins;
    }

    public String getAPLPsel() {
        return APLPsel;
    }

    public void setAPLPsel(String APLPsel) {
        this.APLPsel = APLPsel;
    }

    public String getAPLPupd() {
        return APLPupd;
    }

    public void setAPLPupd(String APLPupd) {
        this.APLPupd = APLPupd;
    }

    public String getAPLPdel() {
        return APLPdel;
    }

    public void setAPLPdel(String APLPdel) {
        this.APLPdel = APLPdel;
    }

    public String getAPLPrep() {
        return APLPrep;
    }

    public void setAPLPrep(String APLPrep) {
        this.APLPrep = APLPrep;
    }

    public clsAsignacionAplicacionPerfil(int Aplcodigo, int Percodigo) {
        this.Aplcodigo = Aplcodigo;
        this.Percodigo = Percodigo;
    }

    public clsAsignacionAplicacionPerfil() {
   
    }

    @Override
    public String toString() {
        return "clsAsignacionAplicacionPerfil{" + "Aplcodigo=" + Aplcodigo + ", Percodigo=" + Percodigo + ", APLPins=" + APLPins + ", APLPsel=" + APLPsel + ", APLPupd=" + APLPupd + ", APLPdel=" + APLPdel + ", APLPrep=" + APLPrep + '}';
    }

}
