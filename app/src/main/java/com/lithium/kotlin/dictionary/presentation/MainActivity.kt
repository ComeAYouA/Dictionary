package com.lithium.kotlin.dictionary.presentation

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelUuid

import androidx.databinding.DataBindingUtil
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category
import com.lithium.kotlin.dictionary.presentation.categories.CategoriesFragment
import com.lithium.kotlin.dictionary.presentation.dictionary.DictionaryFragment
import com.lithium.kotlin.dictionary.presentation.word.EditWordFragment
import java.util.*

private const val DICTIONARY_FRAGMENT_TAG = "DictionaryFragment"
private const val EDIT_WORD_FRAGMENT_TAG = "EditWordFragment"
private const val CATEGORIES_FRAGMENT_TAG = "CategoriesFragment"

class MainActivity : AppCompatActivity(),
    DictionaryFragment.CallBacks,
    EditWordFragment.CallBacks ,
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

    override fun onEditWordButtonClicked() {
        bottomNavigationBar.selectedItemId = R.id.dictionaryFragment
    }

    override fun onCategoryClicked(category: Category) {
    }
}