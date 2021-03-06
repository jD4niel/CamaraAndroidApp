package com.example.dany.proyectocamara;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class VerFoto extends AppCompatActivity {
    private ImageView imagen;
    private TextView mensaje;
    Button btn_abrir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);
        imagen=(ImageView)findViewById(R.id.imageView);
        String dato=getIntent().getStringExtra("foto");
        Bitmap myBitmap = BitmapFactory.decodeFile(dato);
        imagen.setImageBitmap(myBitmap);
        mensaje=(TextView)findViewById(R.id.mensaje);


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
                File filed = new File(dato);
                filed.delete();
                Intent regresar = new Intent(VerFoto.this, InicioCamara.class);
                startActivity(regresar);
            }
        });
        mBuiler.setView(mView);
        AlertDialog dialog = mBuiler.create();
        dialog.show();

    }
}
