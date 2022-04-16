package com.gevciti.upmhrth.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.gevciti.upmhrth.ActivityControl.ChatActivity;
import com.gevciti.upmhrth.ActivityControl.LoginActivity;
import com.gevciti.upmhrth.ActivityControl.PerfilActivity;
import com.gevciti.upmhrth.ActivityControl.AppActivity;
// import com.example.upmhrth.DriveServiceHelper;
import com.gevciti.upmhrth.Entidades.FirebaseControl.Usuario;
import com.gevciti.upmhrth.Entidades.LogicaApp.LogicalUser;
import com.gevciti.upmhrth.R;
import com.gevciti.upmhrth.utilidades.Constantes;
import com.gevciti.upmhrth.utilidades.UserDataAccessObject;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
/*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
*/
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
/*
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.api.client.json.gson.GsonFactory;
*/

public class MenuActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private TabHost tabh;
    private FirebaseRecyclerAdapter adapterUsers;

    // #######3
    NotificationCompat.Builder notificacion;
    private static final int idUnico = 222;
   // DriveServiceHelper driveServiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        rvUsuarios = (RecyclerView) findViewById(R.id.rvUsuarios);
        tabh = (TabHost) findViewById(R.id.tabHost);

        tabh.setup();
        TabHost.TabSpec spec;  // Se instancia una variable para separar las pestanas de TABHOST

            spec = tabh.newTabSpec("Contactos");  // Se declara una nueva categoria
            spec.setIndicator("Contáctos RyTH");  // Indica el nombre de la pestana a mostrar en pantalla
            spec.setContent(R.id.Contactos); // Indica que layout contiene - id del layout
        tabh.addTab(spec);  // Se agrega la pestana en pantalla

            spec = tabh.newTabSpec("BitacorasXdepartamento");
            spec.setIndicator("Bitácoras"); // Indica el nombre de la pestana a mostrar en pantalla
            spec.setContent(R.id.Bitacoras); // Indica que layout contiene - id del layout
        tabh.addTab(spec); // Se agrega la pestana en pantalla

        tabh.setCurrentTab(1);  // Indica cual pestana debe iniciar. Sigue el orden secuencial que se le dio anteriormente.

    // ************* LIsta de usuarios

        obtenerUsuarios();

       // notificacion = new NotificationCompat.Builder(this);
       // notificacion.setAutoCancel(true);

       // requestSignIn();

    }

    private void obtenerUsuarios() {

        LinearLayoutManager llmUser = new LinearLayoutManager(this);  // El LayoutManager es el encargado de añadir y reusar los views en el recycler.
        // Su función es calcular las posiciones fuera del foco del usuario y así reemplazar el contenido de un ítem fuera del volumen visual por el contenido de otro.
        // Esto reduce los tiempos de ejecución, ya que el número de infladas es menor.
        rvUsuarios.setLayoutManager(llmUser);

        Query query = FirebaseDatabase.getInstance().getReference().child(Constantes.NODO_USUARIOS);
        final FirebaseRecyclerOptions<Usuario> options = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(query, Usuario.class).build();

        adapterUsers = new FirebaseRecyclerAdapter<Usuario, holderUsuarios>(options){

            @Override
            public holderUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarios, parent, false);
                return new holderUsuarios(view);
            }

            @Override
            protected void onBindViewHolder(holderUsuarios holder, int position, Usuario model) {
                final LogicalUser user = new LogicalUser(getSnapshots().getSnapshot(position).getKey(), model);

                if(UserDataAccessObject.getInstancia().getKeyUser().equals(user.getKeyLU())){
                   holder.itemViewToGone();
                }else{
                    // Glide.with(MenuActivity.this).load(model.getFotoPerilUsuarioURl()).into(holder.getCivFotoPeril());
                    holder.getUsuarioList().setText(model.getNombre());  // Obtiene la etiqueta xml de la nueva VIEW de HOLDER USUARIO para añadirle el nombre de usuario por medio de la clase USUARIO (el modelo)
                    // Se crea un nuevo obejto de la CLASE LOGICAL USER para añadirle la clave de usuario y la clase USUARIO (el modelo)
                    holder.getCvUsuario().setOnClickListener(new View.OnClickListener() {  // Si se oprime algun CARDVIEW generado con el nombre de usuario
                        @Override
                        public void onClick(View view) {   // Se iniciara una nueva actividad (CHAT ACTIVITY) pasando como parametros el nombre y clave del usuario
                            Toast.makeText(MenuActivity.this, "Key: "+user.getKeyLU(), Toast.LENGTH_SHORT).show();
                            Intent entrarSalaChat = new Intent(MenuActivity.this, ChatActivity.class);
                                entrarSalaChat.putExtra("keyReceptor", user.getKeyLU());
                                entrarSalaChat.putExtra("NameUser", user.getUserLU().getNombre());
                            startActivity(entrarSalaChat);
                        }
                    });
                }

            }

        };
        rvUsuarios.setAdapter(adapterUsers);  // El adaptador añade los valores al RECYCLER VIEW de la actividad
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterUsers.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterUsers.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflanter = getMenuInflater();
        inflanter.inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuToolBar1: return true;
                case R.id.menuToolBar1_1: startActivity(new Intent(this, AppActivity.class)); return true;
                case R.id.menuToolBar1_2: return true;
            case R.id.menuToolBar2: startActivity(new Intent(this, PerfilActivity.class)); return true;
            case R.id.menuToolBar3: FirebaseAuth.getInstance().signOut(); startActivity(new Intent(this, LoginActivity.class)); finish(); return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!UserDataAccessObject.getInstancia().isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class)); finish();
        }
    }

    //  #############3#############3#############3#############3#############3#############3#############3#############3
/*
    private void requestSignIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this,signInOptions);
        startActivityForResult(client.getSignInIntent(), 400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 400: if(resultCode == RESULT_OK){
                handleSignInIntent(data);
            } break;
        }
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        GoogleAccountCredential credential = GoogleAccountCredential
                                .usingOAuth2(MenuActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccount(googleSignInAccount.getAccount());

                        Drive googleDriveService = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("Mi app Drive")
                                .build();

                        driveServiceHelper = new DriveServiceHelper(googleDriveService);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void uploadFile(View v){
        ProgressDialog progressDialog = new ProgressDialog(MenuActivity.this);
        progressDialog.setTitle("Subiendo a Gooogle Drive");
        progressDialog.setMessage("Espere un momento");
        progressDialog.show();

        String filePath = "/storage/emulated/0/ejemplo.pdf";
        driveServiceHelper.createFile(filePath).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();

                notificacion.setSmallIcon(R.mipmap.ic_launcher);
                notificacion.setTicker("Nueva notificacion");
                notificacion.setWhen(System.currentTimeMillis());
                notificacion.setContentTitle("Archivo en la nube");
                notificacion.setContentText("El archivo subio correctamente");

                Intent in = new Intent(MenuActivity.this,MenuActivity.class);
                PendingIntent pi = PendingIntent.getActivity(MenuActivity.this, 0, in, PendingIntent.FLAG_IMMUTABLE);
                notificacion.setContentIntent(pi);


                NotificationManager note = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                note.notify(idUnico, notificacion.build());

                Toast.makeText(getApplicationContext(), "Good for you", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Check your google drive appi key", Toast.LENGTH_LONG).show();
            }
        });
    }

    */
}
