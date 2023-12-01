package com.example.lwp_lab01.ui.slideshow

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lwp_lab01.databinding.FragmentSlideshowBinding
import com.example.lwp_lab01.R
import com.example.lwp_lab01.ui.PDF_List.List_PDF

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val moduleEnglish = root.findViewById<ImageView>(R.id.englishModule)

        moduleEnglish.setOnClickListener{
            val EModule = Intent(context, List_PDF::class.java)
            startActivity(EModule)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}