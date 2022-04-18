package ua.dimabatyuk.androidstatistic.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.NullPointerException

class AndroidIdProvider(
    private val context: Context
    ): Provider<Long> {

    override suspend fun get(): Long = withContext(Dispatchers.IO) {
        val uri = Uri.parse("content://com.google.android.gsf.gservices")
        val cursor = context.contentResolver.query(uri, null, null, arrayOf("android_id"), null)
        if (null == cursor) {
            Toast.makeText(context, "Android ID not found", Toast.LENGTH_SHORT).show()
            -1
        } else {
            cursor.use {
                if (!cursor.moveToFirst() || cursor.columnCount < 2) {
                    Toast.makeText(context, "Android ID not found", Toast.LENGTH_SHORT).show()
                    -1
                } else {
                    try {
                        cursor.getString(1).toLong()
                    } catch (e: NullPointerException) {
                        -1
                    }
                }
            }
        }
    }
}