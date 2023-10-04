package com.lithium.kotlin.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.navigation.NavigationBarView
import com.lithium.kotlin.dictionary.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), AddWordFragment.CallBacks, DictionaryFragment.CallBacks, EditWordFragment.CallBacks {
    private lateinit var bottomNavigationBar: NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bottomNavigationBar = binding.bottomNavigationBar

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null){
            openFragment(DictionaryFragment())
        }

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_add_word -> {
                    openFragment(AddWordFragment())
                    true
                }
                R.id.menu_dictionary -> {
                    openFragment(DictionaryFragment())
                    true
                }
                R.id.menu_categories -> {
                    openFragment(CategoriesFragment())
                    true
                }
                else -> {false}
            }
        }
    }


    override fun onWordClicked(wordId: UUID) {
        openFragment(EditWordFragment.newInstance(wordId))
    }
    override fun onAddWordButtonClicked() {
        openFragment(DictionaryFragment())
        bottomNavigationBar.selectedItemId = R.id.menu_dictionary
    }

    override fun onEditWordButtonClicked() {
        openFragment(DictionaryFragment())
    }

    private fun openFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}