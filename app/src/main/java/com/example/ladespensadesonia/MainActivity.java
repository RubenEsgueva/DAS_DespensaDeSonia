package com.example.ladespensadesonia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ladespensadesonia.ui.login.LoginActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static String nomUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        nomUsuario = getIntent().getExtras().getString("usuario");

        //Notificación para saber con qué usuario estas logeado
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!=
                PackageManager.PERMISSION_GRANTED) {
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);

        }
        else{
            notificarLogin();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.action_add) {
            Intent intent=new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_reminder) {
            long startMillis = System.currentTimeMillis();
            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
            builder.appendPath("time");
            ContentUris.appendId(builder, startMillis);
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
            startActivity(intent);
        } else if (id == R.id.action_logout) {
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_language) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("@string/selectLanguage");
            final CharSequence[] opciones = {"@string/spanish", "@string/basque", "@string/english"};
            builder.setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String idiomaseleccionado = "";
                    if (i == 0){
                        idiomaseleccionado = "es";
                    }
                    else if (i == 1){
                        idiomaseleccionado = "eu";
                    }
                    else if (i == 2){
                        idiomaseleccionado = "en";
                    }
                    cambiarIdioma(idiomaseleccionado);
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Si el usuario da permiso, lanzar la notificación
            notificarLogin();
        }
    }

    private void notificarLogin(){
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "connected");
        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Aviso de inicio de sesión")
                .setContentText("Sesión iniciada como: "+nomUsuario)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("connected", "connectedUserNotif",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elCanal.setDescription("@string/channelDescrip");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);
            elManager.createNotificationChannel(elCanal);
            elManager.notify(1, elBuilder.build());
        }
    }

    private void cambiarIdioma(String langCode){
        if (langCode != ""){
            Locale nuevaloc = new Locale(langCode);
            Locale.setDefault(nuevaloc);
            Configuration configuration =
                    getBaseContext().getResources().getConfiguration();
            configuration.setLocale(nuevaloc);
            configuration.setLayoutDirection(nuevaloc);

            Context context =
                    getBaseContext().createConfigurationContext(configuration);
            getBaseContext().getResources()
                    .updateConfiguration(configuration, context.getResources().getDisplayMetrics());

            finish();
            startActivity(getIntent());
        }
    }
}