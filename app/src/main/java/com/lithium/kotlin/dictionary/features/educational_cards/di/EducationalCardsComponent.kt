package com.lithium.kotlin.dictionary.features.educational_cards.di

import com.lithium.kotlin.dictionary.features.edit_word.di.EditWordScope
import com.lithium.kotlin.dictionary.features.edit_word.screen.EditWordDialog
import com.lithium.kotlin.dictionary.features.educational_cards.screen.EducationalCardsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@EducationalCardsScope
@Subcomponent
interface EducationalCardsComponent {

    fun inject(fragment: EducationalCardsFragment)

    @Subcomponent.Factory
    interface Factory{

        fun create(
            @BindsInstance educationalFragment: EducationalCardsFragment
        ): EducationalCardsComponent
    }
}