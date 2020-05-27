package com.example.notificacionpush

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.RemoteViews
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var notificationManager: NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder :Notification.Builder
        val channelId = "com.example.notificacionpush"
        val description = "Test notification"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        tv_registrar.setOnClickListener{
            startActivity(Intent(this@MainActivity, Registrar::class.java) )

        }

        btn_notify.setOnClickListener{
            var intent = Intent(this, LauncherActivity::class.java)
            var pendingIntent  = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            var contentView = RemoteViews(packageName,R.layout.notification_layaut)
            var usuario = et_usuario.text.toString().trim()
            var password = et_pass.text.toString().trim()

            if(usuario.isEmpty()){
                et_usuario.error = "Ingresa un usuario"
                et_usuario.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                et_pass.error = "Ingresa una contraseña"
                et_pass.requestFocus()
                return@setOnClickListener
            }



            //Notificacion
            contentView.setTextViewText(R.id.tv_title, "Software Development System")
            contentView.setTextViewText(R.id.tv_content, "Bienvenido a SDS")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel (channelId,description,NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setContent(contentView)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent)
            }
/*
            else{
                builder = Notification.Builder(this)
                    .setContent(contentView)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent)
            }*/

            notificationManager.notify(1234,builder.build())

            startActivity(Intent(this@MainActivity, InicioActivity::class.java) )

        }
    }
}
