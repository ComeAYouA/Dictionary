package com.lithium.kotlin.dictionary.features.educational_cards.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lithium.kotlin.dictionary.features.main.di.appComponent
import com.lithium.kotlin.dictionary.features.educational_cards.screen.educationalCardsWidget.EducationalCardWidget
import javax.inject.Inject

class EducationalCardsFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: EducationalCardsViewModel.Factory

    private val viewModel: EducationalCardsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        context.appComponent.educationalCardsComponent().create(this).inject(this)
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                EducationalCardWidget(
                    viewModel
                )
            }
        }
    }
}