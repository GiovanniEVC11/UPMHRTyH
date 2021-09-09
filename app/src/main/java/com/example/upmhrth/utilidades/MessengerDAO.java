package com.example.upmhrth.utilidades;

import com.example.upmhrth.Entidades.FirebaseControl.Mensaje;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessengerDAO {

    private static MessengerDAO mensajeriaDAO;

    private FirebaseDatabase database;
    private DatabaseReference referenceMensajeria;

    private MessengerDAO(){
        database = FirebaseDatabase.getInstance();
        referenceMensajeria = database.getReference(Constantes.NODO_MENSAJES);
    }

    public static MessengerDAO getInstancia(){
        if(mensajeriaDAO == null) mensajeriaDAO = new MessengerDAO();
        return mensajeriaDAO;
    }

    public void newMessage(String keyEmisor, String keyReceptor, Mensaje message){
        DatabaseReference referenceEmisor = referenceMensajeria.child(keyEmisor).child(keyReceptor);
        DatabaseReference referenceReceptor = referenceMensajeria.child(keyReceptor).child(keyEmisor);
        referenceEmisor.push().setValue(message);
        referenceReceptor.push().setValue(message);
    }
}