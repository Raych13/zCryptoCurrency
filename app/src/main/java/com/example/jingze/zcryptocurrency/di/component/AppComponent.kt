package com.example.jingze.zcryptocurrency.di.component

import dagger.Component
import javax.inject.Singleton
import android.app.Application
import com.example.jingze.zcryptocurrency.MyApplication
import com.example.jingze.zcryptocurrency.di.module.ActivityBindingModule
import com.example.jingze.zcryptocurrency.di.module.AndroidSupportInjectionAndProviderModule
import com.example.jingze.zcryptocurrency.di.module.AppModule
import com.example.jingze.zcryptocurrency.di.module.ViewModelBindingModule
import dagger.BindsInstance
import dagger.android.AndroidInjector

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        ActivityBindingModule::class,
        ViewModelBindingModule::class,
        AndroidSupportInjectionAndProviderModule::class))
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}