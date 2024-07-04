package uz.apphub.fayzullo.domain.model

import uz.apphub.fayzullo.data.room.entity.AppsEntity

data class AppsModel(
    val id: Long,
    var appName: String,
    var appIcon: ByteArray,
    val isPlayMarket: Boolean,
    var permissionModels: List<PermissionModel>,
    var packageName: String,
    var hashSHA256: String
)

fun AppsModel.toAppsEntity() = AppsEntity(
    appName = appName,
    appIcon = appIcon,
    isPlayMarket = isPlayMarket,
    packageName = packageName,
    hashSHA256 = hashSHA256
)
