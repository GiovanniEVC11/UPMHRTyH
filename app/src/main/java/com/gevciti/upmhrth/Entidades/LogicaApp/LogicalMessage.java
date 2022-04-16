package com.gevciti.upmhrth.Entidades.LogicaApp;
import com.gevciti.upmhrth.Entidades.FirebaseControl.Mensaje;

import java.text.SimpleDateFormat;
import java.util.Date;

// Esta clase sirve para mantener los mensajes separados.
public class LogicalMessage {

    private String key;  // Clave del mensaje (en FB se vera una serie de letras y numeros que se asigna a cada mensaje)
    private Mensaje message;  // Se declara una variable de tipo MENSAJE
    private LogicalUser logicalUser; //

    public LogicalMessage(String key, Mensaje mensaje) {
        this.key = key;
        this.message = mensaje;
    }

    // GETS
    public String getKeyLM() { return key; }
    public Mensaje getMessageLM() { return message; }
    public LogicalUser getUserLM() { return logicalUser; }
    public long getCreatedTimestampLong(){ return (long) message.getCreatedTimestamp(); }  // Se obtiene la fecha de creacion del mensaje de la CLASE MENSAJE

    // SETS
    public void setKeyLM(String key) { this.key = key; }
    public void setMessageLM(Mensaje mensaje) { this.message = mensaje; }
    public void setUserLM(LogicalUser logicalUser) { this.logicalUser = logicalUser; }

    // Convierte la fecha del mensaje que se obtiene de la funcion getCreatedTimestamLong a uno mas adecuado para mostrar en pantalla al usuario
    public String getMessageCreatedLM(){
     //   Long codigoHora = listMensaje.get(position).getHora();
        Date date = new Date(getCreatedTimestampLong()); // codigoHora
        SimpleDateFormat SDF = new SimpleDateFormat("hh:mm a");  // a pm/am
        return SDF.format(date);
    }

}   // revisado