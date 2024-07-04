package uz.apphub.fayzullo.data.network.repository

import uz.apphub.fayzullo.data.room.dao.SignatureDao
import uz.apphub.fayzullo.data.room.entity.SignatureEntity
import uz.apphub.fayzullo.domain.repository.SignatureRepository
import javax.inject.Inject


class SignatureRepositoryImpl @Inject constructor(
    private val signatureDao: SignatureDao
): SignatureRepository  {
    override suspend fun saveSignature(signature: SignatureEntity) {
        signatureDao.insertSignatures(signature)
    }

    override suspend fun getSignature(): List<SignatureEntity> {
        return signatureDao.getAllSignature()
    }
}