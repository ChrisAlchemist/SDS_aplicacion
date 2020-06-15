package com.example.notificacionpush

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jsonandhttprequest.ListAdapter
import com.example.jsonandhttprequest.Resultado
import kotlinx.android.synthetic.main.activity_inicio.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class UsuarioBuscarFragment : Fragment() {
    var thiscontext: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            thiscontext= container.context
        }

        return inflater.inflate(R.layout._fragment_inicio, container, false)
    }
    var url =""

    @SuppressLint("WrongConstant")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //val objetoIntent: Intent = intent
        //val nombreUsuario = objetoIntent.getStringExtra("Nombre")
        //tv_usuario_nombre.text = "$nombreUsuario
        tv_usuario_nombre.text = "nombreUsuario"


        tv_limpiar.setOnClickListener {
            pb_Cargando.visibility = ConstraintSet.GONE
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
            pb_Cargando.visibility = ConstraintSet.VISIBLE
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
                    pb_Cargando.visibility = ConstraintSet.VISIBLE
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
            pb_Cargando.visibility = ConstraintSet.GONE
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

        //this@MainActivity, Principal_activity::class.java
        val adapter = ListAdapter(thiscontext,usuarios)
        usuarios_lista.adapter = adapter
        pb_Cargando.visibility = ConstraintSet.GONE

    }
}

