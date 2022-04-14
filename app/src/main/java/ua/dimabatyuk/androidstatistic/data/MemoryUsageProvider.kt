package ua.dimabatyuk.androidstatistic.data

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class MemoryUsagePercentageProvider(context: Context) : Provider<Float> {
    private val activityManager: ActivityManager by lazy { context.getSystemService(ActivityManager::class.java) }

    private val df = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.CEILING
    }


    override suspend fun get(): Float {
        return activityManager.runningAppProcesses
            .firstOrNull()?.pid
            ?.let { pid ->
                val memInfo =
                    activityManager.getProcessMemoryInfo(intArrayOf(pid))
                        .first()

                val javaHeapMb = getMegabytes(memInfo, "summary.java-heap")
                val nativeHeapMb = getMegabytes(memInfo, "summary.native-heap")
                val usedTotalMb = getMegabytes(memInfo, "summary.total-pss")
                val heapSizeMb = activityManager.largeMemoryClass

                df.format(usedTotalMb * 100.0 / heapSizeMb).toFloat()
            } ?: 0F
    }


    private fun getMegabytes(info: Debug.MemoryInfo, key: String): Int {
        return info.memoryStats[key]!!.toInt() / 1024
    }

}