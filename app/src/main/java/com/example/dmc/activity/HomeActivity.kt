package com.example.dmc.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.dmc.ui.theme.DMCTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Di sinilah Compose UI dimulai untuk Activity ini.
        // setContent adalah fungsi non-Composable yang menerima Composable lambda.
        setContent {
            // Di sini Anda memanggil fungsi Composable utama untuk layar ini
            DMCTheme  { // Memanggil tema Composable Anda
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen() // Ini adalah panggilan fungsi @Composable yang benar
                }
            }
        }
    }
}

@Composable
fun HomeScreen(){

}