package com.lithium.kotlin.dictionary.features.edit_word.screen

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.features.main.di.appComponent
import com.lithium.kotlin.dictionary.databinding.DialogFragmentEditWordBinding
import com.lithium.kotlin.dictionary.features.add_word.screen.DeletableItemAdapter
import com.lithium.kotlin.dictionary.utils.PicturesScalingUtil
import com.lithium.kotlin.dictionary.utils.WindowUtil
import com.lithium.kotlin.dictionary.utils.WordIconUtil
import com.squareup.picasso.Picasso
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

private const val ARG_WORD_ID = "ARG_WORD_ID"

class EditWordDialog: DialogFragment() {

    init {
        Log.d("myTag", "dialogInit")
    }

    private val translationsRVAdapter: DeletableItemAdapter by lazy { DeletableItemAdapter() }

    private val categoriesRVAdapter: DeletableItemAdapter by lazy { DeletableItemAdapter() }

    @Inject
    lateinit var viewModel: EditWordViewModel

//    private val viewModel: EditWordViewModel by viewModels { viewModelFactory }

    private var _binding: DialogFragmentEditWordBinding? = null
    private val binding: DialogFragmentEditWordBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        context.appComponent.editWordComponent().create(this).inject(this)
        super.onAttach(context)
        Log.d("myTag", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupWordObserver()
        (arguments?.get(ARG_WORD_ID) as UUID?)?.let {  wordId ->
            viewModel.loadWord(wordId)
        }
        Log.d("myTag", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_edit_word,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(
            (WindowUtil.getWindowWidth() * 0.84).toInt(),
            (WindowUtil.getWindowHeight() * 0.72).toInt()
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.translationRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = translationsRVAdapter
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = categoriesRVAdapter
        }

    }

    override fun onDestroy() {
        Log.d("myTag", "onDestroy")

        super.onDestroy()

        viewModel.saveWord(
            viewModel.word.value.copy(
                sequence = binding.wordEditText.text.toString(),
                translation = translationsRVAdapter.deletableItemsList,
                categories = categoriesRVAdapter.deletableItemsList,
            )
        )
    }

    override fun onDetach() {
        Log.d("myTag", "onDetach")
        super.onDetach()
    }

    companion object{
        fun newInstance(wordId: UUID): EditWordDialog{
            val args = Bundle().apply {
                putSerializable(ARG_WORD_ID, wordId)
            }

            return EditWordDialog().apply {
                arguments = args
            }
        }
    }

    private fun setupWordObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.word.collect { word ->

                    WordIconUtil.loadCorrectWordIcon(word.photoFilePath, binding.wordIcon).also {
                        val bitmap = this.async (Dispatchers.IO){
                            it.get()
                        }.await()
                        val size = PicturesScalingUtil.getSizeThatFitIntoLayout(
                            bitmap.width,
                            bitmap.height,
                            binding.wordIcon.measuredWidth,
                            binding.wordIcon.measuredHeight
                        )
                        Log.d("myTag", "size $size")
                        it.resize(size.width, size.height)
                    }
                        .into(binding.wordIcon)

                    binding.wordEditText.setText(word.sequence)

                    translationsRVAdapter.setDeletableItemsList(word.translation)
                    categoriesRVAdapter.setDeletableItemsList(word.categories)
                }
            }
        }
    }
}