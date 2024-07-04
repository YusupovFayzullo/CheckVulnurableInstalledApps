package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentSignatureBinding
import uz.apphub.fayzullo.presentation.adapter.ScannerAdapter
import uz.apphub.fayzullo.presentation.viewmodel.ScannerResultViewModel

@AndroidEntryPoint
class ScannerResultFragment : Fragment(R.layout.fragment_signature) {

    private var _binding: FragmentSignatureBinding? = null
    private val binding get() = _binding!!
    private var adapter: ScannerAdapter? = null

    private val viewModel: ScannerResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignatureBinding.bind(view)

        collectData()
        initAdapter()
        viewModel.initList()

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        adapter = ScannerAdapter()
        binding.signatureRv.adapter = adapter
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.signatureList.collectLatest { list ->
                adapter?.submitList(list.sortedByDescending { it.created })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

