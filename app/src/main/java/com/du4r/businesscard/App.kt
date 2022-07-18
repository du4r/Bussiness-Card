package com.du4r.businesscard

import android.app.Application
import com.du4r.businesscard.data.AppDatabase
import com.du4r.businesscard.data.BusinessCardRepository

class App: Application() {
    val database by lazy {AppDatabase.getDatabase(this)}
    val repository by lazy {BusinessCardRepository(database.businessDao())}
}
//mudar tudo para business