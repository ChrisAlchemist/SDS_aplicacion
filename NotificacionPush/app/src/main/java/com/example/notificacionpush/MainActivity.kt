package com.example.notificacionpush

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.RemoteViews
import android.widget.Toast
import com.example.jsonandhttprequest.ListAdapter
import com.example.jsonandhttprequest.Resultado
import kotlinx.android.synthetic.main.activity_inicio.*

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.security.auth.callback.Callback

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        //Thread.sleep(1000)
        setTheme(R.style.Theme_AppCompat_NoActionBar)

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
            //var intent = Intent(this, LauncherActivity::class.java)
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
            contentView.setTextViewText(R.id.tv_content, "$usuario, bienvenido a SDS")

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
            //val url ="https://mysafeinfo.com/api/data?list=presidents&format=json" //+"&fullname=james madison"
            val url ="https://jsonplaceholder.typicode.com/users" + "?username=" + et_usuario.text
            //val url ="https://api.jikan.moe/v3/search/anime"+"?q="+software+"&limit=16"
            AsyncTaskHandleJson().execute(url)

            /*
            val intent: Intent = Intent(this@MainActivity, InicioActivity::class.java)
            intent.putExtra("Nombre", usuario)
            startActivity(intent)
            finish()*/

        }
    }


    inner class AsyncTaskHandleJson : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {

            var text : String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            }finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {

        val jsonArray = JSONArray(jsonString)
        val usuarios = ArrayList<Usuario>()

        if(jsonArray.length()<=0){
            usuarios_lista.adapter = null
            Toast.makeText(applicationContext,"Ningun elemento encontrado", Toast.LENGTH_LONG).show()
            return
        }

        var x= 0
        while (x<jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(x)
            usuarios.add(
                Usuario(
                jsonObject.getInt("id"),
                jsonObject.getString("name").toString(),
                jsonObject.getString("username").toString(),
                jsonObject.getString("email").toString()
                )

            )
            Toast.makeText(applicationContext,usuarios[x].username, Toast.LENGTH_LONG).show()

            val intent: Intent = Intent(this@MainActivity, InicioActivity::class.java)
            intent.putExtra("Nombre", usuarios[x].username)
            startActivity(intent)
            finish()

            x++
        }



        //val adapter = ListAdapter(this,usuarios)
        //usuarios_lista.adapter = adapter
    }
}
