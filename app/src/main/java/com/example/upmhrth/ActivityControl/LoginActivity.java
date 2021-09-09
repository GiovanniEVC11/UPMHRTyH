package com.example.upmhrth.ActivityControl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upmhrth.MainActivity.MenuActivity;
import com.example.upmhrth.R;
import com.example.upmhrth.mensajesApp;
import com.example.upmhrth.utilidades.UserDataAccessObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
// Librerias Firebase
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private CardView btnLoginCv, btnRegisterCv;
    private EditText correoET, passwordET;   // Entradas
    private TextView recoverPassword;
    private FirebaseAuth mAuth;  // Declaracion de variable de tipo Autentication de Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();  // Se consigue instanciar en esta clase el servicio Autentication de Firebase
        btnLoginCv  = (CardView) findViewById(R.id.btnLoginCv);
        btnRegisterCv = (CardView) findViewById(R.id.btnRegisterCv);
        correoET = (EditText)  findViewById(R.id.userET);  passwordET = (EditText) findViewById(R.id.passwordET);
        recoverPassword = (TextView) findViewById(R.id.txtViewForgotPassword);

        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflanter = getLayoutInflater();
                mensajesApp.recuperarPassword(LoginActivity.this, inflanter, mAuth);
            }
        });

        btnLoginCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){  // Valida campos vacios
                    String email = correoET.getText().toString(), password = passwordET.getText().toString();  // Declaracion de varaiables EMAIL y PASSWORD
                    if(isValidEmail(email) && isValidPassword(password)){  // Validacion de las caracteristicas principales de varaibles EMAIL y PASSWORD
                        mAuth.signInWithEmailAndPassword(email, password). // Se elige el modo de acceso de EMAIL y PASSWORD
                                addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    entrar();
                                    Toast.makeText(LoginActivity.this, "Entrando (key): " + UserDataAccessObject.getInstancia().getKeyUser(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Email y/o Password Incorrecto", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this, "Email o Password no cumple con los requisitos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnRegisterCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
            }
        });

    }

    // Metodo que valida la longitud del password
    private boolean isValidPassword(String password) {
        if(password.length() >= 6){ return true; }else{ return false;}
    }

    // Metodo que valida si es un Email
    private boolean isValidEmail(CharSequence email){ // A CharSequence is a readable sequence of char values.
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();  // Valida si se ingreso el email y si tiene la estructura de un email
    }// Mas: TextUtil in a CharSequece ==> the text to truncate; TextUtils.indexOf(string, char)  = similar a =  string.indexOf(char);

    // Metodo que valida los campos no esten vacios
    public boolean validarCampos(){
        if(correoET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Ingrese los datos para entrar", Toast.LENGTH_LONG).show();
            return false;
        }else{ return true; }
    }

    // Metodo para pasar al activity menu
    public void entrar(){ startActivity(new Intent(LoginActivity.this, MenuActivity.class)); finish(); }

    @Override
    protected void onResume() {
        super.onResume();
        // Valida si existe un usuario logeado a la aplicacion, si es asi muestra la pantalla Menu, de lo contrario muestra en pantalla Login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            entrar();
        }
    }
}