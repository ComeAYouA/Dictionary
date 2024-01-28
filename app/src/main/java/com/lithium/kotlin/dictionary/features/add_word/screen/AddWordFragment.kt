package com.lithium.kotlin.dictionary.features.add_word.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lithium.kotlin.dictionary.R
import com.lithium.kotlin.dictionary.appComponent
import com.lithium.kotlin.dictionary.databinding.FragmentWordBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject



class AddWordFragment: Fragment() {

    interface CallBacks{
        fun onAddWordButtonClicked()
    }

    @Inject
    lateinit var  viewModel: AddWordViewModel
    @Inject
    lateinit var fragmentHelper: AddWordFragmentBindingAdapter

    private var _binding: FragmentWordBinding? = null
    val binding get() = _binding!!

    var callBacks: CallBacks? = null

    private val pickImageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                result.data?.data?.let { imageUri ->
                    viewModel.iconPath = getImageFromPath(imageUri)
                    Picasso.with(requireContext()).load(viewModel.iconPath).into(binding.newWordIcon)
                }?:{
                    Log.d("myTag", "Error while getting image")
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBacks = context as CallBacks?

        context.appComponent.addWordComponent().create(this).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(fragmentHelper){
            this@AddWordFragment.setupObservers()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_word,
            container,
            false
        )

        fragmentHelper.bindingInit(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentHelper){
            setupTranslationsRv()
            setupCategoriesRv()

            setupAddButton()

            setupWordEditTextListener()
            setupTranslationsEditTextListener()
            setupCategoriesEditTextListener()

            setupWordIconListener()
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

    private fun setupWordIconListener() {
        binding.newWordIcon.setOnClickListener {
            startSelectWordIconIntent(pickImageIntentLauncher)
        }
    }

    private fun startSelectWordIconIntent(intentLauncher: ActivityResultLauncher<Intent>){
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_PICK
        }
        intentLauncher.launch(intent)
    }


    private fun getImageFromPath(selectedImage: Uri): String {
        val c: Cursor = requireActivity()
            .contentResolver
            .query(
                selectedImage,
                null,
                null,
                null,
                null
            )!!
        c.moveToFirst()

        val path: String = c.getString(
            if (c.getColumnIndex(MediaStore.MediaColumns.DATA) >= 0) {
                c.getColumnIndex(MediaStore.MediaColumns.DATA)
            } else {
                0
            }
        )

        c.close()

        return path
    }
}