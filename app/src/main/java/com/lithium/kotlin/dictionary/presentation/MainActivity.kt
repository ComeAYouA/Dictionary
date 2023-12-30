package com.lithium.kotlin.dictionary.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.ActivityMainBinding
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category
import com.lithium.kotlin.dictionary.presentation.categories.CategoriesFragment
import com.lithium.kotlin.dictionary.presentation.dictionary.DictionaryFragment
import com.lithium.kotlin.dictionary.presentation.word.EditWordFragment
import java.util.*

private const val DICTIONARY_FRAGMENT_TAG = "DictionaryFragment"
private const val WORD_FRAGMENT_TAG = "DictionaryFragment"
private const val CATEGORIES_FRAGMENT_TAG = "DictionaryFragment"
private const val DICTIONARY_TAG = "DictionaryFragment"
class MainActivity : AppCompatActivity(),
    DictionaryFragment.CallBacks,
    EditWordFragment.CallBacks ,
    CategoriesFragment.CallBacks{

    private lateinit var bottomNavigationBar: NavigationBarView
    private lateinit var currentFragmentId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        bottomNavigationBar = binding.bottomNavigationBar

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null){
            supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_container, DictionaryFragment())
                .commit()
            currentFragmentId = DICTIONARY_FRAGMENT_TAG
        }

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_add_word -> {
                    openFragment(EditWordFragment.newInstance(), clear = true)
                    currentFragmentId = WORD_FRAGMENT_TAG
                    true
                }
                R.id.menu_dictionary -> {
                    openFragment(DictionaryFragment(), clear = true)
                    currentFragmentId = DICTIONARY_FRAGMENT_TAG
                    true
                }
                R.id.menu_categories -> {
                    openFragment(CategoriesFragment(), clear = true)
                    currentFragmentId = CATEGORIES_FRAGMENT_TAG
                    true
                }
                else -> {false}
            }
        }
    }

    override fun onWordClicked(wordId: UUID) {
        openFragment(EditWordFragment.newInstance(wordId), true)
    }
    override fun onAddWordButtonClicked() {
        openFragment(DictionaryFragment())
        bottomNavigationBar.selectedItemId = R.id.menu_dictionary
    }

    override fun onEditWordButtonClicked() {
        openFragment(DictionaryFragment())
        bottomNavigationBar.selectedItemId = R.id.menu_dictionary
    }

    override fun onCategoryClicked(category: Category) {
        openFragment(DictionaryFragment.newInstance(category.ids.toList()), true)
    }

    private fun openFragment(fragment: Fragment, backStack: Boolean = false, clear: Boolean = false){

        if (clear) supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragment_container, fragment)
            .apply {
                if (backStack) {
                    addToBackStack(null)
                    commit()
                }else{
                    commit()
                }
            }
    }
}