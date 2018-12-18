package com.example.jingze.zcryptocurrency.view.market_list

import com.example.jingze.zcryptocurrency.customized.BaseViewModel
import com.example.jingze.zcryptocurrency.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MarketListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MarketListViewModel::class)
    abstract fun bindAndroidInjectorFactory(t: MarketListViewModel): BaseViewModel
}
