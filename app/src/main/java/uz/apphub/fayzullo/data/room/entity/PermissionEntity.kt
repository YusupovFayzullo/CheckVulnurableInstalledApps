package uz.apphub.fayzullo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.apphub.fayzullo.domain.model.PermissionModel

@Entity
data class PermissionEntity(
    @PrimaryKey(autoGenerate = true) val permissionId: Long = 0,
    val appId: Long,
    var name: String,
    var isGranted: Boolean
)
fun PermissionEntity.toPermissionModel() = PermissionModel(name, isGranted)