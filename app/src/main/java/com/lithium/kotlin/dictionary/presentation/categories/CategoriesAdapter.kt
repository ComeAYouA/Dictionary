package com.lithium.kotlin.dictionary.presentation.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.domain.localdatabasemodels.Category

private val colors = arrayOf(R.color.purple_200, R.color.purple_500, R.color.purple_700)

class CategoriesAdapter(
    private val callBacks: CategoriesFragment.CallBacks?,
    private val layoutInflater: LayoutInflater
) {
    inner class CategoryHolder(private val item: View) : RecyclerView.ViewHolder(item){

        fun bind(category: Category, colorRow: Int){
            val text = item.findViewById(R.id.category_text_view) as TextView
            val layout = item.findViewById(R.id.category_layout) as LinearLayout
            text.text = category.name
            layout.setBackgroundResource(colors[colorRow])
            layout.setOnClickListener {callBacks?.onCategoryClicked(category) }
        }

    }
    inner class Adapter(val categories: List<Category>): RecyclerView.Adapter<CategoryHolder>(){
        override fun getItemCount(): Int = categories.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            val view = layoutInflater.inflate(R.layout.list_item_category, parent, false)
            return CategoryHolder(
                view
            )
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            holder.bind(category, position%3)
        }
    }
}