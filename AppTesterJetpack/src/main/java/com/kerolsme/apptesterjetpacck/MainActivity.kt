package com.kerolsme.apptesterjetpacck

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kerolsme.apptesterjetpacck.ff.AppTesterJetpacckTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    var period : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            Log.e("TAG", "onCreate:dsadsdsdsasa ", )

        }
        super.onCreate(savedInstanceState)

     /*   val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    val ii = false
                    return if (ii) {
                        // The content is ready. Start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            })*/

        setContent {
            AppTesterJetpacckTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "sex $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, device = "id:Nexus 5")
@Composable
fun GreetingPreview() {
    AppTesterJetpacckTheme {
        Greeting("Android")
    }
}