package com.lithium.kotlin.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.lithium.kotlin.dictionary.databinding.ActivityMainBinding
import com.lithium.kotlin.dictionary.models.Category
import com.lithium.kotlin.dictionary.views.CategoriesFragment
import com.lithium.kotlin.dictionary.views.DictionaryFragment
import com.lithium.kotlin.dictionary.views.EditWordFragment
import java.util.*

class MainActivity : AppCompatActivity(),
    DictionaryFragment.CallBacks,
    EditWordFragment.CallBacks ,
    CategoriesFragment.CallBacks{
    private lateinit var bottomNavigationBar: NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bottomNavigationBar = binding.bottomNavigationBar

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null){
            supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.fragment_container, DictionaryFragment())
                .commit()
        }

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_add_word -> {
                    openFragment(EditWordFragment.newInstance(), backStack = true, clear = true)
                    true
                }
                R.id.menu_dictionary -> {
                    openFragment(DictionaryFragment(), clear = true)
                    true
                }
                R.id.menu_categories -> {
                    openFragment(CategoriesFragment(), clear = true)
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