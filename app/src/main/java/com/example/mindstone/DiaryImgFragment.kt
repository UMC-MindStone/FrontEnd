package com.example.mindstone

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mindstone.databinding.FragmentDiaryImgBinding
import java.net.URI


class DiaryImgFragment : Fragment() {
    private var _binding: FragmentDiaryImgBinding? = null
    private val binding get() = _binding!!

    private val imageViews = mutableListOf<ImageView>()
    private var imageCnt = 0

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let{
            if(imageCnt < 4){
                addImage(it)
            } else {
                binding.diaryAddImgTv.visibility = View.INVISIBLE
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDiaryImgBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViews.addAll(
            listOf(
                binding.diaryImg1Iv,
                binding.diaryImg2Iv,
                binding.diaryImg3Iv,
                binding.diaryImg4Iv
            )
        )

        binding.diaryAddImgIv.setOnClickListener{
            selectImageLauncher.launch("image/*")
        }
    }

    private fun addImage(uri : Uri){
        if (imageCnt < 4){
            val imageView = imageViews[imageCnt]
            imageView.setImageURI(uri)
            imageViews.add(imageView)
            imageCnt++
        }
    }
    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }


}