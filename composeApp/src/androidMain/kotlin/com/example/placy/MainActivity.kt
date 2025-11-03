package com.example.placy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.sulgik.mapkit.MapKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        MapKit.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKit.initialize(this)

        setContent {
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        MapKit.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKit.getInstance().onStop()
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
