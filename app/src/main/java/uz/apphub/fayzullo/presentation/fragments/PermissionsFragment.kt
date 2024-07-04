package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentPermissionsBinding
import uz.apphub.fayzullo.presentation.adapter.PermissionAdapter
import uz.apphub.fayzullo.presentation.viewmodel.PermissionsViewModel
import uz.apphub.fayzullo.utils.Constants

@AndroidEntryPoint
class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private var _binding: FragmentPermissionsBinding? = null
    private val binding get() = _binding!!
    private var adapter: PermissionAdapter? = null

    private val viewModel: PermissionsViewModel by viewModels()

    private val TAG = "PermissionsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPermissionsBinding.bind(view)
        collectData()
        initAdapter()

        val appName = requireArguments().getString(Constants.APP_NAME)
        val appId = requireArguments().getLong(Constants.APP_ID)
        viewModel.initPermissionList(appId)
        binding.topBar.text = appName
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.permissionList.collectLatest { list ->
                Log.d(TAG, "collectData: $list")
                adapter?.initList(list)
            }
        }
    }

    private fun initAdapter() {
        adapter = PermissionAdapter()
        binding.rvPermissions.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

