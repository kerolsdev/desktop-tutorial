package com.company.aicall

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.company.aicall.models.ContactModel


@Composable
fun MainActivityScreen () {

    var viewmodel

    Box {
         LazyColumn(modifier = Modifier.fillMaxSize()) {

             ListItem()

         }



    }


    @Composable
    fun ListItem(data: ContactModel , modifier: Modifier = Modifier) {
        Row(modifier.fillMaxWidth()) {
            Text(text = data.name)
            // … other composables required for displaying `data`
        }
    }


}



