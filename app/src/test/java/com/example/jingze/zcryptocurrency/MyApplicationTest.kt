package com.example.jingze.zcryptocurrency

import com.example.jingze.zcryptocurrency.customized.base.BaseViewModel
import com.example.jingze.zcryptocurrency.view.market_list.MarketListViewModel
import dagger.android.AndroidInjector
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import javax.inject.Provider
import kotlin.reflect.KClass

class MyApplicationTest {

    private val mApplication: MyApplication = MyApplication()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testApplicationDI() {
        doInjection()

        // Verify all the injected members
        assertNotNull(mApplication.VIEWMODEL_FACTORY)
    }

    @Test
    fun testViewModelFactory() {
        doInjection()

        val viewModelFactory = mApplication.getViewModelFactoryInstance()

        val viewModelProviders = viewModelFactory.providers

        val viewModelProvidersSize = 1
        assertEquals(viewModelProvidersSize, viewModelProviders.size)

        val marketListViewModel = viewModelFactory.create(MarketListViewModel::class.java)
        assertTrue(MarketListViewModel::class.isInstance(marketListViewModel))
    }

    private fun doInjection() {
        val applicationInjector = mApplication.applicationInjector() as AndroidInjector<MyApplication>
        applicationInjector.inject(mApplication)
    }
}