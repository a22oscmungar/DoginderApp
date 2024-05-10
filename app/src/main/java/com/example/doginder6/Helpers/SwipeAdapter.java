package com.example.doginder6.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.doginder6.Objects.Usuario2;
import com.example.doginder6.R;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import com.google.android.material.imageview.ShapeableImageView;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SwipeAdapter extends BaseAdapter {
    private Context context;
    public List<Usuario2> list;
    public Koloda koloda;
    private UserClickListener userClickListener;
    private int currentIndex = 0;
    public final String URL = "http://doginder.dam.inspedralbes.cat:3745/";
    public final String URL2 = "http://192.168.19.88:3745/";
    public Retrofit retrofit;
    public doginderAPI doginderAPI;
    public int idUsu;

    //creamos el constructor con el context, los usuarios, la koloda, el listener y el id del usuario
    public SwipeAdapter(Context context, List<Usuario2> list, Koloda koloda, UserClickListener userClickListener, int idUsu){
        this.context = context;
        this.list = list;
        this.koloda = koloda;
        this.userClickListener = userClickListener;
        this.idUsu = idUsu;


        configureKolodaListener();

    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // Devuelve el objeto en la posición dada
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getIdUsu(); // Puedes cambiar esto según la estructura real de tu objeto Usuario
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koloda,parent,false);
        }else {
            view = convertView;
        }
        //para cada usuario se asignan sus valores a los elementos de la vista
        Usuario2 user = list.get(position);
        Log.d("pruebaCosas", user.toString());

        ShapeableImageView imageView = view.findViewById(R.id.image);
        TextView tvNombre = view.findViewById(R.id.tvNombreMascota);
        ImageView btnNo = view.findViewById(R.id.btnNo);
        ImageView btnSi = view.findViewById(R.id.btnSi);
        TextView tvEdad = view.findViewById(R.id.tvEdad);
        TextView tvRaza = view.findViewById(R.id.tvRaza);
        TextView tvSexo = view.findViewById(R.id.tvSexo);
        ImageButton btnMas = view.findViewById(R.id.btnMas);
        ImageButton btnUser = view.findViewById(R.id.btnUser);
        TextView tvDescripcion = view.findViewById(R.id.tvDescripcion);
        TextView tvRelacionMascotas = view.findViewById(R.id.tvRelacionMascotas);
        TextView tvRelacionHumanos = view.findViewById(R.id.tvRelacionHumanos);
        ImageView ivSexo = view.findViewById(R.id.ivGeneroMascota);
        TextView tvTamano = view.findViewById(R.id.tvTamano);

        tvNombre.setText(user.getNombre());
        String url = "http://doginder.dam.inspedralbes.cat:3745"+user.getFoto();
        Picasso.get().load(url).error(R.drawable.two).into(imageView);
        //http://doginder.dam.inspedralbes.cat:3745/uploads/perro1.jpg

        //String añoString = (user.getEdad() > 1) ? " años" : " año";

        if(user.getSexo().equals("Hembra"))
            Glide.with(this.context)
                    .load(R.drawable.hembra)
                    .into(ivSexo);
        else{
            Glide.with(this.context)
                    .load(R.drawable.macho)
                    .into(ivSexo);
        }

        tvTamano.setText(user.getTamano());
        tvRaza.setText(user.getRaza());
        tvDescripcion.setText(user.getDescripcion());
        tvRelacionMascotas.setText("Como se lleva con otras mascotas? " + user.getRelacionMascotas() );
        tvRelacionHumanos.setText("Como se lleva con humanos? " + user.getRelacionHumanos());

        // Parsea la cadena de fecha a un objeto ZonedDateTime
        ZonedDateTime zonedDateTime = null;
        ZonedDateTime zonedDateTimeMascota = null;
        // Convierte el Instant a un LocalDate
        LocalDate fechaNacimiento = null;
        LocalDate fechaNacimientoMascota = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zonedDateTime = ZonedDateTime.parse(user.getEdadUsu());
            zonedDateTimeMascota = ZonedDateTime.parse(user.getEdad());

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

        tvEdad.setText("Edad: " + edadEnAniosMascota + " años");

        //se asignan los listeners a los botones
        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koloda.onClickRight();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koloda.onClickLeft();
            }
        });

        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Aqui saldrá la información sobre " + user.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });

        long finalEdadEnAnios = edadEnAnios;
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Crear un ImageView y establecer la imagen usando Picasso
                ImageView imageView = new ImageView(context);
                String url = "http://doginder.dam.inspedralbes.cat:3745" + user.getImgProfile();
                Log.d("pruebaSwipe", url);
                Picasso.get().load(url).error(R.drawable.two).resize(500,600).into(imageView); // Utiliza tu imagen de error personalizada

                // Crear el texto con formato utilizando SpannableString
                SpannableString spannableString = new SpannableString("Nombre: " + user.getNombreUsu() + " " + user.getApellidosUsu()
                        + "\nEdad: " + finalEdadEnAnios + " años\nSexo: " + user.getGenero());

