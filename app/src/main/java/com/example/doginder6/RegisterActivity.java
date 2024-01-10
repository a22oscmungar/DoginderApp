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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.location.LocationListener;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    Button btnSiguiente, btnFoto, btnRegistro, btnMain;
    EditText etName, etSurname, etMail;
    Retrofit retrofit;
    String nombre, apellidos, mail;

    doginderAPI doginderAPI;
    ImageView ivFoto;
    double latitude = 0.0;
    double longitude = 0.0;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationHelper locationHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSiguiente = findViewById(R.id.btnSiguiente);
        etName = findViewById(R.id.etNombre);
        etSurname = findViewById(R.id.etApellidos);
        btnFoto = findViewById(R.id.btnFoto);
        ivFoto = findViewById(R.id.ivFoto);
        btnRegistro = findViewById(R.id.btnRegistrar);
        etMail = findViewById(R.id.etMail);
        btnMain = findViewById(R.id.btnMain);

        btnMain.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnFoto.setVisibility(View.GONE);
        ivFoto.setVisibility(View.GONE);
        btnRegistro.setVisibility(View.GONE);

        // Verificar y solicitar permisos si es necesario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            Log.d("prueba", "onCreate: No hay permisos");
        } else {
            // Inicializar y comenzar las actualizaciones de ubicación
            startLocationUpdates();
            Log.d("prueba", "onCreate: Hay permisos");
        }

        btnSiguiente.setOnClickListener(v -> {
            // Ocultar vistas que ya no necesitas mostrar
            etName.setVisibility(View.GONE);
            etSurname.setVisibility(View.GONE);
            btnSiguiente.setVisibility(View.GONE);
            btnFoto.setVisibility(View.VISIBLE);
            ivFoto.setVisibility(View.VISIBLE);
            btnRegistro.setVisibility(View.VISIBLE);
            etMail.setVisibility(View.GONE);

            btnFoto.setOnClickListener(v1 -> {
                openFileChooser();
            });
        });

        btnRegistro.setOnClickListener(v -> {
            register();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("prueba", "onActivityResult: "+requestCode+" "+resultCode+" "+data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Aquí verificas los permisos antes de obtener la última ubicación conocida
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                latitude = locationHelper.getLastKnownLocation().getLatitude();
                longitude = locationHelper.getLastKnownLocation().getLongitude();
            } else {
                Log.d("prueba", "onActivityResult: No hay permisos");
            }

            imageUri = data.getData();
            ivFoto.setImageURI(imageUri);
        }
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
        mail = etMail.getText().toString();

        Log.d("pruebaRegistro", "entro al registro");
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody nameRequestBody = (nombre != null) ? RequestBody.create(MediaType.parse("text/plain"), nombre) : RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody latitudeRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
        RequestBody longitudeRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
        RequestBody surnameRequestBody = (apellidos != null) ? RequestBody.create(MediaType.parse("text/plain"), apellidos) : RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mailRequestBody = (mail != null) ? RequestBody.create(MediaType.parse("text/plain"), mail) : RequestBody.create(MediaType.parse("text/plain"), "");


        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagenFile", file.getName(), imageRequestBody);

        Log.d("pruebaFoto", imageRequestBody.toString());
        Log.d("pruebaRegister", nameRequestBody.toString() + " " + latitudeRequestBody.toString() + " " + longitudeRequestBody.toString() + " " + surnameRequestBody.toString() + " " + imagenPart.toString());

        retrofit = new Retrofit.Builder()
                .baseUrl("http://doginder.dam.inspedralbes.cat:3745/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        doginderAPI = retrofit.create(doginderAPI.class);

        // Utilizar directamente los RequestBody en lugar de userRequestBody
        Call<Void> call = doginderAPI.registerUser(nameRequestBody, latitudeRequestBody, longitudeRequestBody, surnameRequestBody, mailRequestBody, imagenPart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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

    private void startLocationUpdates() {
        locationHelper = new LocationHelper(this);
        locationHelper.requestLocationUpdates();
        Log.d("prueba", "startLocationUpdates: "+locationHelper.getLastKnownLocation());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, iniciar actualizaciones de ubicación
                startLocationUpdates();
            } else {
                // Permiso denegado, puedes manejarlo según tus necesidades
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener las actualizaciones de ubicación al destruir la actividad
        if (locationHelper != null) {
            locationHelper.removeLocationUpdates();
        }
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
}