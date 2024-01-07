package com.lithium.kotlin.dictionary.presentation.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category
import kotlinx.coroutines.launch

private const val TAG = "CategoriesFragment"

class CategoriesFragment: Fragment() {

    private val viewModel: CategoriesViewModel by lazy {
        ViewModelProvider(this)[CategoriesViewModel::class.java]
    }
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var callBacks: CallBacks? = null

    interface CallBacks {
        fun onCategoryClicked(category: Category)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadCategories()

        categoriesAdapter = CategoriesAdapter(callBacks, layoutInflater)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.categories.collect {
                    categoriesRecyclerView.adapter = categoriesAdapter.Adapter(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        categoriesRecyclerView = view.findViewById(R.id.categories_RV) as RecyclerView

        categoriesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        return view
    }

    override fun onDetach() {
        super.onDetach()

        callBacks = null
    }
}