package com.example.appscan

import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragement_app_list.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class appListFragement: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragement_app_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vm= ViewModelProviders.of(requireActivity()).get(appViewModel::class.java)
        //val packages1= requireActivity().packageManager.getInstalledPackages(0)
        app_list_rv.layoutManager= LinearLayoutManager(this.requireActivity())

        app_list_rv.adapter = appAdapter(this.requireActivity(),vm.app)

        /*for(a in apps_info){
           vm.app.add(a)
        }*/

        scan_button.setOnClickListener({view->
            //view?.findNavController()?.navigate(R.id.init_scan)
            val fragement= ScanFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_container, fragement)?.commit()
        })
    }



    fun loadData( apps_list : List<PackageInfo>?):List<AppInfo>{
        val data = mutableListOf<AppInfo>()
        var app_index=1
        while (app_index<2){
            if(apps_list!=null) {
                for (app in apps_list) {
                    val apk: String = app.applicationInfo.sourceDir
                    if (apk.startsWith("/data/app/")) {
                        data.add(
                            AppInfo(
                                app_index.toString(),
                                app.applicationInfo.loadLabel(requireActivity().packageManager).toString(),
                                app.packageName,
                                app.applicationInfo.loadIcon(requireActivity().packageManager),
                                app.applicationInfo.sourceDir,
                                "https://play.google.com/store/apps/details?id="+app.packageName
                            ,"null")
                        )
                        app_index += 1
                    }
                }
            }
        }
        return data
    }



}