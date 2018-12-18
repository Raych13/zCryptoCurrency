package com.example.jingze.zcryptocurrency.di.application

import com.example.jingze.zcryptocurrency.customized.AndroidProvider
import com.example.jingze.zcryptocurrency.customized.BaseViewModel
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.Multibinds

@Module(includes = arrayOf(AndroidSupportInjectionModule::class))
abstract class AndroidSupportInjectionAndProviderModule {

    @Multibinds
    abstract fun baseViewModelFactories(): Map<Class<out BaseViewModel>, AndroidProvider.Factory<out BaseViewModel>>
}