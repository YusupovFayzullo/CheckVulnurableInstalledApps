package uz.apphub.fayzullo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.apphub.fayzullo.data.room.entity.ScannerEntity


@Dao
interface ScannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanner(scanner: ScannerEntity)
    @Query("SELECT * FROM ScannerEntity")
    suspend fun getAllScanner(): List<ScannerEntity>

    @Query("SELECT * FROM ScannerEntity ORDER BY created DESC LIMIT 1")
    suspend fun getLatestScannerEntity(): ScannerEntity?
}