@file:OptIn(ExperimentalMaterial3Api::class)

package com.company.etolv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.EditCommand
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.etolv.api.LoginModel
import com.company.etolv.api.MyViewModelFactory
import com.company.etolv.api.RetrofitClient
import com.company.etolv.api.RetrofitMvvm
import com.company.etolv.api.RetrofitRepository
import com.company.etolv.api.kerols
import com.company.etolv.api.ss2
import com.company.etolv.api.ssd
import com.company.etolv.api.type
import com.company.etolv.ui.theme.EtolvTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.putString("le","kerols")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            EtolvTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text(" ", color = Color.Blue)
                        }
                    )
                }
                ) { innerPadding ->
                    HomePage(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }



    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.e("TAG", "onRestoreInstanceState: " + savedInstanceState.getString("le") )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun postDataUsingRetrofit(context : Context, user : String, password : String) {
        val loginModel = LoginModel(user,password)

        GlobalScope.launch(Dispatchers.IO) {

            val response = try {
                RetrofitClient.api(context.getString(R.string.Token)).login(loginModel)

            } catch (re : RuntimeException) {
                Log.e("TAG", "postDataUsingRetrofit: ",re )
                Toast.makeText(context,"Check your internet Connection and Try Again",Toast.LENGTH_LONG).show()
                return@launch

            }

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(context,"onResponse Succeed",Toast.LENGTH_LONG).show()
                    context.startActivity(Intent(context,MainActivity2::class.java))
                } else {
                    Log.e(RetrofitMvvm::class.java.name, "getDataForScope: ${response.message()}", )
                }
            }
        }


    }
}

@Composable
fun HomePage(name: String, modifier: Modifier = Modifier) {

    val ctx = LocalContext.current

    var textuser by remember { mutableStateOf("2110007") }

    var textpass by remember { mutableStateOf("123456") }

    Log.e("TAG", "HomePagedsadsadsa: ", )
    Column(
        modifier = modifier
    ) {

      Box(
          Modifier
              .align(Alignment.CenterHorizontally)
              .fillMaxWidth()
              .heightIn(55.dp)
              .background(MaterialTheme.colorScheme.primaryContainer)){

          Text(text = ctx.getString(R.string.app_name),
              Modifier.align(Alignment.Center),
              fontSize = TextUnit(value = 21f , TextUnitType.Sp),
              color = Color.Black)
      }


        TextField(value = textuser , onValueChange = { textuser = it } , modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        )

        TextField(value = textpass , onValueChange = { textpass = it } , modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        )

        Button(onClick = {

            ctx.startActivity(Intent(ctx,MainActivity2::class.java))

        },
                 modifier  = Modifier
                     .fillMaxWidth(fraction = 0.5f)
                     .padding(16.dp)
                     .align(Alignment.CenterHorizontally)
        )  {
                Text(text = "Sing in")
        }
    }

}
  /*  var counter : Int = 0

    fun ssd () {

        Log.e("kerols", "ssd: ${counter++}", )

    }*/

/*    @OptIn(DelicateCoroutinesApi::class)
private fun postDataUsingRetrofit(context : Context, user : String, password : String) {
    val loginModel = LoginModel(user,password)

    GlobalScope.launch(Dispatchers.IO) {

       val response = try {
            RetrofitClient.api(context.getString(R.string.Token)).login(loginModel)

        } catch (re : RuntimeException) {
           Log.e("TAG", "postDataUsingRetrofit: ",re )
           Toast.makeText(context,"Check your internet Connection and Try Again",Toast.LENGTH_LONG).show()
           return@launch

       }

        withContext(Dispatchers.Main) {
            if (response.isSuccessful && response.body() != null) {
                Toast.makeText(context,"onResponse Succeed",Toast.LENGTH_LONG).show()
                context.startActivity(Intent(context,MainActivity2::class.java))
            } else {
                Log.e(RetrofitMvvm::class.java.name, "getDataForScope: ${response.message()}", )
            }
        }

    }


}*/

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun GreetingPreview() {
    EtolvTheme {
        EtolvTheme {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                TopAppBar (
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(" ", color = Color.Blue)
                    }
                )
            }
            ) { innerPadding ->
                HomePage (
                    name = "Android",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
