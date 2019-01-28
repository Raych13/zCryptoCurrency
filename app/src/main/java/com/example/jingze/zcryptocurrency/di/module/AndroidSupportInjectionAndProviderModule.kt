package com.example.jingze.zcryptocurrency.di.module

import com.example.jingze.zcryptocurrency.customized.base.BaseViewModel
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.Multibinds

@Module(includes = arrayOf(AndroidSupportInjectionModule::class))
abstract class AndroidSupportInjectionAndProviderModule {

    @Multibinds
    abstract fun viewModelProviders(): Map<Class<out BaseViewModel>, BaseViewModel>
}