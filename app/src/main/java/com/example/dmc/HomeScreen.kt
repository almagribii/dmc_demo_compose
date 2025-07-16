package com.example.dmc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.ui.theme.DMCTheme // Pastikan path tema ini benar

@Composable
fun HomeScreen(username: String, role: String, pasienType: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selamat Datang di Home!",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Username: $username",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Peran: $role",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (pasienType != null) {
            Text(
                text = "Tipe Pasien: $pasienType",
                fontSize = 20.sp
            )
        }
        // TODO: Tambahkan konten Home Screen lainnya di sini
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DMCTheme {
        HomeScreen(username = "contohUser", role = "Dokter", pasienType = null)
    }
}