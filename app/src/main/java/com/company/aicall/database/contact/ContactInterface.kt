package com.company.aicall.database.contact

import com.company.aicall.models.ContactModel

interface ContactInterface {

    suspend fun getContact() : ArrayList<ContactModel>

    suspend fun getContactFilter(arrayList: ArrayList<ContactModel> , type : String) : ArrayList<ContactModel>

}