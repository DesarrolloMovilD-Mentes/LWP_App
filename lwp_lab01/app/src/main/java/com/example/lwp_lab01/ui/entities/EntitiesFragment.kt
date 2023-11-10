package com.example.lwp_lab01.ui.entities

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lwp_lab01.R
import com.example.lwp_lab01.databinding.FragmentEntitiesBinding

class EntitiesFragment : Fragment() {

    companion object {
        fun newInstance() = EntitiesFragment()
    }

    private lateinit var viewModel: EntitiesViewModel

    private var _binding: FragmentEntitiesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntitiesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}