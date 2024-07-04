package uz.apphub.fayzullo.domain.repository

import uz.apphub.fayzullo.data.room.entity.AppsEntity
import uz.apphub.fayzullo.data.room.entity.PermissionEntity
import uz.apphub.fayzullo.domain.model.AppsModel



interface AppsRepository {
    suspend fun saveApp(app: AppsEntity): Long
    suspend fun findByPackageName(packageName: String): AppsEntity?
    suspend fun savePermission(permissions: List<PermissionEntity>)
    suspend fun getAllAppsWithPermissions(): List<AppsModel>
    suspend fun getPermission(appId: Long): List<PermissionEntity>
    suspend fun getAppsCount(): Int
    suspend fun getAllPlayMarketApps(isPlayMarket: Boolean): List<AppsModel>
    suspend fun getPlayMarketAppsCount(isPlayMarket: Boolean): Int
}