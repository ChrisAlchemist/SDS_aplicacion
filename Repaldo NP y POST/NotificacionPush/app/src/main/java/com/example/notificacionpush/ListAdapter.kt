package com.example.jsonandhttprequest

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.notificacionpush.R
import com.squareup.picasso.Picasso

class ListAdapter(val context: Context, val list: ArrayList<Usuario>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_layout,parent, false)

        val presidentId = view.findViewById(R.id.usuario_id) as TextView
        val presidentName = view.findViewById(R.id.usuario_name) as TextView
        val presidentPolitic = view.findViewById(R.id.usuario_username) as TextView
        val presidentTime = view.findViewById(R.id.usuario_email) as ImageView
        //val presidentStreet =  view.findViewById(R.id.usuario_street) as TextView

        presidentId.text = list[position].results.mal_id.toString()
        presidentName.text = list[position].results.title
        presidentPolitic.text = list[position].results.url
        Picasso.get()
            .load(list[position].results.image_url)
            .into(presidentTime)
        //presidentTime.text = list[position].results.image_url
        //presidentStreet.text = list[position].address.street
        //presidentStreet.text = list[position].username

        return view
    }

    override fun getItem(position: Int): Any {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}