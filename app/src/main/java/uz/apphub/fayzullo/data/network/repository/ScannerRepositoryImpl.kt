package uz.apphub.fayzullo.data.network.repository

import uz.apphub.fayzullo.data.room.dao.ScannerDao
import uz.apphub.fayzullo.data.room.entity.ScannerEntity
import uz.apphub.fayzullo.domain.repository.ScannerRepository
import javax.inject.Inject


class ScannerRepositoryImpl @Inject constructor(
    private val scannerDao: ScannerDao
): ScannerRepository  {
    override suspend fun saveScanned(scanner: ScannerEntity) {
        scannerDao.insertScanner(scanner)
    }

    override suspend fun getAllScanner(): List<ScannerEntity> {
        return scannerDao.getAllScanner()
    }

    override suspend fun getLatestScannerEntity(): ScannerEntity?{
        return scannerDao.getLatestScannerEntity()
    }

}