package com.lithium.kotlin.dictionary

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lithium.kotlin.dictionary.databinding.FragmentDictionaryBinding
import com.lithium.kotlin.dictionary.databinding.ListItemWordBinding
import com.squareup.picasso.Picasso
import java.net.URL
import java.util.*


private val PERMISSIONS = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
private const val MY_PERMISSION_ID = 1234

class DictionaryFragment: Fragment() {

    interface CallBacks{
        fun onWordClicked(wordId: UUID)
    }

    private val repository = WordsRepository.get()
    private var callBacks: CallBacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callBacks = context as CallBacks?
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = DataBindingUtil.inflate<FragmentDictionaryBinding>(
            inflater,
            R.layout.fragment_dictionary,
            container,
            false
        )

        view.dictionaryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            repository.getWords().observe(viewLifecycleOwner){
                adapter = WordAdapter(it)
            }
        }
         return view.root
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(PERMISSIONS, MY_PERMISSION_ID)
            return
        }
    }

    override fun onDetach() {
        super.onDetach()

        callBacks = null
    }

    private inner class WordHolder(private val binding: ListItemWordBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.viewModel = WordViewModel(requireContext())
        }

        fun bind(word: Word){
            binding.apply {
                viewModel?.word = word
                viewModel?.loadIcon(binding.wordIcon, word.photoFilePath)
                wordLayout.setOnClickListener{
                    callBacks?.onWordClicked(word.id)
                }
            }
        }
    }

    private inner class WordAdapter(val words: List<Word>) : RecyclerView.Adapter<WordHolder>(){
        override fun getItemCount(): Int = words.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
            val binding = DataBindingUtil.inflate<ListItemWordBinding>(
                layoutInflater,
                R.layout.list_item_word,
                parent,
                false
            )
            return WordHolder(binding)
        }

        override fun onBindViewHolder(holder: WordHolder, position: Int) {
            val word = words[position]
            holder.bind(word)
        }
    }
}