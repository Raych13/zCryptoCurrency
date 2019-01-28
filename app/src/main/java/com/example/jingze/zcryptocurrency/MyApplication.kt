package com.example.jingze.zcryptocurrency

import com.example.jingze.zcryptocurrency.customized.base.ViewModelFactory
import com.example.jingze.zcryptocurrency.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class MyApplication : DaggerApplication() {

    @Inject
    lateinit var VIEWMODEL_FACTORY: ViewModelFactory

    public override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    fun getViewModelFactoryInstance() : ViewModelFactory {
        return VIEWMODEL_FACTORY
    }
}
