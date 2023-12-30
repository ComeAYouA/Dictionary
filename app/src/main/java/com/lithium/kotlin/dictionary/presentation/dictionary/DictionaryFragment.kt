package com.lithium.kotlin.dictionary.presentation.dictionary

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.databinding.FragmentDictionaryBinding
import java.util.UUID

private val PERMISSIONS = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
private const val MY_PERMISSION_ID = 1234
private const val IDS_TAG = "ids"

class DictionaryFragment: Fragment() {

    interface CallBacks{
        fun onWordClicked(wordId: UUID)
    }

    private lateinit var binding: FragmentDictionaryBinding
    private val viewModel : DictionaryViewModel by lazy {
        ViewModelProvider(this)[DictionaryViewModel::class.java]
    }
    private lateinit var dictionaryAdapter: DictionaryAdapter
    private var callBacks: CallBacks? = null
    private val gson = Gson()

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

        lifecycleScope.launchWhenStarted {
            dictionaryAdapter = DictionaryAdapter(requireContext(), callBacks, layoutInflater)

            viewModel.words.collect{
                binding.dictionaryRecyclerView.adapter = dictionaryAdapter.Adapter(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = DataBindingUtil.inflate<FragmentDictionaryBinding>(
            inflater,
            R.layout.fragment_dictionary,
            container,
            false
        )

        binding.dictionaryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
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
}