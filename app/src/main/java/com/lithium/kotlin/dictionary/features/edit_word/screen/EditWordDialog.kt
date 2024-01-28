package com.lithium.kotlin.dictionary.features.edit_word.screen

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.appComponent
import com.lithium.kotlin.dictionary.databinding.DialogFragmentEditWordBinding
import com.lithium.kotlin.dictionary.features.add_word.screen.DeletableItemAdapter
import com.lithium.kotlin.dictionary.utils.DialogWindowUtil
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

private const val ARG_WORD_ID = "ARG_WORD_ID"

class EditWordDialog: DialogFragment() {


    private var _binding: DialogFragmentEditWordBinding? = null
    private val binding: DialogFragmentEditWordBinding
        get() = _binding!!

    private val translationsRVAdapter: DeletableItemAdapter by lazy { DeletableItemAdapter() }
    private val categoriesRVAdapter: DeletableItemAdapter by lazy { DeletableItemAdapter() }

    @Inject
    lateinit var viewModel: EditWordViewModel

    override fun onAttach(context: Context) {
        context.appComponent.editWordComponent().create(this).inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupWordObserver()
        (arguments?.get(ARG_WORD_ID) as UUID?)?.let {  wordId ->
            viewModel.loadWord(wordId)
        }
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

        with(DialogWindowUtil){
            this@EditWordDialog.setLayoutPercents(90, 65)
        }

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
                    if(word.photoFilePath != ""){
                        Picasso.with(binding.wordIcon.context)
                            .load(File(word.photoFilePath))
                            .resize(240, 240)
                            .into(binding.wordIcon)
                    }else{
                        Picasso.with(binding.wordIcon.context)
                            .load(R.drawable.ic_empty_picture)
                            .resize(240, 240)
                            .into(binding.wordIcon)
                    }

                    binding.wordEditText.setText(word.sequence)

                    translationsRVAdapter.setDeletableItemsList(word.translation)
                    categoriesRVAdapter.setDeletableItemsList(word.categories)
                }
            }
        }
    }
}