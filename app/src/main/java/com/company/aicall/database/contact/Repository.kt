package com.company.aicall.database.contact

import android.database.Cursor
import com.company.aicall.models.ContactModel

class Repository(private var contactProvider: ContactProvider) {

      suspend fun getDataContact () : ArrayList<ContactModel>
      =  contactProvider.getContact()

      suspend fun getDataContactFilter (arrayList: ArrayList<ContactModel>) : ArrayList<ContactModel>
      =  contactProvider.getContactFilter(arrayList,"")


}