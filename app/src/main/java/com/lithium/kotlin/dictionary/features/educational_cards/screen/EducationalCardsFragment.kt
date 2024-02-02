package com.lithium.kotlin.dictionary.features.educational_cards.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lithium.kotlin.dictionary.R

class EducationalCardsFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_educational_cards,
            container,
            false
        )

        return view
    }
}