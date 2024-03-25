package com.free.tvtracker

import android.content.Intent
import besttvtracker.WatchingScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchingScreen {
                startActivity(Intent(this, ShowDetailsActivity::class.java))
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MaterialTheme {
        Text("hi")
    }
}
