package uz.apphub.fayzullo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.data.room.entity.toScannerModel
import uz.apphub.fayzullo.domain.model.ScannerModel
import uz.apphub.fayzullo.domain.repository.ScannerRepository
import javax.inject.Inject


@HiltViewModel
class ScannerResultViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
) : ViewModel() {

    private val _signatureList = MutableStateFlow<List<ScannerModel>>(emptyList())
    val signatureList = _signatureList.asStateFlow()

    fun initList() {
        viewModelScope.launch {
            _signatureList.value = scannerRepository.getAllScanner().map { it.toScannerModel() }
        }
    }
}