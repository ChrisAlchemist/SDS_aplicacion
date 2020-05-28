package com.example.notificacionpush

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
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


class InicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val objetoIntent: Intent = intent
        val nombreUsuario = objetoIntent.getStringExtra("Nombre")
        tv_usuario_nombre.text = "$nombreUsuario"


        tv_limpiar.setOnClickListener {
            et_software.text =null
            et_ubicacion.text =null
            usuarios_lista.adapter = null
        }

        btn_buscar.setOnClickListener{
            var software = et_software.text.toString().trim()

            if(software.isEmpty()){
                et_software.error = "Ingresa un software a buscar"
                et_software.requestFocus()
                return@setOnClickListener
            }
            //val url ="https://mysafeinfo.com/api/data?list=presidents&format=json" //+"&fullname=james madison"
            //val url ="https://jsonplaceholder.typicode.com/users" + "?username=" + software
            val url ="https://api.jikan.moe/v3/search/anime"+"?q="+software+"&limit=16"
            AsyncTaskHandleJson().execute(url)


        }

        //val url ="https://api.jikan.moe/v3/search/anime?q=naruto&limit=16"
        //AsyncTaskHandleJson().execute(url)

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
        //val jsonArray = JSONArray(jsonString)
        //val jsonArray = JSONObject(jsonString)
        val jsonArray = JSONArray(JSONObject(jsonString).getString("results"))
        val usuarios = ArrayList<Resultado>()

        if(jsonArray.length()<=0){
            usuarios_lista.adapter = null
            Toast.makeText(applicationContext,"Ningun elemento encontrado", Toast.LENGTH_LONG).show()
            return
        }

        var x= 0
        while (x<jsonArray.length()){
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
    }



}