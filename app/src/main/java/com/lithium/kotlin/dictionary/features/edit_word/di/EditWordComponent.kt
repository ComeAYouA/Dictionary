package com.lithium.kotlin.dictionary.features.edit_word.di

import androidx.fragment.app.DialogFragment
import com.lithium.kotlin.dictionary.features.edit_word.screen.EditWordDialog
import dagger.BindsInstance
import dagger.Subcomponent

@EditWordScope
@Subcomponent
interface EditWordComponent {

    fun inject(editWordFragment: EditWordDialog)

    @Subcomponent.Factory
    interface Factory{

        fun create(
            @BindsInstance dialogFragment: EditWordDialog
        ): EditWordComponent
    }
}