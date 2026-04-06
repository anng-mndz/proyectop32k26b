/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author isaias 9959-24-1672
 */
public class clsAsignacionPerfilUsuario {
    private int UsuId;
    private int Percodigo;
    
    public clsAsignacionPerfilUsuario()
    {
    }
    public int getUsuId()
    {
    return UsuId;
    }
    public void setUsuId(int UsuId)
    {
     this.UsuId=UsuId;
    }
    public int getPercodigo()
    {
    return Percodigo;
    }
    public void setPercodigo(int Percodigo)
    {
     this.Percodigo=Percodigo;
    }
    
    public clsAsignacionPerfilUsuario(int UsuId)
    {
     this.UsuId = UsuId;
    }
    
    public clsAsignacionPerfilUsuario(int UsuId, int Percodigo)
    {
     this.UsuId = UsuId;
     this.Percodigo = Percodigo;
    }
    @Override
    public String toString()
    {
     return "AsignacionPerfilUsuario{ UsuId=" + UsuId + ", Percodigo=" + Percodigo + "}";  
    }
    }
    
