package com.company.aicall.database.contact

import com.company.aicall.models.ContactModel
import kotlinx.coroutines.withContext

class ContactProvider : ContactInterface {


    override suspend fun getContact(): ArrayList<ContactModel> {
        return ArrayList()
    }

    override suspend fun getContactFilter(
        arrayList: ArrayList<ContactModel>,
        type: String
    ): ArrayList<ContactModel> {

    }



    fun getContactDB (type: String) : ArrayList<ContactModel> {






    }






}