package di

import data.MongoDB
import data.viewmodels.HomeVM
import data.viewmodels.NoteDetailsVM
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import screens.HomePageNotes


val mongoModule = module {
    single { MongoDB() }
    single { HomePageNotes(get()) }
    factory { HomeVM(get()) }
    factory { NoteDetailsVM(get()) }
}

fun initKoin() {
    startKoin { modules(mongoModule) }
}