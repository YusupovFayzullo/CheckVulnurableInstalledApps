package uz.apphub.fayzullo.utils

import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.domain.model.SplashItemModel


class Constants {
    companion object {
        const val API_KEY = "e83df9603e59926d38ec169c603fc3079fa30993cf57a4a002388aa15b84b9ba"
        const val VIRUS_TOTAL_URL = "https://www.virustotal.com/api/v3/files/"
        const val APP_NAME = "appName"
        const val APP_ID = "appId"
        const val APP_PACKAGE_NAME = "appPackageName"
        const val IS_PLAY_MARKET = "isPlayMarket"
        val SPLASH_LIST = listOf(
            SplashItemModel("Biz bilan qurilmangizni xavfsizlikda saqlang.", R.drawable.ic_yulduz),
            SplashItemModel(
                "Qurilmangizni (o‘zingizni) kiber firibgarlardan asrang ",
                R.drawable.ic_yulduz
            ),
            SplashItemModel(
                "O’z shaxsiy hayotingizni kiberbuzg’unchilarga berib qo’ymang!",
                R.drawable.ic_yulduz
            ),
            SplashItemModel("O’z qurilmangni – birga asraymiz !", R.drawable.ic_yulduz)
        )
    }
}