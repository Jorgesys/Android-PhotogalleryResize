package com.jorgesys.selectpicture;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView imagenPersona;
    private int SELECCIONAR_IMAGEN = 244;
    private Uri pathImagenUri;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagenPersona = (ImageView) findViewById(R.id.imagenPersona);


        imagenPersona.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaImagen();
            }
        });
    }

    private void ventanaImagen() {
        try {
            final CharSequence[] items = {"Seleccionar de la galería"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar una foto");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            /*Intent intentSeleccionarImagen = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            intentSeleccionarImagen.setType("image/*");
                            startActivityForResult(intentSeleccionarImagen, SELECCIONAR_IMAGEN);
                            */
                            Intent intent = new Intent();
                            // Show only images, no videos or anything else
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            // Always show the chooser (if there are multiple options available)
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                            break;
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                /*//Muestra imagen seleccionada de galería en ImageView.
                ImageView imageView = (ImageView) findViewById(R.id.imagenPersona);
                imageView.setImageBitmap(bitmap);*/

                //Define path donde sera guardada una nueva imagen con medidas 150x150px
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File file = new File(dir, "resized_jorgesys.png");

                if (!file.exists()) { //Si archivo no existe.
                    try {
                        file.createNewFile(); //Procede a crearlo.
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (bitmap != null) {

                    //Redimensiona imagen.
                    Bitmap bitmapout = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                        bitmapout.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    //Muestra imagen con nuevas medidas en ImageView.
                    ImageView imageView = (ImageView) findViewById(R.id.imagenPersona);
                    imageView.setImageBitmap(bitmapout);

                } else {
                    Toast.makeText(this, "Error obteniendo imagen", Toast.LENGTH_LONG).show();
                }




            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }



       /*if (requestCode == SELECCIONAR_IMAGEN && resultCode == Activity.RESULT_OK) {
            if (pathImagenUri != null) {

                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File file = new File(dir, "resized_image.png");

                Uri imagenSeleccionada = pathImagenUri;
                Bitmap bitmap = getBitmap(pathImagenUri.getPath());

                if (bitmap != null) { //Bitmap no es null?

                    //Redimensiona imagen.
                    Bitmap bitmapout = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmapout.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    try {
                        fOut.flush();
                    } catch (IOException e) {
                       Log.e(TAG, e.getMessage());
                    }
                    try {
                        fOut.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }


                } else {
                    Toast.makeText(this, "Error obteniendo imagen", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Error obteniendo imagen", Toast.LENGTH_LONG).show();
            }
        } */
        }
    }

    private Bitmap getBitmap(String ruta_imagen) {
        File imagenArchivo = new File(ruta_imagen);
        Bitmap bitmap = null;
        if (imagenArchivo.exists()) {
            bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath());
        }
        return bitmap;
    }
}