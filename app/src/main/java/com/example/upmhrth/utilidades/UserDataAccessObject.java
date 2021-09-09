package com.example.upmhrth.utilidades;

import androidx.annotation.NonNull;

import com.example.upmhrth.Entidades.FirebaseControl.Usuario;
import com.example.upmhrth.Entidades.LogicaApp.LogicalUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataAccessObject {

    public interface IDevolverUsuario{
        public void returnUser(LogicalUser LUsuario);
        public void devolverError(String error);
    }

    private static UserDataAccessObject UserDAO;
    private FirebaseDatabase database;
    private DatabaseReference usersReference;

    private UserDataAccessObject(){
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference(Constantes.NODO_USUARIOS);
    }

    public static UserDataAccessObject getInstancia(){
        if(UserDAO == null) UserDAO = new UserDataAccessObject();
        return UserDAO;
    }

    public boolean isLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null; // if(user!=null){ return true; }else{ return false; }
    }

    // Se insatancia FirebaseAuth en la clase y recupera el Uid (Id del Usuario) de la base de datos de Firebase
    public String getKeyUser(){ return FirebaseAuth.getInstance().getUid(); }

    public String getUserKey(){
        if(getKeyUser().equals(database.getReference().child(Constantes.NODO_USUARIOS).getKey())){
            return database.getReference().child(Constantes.NODO_USUARIOS).getKey();
        }
        return null;
    }

    public long dateCreatedLong(){ return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp(); }

    public long dateLastConnection() { return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp(); }

    public void getUserInformationByKey(final String key, final IDevolverUsuario iDevolverUsuario){
        usersReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() { // Se hace referencia a la clave del usuario que esta en FB y
            @Override// A DataSnapshot instance contains data from a Firebase Database location. Any time you read Database data, you receive the data as a DataSnapshot.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);   // Se obtienen los valores de FB del nodo USUARIOS  en un objeto Usuario de la clase USUARIO
                LogicalUser LUsuario = new LogicalUser(key,usuario);      // Se guarda la llave del usuario con sus datos en la clase Logical USER
                iDevolverUsuario.returnUser(LUsuario);                     // Devuelve los valores obtenidos de LOGICAL USER al metodo  de interfaz ... El cual se usara en el CHAT ACTIVITY
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iDevolverUsuario.devolverError(databaseError.getMessage());
            }

        });
    }

}

