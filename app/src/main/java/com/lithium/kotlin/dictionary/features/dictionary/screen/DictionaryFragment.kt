package com.lithium.kotlin.dictionary.features.dictionary.screen

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.features.main.di.appComponent
import com.lithium.kotlin.dictionary.databinding.FragmentDictionaryBinding
import com.lithium.kotlin.dictionary.features.edit_word.screen.EditWordDialog
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private val PERMISSIONS = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
private const val MY_PERMISSION_ID = 1234

class DictionaryFragment: Fragment() {

    interface CallBacks{ fun onWordClicked(wordId: UUID) }

    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!
    private val dictionaryAdapter: DictionaryAdapter by lazy { DictionaryAdapter() }

    @Inject
    lateinit var viewModelFactory : DictionaryViewModel.Factory
    private val viewModel: DictionaryViewModel by viewModels { viewModelFactory }
    var callBacks: CallBacks? = null


    override fun onAttach(context: Context){
        context.appComponent.dictionaryComponent().create(this).inject(this)

        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding  = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dictionary,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDictionaryRv()
        setupSearchView()

    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(PERMISSIONS, MY_PERMISSION_ID)
            return
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    private fun setupData(){
       viewModel.loadDictionary()
    }

    private fun setupObservers(){
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.tempWords.collect {
                    dictionaryAdapter.setWords(it)
                }
            }
        }
    }

    private fun setupDictionaryRv() {
        binding.dictionaryRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dictionaryAdapter
        }
        dictionaryAdapter.setWordEditClickListener { word ->
            EditWordDialog.newInstance(word.id).show(requireFragmentManager(), "")
        }
    }

    private fun setupSearchView(){

        binding.searchView.apply {
            queryHint = "Введите слово"
            isIconifiedByDefault = false

            setOnQueryTextListener( object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { viewModel.searchWord(it) }
                    return true
                }

            }
            )
        }

    }
}