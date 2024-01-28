package com.lithium.kotlin.dictionary.features.categories.sreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.appComponent
import com.lithium.kotlin.dictionary.domain.models.Category
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CategoriesFragment"

class CategoriesFragment: Fragment() {

    @Inject
    lateinit var viewModel: CategoriesViewModel
    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var categoriesRecyclerView: RecyclerView
    var callBacks: CallBacks? = null

    interface CallBacks {
        fun onCategoryClicked(category: Category)
    }

    override fun onAttach(context: Context) {
        context.appComponent.categoriesComponent().create(this).inject(this)

        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.loadCategories()

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