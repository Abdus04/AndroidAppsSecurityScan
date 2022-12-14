package com.example.appscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanFragment: Fragment() {

    var apps_scanned=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_load, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var vm =  ViewModelProviders.of(requireActivity()).get(appViewModel::class.java)
        val app = vm.app[0]
        //Toast.makeText(requireActivity(),app.url , Toast.LENGTH_LONG).show()

        for(i in vm.app.indices){
            scanurl(i,vm)
        }


    }

     fun go_to_screen(): Boolean{

         var vm =  ViewModelProviders.of(requireActivity()).get(appViewModel::class.java)
         var harmless=0
         var malicious=0
         var suspicious=0
         for (app in vm.app){
             if (app.scan_state.equals("harmless")){
                 harmless++
             } else if( app.scan_state.equals("malicious")){
                 malicious++
             } else if( app.scan_state.equals("suspicious")){
                 suspicious++
             }
         }
         if (malicious>0){
             val fragment= MalFragment()
             val transaction = fragmentManager?.beginTransaction()
             transaction?.replace(R.id.nav_container, fragment)?.commit()
             return true
         } else if(suspicious>0){
             val fragment= SusFragment()
             val transaction = fragmentManager?.beginTransaction()
             transaction?.replace(R.id.nav_container, fragment)?.commit()
             return true
         }else {
             val fragment= SafeFragment()
             val transaction = fragmentManager?.beginTransaction()
             transaction?.replace(R.id.nav_container, fragment)?.commit()
             return true
         }

    }

    fun scanurl(index: Int, vm : appViewModel) {
        val call = RetrofitService.endpoint.scanurl(vm.app[index].url)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val data = response.body()
                if (response.isSuccessful){
                    val analyse_id= MainActivity.jsonUrl(response.body()!!.string()).id
                    getanalyseinfos(index, vm,analyse_id )
                    //val toast = Toast.makeText(this@MainActivity, analyse_id, Toast.LENGTH_LONG)
                    //toast.show()

                }
                else{
                    apps_scanned+=1
                    vm.set_state(index,"undetected")
                    if (apps_scanned== vm.app.size){
                        go_to_screen()
                    }
                //Toast.makeText(requireActivity(),"Server Error: "+response.body().toString(), Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apps_scanned+=1
                vm.set_state(index,"undetected")
                if (apps_scanned== vm.app.size){
                    go_to_screen()
                }
                //Toast.makeText(requireActivity(),"Failed Request: "+t.toString() , Toast.LENGTH_LONG).show()
            }

        })
    }

    fun getanalyseinfos(index: Int ,vm:appViewModel,  id: String) {
        val call = RetrofitService.endpoint.getanalyses(id)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val data = response.body()
                if (response.isSuccessful){
                    if (data!= null) {
                        val result = MainActivity.jsonAnalyse(response.body()!!.string()).status
                        vm.set_state(index,result)
                        val toast = Toast.makeText(
                            requireActivity(),
                            apps_scanned.toString()+" -HARMLESS: "+vm.app[index].url+ "==" + result.toString(),
                            Toast.LENGTH_SHORT
                        )
                        //toast.show()
                        apps_scanned+=1
                        if (apps_scanned== vm.app.size){
                            go_to_screen()
                        }
                    }
                }
                else{
                    vm.set_state(index,"undetected")
                    apps_scanned+=1
                    if (apps_scanned== vm.app.size){
                        go_to_screen()
                    }
                    //Toast.makeText(requireActivity(),"Server Error: "+response.body().toString(), Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apps_scanned+=1
                vm.set_state(index,"undetected")
                if (apps_scanned== vm.app.size){
                    go_to_screen()
                }
                //Toast.makeText(requireActivity(),"Failed Request: "+t.toString() , Toast.LENGTH_LONG).show()
            }

        })
    }
}