package com.ragiant.ticketfacil

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.ragiant.ticketfacil.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var ip = "192.168.0.250"
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
                val url = result.contents.replace(
                    "www.ticketfacil.com.ar",
                    "http://${ip}:8080/ticketfacil"
                )
                Log.d("DEBUG", url)
                Thread {
                    try {
                        val msg = URL(url).readText().split("<!doctype html>")[0]
                        runOnUiThread {
                            binding.response.text = msg.uppercase()
                            when{
                                msg == "No se encontró una entrada con ese DNI y CL." || msg == "No hay entradas disponibles."->{
                                    binding.layout.setBackgroundColor(Color.RED)
                                    binding.response.setTextColor(Color.WHITE)
                                }
                                msg.contains("Entrada validada: Tipo de entrada: estandar".toRegex()) ->{
                                    binding.layout.setBackgroundColor(Color.GREEN)
                                    binding.response.setTextColor(Color.BLACK)
                                }
                                msg.contains("Entrada validada: Tipo de entrada: VIP".toRegex()) ->{
                                    binding.layout.setBackgroundColor(Color.BLUE)
                                    binding.response.setTextColor(Color.WHITE)
                                }
                                else ->{
                                    binding.layout.setBackgroundColor(Color.WHITE)
                                    binding.response.setTextColor(Color.BLACK)
                                }
                            }

                        }
                    }catch (e:java.lang.Exception){
                        runOnUiThread {Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()}
                    }
                }.start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreference =  getSharedPreferences("PREFERENCE_IP", Context.MODE_PRIVATE)
        ip = sharedPreference.getString("ip","192.168.0.250").toString()
        binding.button2.setOnClickListener {
            val option = ScanOptions()
            option.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(option)
        }

        binding.floatingActionButton.setOnClickListener {
            val dialog = IpDialogFragment(ip){
                ip=it
                val editor = sharedPreference.edit()
                editor.putString("ip",ip)
                editor.apply()
            }
            dialog.show(supportFragmentManager, "IP")
        }
    }
}