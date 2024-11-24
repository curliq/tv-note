package com.free.tvtracker.activities.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.free.tvtracker.activities.splash.SplashActivity
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.expect.data.DatabaseNameAndroid
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.settings.SettingsScreen
import com.free.tvtracker.ui.settings.SettingsScreenNavAction
import com.free.tvtracker.ui.settings.SettingsViewModel
import org.koin.androidx.compose.get
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val viewModel: SettingsViewModel = get()
//            if (viewModel.logout.collectAsState().value) {
//                this.deleteDatabase(DatabaseNameAndroid)
//                val intent = Intent(context, SplashActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context.startActivity(intent)
//                (context as Activity).finish()
//                Runtime.getRuntime().exit(0)
//            }
            viewModel.shareCsvFile.collectAsState().value?.let { csvContent ->
                try {
                    // Get the Downloads directory
                    val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    if (!downloadsFolder.exists()) {
                        downloadsFolder.mkdirs()
                    }

                    // Create the CSV file
                    val file = File(downloadsFolder, "tvshows-movies-data-export.csv")
                    val fileOutputStream = FileOutputStream(file)
                    fileOutputStream.write(csvContent.toByteArray())
                    fileOutputStream.close()

                    // Open share dialog to give the user the option to save it or upload somewhere
                    // Create the Uri for the file
                    val fileUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(
                            this,
                            "$packageName.fileprovider",
                            file
                        )
                    } else {
                        Uri.fromFile(file)
                    }

                    // Create the share intent
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, fileUri)
                        type = "text/plain"
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    // Show the share dialog
                    startActivity(Intent.createChooser(shareIntent, "File saved to Downloads."))
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to save CSV file", Toast.LENGTH_LONG).show()
                }
                viewModel.shareCsvFile.value = null
            }

            TvTrackerTheme {
                val navAction = { action: SettingsScreenNavAction ->
                    when (action) {
                        SettingsScreenNavAction.GoLogin -> {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }

                        SettingsScreenNavAction.GoSignup -> {
                            startActivity(Intent(this, SignupActivity::class.java))
                        }

                        is SettingsScreenNavAction.GoBrowser -> {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(action.url))
                            startActivity(browserIntent)
                        }

                        is SettingsScreenNavAction.EmailSupport -> {
                            val emailIntent = Intent(Intent.ACTION_SEND)
                            emailIntent.setType("plain/text")
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(action.email))
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support from Android app")
                            context.startActivity(Intent.createChooser(emailIntent, "Send mail to ${action.email}"))
                        }
                    }
                }
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        MediumTopAppBar(
                            title = {
                                Text(
                                    text = "Settings",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },
                            scrollBehavior = scrollBehavior,
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            }
                        )
                    }
                ) { padding ->
                    SettingsScreen(viewModel = viewModel, navAction, padding)
                }
            }
        }
    }
}
