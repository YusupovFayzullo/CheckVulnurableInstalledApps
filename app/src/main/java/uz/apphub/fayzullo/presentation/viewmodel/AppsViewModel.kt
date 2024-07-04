package uz.apphub.fayzullo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.domain.model.AppsModel
import uz.apphub.fayzullo.domain.repository.AppsRepository
import uz.apphub.fayzullo.utils.AppSelectType
import javax.inject.Inject


@HiltViewModel
class AppsViewModel @Inject constructor(
    private val localRepository: AppsRepository
) : ViewModel() {


    private val _appList = MutableStateFlow<List<AppsModel>?>(null)
    val appList = _appList.asStateFlow()

    private val _topBarText = MutableStateFlow("")
    val topBarText: StateFlow<String> = _topBarText.asStateFlow()


    private val appsList = mutableListOf<AppsModel>()

    fun initPermissionList(isPlayMarket: AppSelectType) {
        viewModelScope.launch {
            appsList.clear()
            when (isPlayMarket) {
                AppSelectType.All -> {
                    appsList.addAll(localRepository.getAllAppsWithPermissions())
                    _topBarText.emit("O'rnatilgan ilovalar")
                }

                AppSelectType.PLAY_MARKET -> {
                    appsList.addAll(localRepository.getAllPlayMarketApps(true))
                    _topBarText.emit("Play Market")
                }

                AppSelectType.NO_PLAY_MARKET -> {
                    appsList.addAll(localRepository.getAllPlayMarketApps(false))
                    _topBarText.emit("Boshqa resurslar")
                }
            }
        }
    }

    suspend fun initSortList(text: String) {
        val myApps = mutableListOf<AppsModel>()
        appsList.forEach { app ->
            if (app.permissionModels.any {
                    it.name.contains(
                        text, ignoreCase = true
                    ) && it.isGranted
                }) {
                Log.d("TAG", "initSort List: ${app.permissionModels}")
                myApps.add(app)
            }
        }
        _appList.emit(myApps.toList())
    }
}