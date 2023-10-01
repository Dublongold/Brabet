package com.bbbrbetss.comebbrabettt.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.bbbrbetss.comebbrabettt.app.core.viewmodel.ViewModelFactory
import com.bbbrbetss.comebbrabettt.app.firebase.FirebaseRemoteConfigsRepository
import com.bbbrbetss.comebbrabettt.app.navigation.Navigation
import com.bbbrbetss.comebbrabettt.app.screens.webview.DataForWeb
import com.bbbrbetss.comebbrabettt.app.ui.backgroundColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private var webviewObject: WebView? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var firebaseRemoteConfigsRepository: FirebaseRemoteConfigsRepository

    private var uriC: Uri? = null
    private var filePathC: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
                Navigation(
                    viewModelFactory = viewModelFactory,
                    dataForWeb = DataForWeb(
                        webviewLink = firebaseRemoteConfigsRepository.webviewLink,
                        activityLauncherForCamera = activityLauncherForCamera,
                        activityLauncherForFile = activityLauncherForFile,
                        filePathCSetter = {
                            filePathC = it
                        },
                        webviewObjectSetter = {
                            webviewObject = it
                        }
                    )
                )
            }
        }
    }

    private fun saveTempFileCreation(doAfterEnd: (File?) -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val createdTempFile = try {
                File.createTempFile(
                    "temp_img_file",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
            } catch (ex: IOException) {
                Log.e("PhotoFile", "Unable to cre", ex)
                null
            }
            doAfterEnd(createdTempFile)
        }
    }
    private fun<T> T.doItWithThis(action: T.() -> Unit) {
        action()
    }

    private val activityLauncherForCamera = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) {
        val grabImg = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val forgotten = Intent(Intent.ACTION_GET_CONTENT)
        forgotten.doItWithThis {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        saveTempFileCreation {
            uriC = Uri.fromFile(it)
            grabImg.doItWithThis {
                putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }

            val select = Intent(Intent.ACTION_CHOOSER).apply {
                putExtra(Intent.EXTRA_INTENT, forgotten)
            }
            select.doItWithThis {
                putExtra(Intent.EXTRA_INITIAL_INTENTS, grabImg.toArrayOrNull())
            }
            activityLauncherForFile.launch(select)
        }
    }

    private inline fun<reified T> T?.toArrayOrNull(): Array<T>? {
        return if(this != null) {
            arrayOf(this)
        }
        else {
            null
        }
    }

    private val activityLauncherForFile  = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val resultCode = it.resultCode
        val data = it.data
        filePathC?.doItWithThis {
            if (resultCode == -1) {
                data?.takeDataString()?.let {ds ->
                    val u = Uri.parse(ds)
                    onReceiveValueToo(u.toArrayOrNull())
                } ?: onReceiveValueToo(uriC.toArrayOrNull())
            } else {
                onReceiveValueToo(null)
            }
        }
    }
    private fun Intent.takeDataString() = dataString
    private var calls = 0
    private fun ValueCallback<Array<Uri>>.onReceiveValueToo(value: Array<Uri>?) {
        Log.i("Test", "Call ${++calls}. Value: $value.")
        onReceiveValue(value)
        filePathC = null
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webviewObject?.restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webviewObject?.saveState(outState)
    }
}
