package com.objectorientedoleg.movieroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.objectorientedoleg.movieroll.ui.MovieRollApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieRollApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieRollApp()
}