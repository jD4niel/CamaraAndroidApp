package com.example.dany.proyectocamara;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class InicioCamara extends AppCompatActivity {
    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
    int day = now.get(Calendar.DAY_OF_MONTH);
    int hour = now.get(Calendar.HOUR_OF_DAY);
    int minute = now.get(Calendar.MINUTE);
    int second = now.get(Calendar.SECOND);
    int millis = now.get(Calendar.MILLISECOND);
    private static final int REQUEST_CODE=43;
    TextView texto;
    ImageButton btn_abrir;
    Camera camera;
    FrameLayout frameLayout;
    MostrarCamara muestraCamara;
    private SurfaceHolder cameraSurfaceHolder = null;
    //
    private Uri imageCaptureUri;
    private static final int PICK_FROM_CAMERA=1;
    private static final int PICK_FROM_FILE=2;
    //
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_camara);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        //Abrir Camara
        camera = Camera.open();
        muestraCamara = new MostrarCamara(this,camera);
        frameLayout.addView(muestraCamara);
        btn_abrir = (ImageButton)findViewById(R.id.abrir_galeria);

        //selector de imagenes
        final String[] items = new String[]{"Tomar con otra camara", "Desde archivos"};
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,items);
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Seleccione Imagen");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Environment.getExternalStorageDirectory(),"tmp_avatar"+String.valueOf(System.currentTimeMillis())+".jpeg");
                        imageCaptureUri=Uri.fromFile(file);
                        try {

                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,imageCaptureUri);
                            intent.putExtra("return data",true);

                            startActivityForResult(intent,PICK_FROM_CAMERA);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }else{
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Accion completa usada"), PICK_FROM_FILE);
                    }
                }
            });
            final AlertDialog dialog = builder.create();
            btn_abrir=(ImageButton)findViewById(R.id.abrir_galeria);
        btn_abrir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.show();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        frameLayout.removeAllViews();
        if(resultCode != RESULT_OK)
            return;
        Bitmap bitmap = null;
        String path="";
        if (requestCode == PICK_FROM_FILE){
            imageCaptureUri = data.getData();
            path=getRealPathFromURI(imageCaptureUri);
            System.out.println("he we aqui es la pinche path: "+path);
            if (path == null)
                path=imageCaptureUri.getPath();
            if (path != null)
                bitmap = BitmapFactory.decodeFile(path);
        }else{
            path=imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        String hoy = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) + "_" + String.valueOf(hour) +  String.valueOf(minute) + String.valueOf(second) + "_foto.jpeg";

        Intent siguiente = new Intent(InicioCamara.this, VerFoto.class);
        siguiente.putExtra("foto",path);
        siguiente.putExtra("nombre_foto",hoy);
        startActivity(siguiente);



    }
    public String getRealPathFromURI(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,proj,null,null,null);
        if (cursor==null)return null;
        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        if (camera!=null) camera.takePicture(null,null,mPictureCallback);

    }
    public void abrirGaleria(View view){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int ACTIVITY_SELECT_IMAGE = 1234;
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
    }

    public void info(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(InicioCamara.this).create();
        alertDialog.setTitle("Información");
        alertDialog.setMessage("Criptografia \n Jesus Daniel Acosta \n Proyecto Final");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok tienes 10",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
