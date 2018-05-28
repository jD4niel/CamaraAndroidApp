package com.example.dany.proyectocamara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class VerFoto extends AppCompatActivity {
    private ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);
        imagen=(ImageView)findViewById(R.id.imageView);
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
        AlertDialog.Builder mBuiler = new AlertDialog.Builder(VerFoto.this);
        View mView =  getLayoutInflater().inflate(R.layout.guardar_alert, null);
        final EditText nom = mView.findViewById(R.id.nom);
        Button btn = mView.findViewById(R.id.btn_name);
        //guarda fotos con nombre
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dato=getIntent().getStringExtra("foto");
                String nombre=getIntent().getStringExtra("nombre_foto");
                if(!nom.getText().toString().isEmpty()){
                    String folder = Environment.getExternalStorageDirectory()+File.separator+"GUI/"+nom.getText()+".jpeg";
                    CifradoBytes cifrado = new CifradoBytes();
                    cifrado.copia(dato, folder, 1111111111);
                }else{
                    String folder = Environment.getExternalStorageDirectory()+File.separator+"GUI/foto_cifrada_"+nombre;
                    CifradoBytes cifrado = new CifradoBytes();
                    cifrado.copia(dato, folder, 1111111111);
                }
                Intent regresar = new Intent(VerFoto.this, InicioCamara.class);
                startActivity(regresar);
            }
        });
        mBuiler.setView(mView);
        AlertDialog dialog = mBuiler.create();
        dialog.show();

    }
}
