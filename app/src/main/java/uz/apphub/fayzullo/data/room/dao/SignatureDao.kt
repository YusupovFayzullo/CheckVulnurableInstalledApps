package uz.apphub.fayzullo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.apphub.fayzullo.data.room.entity.SignatureEntity


@Dao
interface SignatureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignatures(signatures: SignatureEntity)
    @Query("SELECT * FROM SignatureEntity")
    suspend fun getAllSignature(): List<SignatureEntity>
}