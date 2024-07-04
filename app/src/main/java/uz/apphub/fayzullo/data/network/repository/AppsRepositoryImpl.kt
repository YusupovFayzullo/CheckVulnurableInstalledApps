package uz.apphub.fayzullo.data.network.repository

import uz.apphub.fayzullo.data.room.dao.AppsDao
import uz.apphub.fayzullo.data.room.entity.AppsEntity
import uz.apphub.fayzullo.data.room.entity.AppsWithPermissions
import uz.apphub.fayzullo.data.room.entity.PermissionEntity
import uz.apphub.fayzullo.domain.model.AppsModel
import uz.apphub.fayzullo.domain.model.PermissionModel
import uz.apphub.fayzullo.domain.repository.AppsRepository
import javax.inject.Inject


class AppsRepositoryImpl @Inject constructor(
    private val appsDao: AppsDao,
): AppsRepository {
    override suspend fun saveApp(app: AppsEntity): Long {
       return appsDao.insertAllApp(app)
    }
    override suspend fun findByPackageName(packageName: String): AppsEntity?{
        return appsDao.findByPackageName(packageName)
    }
    override suspend fun savePermission(permissions: List<PermissionEntity>) {
        appsDao.insertAllPermission(permissions)
    }

    override suspend fun getAllAppsWithPermissions(): List<AppsModel> {
        val data = appsDao.getAllAppsWithPermissions()
       return appsEntityToAppsModels(data)
    }

    override suspend fun getPermission(appId: Long): List<PermissionEntity> {
        return appsDao.getPermissionsByAppId(appId)
    }

    override suspend fun getAppsCount(): Int {
        return appsDao.getAppsCount()
    }

    override suspend fun getAllPlayMarketApps(isPlayMarket: Boolean): List<AppsModel> {
        val data =  appsDao.getAllPlayMarketApps(isPlayMarket)
        return appsEntityToAppsModels(data)
    }

    override suspend fun getPlayMarketAppsCount(isPlayMarket: Boolean): Int {
        return appsDao.getPlayMarketAppsCount(isPlayMarket)
    }

    private fun appsEntityToAppsModels(data: List<AppsWithPermissions>): List<AppsModel> {
        return data.map { appsWithPermissions ->
            AppsModel(
                id = appsWithPermissions.app.id,
                appName = appsWithPermissions.app.appName,
                appIcon = appsWithPermissions.app.appIcon,
                isPlayMarket = appsWithPermissions.app.isPlayMarket,
                permissionModels = appsWithPermissions.permissions.map { permission ->
                    PermissionModel(
                        name = permission.name,
                        isGranted = permission.isGranted
                    )
                },
                packageName = appsWithPermissions.app.packageName,
                hashSHA256 = appsWithPermissions.app.hashSHA256
            )
        }
    }
}