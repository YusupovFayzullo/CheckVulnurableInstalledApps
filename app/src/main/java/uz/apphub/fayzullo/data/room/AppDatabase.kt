package uz.apphub.fayzullo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.apphub.fayzullo.data.room.dao.AppsDao
import uz.apphub.fayzullo.data.room.dao.ScannerDao
import uz.apphub.fayzullo.data.room.dao.SignatureDao
import uz.apphub.fayzullo.data.room.entity.AppsEntity
import uz.apphub.fayzullo.data.room.entity.PermissionEntity
import uz.apphub.fayzullo.data.room.entity.ScannerEntity
import uz.apphub.fayzullo.data.room.entity.SignatureEntity


@Database(
    entities = [SignatureEntity::class, AppsEntity::class, PermissionEntity::class, ScannerEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun signatureDao(): SignatureDao
    abstract fun appsDao(): AppsDao
    abstract fun scannerDao(): ScannerDao
}