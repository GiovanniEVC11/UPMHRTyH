package com.example.upmhrth.ActivityControl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.upmhrth.R;
import com.example.upmhrth.utilidades.Constantes;
import com.example.upmhrth.utilidades.UserDataAccessObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AppActivity extends AppCompatActivity {

    private ImageButton DropMyChats, DropAllUsers, DropAllChats, DropImagesChat;
    private FirebaseDatabase database;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        DropMyChats = (ImageButton) findViewById(R.id.btnDropMyChats);
        DropAllUsers = (ImageButton) findViewById(R.id.btnDropUsers);
        DropAllChats = (ImageButton) findViewById(R.id.btnDropChats);
        DropImagesChat = (ImageButton) findViewById(R.id.btnDropImages);

        DropAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropUsers();
            }
        });

        DropAllChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropChats();
            }
        });

        DropMyChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropMyChats();
            }
        });

        DropImagesChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropImagesChat();
            }
        });

    }

    private void dropMyChats() {
        database.getReference().child(Constantes.NODO_MENSAJES).child(UserDataAccessObject.getInstancia().getKeyUser()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AppActivity.this, "Se eliminaron tus mensajes", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AppActivity.this, "Error al eliminar tus mensajes", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dropImagesChat() {

/*
        storageReference.child(Constantes.NODO_FOTOS).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AppActivity.this, "Se elimino todas las imagenes de la base de datos", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AppActivity.this, "Error al eliminar las imagenes de la base de datos ", Toast.LENGTH_LONG).show();
            }
        });
*/
    }

    private void dropChats() {
        database.getReference().child(Constantes.NODO_MENSAJES).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AppActivity.this, "Se elimino todos los mensajes de la base de datos", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AppActivity.this, "Error al eleminar los mensajes de la base de datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dropUsers() {
        database.getReference().child(Constantes.NODO_USUARIOS).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AppActivity.this, "Se eliminaron todos los usuarios de la base de datos", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AppActivity.this, "Error al eliminiar los usuarios de la base de datos", Toast.LENGTH_LONG).show();

            }
        });
    }

}
