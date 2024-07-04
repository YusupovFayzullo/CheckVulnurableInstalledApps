package uz.apphub.fayzullo.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AppsWithPermissions(
    @Embedded val app: AppsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "appId"
    )
    val permissions: List<PermissionEntity>
)