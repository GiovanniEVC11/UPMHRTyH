package com.example.upmhrth.ActivityControl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.upmhrth.R;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    private EditText nombre, correo, currentPassword, newPassword, newPassword2;
    private CardView btnModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nombre = (EditText) findViewById(R.id.nombreID);
        correo = (EditText) findViewById(R.id.correoID);
        currentPassword = (EditText) findViewById(R.id.passwordActualID);
        newPassword = (EditText) findViewById(R.id.passwordID);
        newPassword2 = (EditText) findViewById(R.id.password2ID);

        btnModificar = (CardView) findViewById(R.id.btnModifyUserCv);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateUser();
            }
        });

    }

    private void upDateUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("","");
        user.put("","");
    }
}
