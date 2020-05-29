package com.example.notificacionpush

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.jsonandhttprequest.ListAdapter
import com.example.jsonandhttprequest.Resultado
import kotlinx.android.synthetic.main.activity_inicio.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

import android.inputmethodservice.InputMethodService
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintSet.*
import android.text.Editable
import android.text.TextWatcher
import android.widget.RemoteViews


class InicioActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder :Notification.Builder
    val channelId = "com.example.notificacionpush"
    val description = "Test notification"

    var url =""

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        //Seteo de datos de login
        val objetoIntent: Intent = intent
        val nombreUsuario = objetoIntent.getStringExtra("Nombre")
        tv_usuario_nombre.text = "$nombreUsuario"
        //FIN Seteo de datos de login

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

        tv_limpiar.setOnClickListener {
            pb_Cargando.visibility = GONE
            usuarios_lista.adapter = null
            et_software.text =null
            et_ubicacion.text =null

        }

        btn_buscar.setOnClickListener{
            usuarios_lista.adapter = null

            var software = et_software.text.toString().trim()

            if(software.isEmpty()){
                et_software.error = "Ingresa un software a buscar"
                et_software.requestFocus()
                return@setOnClickListener
            }
            pb_Cargando.visibility = VISIBLE
            url ="https://api.jikan.moe/v3/search/anime"+"?q="+software+"&limit=16"
            AsyncTaskHandleJson().execute(url)
        }

        et_software.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // Do something after text changed

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do something before text changed on EditText

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do something on text changed in EditText
                // Display the EditText change text on TextView real time
                if(et_software.text.length>2){
                    usuarios_lista.adapter = null
                    pb_Cargando.visibility = VISIBLE
                    url ="https://api.jikan.moe/v3/search/anime"+"?q="+et_software.text+"&limit=16"
                    AsyncTaskHandleJson().execute(url)
                }


            }
        })

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

    @SuppressLint("WrongConstant")
    private fun handleJson(jsonString: String?) {

        val jsonArray = JSONArray(JSONObject(jsonString).getString("results"))
        val usuarios = ArrayList<Resultado>()

        if(jsonArray.length()<=0){
            usuarios_lista.adapter = null
            pb_Cargando.visibility = GONE
            //Toast.makeText(applicationContext,"Ningun elemento encontrado", Toast.LENGTH_LONG).show()
            return
        }

        var x= 0
        while (x<jsonArray.length() ){
            val jsonObject = jsonArray.getJSONObject(x)

            val id = jsonObject.getInt("mal_id")
            val url = jsonObject.getString("url")
            val image = jsonObject.getString("image_url")
            val titulo = jsonObject.getString("title")

            usuarios.add(
                Resultado(
                    Resultados(mal_id=id,url=url, image_url=image, title=titulo)
                )
            )
            x++
        }
            val adapter = ListAdapter(this,usuarios)
            usuarios_lista.adapter = adapter
            pb_Cargando.visibility = GONE

    }



}