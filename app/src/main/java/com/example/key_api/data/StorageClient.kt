package com.example.key_api.data

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}