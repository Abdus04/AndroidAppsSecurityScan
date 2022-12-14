package com.example.appscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragement_safe.*
import kotlinx.android.synthetic.main.fragment_sus.*

class SusFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sus, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_sus.setOnClickListener({ view-> go_home()})
    }

    fun go_home(){
        val fragment= appListFragement()
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.nav_container, fragment)?.commit()
    }
}