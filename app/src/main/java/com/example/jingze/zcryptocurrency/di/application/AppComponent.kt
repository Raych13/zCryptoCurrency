package com.example.jingze.zcryptocurrency.di.application

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import android.app.Application
import com.example.jingze.zcryptocurrency.MyApplication
import dagger.BindsInstance
import dagger.android.AndroidInjector

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        ApplicationModule::class))
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}