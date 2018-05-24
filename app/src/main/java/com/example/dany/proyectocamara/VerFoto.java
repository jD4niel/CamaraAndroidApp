package com.example.dany.proyectocamara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class VerFoto extends AppCompatActivity {
    private ImageView imagen;
    private Button eliminar;
    private Button guardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);
        imagen=(ImageView)findViewById(R.id.imageView);
        eliminar = (Button)findViewById(R.id.eliminar);

        String dato=getIntent().getStringExtra("foto");
        Bitmap myBitmap = BitmapFactory.decodeFile(dato);
        imagen.setImageBitmap(myBitmap);

    }
    public void eliminar(View view){
        String dato=getIntent().getStringExtra("foto");
        File file = new File(dato);
        file.delete();
        Toast.makeText(VerFoto.this,"Eliminado Correctamente",Toast.LENGTH_SHORT).show();
        Intent regresar = new Intent(VerFoto.this, InicioCamara.class);
        startActivity(regresar);
    }
    public void guardar(View view){
        String dato=getIntent().getStringExtra("foto");
        String nombre=getIntent().getStringExtra("nombre_foto");
        String folder = Environment.getExternalStorageDirectory()+File.separator+"GUI/foto_cifrada_"+nombre;
        System.out.println("");
        System.out.println("\n \n");
        System.out.println("folder es: " + folder);
        System.out.println("\n \n");

        CifradoBytes cifrado = new CifradoBytes();
        cifrado.copia(dato, folder, 1111111111);
        Intent regresar = new Intent(VerFoto.this, InicioCamara.class);
        startActivity(regresar);
    }
}
