package com.example.jingze.zcryptocurrency.customized

interface AndroidProvider<T> {

    fun get(): T

    interface Factory<T> {

        fun create(): AndroidProvider<T>
    }

    abstract class Builder<T> : AndroidProvider.Factory<T>
}