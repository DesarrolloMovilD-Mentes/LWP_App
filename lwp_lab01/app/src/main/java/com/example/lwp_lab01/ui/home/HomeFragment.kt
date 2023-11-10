package com.example.lwp_lab01.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lwp_lab01.databinding.FragmentHomeBinding
import com.example.lwp_lab01.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var text_home = root.findViewById<TextView>(R.id.text_home)
        text_home.setText(R.string.message_home)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}