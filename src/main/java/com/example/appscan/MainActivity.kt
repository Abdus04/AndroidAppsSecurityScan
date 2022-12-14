package com.example.appscan

import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportFragmentManager.beginTransaction().replace(R.id.nav_container,appListFragement()).commit()

        val pm = packageManager
        val packages1= pm.getInstalledPackages(0)
        var apps_info = loadData(packages1)
        val vm= ViewModelProviders.of(this).get(appViewModel::class.java)
        for(a in apps_info){
            vm.app.add(a)
        }


        /*var scanButton= findViewById<FloatingActionButton>(R.id.scan_button)
        scanButton.setOnClickListener {
            //scanurl("https://square.github.io/retrofit/")
            //view -> view?.findNavController()?.navigate(R.id.scan_apps)

        }*/



    }

    override fun onSupportNavigateUp(): Boolean {
        //return super.onSupportNavigateUp()
        val navController = findNavController(R.id.nav_container)
        return navController.navigateUp()
    }



    fun loadData( apps_list : List<PackageInfo>):List<AppInfo>{
        val data = mutableListOf<AppInfo>()
        var app_index=1
        while (app_index<2){
            for (app in apps_list){
                val apk: String = app.applicationInfo.sourceDir
                if(apk.startsWith("/data/app/")){
                    data.add(AppInfo(app_index.toString() , app.applicationInfo.loadLabel(packageManager).toString(),app.packageName ,app.applicationInfo.loadIcon(packageManager), app.applicationInfo.sourceDir,"https://play.google.com/store/apps/details?id="+app.packageName, "null"))
                    app_index+=1
                }
            }
        }
        return data
    }

    fun getapk() {

        // using extension function walkBottomUp
        File("/data").walk().filter {
            it.name.endsWith(".apk")
        }.forEach {
            Log.d("apk", it.name)
        }

    }

    private fun getHash(sourceDir: String): String? {
        // TODO Auto-generated method stub

        val outputTxt = ""
        var hashcode: String? = null
        try {
            val file: File = File(sourceDir)
            val input = FileInputStream(file)
            val output = ByteArrayOutputStream()
            val buffer = ByteArray(65536)
            var l: Int
            while (input.read(buffer).also { l = it } > 0) output.write(buffer, 0, l)
            input.close()
            output.close()
            val data: ByteArray = output.toByteArray()
            val digest = MessageDigest.getInstance("SHA-1")
            var bytes = data
            digest.update(bytes, 0, bytes.size)
            bytes = digest.digest()
            val sb = StringBuilder()
            for (b in bytes) {
                sb.append(String.format("%02X", b))
            }
            hashcode = sb.toString()
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()

        } catch(e: ClassNotFoundException){
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return hashcode
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun calculateMD5(file: File): String? {
        val digest: MessageDigest
        digest = try {
            MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            return null
        }
        val `is`: InputStream
        try {
            `is` = FileInputStream(file)
        } catch (e: FileNotFoundException) {
            return null
        }
        val buffer = ByteArray(8192)
        var read: Int
        return try {
            while (`is`.read(buffer).also { read = it } > 0) {
                digest.update(buffer, 0, read)
            }
            val md5sum = digest.digest()
            val bigInt = BigInteger(1, md5sum)
            var output = bigInt.toString(16)
            output = String.format("%32s", output).replace(' ', '0')
            output
        } catch (e: IOException) {
            throw RuntimeException("Unable to process file for MD5", e)
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
            }
        }
    }

    class jsonUrl(json: String) : JSONObject(json) {
        val data = this.optJSONObject("data")
        val type = data.optString("type")
        val id = data.optString("id")
    }

    class jsonAnalyse(json: String): JSONObject(json){
        val data = this.optJSONObject("data")
        val attributes= data.optJSONObject("attributes")
        val stats= attributes.optJSONObject("stats")
        val harmless= stats.optInt("harmless")
        val malicious= stats.optInt("malicious")
        val suspicious= stats.optInt("suspicious")
        var status = if(malicious>0 ){
            "malicious"
        } else if (suspicious>0){
            "suspicious"
        } else if (harmless>0){
            "harmless"
        } else {
            "undetected"
        }
    }
}