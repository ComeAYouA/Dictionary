package com.lithium.kotlin.dictionary

import android.app.Application
import android.content.Context
import com.lithium.kotlin.dictionary.data.di.RepositoryModule
import com.lithium.kotlin.dictionary.presentation.categories.CategoriesFragment
import com.lithium.kotlin.dictionary.presentation.di.AddWordComponent
import com.lithium.kotlin.dictionary.presentation.di.CategoriesFragmentComponent
import com.lithium.kotlin.dictionary.presentation.di.DictionaryFragmentComponent
import com.lithium.kotlin.dictionary.presentation.dictionary.DictionaryFragment
import com.lithium.kotlin.dictionary.presentation.word.AddWordFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope


@Scope
annotation class AppScope

@AppScope
@Component(modules = [RepositoryModule::class])
interface AppComponent{
    fun dictionaryComponent(): DictionaryFragmentComponent
    fun categoriesComponent(): CategoriesFragmentComponent
    fun addWordComponent(): AddWordComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

}


class DictionaryApplication: Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when(this) {
        is DictionaryApplication ->  appComponent
        else -> this.applicationContext.appComponent
    }