package com.lithium.kotlin.dictionary.views

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.FragmentDictionaryBinding
import com.lithium.kotlin.dictionary.databinding.ListItemWordBinding
import com.lithium.kotlin.dictionary.models.Word
import com.lithium.kotlin.dictionary.view_models.DictionaryViewModel
import com.lithium.kotlin.dictionary.view_models.WordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher

import java.util.*


private val PERMISSIONS = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
private const val MY_PERMISSION_ID = 1234
private const val IDS_TAG = "ids"

class DictionaryFragment: Fragment() {

    interface CallBacks{
        fun onWordClicked(wordId: UUID)
    }
    private val viewModel :DictionaryViewModel by lazy {
        ViewModelProvider(this).get(DictionaryViewModel::class.java)
    }
    private var callBacks: CallBacks? = null
    private val gson = Gson()
    private lateinit var binding: FragmentDictionaryBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let{
            val json = it.get(IDS_TAG) as String
            val type = object : TypeToken<List<UUID>>() {}.type
            val ids = gson.fromJson<List<UUID>>(json, type)
            viewModel.load(ids)
        }?:viewModel.load()
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

        binding = view
        view.dictionaryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

         return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.wordsLiveData?.observe(viewLifecycleOwner){
            binding.dictionaryRecyclerView.apply {
                adapter = WordAdapter(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(PERMISSIONS, MY_PERMISSION_ID)
            return
        }
    }
    companion object {
        fun newInstance(ids: List<UUID>): DictionaryFragment {
            val args = Bundle().apply {
                putSerializable(IDS_TAG, Gson().toJson(ids))
            }
            return DictionaryFragment().apply {
                arguments = args
            }
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