package uz.apphub.fayzullo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import uz.apphub.fayzullo.data.room.entity.AppsEntity
import uz.apphub.fayzullo.data.room.entity.AppsWithPermissions
import uz.apphub.fayzullo.data.room.entity.PermissionEntity


@Dao
interface AppsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllApp(app: AppsEntity): Long

    @Query("SELECT * FROM AppsEntity WHERE packageName = :packageName LIMIT 1")
    suspend fun findByPackageName(packageName: String): AppsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPermission(permission: List<PermissionEntity>)

    @Transaction
    @Query("SELECT * FROM AppsEntity")
    suspend fun getAllAppsWithPermissions(): List<AppsWithPermissions>

    @Query("SELECT * FROM PermissionEntity WHERE appId = :appId")
    suspend fun getPermissionsByAppId(appId: Long): List<PermissionEntity>

    @Query("SELECT COUNT(*) FROM AppsEntity")
    suspend fun getAppsCount(): Int

    @Query("SELECT * FROM AppsEntity WHERE isPlayMarket = :isPlay")
    suspend fun getAllPlayMarketApps(isPlay: Boolean): List<AppsWithPermissions>

    @Query("SELECT COUNT(*) FROM AppsEntity WHERE isPlayMarket = :isPlay")
    suspend fun getPlayMarketAppsCount(isPlay: Boolean): Int
}