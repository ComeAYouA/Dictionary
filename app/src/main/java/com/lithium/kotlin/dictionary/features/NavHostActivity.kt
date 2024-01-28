package com.lithium.kotlin.dictionary.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.domain.models.Category
import com.lithium.kotlin.dictionary.features.categories.sreen.CategoriesFragment
import com.lithium.kotlin.dictionary.features.dictionary.screen.DictionaryFragment
import com.lithium.kotlin.dictionary.features.add_word.screen.AddWordFragment
import java.util.*


class NavHostActivity : AppCompatActivity(),
    DictionaryFragment.CallBacks,
    AddWordFragment.CallBacks ,
    CategoriesFragment.CallBacks {

    private lateinit var bottomNavigationBar: NavigationBarView
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)

        navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationBar.setupWithNavController(navController)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onWordClicked(wordId: UUID) {
    }

    override fun onAddWordButtonClicked() {
        bottomNavigationBar.selectedItemId = R.id.dictionaryFragment
    }

    override fun onCategoryClicked(category: Category) {
    }
}