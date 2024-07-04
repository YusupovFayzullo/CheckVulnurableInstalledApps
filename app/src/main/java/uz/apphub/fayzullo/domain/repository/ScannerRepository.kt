package uz.apphub.fayzullo.domain.repository

import uz.apphub.fayzullo.data.room.entity.ScannerEntity


interface ScannerRepository {
    suspend fun saveScanned(scanner: ScannerEntity)
    suspend fun getAllScanner(): List<ScannerEntity>

    suspend fun getLatestScannerEntity(): ScannerEntity?

}