package com.company.aicall

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.company.aicall.ui.theme.AiCallTheme

class PermissionActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
               startActivity(Intent(this,MainActivity::class.java))
               finish()
        }

        setContent {

            AiCallTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    PermissionScreen()

                }
            }
        }
    }
}
