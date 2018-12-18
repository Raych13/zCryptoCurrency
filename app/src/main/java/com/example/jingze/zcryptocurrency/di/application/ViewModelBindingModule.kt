package com.example.jingze.zcryptocurrency.di.application

import com.example.jingze.zcryptocurrency.view.market_list.MarketListViewModelModule
import dagger.Module

@Module(includes = arrayOf(MarketListViewModelModule::class))
abstract class ViewModelBindingModule internal constructor()