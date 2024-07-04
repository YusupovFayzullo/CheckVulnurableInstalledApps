package uz.apphub.fayzullo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.apphub.fayzullo.domain.model.ScannerModel

@Entity
data class ScannerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val isSecured: Boolean,
    val created: Long,
)

fun ScannerEntity.toScannerModel() = ScannerModel(
    isSecured = isSecured,
    created = created,
)

