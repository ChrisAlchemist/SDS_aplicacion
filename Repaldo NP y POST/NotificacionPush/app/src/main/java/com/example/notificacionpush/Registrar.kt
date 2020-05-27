package com.example.notificacionpush

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.RemoteViews
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registrar.*

class Registrar : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var notificationManager: NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder :Notification.Builder
        val channelId = "com.example.notificacionpush"
        val description = "Test notification"



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        iv_regresar.setOnClickListener{
            startActivity(Intent(this@Registrar, MainActivity::class.java))
            Registrar().onDestroy()
        }

        btn_registrar.setOnClickListener{
            var intent = Intent(this, LauncherActivity::class.java)
            var pendingIntent  = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
            var contentView = RemoteViews(packageName,R.layout.notification_layaut)
            var usuario = et_R_usuario.text.toString().trim()
            var password = et_R_pass.text.toString().trim()
            var conPassword = et_R_con_pass.text.toString().trim()


            if(usuario.isEmpty()){
                et_R_usuario.error = "Ingresa un usuario"
                et_R_usuario.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                et_R_pass.error = "Ingresa una contraseña"
                et_R_pass.requestFocus()
                return@setOnClickListener
            }
            if(conPassword.isEmpty()){
                et_R_con_pass.error = "Confirma tu contraseña"
                et_R_con_pass.requestFocus()
                return@setOnClickListener
            }

            if (password!=conPassword){
                Toast.makeText(applicationContext,"La contraseña no conincide",Toast.LENGTH_LONG).show()
            }
            else{

                //Notificacion
                contentView.setTextViewText(R.id.tv_title, "Software Development System")
                contentView.setTextViewText(R.id.tv_content, "Tu registro fue exitoso")

                Toast.makeText(applicationContext,"Registro exitoso",Toast.LENGTH_LONG).show()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel (channelId,description, NotificationManager.IMPORTANCE_HIGH)
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
                notificationManager.notify(1234,builder.build())
                startActivity(Intent(this@Registrar, MainActivity::class.java))
            }


        }
    }
}
