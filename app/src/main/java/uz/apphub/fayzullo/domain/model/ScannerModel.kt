package uz.apphub.fayzullo.domain.model

import uz.apphub.fayzullo.data.room.entity.ScannerEntity

data class ScannerModel(
    val isSecured: Boolean,
    val created: Long,
)
fun ScannerModel.toScannerEntity() = ScannerEntity(
    isSecured = isSecured,
    created = created,
)