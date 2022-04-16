package com.gevciti.upmhrth.MainActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gevciti.upmhrth.R;

import de.hdodenhof.circleimageview.CircleImageView;

class holderUsuarios extends RecyclerView.ViewHolder {

    private TextView usuarioList;
    private CardView cvUsuario;
    private CircleImageView civFotoPerfil;
    private LinearLayout cvLinearLayout;

    public holderUsuarios(@NonNull View itemView) {
        super(itemView);
        //fotoPerfilUsuario = (CircleImageView) itemView.findViewById(R.id.fotoPerfilUsuarios);
        usuarioList = itemView.findViewById(R.id.usuarioIdList);
        cvUsuario = itemView.findViewById(R.id.cvUsuarioID);
        civFotoPerfil = itemView.findViewById(R.id.fotoPerfilUsuario);
        cvLinearLayout = itemView.findViewById(R.id.cvLinearLayoutId);
    }

    public LinearLayout getCvLienarLayout() {
        return cvLinearLayout;
    }

    public CardView getCvUsuario() {
        return cvUsuario;
    }

    public void setCvUsuarioID(CardView cvUsuario) {
        this.cvUsuario = cvUsuario;
    }

    public TextView getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(TextView usuarioList) {
        this.usuarioList = usuarioList;
    }

    public void setCvUsuario(CardView cvUsuario) {
        this.cvUsuario = cvUsuario;
    }

    public CircleImageView getCivFotoPerfil() {
        return civFotoPerfil;
    }

    public void setCivFotoPerfil(CircleImageView civFotoPerfil) {
        this.civFotoPerfil = civFotoPerfil;
    }

    public void itemViewToGone() {
        getCvUsuario().setVisibility(View.GONE);
        getCivFotoPerfil().setVisibility(View.GONE);
        getUsuarioList().setVisibility(View.GONE);
        getCvLienarLayout().setVisibility(View.GONE);
    }
}
