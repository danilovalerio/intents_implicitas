package com.example.intentsimplcitas

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listView = ListView(this)
        setContentView(listView)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.intent_actions))

        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            openIntentAtPosition(position)
        }
    }

    private fun callNUmber() {
        val uri = Uri.parse("tel:953608894")
        val intent = Intent(Intent.ACTION_CALL, uri)
        openIntent(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            callNUmber()
        }
    }

    private fun openIntentAtPosition(position: Int) {
        val uri: Uri? //Uniform Resource Identifier
        val intent: Intent?

        when (position) {
            0 -> {//Abrindo uma URL
                uri = Uri.parse("http://www.google.com")
                intent = Intent(Intent.ACTION_VIEW, uri)
                openIntent(intent)
            }
            1 -> {//Realiza uma chamada
                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf<String>(android.Manifest.permission.CALL_PHONE), 0);
                } else {
                    callNUmber()
                }
            }
            2 -> {//Pesquisa uma posição do mapa (o emulador tem que estar com o Google Maps)
                uri = Uri.parse("geo:0,0?q=Professor+Joao+Machado,Sao+Paulo")
                intent = Intent(Intent.ACTION_VIEW, uri)
                openIntent(intent)
            }
            3 -> {//Abrindo o editor de SMS
                uri = Uri.parse("sms:12345")
                intent = Intent(Intent.ACTION_VIEW, uri)
                    .putExtra("sms_body", "Corpo do SMS")
                openIntent(intent)
            }
            4 -> {//Compartilhar
                intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, "Compartikllhando via Intent.")
                    .setType("text/plain")
                openIntent(intent)
            }
            5 -> {//Alarme
                intent = Intent(AlarmClock.ACTION_SET_ALARM)
                    .putExtra(AlarmClock.EXTRA_MESSAGE, "Estudar Android")
                    .putExtra(AlarmClock.EXTRA_HOUR, 19)
                    .putExtra(AlarmClock.EXTRA_MINUTES, 0)
                    .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                    .putExtra(AlarmClock.EXTRA_DAYS, arrayListOf(
                        Calendar.MONDAY,
                        Calendar.WEDNESDAY,
                        Calendar.FRIDAY)
                    )
                openIntent(intent)
            }
            6 -> {//Busca na Web
                intent = Intent(Intent.ACTION_SEARCH)
                    .putExtra(SearchManager.QUERY, "Novatec")
                openIntent(intent)
            }
            7 -> {//Configurações do aparelho
                intent = Intent(Settings.ACTION_SETTINGS)
                openIntent(intent)
            }
            8 -> {//Ação customizada 1
                intent = Intent("dominando.android.CUSTOM_ACTION")
                openIntent(intent)
            }
            9 -> {//Ação customizada 2
                uri = Uri.parse("produto://Notebook/Slim")
                intent = Intent(Intent.ACTION_VIEW, uri)
                openIntent(intent)
            }
            else -> finish()
        }
    }

    private fun openIntent(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, R.string.error_intent, Toast.LENGTH_SHORT).show()
        }
    }
}
