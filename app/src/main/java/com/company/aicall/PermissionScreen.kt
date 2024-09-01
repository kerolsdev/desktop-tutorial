package com.company.aicall

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.company.aicall.ui.theme.AiCallTheme
import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract


@Composable
fun PermissionScreen () {

    val ctx = LocalContext.current
    var checkedCallPhone: Boolean by rememberSaveable { mutableStateOf(false) }
    var checkedCallLogs by rememberSaveable { mutableStateOf(false) }
    var checkedContact by rememberSaveable { mutableStateOf(false) }


    checkedCallLogs = (ContextCompat.checkSelfPermission(ctx,Manifest.permission.READ_CALL_LOG)
            == PackageManager.PERMISSION_GRANTED)


    checkedCallPhone = (ContextCompat.checkSelfPermission(ctx,Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED)


    checkedContact = (ContextCompat.checkSelfPermission(ctx,Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED)


    val launcherCall = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            // Log.d("ExampleScreen","PERMISSION GRANTED")
            checkedCallPhone = true
        } else {
            checkedCallPhone = false
            // Permission Denied: Do something
            // Log.d("ExampleScreen","PERMISSION DENIED")

        }
    }

    val launcherContact = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            // Log.d("ExampleScreen","PERMISSION GRANTED")
            checkedContact = true
        } else {
            checkedContact = false
            // Permission Denied: Do something
            // Log.d("ExampleScreen","PERMISSION DENIED")
        }
    }

    val launcherCallLogs = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            // Log.d("ExampleScreen","PERMISSION GRANTED")
            checkedCallLogs = true

        } else {
            checkedCallLogs = false
            // Permission Denied: Do something
            // Log.d("ExampleScreen","PERMISSION DENIED")
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .safeGesturesPadding()) {

        Column(
            modifier = Modifier
                .wrapContentSize()
                .verticalScroll(rememberScrollState())

        ) {

            Text(
                text = "Need Permission For App Work",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
            ) {

                Checkbox(
                    checked = checkedCallPhone,
                    onCheckedChange = {
                        checkedCallPhone = it
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(ctx,
                                android.Manifest.permission.CALL_PHONE
                            ) -> {
                                // Some works that require permission
                            }
                            else -> {
                                // Asking for permission
                                launcherCall.launch(Manifest.permission.CALL_PHONE)
                            }
                        }
                    }
                )

                Text(
                    "Call Permission"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
            ) {

                Checkbox(
                    checked = checkedCallLogs,
                    onCheckedChange = { checkedCallLogs = it
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(ctx,
                                Manifest.permission.CALL_PRIVILEGED
                            ) -> {
                                // Some works that require permission

                            }
                            else -> {
                                // Asking for permission
                                launcherCallLogs.launch(Manifest.permission.READ_CALL_LOG)
                            }
                        }

                    }
                )

                Text(
                    "Call PRIVILEGED"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
            ) {

                Checkbox(
                    checked = checkedContact,
                    onCheckedChange = { checkedContact = it

                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(ctx,
                                Manifest.permission.READ_CONTACTS
                            ) -> {
                                // Some works that require permission
                            }
                            else -> {
                                // Asking for permission
                                launcherContact.launch(Manifest.permission.READ_CONTACTS)
                            }
                        }

                    }
                )

                Text(
                    "Contact Permission"
                )
            }

        }
        Button(onClick = {

            if (checkedContact && checkedCallLogs && checkedCallPhone)
            {
                ctx.startActivity(Intent(ctx,MainActivity::class.java))
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp))
        {
            Text(text = "Next")
        }


    }



}




@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun GreetingPreview() {
    AiCallTheme {
        PermissionScreen()
    }
}