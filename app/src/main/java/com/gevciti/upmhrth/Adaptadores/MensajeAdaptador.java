package com.gevciti.upmhrth.Adaptadores;

import com.bumptech.glide.Glide;
import com.gevciti.upmhrth.Entidades.LogicaApp.LogicalMessage;
import com.gevciti.upmhrth.Entidades.LogicaApp.LogicalUser;
import com.gevciti.upmhrth.Holders.MensajeHolder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

import com.gevciti.upmhrth.R;
import com.gevciti.upmhrth.utilidades.UserDataAccessObject;

public class MensajeAdaptador extends RecyclerView.Adapter<MensajeHolder> {

    private List<LogicalMessage> listMensaje = new ArrayList<>();
    private Context c;

    // A Context is a handle to the system; it provides services like resolving resources, obtaining access to databases and preferences, and so on.
    // An Android app has activities. Context is like a handle to the environment your application is currently running in.
    public MensajeAdaptador(Context c) { this.c = c; }

    public int addMensaje(LogicalMessage mensajeLogico){
        listMensaje.add(mensajeLogico);
        int posicion = listMensaje.size()-1; // anadido
        notifyItemInserted(listMensaje.size());
        return posicion; // anadido
    }

    public void updateMessage(int posicion, LogicalMessage mensajeLogico){
        listMensaje.set(posicion, mensajeLogico); notifyItemChanged(posicion);
    }

    @NonNull
    @Override
    public MensajeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == 1){
            v = LayoutInflater.from(c).inflate(R.layout.mensajes_emisor, parent, false); }
        else{
            v = LayoutInflater.from(c).inflate(R.layout.mensajes_receptor, parent, false);  }
        return new MensajeHolder(v);
    }

    // LayoutInflater: Crea una instancia de un archivo XML de diseño en sus objetos de vista correspondientes.
        //LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //view = vi.inflate(R.layout.adjuntar_en_un_viewgroup, null);

    // It is called by RecyclerView to display the data at the specified position. This method is used to update the contents of the itemView to reflect the item at the given position.
    @Override
    public void onBindViewHolder(@NonNull MensajeHolder holder, int position) {

        LogicalMessage lmessage = listMensaje.get(position);
        LogicalUser luser = lmessage.getUserLM();  // Se declara el objeto luser el cual define de quien es el usuario

        if(luser != null){
            Log.d("LogicalUser =  ", String.valueOf(luser));
            holder.getNombre().setText(luser.getUserLU().getNombre());     // Glide.with(c).load(lu.getUserLU().getFotoPerfilURl()).into(holder.getFotoMensaje());
        }

        holder.getMensaje().setText(lmessage.getMessageLM().getMensaje());  // BIEN

        if(lmessage.getMessageLM().getContenedorArchivo().equals("1")){
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(lmessage.getMessageLM().getUrlArchivo()).into(holder.getFotoMensaje());
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
        }else{
            if(lmessage.getMessageLM().getContenedorArchivo().equals("0")){
                holder.getMensaje().setVisibility(View.VISIBLE);
                holder.getFotoMensaje().setVisibility(View.GONE);
            }
        }
        holder.getHora().setText(lmessage.getMessageCreatedLM());   // BIEN
    }

    // This method returns the size of the collection that contains the items we want to display. (Returns the number of items in the adapter bound to the parent RecyclerView).
    @Override
    public int getItemCount() { return listMensaje.size(); }

    @Override
    public int getItemViewType(int position) {
        if(listMensaje.get(position).getUserLM()!= null){
            if(listMensaje.get(position).getUserLM().getKeyLU().equals(UserDataAccessObject.getInstancia().getKeyUser())){
                Log.d("Emisor KeyDAO: ", String.valueOf(UserDataAccessObject.getInstancia().getKeyUser()) );
                Log.d("Emisor KeyListLU: ", String.valueOf(listMensaje.get(position).getUserLM().getKeyLU()) );
                return 1;  // Es el Mensaje enviado por mi
            }else{
                Log.d("Receptor KeyListLU: ", String.valueOf(listMensaje.get(position).getUserLM().getKeyLU()) );
                return -1;
            } // Mensaje recibido
        }else{
            return -1;
        }
    }
}
