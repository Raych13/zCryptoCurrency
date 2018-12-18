package com.example.jingze.zcryptocurrency.customized

import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory private constructor() : ViewModelProvider.NewInstanceFactory() {

    @Inject
    lateinit var providerFactories: Map<Class<out BaseViewModel>, Provider<AndroidProvider.Factory<out BaseViewModel>>>

    fun <T : BaseViewModel> create(modelClass: Class<T>): T {
//        providerFactories.get(modelClass)?.let { it.get().create().provide() } ?:

        return super.create(modelClass)
    }
}
