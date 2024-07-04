package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentSplashBinding
import uz.apphub.fayzullo.presentation.adapter.ViewPagerAdapter
import uz.apphub.fayzullo.presentation.viewmodel.SplashViewModel
import uz.apphub.fayzullo.utils.Constants

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash),
    ViewPagerAdapter.OnSplashButtonClickListener {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewPagerList = Constants.SPLASH_LIST

    private val viewModel: SplashViewModel by viewModels()

    private val TAG = "SplashFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)

        collectData()
        viewModel.saveInstalledApps()
    }


    private fun collectData() {
        lifecycleScope.launch {
            viewModel.isSavedAppList.collect {
                if (it == true) {
                    Log.d(TAG, "collectData: true")
                    if (viewModel.isFirstLaunch()) {
                        startViewPager()
                    } else {
                        openNewFragment()
                    }
                } else {
                    Log.d(TAG, "collectData: false")
                    Toast.makeText(requireContext(), "Qayta urinib ko'rish", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startViewPager() {
        if (viewModel.isFirstLaunch()) {
            binding.viewPager2.visibility = View.VISIBLE
            val adapter = ViewPagerAdapter(viewPagerList)
            adapter.initListener(this)
            binding.viewPager2.adapter = adapter
        } else {
            binding.viewPager2.visibility = View.GONE
            openNewFragment()
        }
    }

    override fun onButtonClick(position: Int) {
        viewModel.saveFirstLaunch()
        openNewFragment()
    }

    private fun openNewFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
