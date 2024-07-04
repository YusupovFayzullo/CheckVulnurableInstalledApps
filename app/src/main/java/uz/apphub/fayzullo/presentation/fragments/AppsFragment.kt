package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentAppsBinding
import uz.apphub.fayzullo.domain.model.AppsModel
import uz.apphub.fayzullo.presentation.adapter.AppsAdapter
import uz.apphub.fayzullo.presentation.viewmodel.AppsViewModel
import uz.apphub.fayzullo.utils.AppSelectType
import uz.apphub.fayzullo.utils.Constants

@AndroidEntryPoint
class AppsFragment : Fragment(R.layout.fragment_apps), AppsAdapter.AppClickListener {

    private var _binding: FragmentAppsBinding? = null
    private val binding get() = _binding!!

    private var adapter: AppsAdapter? = null

    private val viewModel: AppsViewModel by viewModels()

    private val TAG = "AppsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAppsBinding.bind(view)

        initAdapter()
        tabClick()
        collectData()

        val isPlayMarket =
            requireArguments().getString(Constants.IS_PLAY_MARKET) ?: AppSelectType.All.name
        viewModel.initPermissionList(AppSelectType.valueOf(isPlayMarket))

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.appList.collectLatest { list ->
                Log.d(TAG, "collectData: $list")
                if (list == null){
                    binding.tabLayout.visibility = View.GONE
                } else {
                    adapter?.initList(list)
                    binding.tabLayout.visibility = View.VISIBLE
                }
            }
        }
        lifecycleScope.launch {
            viewModel.topBarText.collectLatest {topBarText->
                if (topBarText.isEmpty()){
                    binding.topBar.text = "Yuklanmoqda..."
                } else {
                    binding.topBar.text = topBarText
                    initSortList("SMS")
                }
            }
        }
    }

    private fun tabClick() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabItemClicked(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun tabItemClicked(position: Int) {
        when (position) {
            0 -> {
                initSortList("SMS")
            }

            1 -> {
                initSortList("LOCATION")
            }

            2 -> {
                initSortList("CAMERA")
            }

            3 -> {
                initSortList("PHONE")
            }

            4 -> {
                initSortList("STORAGE")
            }

            5 -> {
                initSortList("INTERNET")
            }

            6 -> {
                initSortList("MICROPHONE")
            }

            7 -> {
                initSortList("CONTACTS")
            }
        }
    }

    private fun initSortList(text: String) {
        lifecycleScope.launch {
            viewModel.initSortList(text)
        }
    }


    private fun initAdapter() {
        adapter = AppsAdapter()
        adapter?.initListener(this)
        binding.rvApps.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(app: AppsModel) {
        findNavController().navigate(
            R.id.action_appsFragment_to_permissionsFragment,
            bundleOf(
                Constants.APP_NAME to app.appName,
                Constants.APP_ID to app.id
            )
        )
    }
}

