package com.example.doginder6.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doginder6.Objects.Usuario;
import com.example.doginder6.R;
import com.example.doginder6.Helpers.doginderAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    Button btnSiguiente, btnFoto, btnFotoPerfil, btnRegistro, btnConfirmarMail, btnPass;
    EditText etName, etSurname, etMail1, etMail2, etPass1, etPass2, etNombreMascota, etDescripcion, etEdadPerro, etEdadPersona;
    TextView tvSexo, tvRelacionPersonas, tvRelacionMascotas;
    int edadPerro, edadPersona;
    Retrofit retrofit;
    String nombre, apellidos, mail, pass1, pass2, genero, nombreMascota, raza, descripcion, relacionPersonas, relacionMascotas, sexoPerro;
    ScrollView svMascota;
    RadioGroup rgGenero, rgRelacionPersonas, rgRelacionMascotas, rgSexoPerro;
    Spinner spinnerRaza;
    TextView oLogin, tvSobreTi, tvNombre, tvApellidos, tvMail, tvMail2, tvContrasena, tvContrasena2, tvGenero, tvEdad, tvSobreMascota;

    com.example.doginder6.Helpers.doginderAPI doginderAPI;
    ImageView ivFoto, ojo1, ojo2, ivFotoPerfil;
    double latitude = 0.0;
    double longitude = 0.0;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    Uri imageUriPerfil;
    private static final int PICK_IMAGE_REQUEST_USER = 1; // Código de solicitud para la imagen del usuario
    private static final int PICK_IMAGE_REQUEST_PROFILE = 2; // Código de solicitud para la imagen del perfil


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        genero = "";

        //inicializamos los elementos
        btnSiguiente = findViewById(R.id.btnSiguiente);
        etName = findViewById(R.id.etNombre);
        etSurname = findViewById(R.id.etApellidos);
        btnFoto = findViewById(R.id.btnFoto);
        ivFoto = findViewById(R.id.ivFoto);
        btnRegistro = findViewById(R.id.btnRegistrar);
        etMail1 = findViewById(R.id.etMail1);
        etMail2 = findViewById(R.id.etMail2);
        btnConfirmarMail = findViewById(R.id.btnConfirmarMail);
        etPass1 = findViewById(R.id.etPass1);
        etPass2 = findViewById(R.id.etPass2);
        btnPass = findViewById(R.id.btnPass);
        rgGenero = findViewById(R.id.rgGenero);
        svMascota = findViewById(R.id.svMascota);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        spinnerRaza = findViewById(R.id.spinnerRaza);
        etDescripcion = findViewById(R.id.etDescripcion);
        etEdadPerro = findViewById(R.id.etEdad);
        etEdadPersona = findViewById(R.id.etEdadPersona);
        rgRelacionPersonas = findViewById(R.id.rgRelacionPersonas);
        rgRelacionMascotas = findViewById(R.id.rgRelacionMascotas);
        rgSexoPerro = findViewById(R.id.rgSexoPerro);
        tvSexo = findViewById(R.id.tvSexo);
        tvRelacionMascotas = findViewById(R.id.tvRelacionMascotas);
        tvRelacionPersonas = findViewById(R.id.tvRelacionHumanos);
        tvSobreTi = findViewById(R.id.tvSobreTi);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvMail = findViewById(R.id.tvMail);
        tvMail2 = findViewById(R.id.tvMail2);
        tvContrasena = findViewById(R.id.tvContrasena);
        tvContrasena2 = findViewById(R.id.tvConfirmaContrasena);
        tvGenero = findViewById(R.id.tvGenero);
        tvEdad = findViewById(R.id.tvEdadPersona);
        ojo1 = findViewById(R.id.ojo1);
        ojo2 = findViewById(R.id.ojo2);
        tvSobreMascota = findViewById(R.id.tvSobreMascota);
        ivFotoPerfil = findViewById(R.id.ivImgProfile);
        btnFotoPerfil = findViewById(R.id.btnFotoPerfil);

        // Para la foto de perfil
        btnFotoPerfil.setOnClickListener(v -> {
            openFileChooser(PICK_IMAGE_REQUEST_PROFILE);
        });



        //listener para ver la contraseña o esconderla
        ojo1.setOnClickListener(v -> {
            if (etPass1.getInputType() == 129) {
                etPass1.setInputType(1);
                ojo1.setImageResource(R.drawable.ojo_cerrado);
            } else {
                etPass1.setInputType(129);
                ojo1.setImageResource(R.drawable.ojo_abierto);
            }
        });

        ojo2.setOnClickListener(v -> {
            if (etPass2.getInputType() == 129) {
                etPass2.setInputType(1);
                ojo2.setImageResource(R.drawable.ojo_cerrado);
            } else {
                etPass2.setInputType(129);
                ojo2.setImageResource(R.drawable.ojo_abierto);
            }
        });


        //adapter para las razas de los animales
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.razas_de_perro, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRaza.setAdapter(adapter);

        //listener para el spinner de las razas
        spinnerRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                raza = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                raza = null;
            }
        });

        //texto para el titulo con letras en naranja
        String titulo = getString(R.string.tvSobreTi);
        SpannableString textoParcial2 = new SpannableString(titulo);
        int color2 = ContextCompat.getColor(this, R.color.naranjaMain);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(color2);
        textoParcial2.setSpan(colorSpan2, 7, titulo.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSobreTi.setText(textoParcial2);

        //hacemos visible lo del usuario
        tvSobreTi.setVisibility(View.VISIBLE);
        etMail1.setVisibility(View.VISIBLE);
        etMail2.setVisibility(View.VISIBLE);
        etPass1.setVisibility(View.VISIBLE);
        etPass2.setVisibility(View.VISIBLE);
        etEdadPersona.setVisibility(View.VISIBLE);
        etName.setVisibility(View.VISIBLE);
        etSurname.setVisibility(View.VISIBLE);
        rgGenero.setVisibility(View.VISIBLE);


        //radriogrup para el genero de la persona
        rgGenero.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);

                if (radioButton != null) {
                    // Obtiene el texto del RadioButton seleccionado y guárdalo en una variable String
                    genero = radioButton.getText().toString();
                }
            }
        });

        //radiogrup para la relacion de la mascota con otras mascotas
        rgRelacionMascotas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton!= null){
                    relacionMascotas = radioButton.getText().toString();
                }
            }
        });

        //radiogrup para la relacion de la mascota con las personas
        rgRelacionPersonas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null){
                    relacionPersonas = radioButton.getText().toString();
                }
            }
        });

        //radiogrup para el sexo de la mascota
        rgSexoPerro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton!=null){
                    sexoPerro = radioButton.getText().toString();
                }
            }
        });
        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Verificar y solicitar permisos
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Si los permisos ya están otorgados, obtener la ubicación
            getLastLocation();
        } else {
            // Si no, solicitar permisos
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        //escondemos lo que no queremos que se vea
        btnFoto.setVisibility(View.GONE);
        ivFoto.setVisibility(View.GONE);
        btnRegistro.setVisibility(View.GONE);
        btnPass.setVisibility(View.GONE);
        svMascota.setVisibility(View.GONE);
        etDescripcion.setVisibility(View.GONE);
        tvSexo.setVisibility(View.GONE);
        rgSexoPerro.setVisibility(View.GONE);
        spinnerRaza.setVisibility(View.GONE);

        //boton para cuando ponemos info del usuario
        btnSiguiente.setOnClickListener(v ->{
            nombre = etName.getText().toString();
            apellidos = etSurname.getText().toString();
            String edadString = etEdadPersona.getText().toString();
            if(!nombre.isEmpty() && !apellidos.isEmpty() && !genero.isEmpty() && !genero.isEmpty() && !edadString.isEmpty()){
                edadPersona = Integer.parseInt(edadString);

                Log.d("prueba", "onClick: " + nombre + " " + apellidos + " " + genero + " " + edadPersona);

                confirmarMail();
            }else{
                Toast.makeText(this, "Faltan datos obligatorios!", Toast.LENGTH_SHORT).show();
            }
        });

        //boton para confirmar el mail
        btnConfirmarMail.setOnClickListener(v ->{
            confirmarMail();
        });
        //boton final
        btnRegistro.setOnClickListener(v -> {
            register();
        });

        //texto para ir al login
        oLogin = findViewById(R.id.tvOLogin);

        //texto para ir al login con enlace
        String texto = getString(R.string.oLogin);
        int color = ContextCompat.getColor(this, R.color.naranjaMain);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        SpannableString textoParcial = new SpannableString(texto);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        textoParcial.setSpan(clickableSpan, 2, 15, 0);
        textoParcial.setSpan(colorSpan, 2, 15, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        oLogin.setText(textoParcial);
        oLogin.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        oLogin.setHighlightColor(Color.TRANSPARENT);
    }

    //funcion que comprueba que las contraseñas sean iguales
    public void comprobarPass(){
        //obtenemos los dos campos de las contraseñas
        pass1 = etPass1.getText().toString();
        pass2 = etPass2.getText().toString();

        //primero comprobamos que los haya rellenado
        if(!pass1.isEmpty() && !pass2.isEmpty()){
            //y luego si son iguales
            if (pass1.equals(pass2)){
                //escondemos lo del usuario para mostrar lo de la mascota
                etPass1.setVisibility(View.GONE);
                etPass2.setVisibility(View.GONE);
                btnPass.setVisibility(View.GONE);
                tvSobreTi.setVisibility(View.GONE);
                tvNombre.setVisibility(View.GONE);
                tvApellidos.setVisibility(View.GONE);
                etName.setVisibility(View.GONE);
                etSurname.setVisibility(View.GONE);
                tvMail.setVisibility(View.GONE);
                tvMail2.setVisibility(View.GONE);
                tvContrasena.setVisibility(View.GONE);
                tvContrasena2.setVisibility(View.GONE);
                ojo1.setVisibility(View.GONE);
                ojo2.setVisibility(View.GONE);
                tvGenero.setVisibility(View.GONE);
                rgGenero.setVisibility(View.GONE);
                tvEdad.setVisibility(View.GONE);
                etEdadPersona.setVisibility(View.GONE);
                btnSiguiente.setVisibility(View.GONE);
                ivFotoPerfil.setVisibility(View.GONE);
                btnFotoPerfil.setVisibility(View.GONE);

                //colores para el titulo
                String tituloMascota = getString(R.string.tituloMascota);
                SpannableString textoParcial = new SpannableString(tituloMascota);
                int color = ContextCompat.getColor(this, R.color.naranjaMain);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                textoParcial.setSpan(colorSpan, 6, tituloMascota.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSobreMascota.setText(textoParcial);

                tvSobreMascota.setVisibility(View.VISIBLE);
                ivFoto.setVisibility(View.VISIBLE);
                btnFoto.setVisibility(View.VISIBLE);
                btnRegistro.setVisibility(View.VISIBLE);
                svMascota.setVisibility(View.VISIBLE);
                spinnerRaza.setVisibility(View.VISIBLE);
                etDescripcion.setVisibility(View.VISIBLE);
                rgRelacionPersonas.setVisibility(View.VISIBLE);
                rgRelacionMascotas.setVisibility(View.VISIBLE);
                etNombreMascota.setVisibility(View.VISIBLE);
                tvSexo.setVisibility(View.VISIBLE);
                tvRelacionPersonas.setVisibility(View.VISIBLE);
                tvRelacionMascotas.setVisibility(View.VISIBLE);
                etEdadPerro.setVisibility(View.VISIBLE);
                rgSexoPerro.setVisibility(View.VISIBLE);

                // Para la foto de la mascota
                btnFoto.setOnClickListener(v3 -> {
                    openFileChooser(PICK_IMAGE_REQUEST_USER);
                });
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Introduce la contraseña!", Toast.LENGTH_LONG).show();
        }
    }

    //funcion para abrir el explorador de archivos
    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    //funcion para registrar al usuario
    @SuppressLint("StaticFieldLeak")
    public void register() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // Convertir la imagen a RequestBody
                    File file = new File(getRealPathFromURI(imageUri));

                    // Convertir otros campos a RequestBody
                    if (!file.exists()) {
                        Toast.makeText(RegisterActivity.this, "La imagen seleccionada no es válida", Toast.LENGTH_LONG).show();
                        return null;
                    }

                    File filePerfil = new File(getRealPathFromURI(imageUriPerfil));

                    if (!filePerfil.exists()) {
                        Toast.makeText(RegisterActivity.this, "La imagen seleccionada no es válida", Toast.LENGTH_LONG).show();
                        return null;
                    }

                    // Obtener los valores de los campos de texto
                    nombre = etName.getText().toString();
                    apellidos = etSurname.getText().toString();
                    mail = etMail1.getText().toString();
                    edadPersona = Integer.parseInt(etEdadPersona.getText().toString());
                    nombreMascota = etNombreMascota.getText().toString();
                    edadPerro = Integer.parseInt(etEdadPerro.getText().toString());
                    descripcion = etDescripcion.getText().toString();

                    Log.d("pruebaRegistro", "entro al registro");
                    //pasamos a requestbody los valores
                    RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    RequestBody nameRequestBody = (nombre != null) ? RequestBody.create(MediaType.parse("text/plain"), nombre) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody latitudeRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
                    RequestBody longitudeRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
                    RequestBody surnameRequestBody = (apellidos != null) ? RequestBody.create(MediaType.parse("text/plain"), apellidos) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody mailRequestBody = (mail != null) ? RequestBody.create(MediaType.parse("text/plain"), mail) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody passRequestBody = (pass1 !=null) ? RequestBody.create(MediaType.parse("text/plain"), pass1) :  RequestBody.create(MediaType.parse("text/plain"), "" );
                    RequestBody sexoPersonaRequestBody = (genero != null) ? RequestBody.create(MediaType.parse("text/plain"), genero) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody nombreMascotaRequestBody = (nombreMascota != null) ? RequestBody.create(MediaType.parse("text/plain"), nombreMascota) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody edadPerroRequestBody = (edadPerro != 0) ? RequestBody.create(MediaType.parse("text/plain"), String.valueOf(edadPerro)) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody descripcionRequestBody = (descripcion != null) ? RequestBody.create(MediaType.parse("text/plain"), descripcion) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody relacionPersonasRequestBody = (relacionPersonas != null) ? RequestBody.create(MediaType.parse("text/plain"), relacionPersonas) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody relacionMascotasRequestBody = (relacionMascotas != null) ? RequestBody.create(MediaType.parse("text/plain"), relacionMascotas) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody sexoPerroRequestBody = (sexoPerro != null) ? RequestBody.create(MediaType.parse("text/plain"), sexoPerro) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody edadPersonaRequestBody = (edadPersona != 0) ? RequestBody.create(MediaType.parse("text/plain"), String.valueOf(edadPersona)) : RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody razaRequestBody = (raza != null) ? RequestBody.create(MediaType.parse("text/plain"), raza) : RequestBody.create(MediaType.parse("text/plain"), "");

                    //la imagen tiene que ser un multipart
                    MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagenFile", file.getName(), imageRequestBody);
                    MultipartBody.Part imagenPartPerfil = MultipartBody.Part.createFormData("imagenFile", filePerfil.getName(), imageRequestBody);

                    Log.d("pruebaFoto", imageRequestBody.toString());
                    Log.d("pruebaRegister", nameRequestBody.toString() + " " + latitudeRequestBody.toString() + " " + longitudeRequestBody.toString() + " " + surnameRequestBody.toString() + " " + imagenPart.toString());

                    //hacemos la llamada para registrar
                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                            .build();

                    doginderAPI = retrofit.create(doginderAPI.class);

                    // Utilizar directamente los RequestBody en lugar de userRequestBody
                    Call<Void> call = doginderAPI.registerUser(nameRequestBody, latitudeRequestBody, longitudeRequestBody, surnameRequestBody, mailRequestBody, passRequestBody, edadPersonaRequestBody, sexoPersonaRequestBody, nombreMascotaRequestBody, edadPerroRequestBody, sexoPerroRequestBody, razaRequestBody, descripcionRequestBody, relacionMascotasRequestBody, relacionPersonasRequestBody, imagenPart, imagenPartPerfil);

                    call.execute(); // Ejecutar la llamada de forma síncrona

                    return null;
                } catch (Exception e) {
                    Log.e("Error", "Error en el registro: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }.execute();
    }




    // Método para obtener la ruta real de la imagen seleccionada
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index);
            cursor.close();
            return path;
        }
    }

    // Método para obtener la última ubicación conocida
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Aquí obtienes la ubicación del usuario
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("prueba", "Es esta ubi: " + latitude + " " + longitude);
                            // Puedes hacer lo que necesites con la ubicación aquí
                        }
                    }
                });
    }

    //funcion para comprobar que el mail sea valido y nuevo
    @SuppressLint("StaticFieldLeak")
    public void confirmarMail() {
        // Obtenemos los dos mails
        String mail1 = etMail1.getText().toString();
        String mail2 = etMail2.getText().toString();

        // Patron para comprobar que el mail sea valido
        String patron = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(mail1);
        Matcher matcher2 = pattern.matcher(mail2);

        if (mail1.equals(mail2)) {
            if (matcher.matches() && matcher2.matches()) {
                // Si los mails son iguales y válidos, comprobamos que no estén registrados
                new AsyncTask<String, Void, Usuario>() {
                    @Override
                    protected Usuario doInBackground(String... params) {
                        try {
                            retrofit = new Retrofit.Builder()
                                    .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                                    .build();

                            doginderAPI = retrofit.create(doginderAPI.class);

                            Call<Usuario> call = doginderAPI.verifyMail(params[0]);
                            Response<Usuario> response = call.execute(); // Ejecutar la llamada de forma síncrona

                            return response.isSuccessful() ? response.body() : null;
                        } catch (IOException e) {
                            Log.e("Error", "Error al verificar el correo electrónico: " + e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Usuario usuario) {
                        if (usuario != null) {
                            // El correo ya está registrado
                            Toast.makeText(RegisterActivity.this, "Parece que ya hay un usuario con este correo electrónico :(", Toast.LENGTH_SHORT).show();
                        } else {
                            // El correo no está registrado
                            etMail1.setVisibility(View.GONE);
                            etMail2.setVisibility(View.GONE);
                            btnConfirmarMail.setVisibility(View.GONE);
                            comprobarPass();
                        }
                    }
                }.execute(mail1);
            } else {
                Toast.makeText(this, "Los formatos de los correos no son válidos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Los correos no coinciden", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_USER && data != null && data.getData() != null) {
                // Se seleccionó una imagen para el usuario
                imageUri = data.getData();
                ivFoto.setImageURI(imageUri);
            } else if (requestCode == PICK_IMAGE_REQUEST_PROFILE && data != null && data.getData() != null) {
                // Se seleccionó una imagen para el perfil
                imageUriPerfil = data.getData();
                ivFotoPerfil.setImageURI(imageUriPerfil);
            }
        }

        Log.d("pruebaImagenes", "Mascota: " + imageUri + " Perfil: " + imageUriPerfil);
    }

}