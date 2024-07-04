package uz.apphub.fayzullo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var appName: String,
    var appIcon: ByteArray,
    val isPlayMarket: Boolean,
    var packageName: String,
    var hashSHA256: String
)

