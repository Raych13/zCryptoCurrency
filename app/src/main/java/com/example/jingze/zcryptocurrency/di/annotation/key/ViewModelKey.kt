package com.example.jingze.zcryptocurrency.di.annotation.key

import com.example.jingze.zcryptocurrency.customized.BaseViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ViewModelKey(val value: KClass<out BaseViewModel>)

// Java Stub
//import static java.lang.annotation.ElementType.METHOD;
//
//import com.example.jingze.zcryptocurrency.customized.BaseViewModel;
//import dagger.MapKey;
//import java.lang.annotation.Target;
//
//@MapKey
//@Target(METHOD)
//public @interface ViewModelKey {
//    Class<? extends BaseViewModel> value();
//}