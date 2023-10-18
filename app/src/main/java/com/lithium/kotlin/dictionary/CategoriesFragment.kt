package com.lithium.kotlin.dictionary

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

private const val TAG = "CategoriesFragment"
private val colors = arrayOf(R.color.purple_200, R.color.purple_500, R.color.purple_700)
class CategoriesFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val repository = WordsRepository.get()
    private var callBacks: CallBacks? = null

    interface CallBacks{
        fun onCategoryClicked(category: Category)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callBacks = context as CallBacks?
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  = inflater.inflate(R.layout.fragment_categories, container, false)
        recyclerView = view.findViewById(R.id.categories_RV) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repository.getCategories().observe(
            viewLifecycleOwner
        ){ categories ->
            recyclerView.apply {
                adapter = CategoryAdapter(categories)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()

        callBacks = null
    }

    private inner class CategoryHolder(val item: View) : RecyclerView.ViewHolder(item){

        fun bind(category: Category, colorRow: Int){
            val text = item.findViewById(R.id.category_text_view) as TextView
            val layout = item.findViewById(R.id.category_layout) as LinearLayout
            text.text = category.name
            layout.setBackgroundResource(colors[colorRow])
            layout.setOnClickListener { callBacks?.onCategoryClicked(category) }
        }
    }
    private inner class CategoryAdapter(val categories: List<Category>): RecyclerView.Adapter<CategoryHolder>(){
        override fun getItemCount(): Int = categories.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            val view = layoutInflater.inflate(R.layout.list_item_category, parent, false)
            return CategoryHolder(
                view
            )
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            Log.d(TAG, category.ids.fold(""){acc, s -> acc + "    " + s })
            holder.bind(category, position%5)
        }
    }
}