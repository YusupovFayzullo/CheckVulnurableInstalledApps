package uz.apphub.fayzullo.domain.repository

import uz.apphub.fayzullo.data.room.entity.SignatureEntity


interface SignatureRepository {
    suspend fun saveSignature(signature: SignatureEntity)
    suspend fun getSignature(): List<SignatureEntity>
}