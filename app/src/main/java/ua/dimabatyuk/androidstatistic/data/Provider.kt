package ua.dimabatyuk.androidstatistic.data

interface Provider<T> {
    suspend fun get(): T
}