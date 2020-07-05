package br.sofex.com.upload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

import br.sofex.com.upload.databinding.ActivityMainBinding;
import pub.devrel.easypermissions.BuildConfig;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {


    /*
     * TODO : Android precissa salvar primeiro a image, para depois exibir a imagem em tamanho real
     * TODO : Caso contrário ele irá exibir uma miniatura da imagem
     * */

    private static final int READ_REQUEST_CODE = 200;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int CAM_REQUEST = 1;
    ActivityMainBinding main;
    int PICKFILE_REQUEST_CODE = 1;
    Activity activity =  MainActivity.this;
    Imagem imagem = new Imagem(MainActivity.this);
    final Permissao permissao = new Permissao(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toast.makeText(this, "Result :"+permissao.checkpermissao(), Toast.LENGTH_SHORT).show();
        DisableEdittext(R.id.edit_filenome);

        //Inicializando o nosso conteúdo.
        main = DataBindingUtil.setContentView(this, R.layout.activity_main);
        main.rlUserimg.setVisibility(View.GONE);
        main.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              boolean b1 =  permissao.checkpermissao();
               if(b1 == true)
               {CallUploadImage(); main.rlUserimg.setVisibility(View.VISIBLE);}
              //else {Toast.makeText(MainActivity.this, " Permissões negadas ", Toast.LENGTH_SHORT).show();}
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri filePath = data.getData();
                //File file = new File(filePath.getPath());
                //file.getName();
                //Bitmap bitmap = MediaStore.Images.Media .getBitmap(activity.getContentResolver(), filePath);
                main.imgUserPerfil.setImageBitmap(imagem.CircularImagem( getBitmapByUri(filePath) ));

                if(getNomeArquivoByUri(filePath).length() >= 15)
                {main.editFilenome.setText("ImageUploaded."+imagem.getFileExtension( getFileByUri(filePath) ));}
                else{main.editFilenome.setText(getNomeArquivoByUri(filePath));}
                main.editFilenome.setTextColor(Color.parseColor("#FFFFFF"));

            }

        }
    }

    public void DisableEdittext(Integer editTextID){
        //  EditText ID  = Ex:  R.id.edit_filenome
        EditText edit = findViewById(editTextID);
        edit.setFocusable(false);
        edit.setClickable(false);
        edit.setLongClickable(false);
    }
    public String getNomeArquivoByUri(Uri uri) {
        if(uri != null)
        {
            File file = new File(uri.getPath());
            return file.getName();
        }
        else
        {
          try {}catch (NullPointerException npe)
          {
              Toast.makeText(MainActivity.this, " Error : "+npe, Toast.LENGTH_SHORT).show();
              Log.e("Upload","Error : "+npe);
          }
            return null;
        }
    }
    public File getFileByUri(Uri uri) {
        File file = new File(uri.getPath());
        return file;
    }
    public Bitmap getBitmapByUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media .getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, " Error : "+e, Toast.LENGTH_SHORT).show();
            Log.e("Upload","Error : "+e);
            e.printStackTrace();
        }
        return  bitmap;
    }
    public void CallUploadImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

}
