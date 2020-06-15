package com.example.notificacionpush

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
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

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_registrar.setOnClickListener{
            startActivity(Intent(this@MainActivity, Registrar::class.java) )
        }

        btn_notify.setOnClickListener{
            var progresDialog = ProgressDialog(this)


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
            progresDialog.setMessage("Autentificando..")
            progresDialog.setCancelable(false)
            progresDialog.show()

            //val url ="https://jsonplaceholder.typicode.com/users" + "?username=" + et_usuario.text
            val url ="https://jsonplaceholder.typicode.com/users" + "?id=" + 1
            AsyncTaskHandleJson().execute(url)

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
            //Toast.makeText(applicationContext,usuarios[x].username, Toast.LENGTH_LONG).show()

            //val intent: Intent = Intent(this@MainActivity, InicioActivity::class.java)
            val intent: Intent = Intent(this@MainActivity, Principal_activity::class.java)
            intent.putExtra("Nombre", usuarios[x].username)
            startActivity(intent)
            finish()

            x++
        }

        //val adapter = ListAdapter(this,usuarios)
        //usuarios_lista.adapter = adapter
    }
}
