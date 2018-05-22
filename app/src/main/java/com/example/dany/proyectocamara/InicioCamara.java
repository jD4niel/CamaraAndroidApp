package com.example.dany.proyectocamara;


import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InicioCamara extends AppCompatActivity {
    Camera camera;
    FrameLayout frameLayout;
    MostrarCamara muestraCamara;
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
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file = GuardaFoto();
            if (file == null){
                return;
            }else {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    };

    private File GuardaFoto() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else{
            File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"GUI");
            if (!folder.exists()){
                folder.mkdir();
            }
            File output_file = new File(folder,"temp.jpeg");
            return output_file;
        }
    }

    //Tomar Foto
    public void TomarFoto(View view){
        if (camera!=null){
            camera.takePicture(null,null,mPictureCallback);
        }
    }
}
