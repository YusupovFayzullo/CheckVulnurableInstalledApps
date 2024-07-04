package uz.apphub.fayzullo.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.data.room.entity.toScannerModel
import uz.apphub.fayzullo.domain.model.ScannerModel
import uz.apphub.fayzullo.domain.repository.AppsRepository
import uz.apphub.fayzullo.domain.repository.ScannerRepository
import uz.apphub.fayzullo.utils.NetworkHelper
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val scannerRepository: ScannerRepository
) : ViewModel() {

    private val _appsCount = MutableStateFlow<String>("")
    val appsCount = _appsCount.asStateFlow()

    private val _playAppsCount = MutableStateFlow<String>("")
    val playAppsCount = _playAppsCount.asStateFlow()

    private val _noPlayAppsCount = MutableStateFlow<String>("")
    val noPlayAppsCount = _noPlayAppsCount.asStateFlow()

    private val _scanner = MutableStateFlow<ScannerModel?>(null)
    val scanner = _scanner.asStateFlow()

    private val _isInternet = MutableStateFlow<Boolean?>(null)
    val isInternet = _isInternet.asStateFlow()

    fun getCountText() {
        viewModelScope.launch {
            val appsCount = appsRepository.getAppsCount()
            val playAppCount = appsRepository.getPlayMarketAppsCount(true)
            _playAppsCount.emit(playAppCount.toString())
            _appsCount.emit(appsCount.toString())
            _noPlayAppsCount.emit((appsCount - playAppCount).toString())

            val scanner = scannerRepository.getLatestScannerEntity()
            if (scanner == null){
                _scanner.emit(ScannerModel(
                    isSecured = true,
                    created = 0,
                ))
            } else {
                _scanner.emit(scanner.toScannerModel())
            }
        }
    }
    fun checkInternet(context: Context) {
        viewModelScope.launch {
            _isInternet.emit(NetworkHelper(context).isConnected())
        }
    }
}