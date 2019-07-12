package com.example.jingze.zcryptocurrency.customized.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.VisibleForTesting
import com.example.jingze.zcryptocurrency.MyApplication
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
        val providers: MutableMap<Class<out BaseViewModel>, Provider<BaseViewModel>>)
    : ViewModelProvider.NewInstanceFactory() {

    fun <T : BaseViewModel> create(modelClass: Class<T>): T {
        val provider = providers[modelClass] ?: return super.create(modelClass)
        val viewModel = provider.get()
        if (modelClass.isInstance(viewModel)) {
            @Suppress("UNCHECKED_CAST")
            return viewModel as T
        }
        return super.create(modelClass)
    }

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: (application as MyApplication).getViewModelFactoryInstance()
                            .apply { INSTANCE = this }
                }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
