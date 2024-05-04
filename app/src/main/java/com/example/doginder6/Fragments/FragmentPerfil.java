package com.example.doginder6.Fragments;

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

import com.example.doginder6.Helpers.DataBaseHelper;
import com.example.doginder6.Objects.Usuario2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.doginder6.R;

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
        Log.d("usuarioMiPerfil", usuario.toString());

        TextView nombre = rootView.findViewById(R.id.tvNombreMascota);
        ImageView foto = rootView.findViewById(R.id.ivFotoMascota);
        TextView raza = rootView.findViewById(R.id.tvRazaMascota);
        TextView edad = rootView.findViewById(R.id.tvEdadMascota);
        TextView descripcion = rootView.findViewById(R.id.tvDescripcionMascota);
        TextView relacionMascotas = rootView.findViewById(R.id.tvRelacionMascotas);
        TextView relacionHumanos = rootView.findViewById(R.id.tvRelacionHumanos);
        TextView nombreHumano = rootView.findViewById(R.id.tvNombreHumano);
        TextView edadHumano = rootView.findViewById(R.id.tvEdadHumano);
        TextView generoHumano = rootView.findViewById(R.id.tvGeneroHumano);
        ImageView GeneroMascota = rootView.findViewById(R.id.ivGeneroMascota);
        TextView tamano = rootView.findViewById(R.id.tvTamano);

        String añoString = (usuario.getEdad() > 1) ? " años" : " año";



        nombre.setText(usuario.getNombre().toUpperCase());
        nombre.setShadowLayer(5, 10, 10, R.color.black);
        String url = "http://doginder.dam.inspedralbes.cat:3745"+usuario.getDescripcion();
        Log.d("url", url);
        //Picasso.get().load(url).error(R.drawable.two).into(foto);

        Glide.with(this)
                .load(url)  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))) // Ajusta el radio según tus preferencias
                .into(foto);

        if(usuario.getSexo().equals("Hembra"))
            Glide.with(this)
                    .load(R.drawable.hembra)
                    .into(GeneroMascota);
        else{
            Glide.with(this)
                    .load(R.drawable.macho)
                    .into(GeneroMascota);
        }

        tamano.setText(usuario.getTamano());
        raza.setText(usuario.getRaza());
        edad.setText(String.valueOf(usuario.getEdad()) + añoString);
        descripcion.setText(usuario.getFoto());
        switch (usuario.getRelacionMascotas()){
            case "Bien":
                relacionMascotas.setText("Se lleva bien con otras mascotas, guay!");
                break;
            case "Mal":
                relacionMascotas.setText("No se lleva bien con otras mascotas, cuidado!");
                break;
            case "Indiferente":
                relacionMascotas.setText("No le importa otras mascotas, tranquilo!");
                break;
        }
        switch (usuario.getRelacionHumanos()) {
            case "Bien":
                relacionHumanos.setText("Se lleva bien con humanos, guay!");
                break;
            case "Mal":
                relacionHumanos.setText("No se lleva bien con humanos, cuidado!");
                break;
            case "Indiferente":
                relacionHumanos.setText("No le importa los humanos, tranquilo!");
                break;
        }
        String nombreCompleto = usuario.getNombreUsu() + " " + usuario.getApellidosUsu();
        nombreHumano.setText("Mi nombre: " + nombreCompleto.toUpperCase());
        edadHumano.setText("Mi edad: " + usuario.getEdadUsu() + " AÑOS");
        generoHumano.setText("Mi sexo: " + usuario.getGenero().toUpperCase());


        return rootView;
    }
}
