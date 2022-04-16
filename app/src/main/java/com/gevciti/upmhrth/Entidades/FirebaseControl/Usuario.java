package com.gevciti.upmhrth.Entidades.FirebaseControl;

public class Usuario {

    private String name, email; // photoPerfilURl;

    public Usuario() { }

    // SETS
    public void setCorreo(String correo) {
        this.email = correo;
    }
    public void setNombre(String nombre) {
        this.name = nombre;
    }
  //  public void setFotoPerfilURl(String FotoPerfilURl) { this.photoPerfilURl = FotoPerfilURl; }

    // GETS
    public String getCorreo() {
        return email;
    }
    public String getNombre() {
        return name;
    }
  //  public String getFotoPerfilURl() { return photoPerfilURl; }

}