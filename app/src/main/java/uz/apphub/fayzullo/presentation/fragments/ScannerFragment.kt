package uz.apphub.fayzullo.presentation.fragments

import android.annotation.SuppressLint
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
import uz.apphub.fayzullo.databinding.FragmentScannerBinding
import uz.apphub.fayzullo.domain.model.AppsModel
import uz.apphub.fayzullo.presentation.viewmodel.ScannerViewModel
import uz.apphub.fayzullo.utils.toDrawable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ScannerFragment : Fragment(R.layout.fragment_scanner) {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScannerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScannerBinding.bind(view)

        collectData()
        viewModel.scanInstalledApps()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.appIcon.collectLatest { icon ->
                binding.appIcon.setImageDrawable(icon?.toDrawable(requireContext()))
            }
        }
        lifecycleScope.launch {
            viewModel.progress.collectLatest { progress ->
                updateProgress(progress)
            }
        }
        lifecycleScope.launch {
            viewModel.appName.collectLatest { appName ->
                binding.appName.text = appName
            }
        }
        lifecycleScope.launch {
            viewModel.scanResults.collectLatest { list ->
                list?.let { showScanResults(it) }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showScanResults(vulnerabilities: List<AppsModel>) {
        if (vulnerabilities.isEmpty()) {
            binding.helloText.text = vulnerabilities.toString()
            binding.helloText.text = "Qurilma xavfsiz"
        } else {
            binding.helloText.text =
                "Zaifliklar topildi: \n${vulnerabilities.joinToString { it.appName }}"
        }
        binding.helloText.visibility = View.VISIBLE

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.resultText.text = "Tekshiruv tugadi: $currentDate"
        binding.resultText.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgress(progress: Int) {
        binding.circularProgressBar.progress = progress
        binding.progressText.text = "$progress%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
