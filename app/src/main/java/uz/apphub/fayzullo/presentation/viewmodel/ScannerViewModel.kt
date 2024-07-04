package uz.apphub.fayzullo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import uz.apphub.fayzullo.domain.model.AppsModel
import uz.apphub.fayzullo.domain.model.ScannerModel
import uz.apphub.fayzullo.domain.model.toScannerEntity
import uz.apphub.fayzullo.domain.repository.AppsRepository
import uz.apphub.fayzullo.domain.repository.ScannerRepository
import uz.apphub.fayzullo.utils.Constants
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val scannerRepository: ScannerRepository,
) : ViewModel() {

    private val _appName = MutableStateFlow("")
    val appName = _appName.asStateFlow()

    private val _appIcon = MutableStateFlow<ByteArray?>(null)
    val appIcon = _appIcon.asStateFlow()

    private val _scanResults = MutableStateFlow<List<AppsModel>?>(null)
    val scanResults = _scanResults.asStateFlow()

    private val _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()

    private var todayDate: Long = 0
    fun scanInstalledApps() {
        viewModelScope.launch {
            val installedApps = appsRepository.getAllAppsWithPermissions()
            val totalApps = installedApps.size
            var scannedApps = 0

            (todayDate == 0L).let {
                todayDate = System.currentTimeMillis()
            }

            viewModelScope.launch {
                val vulnerabilities = mutableListOf<AppsModel>()

                for (app in installedApps) {
                    Log.d("VirusTotal", "App: ${app.appName}")

                    _appName.emit(app.appName)
                    _appIcon.emit(app.appIcon)

                    val isDetected = checkAppForVulnerabilities(app)


                    println("App: ${app.packageName}, Vulnerable: $isDetected")

                    if (isDetected) {
                        vulnerabilities.add(app)
                    }

                    scannedApps++
                    _progress.emit((scannedApps * 100) / totalApps)
                }
                scannerRepository.saveScanned(
                    ScannerModel(
                        vulnerabilities.isEmpty(),
                        created = todayDate,
                    ).toScannerEntity()
                )
                _scanResults.emit(vulnerabilities)
            }
        }
    }

    private fun isDetected(fileHash: String, callback: (Boolean, String) -> Unit) {
        val apiKey = Constants.API_KEY
        Log.d("VirusTotal", "File Hash: $fileHash")

        // API so'rov URL manzili
        val url = Constants.VIRUS_TOTAL_URL + fileHash

        val request = Request.Builder()
            .url(url)
            .addHeader("x-apikey", apiKey)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, "Failed to execute request: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("VirusTotal", "resp: $response")
                val body = response.body?.string()
                if (!response.isSuccessful || body.isNullOrBlank()) {
                    callback(false, "Failed to get valid response")
                    return
                }

                try {
                    val jsonObject = JSONObject(body)
                    val attributes = jsonObject.getJSONObject("data").getJSONObject("attributes")

                    val lastAnalysisStats = attributes.getJSONObject("last_analysis_stats")
                    val malicious = lastAnalysisStats.getInt("malicious")
                    val suspicious = lastAnalysisStats.getInt("suspicious")
                    val harmless = lastAnalysisStats.getInt("harmless")
                    Log.d("VirusTotal", "Malicious: $malicious")
                    Log.d("VirusTotal", "Suspicious: $suspicious")
                    Log.d("VirusTotal", "Harmless: $harmless")

                    val isDetected = malicious > 0 || suspicious > 0 || harmless > 0
                    Log.d("VirusTotal", "Is Detected: $isDetected")
                    val applicationName = attributes.getString("meaningful_name")
                    callback(isDetected, "Application Name: $applicationName")

                } catch (e: Exception) {
                    callback(false, "Failed to parse JSON response: ${e.message}")
                }
            }
        })
    }


    private suspend fun checkAppForVulnerabilities(app: AppsModel): Boolean {
        return suspendCoroutine { continuation ->
            isDetected(app.hashSHA256) { isDetect, _ ->
                if (isDetect) {
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }
        }
    }
}