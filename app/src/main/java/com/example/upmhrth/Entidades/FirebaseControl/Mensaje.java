package com.example.upmhrth.Entidades.FirebaseControl;

import com.google.firebase.database.ServerValue;

public class Mensaje {

    private String mensaje, contenedorArchivo, keyEmisor, urlArchivo;
    private Object createdTimestamp;

    public Mensaje() { createdTimestamp = ServerValue.TIMESTAMP; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getContenedorArchivo() { return contenedorArchivo; }
    public void setContenedorArchivo(String contenedorArchivo) { this.contenedorArchivo = contenedorArchivo; }

    public String getKeyEmisor() { return keyEmisor; }
    public void setKeyEmisor(String keyEmisor) { this.keyEmisor = keyEmisor; }

    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }

    public void setCreatedTimestamp(Object createdTimestamp) { this.createdTimestamp = createdTimestamp; }
    public Object getCreatedTimestamp() { return createdTimestamp; }

}