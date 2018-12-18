package com.example.jingze.zcryptocurrency

import com.example.jingze.zcryptocurrency.customized.BaseViewModel
import com.example.jingze.zcryptocurrency.di.application.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject
import javax.inject.Provider

class MyApplication : DaggerApplication() {

    @Inject
    lateinit var providers: MutableMap<Class<out BaseViewModel>, Provider<BaseViewModel>>

    public override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
