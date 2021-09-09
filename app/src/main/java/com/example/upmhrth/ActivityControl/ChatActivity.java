package com.example.upmhrth.ActivityControl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upmhrth.Entidades.FirebaseControl.Mensaje;
import com.example.upmhrth.Entidades.LogicaApp.LogicalMessage;
import com.example.upmhrth.Entidades.LogicaApp.LogicalUser;
import com.example.upmhrth.utilidades.MessengerDAO;
import com.example.upmhrth.R;
import com.example.upmhrth.Adaptadores.MensajeAdaptador;
import com.example.upmhrth.utilidades.UserDataAccessObject;
import com.example.upmhrth.utilidades.Constantes;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private StorageReference storageReference;

    private String KEY_RECEPTOR, fileDir;

    private CircleImageView fotoPerfil;
    private TextView nombreUsuario;
    private RecyclerView rvMesanjes;
    private EditText txtMensaje;
    private ImageButton btnEnviar, btnSubirArchivo;

    // Adaptador para mensajes
    private MensajeAdaptador adapter;
    Uri pathURi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        storageReference = FirebaseStorage.getInstance().getReference();

        // Inicializacion de las variables
        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        nombreUsuario = (TextView) findViewById(R.id.nombreUsuario);
        rvMesanjes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (ImageButton) findViewById(R.id.btnEnviar);
        btnSubirArchivo = (ImageButton) findViewById(R.id.btnSubirArchivo);

        Bundle recibirKey = getIntent().getExtras();      // Recibe los datos de la anterior actividad para delcararlos porsteriormente en esta actividad
        if(recibirKey !=  null){
            KEY_RECEPTOR = recibirKey.getString("keyReceptor");      // Guarda la CLAVE del usuario RECEPTOR
            nombreUsuario.setText(recibirKey.getString("NameUser"));   // Guarda el NOMBERE del usuario RECEPTOR
        }else{ finish(); }    // Si no se recibe los parametros, la actividad es finalizada

        adapter = new MensajeAdaptador(this);      // Se crea un nuevo objeto llamado adapter de la clase MensajeAdaptador. Esta clase contendra las debidas funcionalidades para nuestro RECYLERVIEW.
        LinearLayoutManager llm = new LinearLayoutManager(this);     // Se declara un objeto que sera el que administre el LINEAR LAYOUT que contiene el RECYCLERVIEW.
        rvMesanjes.setLayoutManager(llm);             // Le indica que RECYCLERVIEW es el que se usara dentro de LINEARLAYOUT.
        rvMesanjes.setAdapter(adapter);               // Se indica la clase (ADAPTER) que ocupara el RECYCLERVIEW para el manejo de sus datos.

        // se observan los cambios del RYCLERVIEW por medio del Adaptador
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);  // Por cada nuevo elemento insertado en RECYCLERVIEW se mandara a llamar al metodo setScrollBar
                setScrollbar();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensajeEnviar = txtMensaje.getText().toString();
                if(!mensajeEnviar.isEmpty()){   sendMessage(mensajeEnviar, "0", ""); }
                else{ Toast.makeText(ChatActivity.this, "Error: Mensaje vacio", Toast.LENGTH_LONG).show(); }
            }
        });

        btnSubirArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption();
            }
        });

        // BD
        // database = FirebaseDatabase.getInstance();
        // databaseReference = database.getReference(Constantes.NODO_MENSAJES+"/"+UserDataAccessObject.getInstancia().getKeyUser()+"/"+KEY_RECEPTOR);  //databaseReference.setValue("Hello, World!"); // Sala de chat (nombre de la sala)
        FirebaseDatabase.getInstance().getReference(Constantes.NODO_MENSAJES) // Se busca o crea la referencia del NODO (Nodo para mensajes) de la base de datos que se quiere entrar en FB
                 .child(UserDataAccessObject.getInstancia().getKeyUser())    // Se obtiene la llave del usuario EMISOR del mensaje para agregar a la referencia de FB
                 .child(KEY_RECEPTOR)                                       // Se obtiene la llave del usuario RECEPTOR del mensaje para agregar a la referencia de FB
                 .addChildEventListener(new ChildEventListener() {

                 Map<String, LogicalUser> usuariosTemporales = new HashMap<>();  // Se crea un tipo "Diccionario" (Map<K,V>) donde Hasmap indica: Los elementos que inserta en el map no tendrán un orden específico. No aceptan claves duplicadas ni valores nulos.

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   final Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                   final LogicalMessage mensajeLogico = new LogicalMessage(dataSnapshot.getKey(), mensaje);
                   final int posicion = adapter.addMensaje(mensajeLogico);

                   if(usuariosTemporales.get(mensaje.getKeyEmisor())!=null){
                       mensajeLogico.setUserLM(usuariosTemporales.get(mensaje.getKeyEmisor()));
                       adapter.updateMessage(posicion, mensajeLogico);
                   }else{
                       UserDataAccessObject.getInstancia().getUserInformationByKey(mensaje.getKeyEmisor(), new UserDataAccessObject.IDevolverUsuario() {
                           @Override
                           public void returnUser(LogicalUser LUsuario) {
                               usuariosTemporales.put(mensaje.getKeyEmisor(),LUsuario);  // Se guardan los datos en el diccionario temporal obtenido del metodo returnUser
                               mensajeLogico.setUserLM(LUsuario);
                               adapter.updateMessage(posicion,mensajeLogico);
                           }
                           @Override
                           public void devolverError(String error) {
                               Toast.makeText(ChatActivity.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
                }
                @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
                @Override public void onChildRemoved(DataSnapshot dataSnapshot) { }
                @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void sendMessage(String mensajeEnviar, String archivo, String token) {
        Mensaje mensaje = new Mensaje();  // Se crea un obejeto de la clase MENSAJE
            mensaje.setMensaje(mensajeEnviar);
            mensaje.setContenedorArchivo(archivo);
            if(archivo.equals("1")){ mensaje.setUrlArchivo(token); }
            mensaje.setKeyEmisor(UserDataAccessObject.getInstancia().getKeyUser());
        MessengerDAO.getInstancia().newMessage(UserDataAccessObject.getInstancia().getKeyUser(), KEY_RECEPTOR, mensaje); //   databaseReference.push().setValue(mensaje);
        txtMensaje.setText("");
    }

    // Ubica el scrollbar en la pocision del ultimo mensaje mostrado en pantalla
    private void setScrollbar(){ rvMesanjes.scrollToPosition(adapter.getItemCount()-1); }

    private void selectOption() {
        validation();
        AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
        dialog.setTitle(R.string.option);
        String[] items = {"Galeria", "Camara"};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0: openGalery();   break;
                    case 1: openCamera();   break;
                }
            }
        });
        AlertDialog dialogoConstruido = dialog.create();
        dialogoConstruido.show();
    }

    public void openGalery() {
        Intent galery = new Intent(Intent.ACTION_GET_CONTENT);
        galery.setType("image/*");
        galery.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(galery,"Selecciona una foto"), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode, resultCode, resultData);
        switch (requestCode){
            case 0: if(resultCode == RESULT_OK){
                Uri uriFile = resultData.getData();
                upLoadToStorage(uriFile);
            }   break;
            case 1: if(resultCode == RESULT_OK){
                MediaScannerConnection.scanFile(ChatActivity.this, new String[]{fileDir}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uriFile) {
                        Log.i("ExternalStorage Scanned", s);    Log.i("ExternalStorage Uri", String.valueOf(uriFile));
                        upLoadToStorage(uriFile);
                    }
                });
            }   break;
        }
    }

    private void upLoadToStorage(Uri uriFile) {
        String imageName = uriFile.getLastPathSegment().substring(uriFile.getLastPathSegment().lastIndexOf('/') + 1);
        final StorageReference fotoReferencia = storageReference.child(Constantes.NODO_FOTOS).child(imageName);
        fotoReferencia.putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fotoReferencia.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            String uriStorage = task.getResult().toString();
                            Log.d("URI uriStorage: ", uriStorage);      Log.d("Foto Referencia: ", String.valueOf(fotoReferencia.getDownloadUrl()));
                            Toast.makeText(ChatActivity.this, "Archivo almacenado correctamente", Toast.LENGTH_SHORT).show();
                            String mensajeEnviar = txtMensaje.getText().toString();
                            sendMessage(mensajeEnviar, "1", uriStorage);
                        }else{
                            Toast.makeText(ChatActivity.this, "Error: Archivo no se pudo subir", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void openCamera(){

       Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       if (camara.resolveActivity(getPackageManager()) != null) {

           File fileName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constantes.ALBUM_FOTOS);
           if (!fileName.exists()) { fileName.mkdirs(); }

           String imageFileName, timeStamp;
           timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
           imageFileName = "JPEG_" + timeStamp;
           fileDir = fileName.getPath() + File.separator + imageFileName + ".jpg";
           Log.d("path", String.valueOf(fileDir));

           File image = new File(fileDir);
         //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           pathURi = Uri.parse(image.getAbsolutePath());
         //  }else {
        //       pathURi = Uri.fromFile(image);
         //  }    Log.d("pathURi", String.valueOf(pathURi));
           // Mayor a API 21
           Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", new File(String.valueOf(pathURi)));

           camara.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
           startActivityForResult(camara, 1);
       }
    }

    public void validation(){
       if(ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
       }
   }

}