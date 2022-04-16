package com.gevciti.upmhrth.ActivityControl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gevciti.upmhrth.Entidades.FirebaseControl.Usuario;
import com.gevciti.upmhrth.R;
import com.gevciti.upmhrth.utilidades.Constantes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarActivity extends AppCompatActivity {

    private CardView btnRegistrar;
    private EditText correo, nombre, password, password2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference referenceUser;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnRegistrar = (CardView) findViewById(R.id.btnRegisterUserCv);
        correo = (EditText) findViewById(R.id.correoID);
        nombre = (EditText) findViewById(R.id.nombreID);
        password = (EditText) findViewById(R.id.passwordID);
        password2 = (EditText) findViewById(R.id.password2ID);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    final String email, password, nombreUsuario; email = correo.getText().toString(); password = password2.getText().toString(); nombreUsuario = nombre.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrarActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegistrarActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                        Usuario user = new Usuario();
                                            user.setCorreo(email);
                                            user.setNombre(nombreUsuario);

                                        currentUser = mAuth.getCurrentUser();
                                        referenceUser = database.getReference(Constantes.NODO_USUARIOS + "/" + currentUser.getUid());
                                        referenceUser.setValue(user);
                                        Toast.makeText(RegistrarActivity.this, "Registro Exitoso",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
                                    }else{
                                        Toast.makeText(RegistrarActivity.this, "Error al registrar",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(RegistrarActivity.this, "Error: Compruebe que los datos sean correctos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validarCampos() {
        if(correo.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || password.getText().toString().isEmpty() || password2.getText().toString().isEmpty()){
            return false;
        }else{
            if(password.getText().toString().equals(password2.getText().toString())){
                return true;
            }else {
                return false;
            }
        }
    }
}
