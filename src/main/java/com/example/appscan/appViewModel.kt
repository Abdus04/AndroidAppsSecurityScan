package com.example.appscan

import androidx.lifecycle.ViewModel

class appViewModel: ViewModel() {
    var app=  mutableListOf<AppInfo>()


    fun set_state(int: Int , state: String){
        app[int].scan_state= state
    }
}