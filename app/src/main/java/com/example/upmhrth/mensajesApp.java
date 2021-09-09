package com.example.upmhrth;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class mensajesApp {

    private static AlertDialog.Builder builder;
    private static AlertDialog dialog;
    private static View view;

    private static ProgressDialog progressDialog;

    public static void recuperarPassword(final Context context, LayoutInflater inflanter, final FirebaseAuth mAuth) {
        builder = new AlertDialog.Builder(context);
        view = inflanter.inflate(R.layout.alertdialog_password, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        CardView btnRestaurar = view.findViewById(R.id.btnRecoverCv);
        CardView btnCancelar = view.findViewById(R.id.btnCancelCv);
        final EditText email = view.findViewById(R.id.txtEmail);
        progressDialog = new ProgressDialog(context);

        btnRestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().isEmpty()){
                    Toast.makeText(context.getApplicationContext() , "Error: Campo Vacio.", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.setMessage("Cargando solicitud");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.setLanguageCode("es");  // Idioma con el cual se mandara el mensaje
                    mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(context.getApplicationContext() , "Revise su Correo Electrónico.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(context.getApplicationContext() , "Correo Electrónico Invalido. Intente más Tarde.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}