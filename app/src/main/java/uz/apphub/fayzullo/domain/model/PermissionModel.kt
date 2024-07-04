package uz.apphub.fayzullo.domain.model

import uz.apphub.fayzullo.data.room.entity.PermissionEntity


data class PermissionModel(
    var name: String,
    var isGranted: Boolean
)

fun PermissionModel.toPermissionEntity(appId: Long) =
    PermissionEntity(name = name, appId = appId, isGranted = isGranted)
