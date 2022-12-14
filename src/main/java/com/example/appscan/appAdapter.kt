package com.example.appscan

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView

class appAdapter( private var context: Activity, private var apps: List<AppInfo>) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nom1.text = apps[position].name1
        holder.nom2.text = apps[position].name2
        holder.nom3.text = apps[position].name3
        holder.img.setImageDrawable(apps[position].icon)
        holder.itemView.setOnClickListener{
            val toast = Toast.makeText(context, apps[position].scan_state, Toast.LENGTH_SHORT)
            toast.show()
        }
        if(apps[position].scan_state.equals("harmless")){
            holder.status.setBackgroundColor(Color.parseColor("#4CAF50"))
        }else if(apps[position].scan_state.equals("suspicious")){
            holder.status.setBackgroundColor(Color.parseColor("#DDA107"))
        }else if(apps[position].scan_state.equals("malicious")){
            holder.status.setBackgroundColor(Color.parseColor("#C3060C"))
        }else if(apps[position].scan_state.equals("undetected")){
            holder.status.setBackgroundColor(Color.parseColor("#686868"))
        }else {
            holder.status.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }

    override fun getItemCount() = apps.size


}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nom1 = view.findViewById<TextView>(R.id.info1)
    val nom2 = view.findViewById<TextView>(R.id.info2)
    val img = view.findViewById<ImageView>(R.id.img)
    val nom3 = view.findViewById<TextView>(R.id.info3)
    val status = view.findViewById<ConstraintLayout>(R.id.status)

}