package uz.apphub.fayzullo.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentHomeBinding
import uz.apphub.fayzullo.presentation.viewmodel.HomeViewModel
import uz.apphub.fayzullo.utils.AppSelectType
import uz.apphub.fayzullo.utils.Constants
import uz.apphub.fayzullo.utils.toFormattedDateString

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var isInternet: Boolean? = null

    private val viewModel: HomeViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.openPermission.setOnClickListener {
            openPermissionsManagementScreen()
        }
        binding.apps.setOnClickListener {
            openNewFragment(null)
        }
        binding.noPlay.setOnClickListener {
            openNewFragment(false)
        }
        binding.playApps.setOnClickListener {
            openNewFragment(true)
        }
        binding.scanner.setOnClickListener {
            if (isInternet == true) {
                findNavController().navigate(R.id.action_homeFragment_to_scannerFragment)
            } else {
                Toast.makeText(requireContext(), "Internet bilan aloqa yo'q", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.scannerData.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_signatureFragment)
        }
        collectData()
        viewModel.checkInternet(requireContext())
        viewModel.getCountText()
    }


    @SuppressLint("SetTextI18n")
    private fun collectData() {
        lifecycleScope.launch {
            viewModel.appsCount.collectLatest { text ->
                binding.appsCount.text = text.ifEmpty { "..." }
            }
        }
        lifecycleScope.launch {
            viewModel.playAppsCount.collectLatest { text ->
                binding.playAppsCount.text = text.ifEmpty { "..." }
            }
        }
        lifecycleScope.launch {
            viewModel.noPlayAppsCount.collectLatest { text ->
                binding.noPlayAppsCount.text = text.ifEmpty { "..." }
            }
        }
        lifecycleScope.launch {
            viewModel.scanner.collectLatest { scanner ->
                if (scanner == null) {
                    binding.scannerDate.text = "..."
                    binding.signature.text = "..."
                    binding.isSecured.text = "..."
                } else if (scanner.created == 0L) {
                    val today = System.currentTimeMillis()
                    binding.scannerDate.text = today.toFormattedDateString()
                    binding.signature.text = today.toFormattedDateString()
                    binding.isSecured.text = "Tekshirilmagan"
                } else {
                    binding.scannerDate.text = scanner.created.toFormattedDateString()
                    binding.signature.text = scanner.created.toFormattedDateString()
                    binding.isSecured.text = if (scanner.isSecured) {
                        "Xavfsiz"
                    } else {
                        "Xavf aniqlangan"
                    }

                }
            }
        }
        lifecycleScope.launch {
            viewModel.isInternet.collectLatest { internet ->
                isInternet = internet
                if (internet == null) {
                    binding.isInternet.text = "..."
                } else if (internet) {
                    binding.isInternet.text = "Mavjud"
                } else {
                    binding.isInternet.text = "Mavjud emas"
                }
            }
        }
    }

    private fun openNewFragment(isPlayMarket: Boolean?) {
        val bundle = Bundle()
        if (isPlayMarket == null) {
            bundle.putString(Constants.IS_PLAY_MARKET, AppSelectType.All.name)
        } else if (isPlayMarket) {
            bundle.putString(Constants.IS_PLAY_MARKET, AppSelectType.PLAY_MARKET.name)
        } else {
            bundle.putString(Constants.IS_PLAY_MARKET, AppSelectType.NO_PLAY_MARKET.name)
        }
        findNavController().navigate(
            R.id.action_homeFragment_to_appsFragment, bundle
        )
    }

    private fun openPermissionsManagementScreen() {
        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
