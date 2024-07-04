package uz.apphub.fayzullo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.data.room.entity.toPermissionModel
import uz.apphub.fayzullo.domain.model.PermissionModel
import uz.apphub.fayzullo.domain.repository.AppsRepository
import javax.inject.Inject


@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val localRepository: AppsRepository
) : ViewModel() {

    private val TAG = "PermissionsViewModel"

    private val _permissionList = MutableStateFlow<List<PermissionModel>>(emptyList())
    val permissionList = _permissionList.asStateFlow()

    fun initPermissionList(appId: Long) {
        viewModelScope.launch {
            val permissions = localRepository.getPermission(appId)
            Log.d(TAG, "initPermissionList: $permissions")
            _permissionList.value = permissions.map { it.toPermissionModel() }
        }
    }
}