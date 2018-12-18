package com.example.jingze.zcryptocurrency.view.market_list

import com.example.jingze.zcryptocurrency.customized.AndroidProvider
import com.example.jingze.zcryptocurrency.customized.BaseViewModel
import com.example.jingze.zcryptocurrency.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(MarketListViewModelModule.MarketListViewModelSubcomponent::class))
abstract class MarketListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MarketListViewModel::class)
    abstract fun bindAndroidInjectorFactory(builder: MarketListViewModelSubcomponent.Builder)
            : AndroidProvider.Factory<out BaseViewModel>

    @Subcomponent
    interface MarketListViewModelSubcomponent : AndroidProvider<MarketListViewModel> {

        @Subcomponent.Builder
        abstract class Builder : AndroidProvider.Builder<MarketListViewModel>()
    }
}
