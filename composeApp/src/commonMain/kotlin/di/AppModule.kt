package di

import data.MongoDB
import data.viewmodels.HomeVM
import org.koin.core.context.startKoin
import org.koin.dsl.module


val mongoModule = module {
    single { MongoDB() }
    factory { HomeVM(get()) }
}

fun initKoin() {
    startKoin { modules(mongoModule) }
}