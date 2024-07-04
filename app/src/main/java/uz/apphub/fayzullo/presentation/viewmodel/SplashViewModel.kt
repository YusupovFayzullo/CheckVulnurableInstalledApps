package uz.apphub.fayzullo.presentation.viewmodel

import android.content.Context
import android.content.pm.InstallSourceInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.apphub.chopar2.data.local.AppSharedPref
import uz.apphub.fayzullo.domain.model.AppsModel
import uz.apphub.fayzullo.domain.model.PermissionModel
import uz.apphub.fayzullo.domain.model.toAppsEntity
import uz.apphub.fayzullo.domain.model.toPermissionEntity
import uz.apphub.fayzullo.domain.repository.AppsRepository
import uz.apphub.fayzullo.utils.toByteArray
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPref: AppSharedPref,
    private val appsRepository: AppsRepository,
    @ApplicationContext val context: Context,
) : ViewModel() {

    private val TAG = "SplashViewModel"
    private val _isSavedAppList = MutableStateFlow<Boolean?>(null)
    val isSavedAppList: StateFlow<Boolean?> = _isSavedAppList.asStateFlow()
    fun isFirstLaunch(): Boolean = sharedPref.isFirstLaunch()

    private var job: Job? = null

    fun saveFirstLaunch() {
        sharedPref.setFirstLaunch()
    }

    fun saveInstalledApps() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val apps = installedApps()
                Log.d(TAG, "saveInstalledApps: $apps")
                apps.forEach { app ->
                    Log.d(TAG, "saveInstalledApps perm: ${app.permissionModels}")
                    if (appsRepository.findByPackageName(app.packageName) == null) {
                        val appId = appsRepository.saveApp(app.toAppsEntity())
                        appsRepository.savePermission(app.permissionModels.map { permission ->
                            permission.toPermissionEntity(
                                appId = appId
                            )
                        })
                    }
                }
                _isSavedAppList.emit(true)
            } catch (e: Exception) {
                Log.e(TAG, "saveInstalledApps: ${e.message}", e)
                _isSavedAppList.emit(false)
            }
        }
    }

    private suspend fun installedApps(): List<AppsModel> {
        val installedAppsList = mutableListOf<AppsModel>()
        val packageManager = context.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        for (packageInfo in packageInfoList) {
            val packageName = packageInfo.packageName
            val permissionModels = mutableListOf<PermissionModel>()
            val fileSha256 = getFileSha256(packageManager, packageName)
            packageInfo.requestedPermissions?.forEach { permission ->
                val isGranted = (packageManager.checkPermission(
                    permission!!,
                    packageName
                ) == PackageManager.PERMISSION_GRANTED)
                permissionModels.add(PermissionModel(permission, isGranted))
            }
            if (permissionModels.isNotEmpty() && !isSystemApp(packageInfo)) {
                try {
                    val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                    val appName = packageManager.getApplicationLabel(applicationInfo).toString()
                    val appIcon = packageManager.getApplicationIcon(applicationInfo)
                    val isPlayMarket =
                        isAppFromPlayMarket(packageName)
                    installedAppsList.add(
                        AppsModel(
                            0,
                            appName,
                            appIcon.toByteArray(),
                            isPlayMarket,
                            permissionModels,
                            packageName,
                            fileSha256
                        )
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
        return installedAppsList
    }
    private fun getFileSha256(packageManager: PackageManager, packageName: String): String {
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val file = File(applicationInfo.sourceDir)
        return getSha256Hash(file)
    }

    private fun getSha256Hash(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val inputStream = FileInputStream(file)
        val buffer = ByteArray(8192)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            digest.update(buffer, 0, bytesRead)
        }
        inputStream.close()
        val hashBytes = digest.digest()
        return hashBytes.joinToString("") { "%02x".format(it) }
    }



    private suspend fun isSystemApp(
        packageInfo: PackageInfo
    ): Boolean {
        val applicationInfo = packageInfo.applicationInfo
        return (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
    }

    private suspend fun isAppFromPlayMarket(packageName: String): Boolean {
        val packageManager = context.packageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val installSourceInfo: InstallSourceInfo =
                    packageManager.getInstallSourceInfo(packageName)
                installSourceInfo.installingPackageName == "com.android.vending"
            } catch (e: Exception) {
                false
            }
        } else {
            val installerPackageName = packageManager.getInstallerPackageName(packageName)
            installerPackageName == "com.android.vending"
        }
    }
}