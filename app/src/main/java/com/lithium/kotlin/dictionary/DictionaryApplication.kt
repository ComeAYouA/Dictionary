package com.lithium.kotlin.dictionary

import android.app.Application
import android.content.Context
import com.lithium.kotlin.dictionary.data.di.RepositoryModule
import com.lithium.kotlin.dictionary.domain.di.UseCaseModule
import com.lithium.kotlin.dictionary.features.add_word.di.AddWordComponent
import com.lithium.kotlin.dictionary.features.categories.di.CategoriesComponent
import com.lithium.kotlin.dictionary.features.dictionary.di.DictionaryComponent
import com.lithium.kotlin.dictionary.features.edit_word.di.EditWordComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope


@Scope
annotation class AppScope

@AppScope
@Component(modules = [RepositoryModule::class, UseCaseModule::class])
interface AppComponent{
    fun dictionaryComponent(): DictionaryComponent.Factory
    fun categoriesComponent(): CategoriesComponent.Factory
    fun addWordComponent(): AddWordComponent.Factory
    fun editWordComponent(): EditWordComponent.Factory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

}


class DictionaryApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when(this) {
        is DictionaryApplication ->  appComponent
        else -> this.applicationContext.appComponent
    }