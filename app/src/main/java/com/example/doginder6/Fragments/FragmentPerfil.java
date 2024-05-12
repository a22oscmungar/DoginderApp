package com.example.doginder6.Fragments;

import android.content.SharedPreferences;
import android.os.Build;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

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
        ImageView ivFotoHumano = rootView.findViewById(R.id.ivFotoHumano);

        //String añoString = (usuario.getEdad() > 1) ? " años" : " año";



        nombre.setText(usuario.getNombre().toUpperCase());
        nombre.setShadowLayer(5, 10, 10, R.color.black);
        String url = "http://doginder.dam.inspedralbes.cat:3745"+usuario.getDescripcion();
        String url2 = "http://doginder.dam.inspedralbes.cat:3745"+usuario.getImgProfile();
        //Picasso.get().load(url).error(R.drawable.two).into(foto);

        Glide.with(this)
                .load(url)  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))) // Ajusta el radio según tus preferencias
                .into(foto);

        Glide.with(this)
                .load(url2)  // Reemplaza con tu recurso de imagen
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))) // Ajusta el radio según tus preferencias
                .into(ivFotoHumano);

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


        // Parsea la cadena de fecha a un objeto ZonedDateTime
        ZonedDateTime zonedDateTime = null;
        ZonedDateTime zonedDateTimeMascota = null;
        // Convierte el Instant a un LocalDate
        LocalDate fechaNacimiento = null;
        LocalDate fechaNacimientoMascota = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zonedDateTime = ZonedDateTime.parse(usuario.getEdadUsu());
            zonedDateTimeMascota = ZonedDateTime.parse(usuario.getEdad());

        }

        // Convierte el ZonedDateTime a un LocalDate en la zona horaria local
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fechaNacimiento = zonedDateTime.toLocalDate();
            fechaNacimientoMascota = zonedDateTimeMascota.toLocalDate();

        }

        LocalDate fechaActual = null;
        LocalDate fechaActualMascota = null;
        // Obtener la fecha actual
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fechaActual = LocalDate.now();
            fechaActualMascota = LocalDate.now();

        }

        long edadEnAnios = 0;
        long edadEnAniosMascota = 0;

        // Calcular la diferencia en años entre la fecha actual y la fecha de nacimiento
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            edadEnAnios = ChronoUnit.YEARS.between(fechaNacimiento, fechaActual);
            edadEnAniosMascota = ChronoUnit.YEARS.between(fechaNacimientoMascota, fechaActualMascota);
        }
        if(edadEnAniosMascota == 0)
            edad.setText("Menos de un año");
        else if(edadEnAniosMascota == 1)
            edad.setText(edadEnAniosMascota + " AÑO");
        else if(edadEnAniosMascota > 1) edad.setText(edadEnAniosMascota + " AÑOS");

        if (edadEnAnios == 0)
            edadHumano.setText("Menos de un año");
        else if (edadEnAnios == 1)
            edadHumano.setText("Mi edad: " + edadEnAnios + " AÑO");
        else if (edadEnAnios > 1) edadHumano.setText("Mi edad: " + edadEnAnios + " AÑOS");

        generoHumano.setText("Mi sexo: " + usuario.getGenero().toUpperCase());


        return rootView;
    }
}
