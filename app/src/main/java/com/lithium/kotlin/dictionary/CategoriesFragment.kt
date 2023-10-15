package com.lithium.kotlin.dictionary

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
private val colors = arrayOf(R.color.blue_cat, R.color.red_cat, R.color.purple_cat, R.color.green_cat, R.color.orange_cat)
class CategoriesFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val repository = WordsRepository.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  = inflater.inflate(R.layout.fragment_categories, container, false)
        recyclerView = view.findViewById(R.id.categories_RV) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
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

    private inner class CategoryHolder(val item: View) : RecyclerView.ViewHolder(item){

        fun bind(category: String, colorRow: Int){
            val text = item.findViewById(R.id.category_text_view) as TextView
            val layout = item.findViewById(R.id.category_layout) as LinearLayout
            text.text = category
            layout.setBackgroundResource(colors[colorRow])

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
            holder.bind(category.name, position%5)
        }
    }
}