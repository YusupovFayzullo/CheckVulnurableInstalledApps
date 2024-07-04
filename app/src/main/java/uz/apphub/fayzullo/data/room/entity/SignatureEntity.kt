package uz.apphub.fayzullo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.apphub.fayzullo.domain.model.SignatureModel

@Entity
data class SignatureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appNames: String,
    val packageName: String,
    val appHash: String,
    val created: Long,
)

fun SignatureEntity.toSignatureModel() = SignatureModel(
    id = id,
    appNames = appNames,
    packageName = packageName,
    appHash = appHash,
    created = created,
)

