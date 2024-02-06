package com.example.doginder6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

public class FragmentPerfil extends Fragment {
    View rootView;
    DataBaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
        db = new DataBaseHelper(rootView.getContext(), "MiPerfil", null, 1);

        SharedPreferences preferences = rootView.getContext().getSharedPreferences("credenciales", rootView.getContext().MODE_PRIVATE);
        Usuario2 usuario = db.getUsuarioCompleto(preferences.getInt("id", 0));
        Log.d("usuario", usuario +" Id: "+ preferences.getInt("id", 0));

        TextView nombre = rootView.findViewById(R.id.tvNombreMascota);
        ImageView foto = rootView.findViewById(R.id.ivFotoMascota);
        TextView raza = rootView.findViewById(R.id.tvRazaMascota);
        TextView edad = rootView.findViewById(R.id.tvEdadMascota);
        TextView sexo = rootView.findViewById(R.id.tvSexoMascota);
        TextView descripcion = rootView.findViewById(R.id.tvDescripcionMascota);
        TextView relacionMascotas = rootView.findViewById(R.id.tvRelacionMascotas);
        TextView relacionHumanos = rootView.findViewById(R.id.tvRelacionHumanos);
        TextView nombreHumano = rootView.findViewById(R.id.tvNombreHumano);
        TextView edadHumano = rootView.findViewById(R.id.tvEdadHumano);
        TextView generoHumano = rootView.findViewById(R.id.tvGeneroHumano);

        String añoString = (usuario.getEdad() > 1) ? " años" : " año";



        nombre.setText(usuario.getNombre().toUpperCase());
        String url = "http://doginder.dam.inspedralbes.cat:3745"+usuario.getDescripcion();
        Log.d("url", url);
        //Picasso.get().load(url).error(R.drawable.two).into(foto);

        Glide.with(this)
                .load(url)  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(80))) // Ajusta el radio según tus preferencias
                .into(foto);

        raza.setText(usuario.getRaza());
        edad.setText(String.valueOf(usuario.getEdad()) + añoString);
        sexo.setText(usuario.getSexo());
        descripcion.setText(usuario.getFoto());
        relacionMascotas.setText("Como se lleva con otras mascotas? "+ usuario.getRelacionMascotas());
        relacionHumanos.setText("Como se lleva con humanos? " + usuario.getRelacionHumanos());
        nombreHumano.setText(usuario.getNombreUsu() + " " + usuario.getApellidosUsu());
        edadHumano.setText(usuario.getEdadUsu() + " años");
        generoHumano.setText(usuario.getGenero());


        return rootView;
    }
}
