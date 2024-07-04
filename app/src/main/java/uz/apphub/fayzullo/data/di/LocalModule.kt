package uz.apphub.fayzullo.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.apphub.fayzullo.data.network.repository.AppsRepositoryImpl
import uz.apphub.fayzullo.data.network.repository.ScannerRepositoryImpl
import uz.apphub.fayzullo.data.network.repository.SignatureRepositoryImpl
import uz.apphub.fayzullo.domain.repository.AppsRepository
import uz.apphub.fayzullo.domain.repository.ScannerRepository
import uz.apphub.fayzullo.domain.repository.SignatureRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface LocalModule {
    @Binds
    @Singleton
    fun bindAppsRepository(appsRepository: AppsRepositoryImpl): AppsRepository

    @Binds
    @Singleton
    fun bindSignatureRepository(signatureRepository: SignatureRepositoryImpl): SignatureRepository

    @Binds
    @Singleton
    fun bindScannerRepository(scannerRepository: ScannerRepositoryImpl): ScannerRepository
}