// Aplicar estilo negrita al texto "Nombre", "Edad" y "Sexo"
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Nombre
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), spannableString.toString().indexOf("Edad"), spannableString.toString().indexOf("Edad") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Edad
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), spannableString.toString().indexOf("Sexo"), spannableString.toString().indexOf("Sexo") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Sexo

// Aumentar el tamaño de la letra para "Nombre", "Edad" y "Sexo"
                spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Nombre
                spannableString.setSpan(new RelativeSizeSpan(1.2f), spannableString.toString().indexOf("Edad"), spannableString.toString().indexOf("Edad") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Edad
                spannableString.setSpan(new RelativeSizeSpan(1.2f), spannableString.toString().indexOf("Sexo"), spannableString.toString().indexOf("Sexo") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Sexo

// Crear un AlertDialog.Builder y establecer el título, el mensaje y la vista personalizada
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Dueño/a de " + user.getNombre());
                builder.setMessage(spannableString); // Utilizar el texto con formato
                builder.setPositiveButton("Cerrar", null);
                builder.setView(imageView); // Establecer la vista personalizada con el ImageView
                builder.show();


            }
        });



        return view;
    }

    public interface UserClickListener {
        void onSwipeRight(Usuario2 user);

        void onSwipeLeft(Usuario2 user);

        void onClickRight(Usuario2 user);

        void onClickLeft(Usuario2 user);

        void onLongPress(Usuario2 user);
    }

    private void configureKolodaListener() {
        koloda.setKolodaListener(new KolodaListener() {
            @Override
            public void onNewTopCard(int i) {
                currentIndex = i;
            }

            @Override
            public void onEmptyDeck() {

            }

            @Override
            public void onCardSingleTap(int i) {

            }

            @Override
            public void onCardDrag(int i, @NonNull View view, float v) {

            }

            @Override
            public void onCardDoubleTap(int i) {

            }

            @Override
            public void onCardSwipedRight(int i) {
                if (userClickListener != null && currentIndex >= 0 && currentIndex < list.size()) {
                    Usuario2 user = list.get(currentIndex);
                    userClickListener.onSwipeRight(user);
                    currentIndex++;

                    //Toast.makeText(context, "Has hecho swipe a la derecha a " + user.getNombre()+ " id: "+ user.getIdUsu(), Toast.LENGTH_SHORT).show();
                    darLike(idUsu, user.getIdUsu());

                }
            }

            @Override
            public void onCardSwipedLeft(int i) {
                if (userClickListener != null && currentIndex >= 0 && currentIndex < list.size()) {
                    Usuario2 user = list.get(currentIndex);
                    userClickListener.onSwipeLeft(user);
                    currentIndex++;

                    //Toast.makeText(context, "Has hecho swipe a la izquierda a " + user.getNombre() + " id: "+ user.getIdUsu(), Toast.LENGTH_SHORT).show();
                    darDislike(idUsu, user.getIdUsu());
                }
            }

                @Override
                public void onClickRight(int i) {
                    if (userClickListener != null && currentIndex >= 0 && currentIndex < list.size()) {
                        Usuario2 user = list.get(currentIndex);

                        userClickListener.onClickRight(user);
                        currentIndex++;
                        //Toast.makeText(context, "Has hecho swipe a la derecha a " + user.getNombre() + " id: "+ user.getIdUsu(), Toast.LENGTH_SHORT).show();


                        darLike(idUsu, user.getIdUsu());
                    }
                }

                @Override
                public void onClickLeft(int i) {

                    if (userClickListener != null && currentIndex >= 0 && currentIndex < list.size()) {
                        Usuario2 user = list.get(currentIndex);

                        userClickListener.onClickLeft(user);
                        currentIndex++;
                        //Toast.makeText(context, "Has hecho swipe a la izquierda a " + user.getNombre() + " id: "+ user.getIdUsu(), Toast.LENGTH_SHORT).show();

                        darDislike(idUsu, user.getIdUsu());
                    }
                }

            @Override
            public void onCardLongPress(int i) {

            }
        });
    }

    //en caso de deslizar a la derecha, se envía un like al servidor
    public void darLike(int idUsu1, int idUsu2){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        Call<Void> call = doginderAPI.enviarInteraccion(idUsu1, idUsu2, "LIKE");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){

                }else{
                    Toast.makeText(context, "notSuccesfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
                Log.d("pruebaSwipe", t.getMessage());
            }
        });
    };

    //en caso de deslizar a la izquierda, se envía un dislike al servidor
    public void darDislike(int idUsu1, int idUsu2){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        Call<Void> call = doginderAPI.enviarInteraccion(idUsu1, idUsu2, "DISLIKE");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    //Toast.makeText(context, "Has dado dislike!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "notSuccesfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    };



}

