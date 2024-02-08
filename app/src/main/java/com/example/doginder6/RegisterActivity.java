package com.example.doginder6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
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

    Button btnSiguiente, btnFoto, btnRegistro, btnConfirmarMail, btnPass;
    EditText etName, etSurname, etMail1, etMail2, etPass1, etPass2, etNombreMascota, etDescripcion, etEdadPerro, etEdadPersona;
    TextView tvSexo, tvRelacionPersonas, tvRelacionMascotas;
    int edadPerro, edadPersona;
    Retrofit retrofit;
    String nombre, apellidos, mail, pass1, pass2, genero, nombreMascota, raza, descripcion, relacionPersonas, relacionMascotas, sexoPerro;
    ScrollView svMascota;
    RadioGroup rgGenero, rgRelacionPersonas, rgRelacionMascotas, rgSexoPerro;
    Spinner spinnerRaza;

    doginderAPI doginderAPI;
    ImageView ivFoto;
    double latitude = 0.0;
    double longitude = 0.0;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        genero = "";

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


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.razas_de_perro, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRaza.setAdapter(adapter);

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

        etMail1.setVisibility(View.VISIBLE);
        etMail2.setVisibility(View.VISIBLE);
        btnConfirmarMail.setVisibility(View.VISIBLE);

        spinnerRaza.setVisibility(View.GONE);
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

        rgRelacionMascotas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton!= null){
                    relacionMascotas = radioButton.getText().toString();
                }
            }
        });

        rgRelacionPersonas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null){
                    relacionPersonas = radioButton.getText().toString();
                }
            }
        });

        rgSexoPerro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton!=null){
                    sexoPerro = radioButton.getText().toString();
                }
            }
        });

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

        btnFoto.setVisibility(View.GONE);
        ivFoto.setVisibility(View.GONE);
        btnRegistro.setVisibility(View.GONE);
        etName.setVisibility(View.GONE);
        etSurname.setVisibility(View.GONE);
        btnSiguiente.setVisibility(View.GONE);
        etPass1.setVisibility(View.GONE);
        etPass2.setVisibility(View.GONE);
        btnPass.setVisibility(View.GONE);
        rgGenero.setVisibility(View.GONE);
        svMascota.setVisibility(View.GONE);
        etDescripcion.setVisibility(View.GONE);
        tvSexo.setVisibility(View.GONE);
        rgSexoPerro.setVisibility(View.GONE);

        btnSiguiente.setOnClickListener(v ->{
            nombre = etName.getText().toString();
            apellidos = etSurname.getText().toString();
            String edadString = etEdadPersona.getText().toString();
            if(!nombre.isEmpty() && !apellidos.isEmpty() && !genero.isEmpty() && !genero.isEmpty() && !edadString.isEmpty()){
                edadPersona = Integer.parseInt(edadString);

                Log.d("prueba", "onClick: " + nombre + " " + apellidos + " " + genero + " " + edadPersona);
                etName.setVisibility(View.GONE);
                etSurname.setVisibility(View.GONE);
                rgGenero.setVisibility(View.GONE);
                etEdadPersona.setVisibility(View.GONE);
                btnSiguiente.setVisibility(View.GONE);
                etPass1.setVisibility(View.VISIBLE);
                etPass2.setVisibility(View.VISIBLE);
                btnPass.setVisibility(View.VISIBLE);
                btnPass.setOnClickListener(v2 ->{
                    comprobarPass();
                });
            }else{
                Toast.makeText(this, "Faltan datos obligatorios!", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirmarMail.setOnClickListener(v ->{
            confirmarMail();
        });
        btnRegistro.setOnClickListener(v -> {
            register();
        });
    }

    public void comprobarPass(){
        pass1 = etPass1.getText().toString();
        pass2 = etPass2.getText().toString();

        if(!pass1.isEmpty() && !pass2.isEmpty()){
            if (pass1.equals(pass2)){
                etPass1.setVisibility(View.GONE);
                etPass2.setVisibility(View.GONE);
                btnPass.setVisibility(View.GONE);
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

                btnFoto.setOnClickListener(v3 -> {
                    openFileChooser();
                });
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Introduce la contraseña!", Toast.LENGTH_LONG).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void register() {
        // Convertir la imagen a RequestBody
        File file = new File(getRealPathFromURI(imageUri));

        // Convertir otros campos a RequestBody
        if (file == null || !file.exists()) {
            Toast.makeText(this, "La imagen seleccionada no es válida", Toast.LENGTH_LONG).show();
            return;
        }

        nombre = etName.getText().toString();
        apellidos = etSurname.getText().toString();
        mail = etMail1.getText().toString();
        edadPersona = Integer.parseInt(etEdadPersona.getText().toString());

        nombreMascota = etNombreMascota.getText().toString();
        edadPerro = Integer.parseInt(etEdadPerro.getText().toString());
        descripcion = etDescripcion.getText().toString();
        
        Log.d("pruebaRegistro", "entro al registro");
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

        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagenFile", file.getName(), imageRequestBody);

        Log.d("pruebaFoto", imageRequestBody.toString());
        Log.d("pruebaRegister", nameRequestBody.toString() + " " + latitudeRequestBody.toString() + " " + longitudeRequestBody.toString() + " " + surnameRequestBody.toString() + " " + imagenPart.toString());

        retrofit = new Retrofit.Builder()
                .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        // Utilizar directamente los RequestBody en lugar de userRequestBody
        Call<Void> call = doginderAPI.registerUser(nameRequestBody, latitudeRequestBody, longitudeRequestBody, surnameRequestBody, mailRequestBody, passRequestBody, edadPersonaRequestBody, sexoPersonaRequestBody, nombreMascotaRequestBody, edadPerroRequestBody, sexoPerroRequestBody, razaRequestBody, descripcionRequestBody, relacionMascotasRequestBody, relacionPersonasRequestBody, imagenPart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error al registrar usuario onFailure", Toast.LENGTH_SHORT).show();
                Log.d("prueba", "onFailure: " + t.getMessage());
            }
        });
    }



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

    public void confirmarMail(){
        String mail1 = etMail1.getText().toString();
        String mail2 = etMail2.getText().toString();

        String patron = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(mail1);
        Matcher matcher2 = pattern.matcher(mail2);

        if(mail1.equals(mail2)){
            if(matcher.matches() && matcher2.matches()) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                        .build();

                doginderAPI = retrofit.create(doginderAPI.class);

                Call<Usuario> call = doginderAPI.verifyMail(mail1);

                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()) {
                            Usuario usuario = response.body();
                            if (usuario != null) {
                                Toast.makeText(RegisterActivity.this, "Parece que ya hay un usuario con este correo electrónico :(", Toast.LENGTH_SHORT).show();
                            } else {
                                // El correo no está registrado

                                etMail1.setVisibility(View.GONE);
                                etMail2.setVisibility(View.GONE);
                                btnConfirmarMail.setVisibility(View.GONE);
                                etName.setVisibility(View.VISIBLE);
                                etSurname.setVisibility(View.VISIBLE);
                                btnSiguiente.setVisibility(View.VISIBLE);
                                rgGenero.setVisibility(View.VISIBLE);
                                etEdadPersona.setVisibility(View.VISIBLE);

                            }
                        } else if (response.code() == 404) {
                            // Manejar el caso de correo no registrado
                            etMail1.setVisibility(View.GONE);
                            etMail2.setVisibility(View.GONE);
                            btnConfirmarMail.setVisibility(View.GONE);
                            etName.setVisibility(View.VISIBLE);
                            etSurname.setVisibility(View.VISIBLE);
                            btnSiguiente.setVisibility(View.VISIBLE);
                            rgGenero.setVisibility(View.VISIBLE);
                            etEdadPersona.setVisibility(View.VISIBLE);
                        } else {
                            // Otro tipo de error
                            Log.d("prueba", "Error: " + response.code() + ", " + response.message());
                            Toast.makeText(RegisterActivity.this, "Algo falla", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "error onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(this, "Los formatos de los correos no son validos", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Actualiza el ImageView con la imagen seleccionada
            ivFoto.setImageURI(imageUri);

            // Puedes agregar más código aquí si necesitas realizar alguna acción adicional con la imagen seleccionada
        }
    }
}