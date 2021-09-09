// Name: Giovanni Efrain Villanueva Caballero
package com.example.upmhrth.Entidades.LogicaApp;

import com.example.upmhrth.Entidades.FirebaseControl.Usuario;
import com.example.upmhrth.utilidades.UserDataAccessObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogicalUser {

    private String key;
    private Usuario user;

    // CONSTRUCTOR
    public LogicalUser(String key, Usuario user) {
        this.key = key;
        this.user = user;
    }

    // GETS
    public String getKeyLU() { return key; }
    public Usuario getUserLU() { return user; }

    // SETS
    public void setKeyLU(String key) { this.key = key; }
    public void setUserLU(Usuario user) { this.user = user; }

    // METODOS DE LA CLASE

    // Metodo para retornar un casting (castear) de la variable TimestampCreacion de tipo Object a tipo Long
   // public long getTimestampLongCreation(){ return (long) user.getCreatedTimestamp(); }

    // Metodo para obtener la fecha de creacion de Usuario en formato dd/MM/yyyy
    public String getDateCreation(){
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());  // Objeto con la Estructura de la fecha y de que locación (En este caso por defecto)
        Date date = new Date(UserDataAccessObject.getInstancia().dateCreatedLong());  // Se crea Objeto de tipo Date con el valor que retorna getTimestampLongCreation()
        return SDF.format(date); // Se le da el formato establecido por la variable SPF a la variable DATE de tipo Date
    }

    public String getDateLastConnection(){
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());  // Objeto con la Estructura de la fecha y de que locación (En este caso por defecto)
        Date date = new Date(UserDataAccessObject.getInstancia().dateLastConnection());  // Se crea Objeto de tipo Date con el valor que retorna getTimestampLongCreation()
        return SDF.format(date);
    }

}