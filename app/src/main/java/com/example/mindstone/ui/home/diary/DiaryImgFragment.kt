package com.example.mindstone.ui.home.diary

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.example.mindstone.MainActivity
import com.example.mindstone.databinding.FragmentDiaryImgBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DiaryImgFragment : Fragment() {
    private var _binding: FragmentDiaryImgBinding? = null
    private val binding get() = _binding!!
    private val diaryViewModel: DiaryViewModel by activityViewModels()

    private val imageViews = mutableListOf<ImageView>()
    private var imageCnt = 0
    private var beforeFragment: String? = null

    private var currentYear = arguments?.getInt("currentYear")?: 2025
    private var currentMonth = arguments?.getInt("currentMonth")?: 1
    private var currentDay= arguments?.getInt("currentDay")?: 1

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (imageCnt < 4) {
                addImage(it)
            } else {
                binding.diaryAddImgTv.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            beforeFragment = bundle.getString("fragment", "Unknown")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryImgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        when (beforeFragment) {
            "today" -> {
                binding.diaryImgAutoTv.visibility = View.VISIBLE
                binding.diaryImgEditTv.visibility = View.GONE
            }
            "calendar" -> {
                binding.diaryImgAutoTv.visibility = View.GONE
                binding.diaryImgEditTv.visibility = View.VISIBLE
            }
        }

        imageViews.addAll(
            listOf(
                binding.diaryImg1Iv,
                binding.diaryImg2Iv,
                binding.diaryImg3Iv,
                binding.diaryImg4Iv
            )
        )

        diaryViewModel.images.observe(viewLifecycleOwner) { images ->
            for (i in imageViews.indices) {
                if (i < images.size) {
                    imageViews[i].setImageURI(images[i])
                    imageViews[i].tag = images[i]
                } else {
                    imageViews[i].setImageDrawable(null)
                    imageViews[i].tag = null
                }
            }
            imageCnt = images.size
        }

        binding.diaryAddImgIv.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        binding.diaryImgCloseIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.diaryImgCompleteIv.setOnClickListener {
            val updatedText = binding.diaryImgEditTv.text.toString()
            val selectedImages = imageViews.mapNotNull { it.tag as? Uri }
            diaryViewModel.updateImages(selectedImages)
            val date = LocalDate.of( currentYear, currentMonth, currentDay)
            val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            // 파라미터 수정 필요
            diaryViewModel.saveDiary(formattedDate, updatedText )
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun addImage(uri: Uri) {
        if (imageCnt < 4) {
            val imageView = imageViews[imageCnt]
            imageView.setImageURI(uri)
            imageView.tag = uri
            imageCnt++
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
