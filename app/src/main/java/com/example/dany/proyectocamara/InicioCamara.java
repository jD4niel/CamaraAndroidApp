package com.example.dany.proyectocamara;


import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InicioCamara extends AppCompatActivity {
    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
    int day = now.get(Calendar.DAY_OF_MONTH);
    int hour = now.get(Calendar.HOUR_OF_DAY);
    int minute = now.get(Calendar.MINUTE);
    int second = now.get(Calendar.SECOND);
    int millis = now.get(Calendar.MILLISECOND);

    Camera camera;
    FrameLayout frameLayout;
    MostrarCamara muestraCamara;
    private SurfaceHolder cameraSurfaceHolder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_camara);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);

        //Abrir Camara
        camera = Camera.open();
        muestraCamara = new MostrarCamara(this,camera);
        frameLayout.addView(muestraCamara);
    }

    final Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file = GuardaFoto();
            if (file == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    Intent siguiente = new Intent(InicioCamara.this, VerFoto.class);
                    siguiente.putExtra("foto",file.getAbsolutePath().toString());
                    siguiente.putExtra("nombre_foto",file.getName().toString());
                    System.out.println(file.getAbsolutePath().toString());
                    startActivity(siguiente);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private File GuardaFoto() {
        InicioCamara main = new InicioCamara();
        File output_file;
        String hoy = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) + "_" + String.valueOf(hour) +  String.valueOf(minute) + String.valueOf(second) + "_foto.jpeg";
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else{
            File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"GUI");
            if (!folder.exists()){
                folder.mkdir();
            }
            output_file = new File(folder,hoy);
        }

       // System.exit(0);
        return output_file;
    }

    //Tomar Foto
    public void TomarFoto(View view){
        if (camera!=null){
            camera.takePicture(null,null,mPictureCallback);
        }
    }
    public void abrirGaleria(View view){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int ACTIVITY_SELECT_IMAGE = 1234;
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
    }
}
