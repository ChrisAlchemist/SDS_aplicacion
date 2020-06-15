package com.example.notificacionpush

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentTransaction
import android.widget.RemoteViews
import kotlinx.android.synthetic.main.activity_principal_activity.*

class Principal_activity : AppCompatActivity() {

    lateinit var fragmentUsuarioBuscar: UsuarioBuscarFragment
    lateinit var fragmentUsuarioInicio: UsuarioInicioFragment
    lateinit var fragmentUsuarioFavoritos: UsuarioFavoritosFragment

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : Notification.Builder
    val channelId = "com.example.notificacionpush"
    val description = "Test notification"

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal_activity)
        val objetoIntent: Intent = intent
        val nombreUsuario = objetoIntent.getStringExtra("Nombre")
        //tv_usuario_nombre.text = "$nombreUsuario"


        //Notificacion
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var pendingIntent  = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var contentView = RemoteViews(packageName,R.layout.notification_layaut)

        contentView.setTextViewText(R.id.tv_title, "Software Development System")
        contentView.setTextViewText(R.id.tv_content, "$nombreUsuario, bienvenido a SDS")

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
        // fin Notificacion


        var bottomNavigation = bottom_navigation

        fragmentUsuarioInicio = UsuarioInicioFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container1, fragmentUsuarioInicio)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener {menuItem ->

            when(menuItem.itemId){
                R.id.nav_search ->{

                    fragmentUsuarioBuscar = UsuarioBuscarFragment()

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container1, fragmentUsuarioBuscar)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }

                R.id.nav_home ->{

                    fragmentUsuarioInicio = UsuarioInicioFragment()

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container1, fragmentUsuarioInicio)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }

                R.id.nav_favorites ->{

                    fragmentUsuarioFavoritos = UsuarioFavoritosFragment()

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container1, fragmentUsuarioFavoritos)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
            }
            true

        }


    }








}
