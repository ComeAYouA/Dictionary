package com.lithium.kotlin.dictionary.presentation.dictionary

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.FragmentDictionaryBinding
import kotlinx.coroutines.launch
import java.util.UUID

private val PERMISSIONS = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
private const val MY_PERMISSION_ID = 1234


class DictionaryFragment: Fragment() {

    interface CallBacks{
        fun onWordClicked(wordId: UUID)
    }

    private lateinit var binding: FragmentDictionaryBinding
    private val viewModel : DictionaryViewModel by lazy {
        ViewModelProvider(this)[DictionaryViewModel::class.java]
    }
    private lateinit var dictionaryAdapter: DictionaryAdapter
    private val args: DictionaryFragmentArgs by navArgs()
    private var callBacks: CallBacks? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBacks = context as CallBacks?

        Log.d("myTag", "DictionaryFragment attached")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("myTag", "DictionaryFragment Opened")

        args.ids?.let { ids ->
            viewModel.load(ids.toList().map { id -> id.uuid })
        }?:viewModel.load()


        lifecycleScope.launch {
            dictionaryAdapter = DictionaryAdapter(requireContext(), callBacks, layoutInflater)

            repeatOnLifecycle(Lifecycle.State.STARTED){
                Log.d("myTag", "Dictionary scope")

                viewModel.words.collect {
                    binding.dictionaryRecyclerView.adapter = dictionaryAdapter.Adapter(it)
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding  = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dictionary,
            container,
            false
        )

        binding.dictionaryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }


        return binding.root
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
}