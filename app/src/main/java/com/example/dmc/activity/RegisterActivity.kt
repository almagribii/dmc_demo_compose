package com.example.dmc.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.ui.theme.DMCTheme

class RegisterActivity : ComponentActivity() {
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
                    RegisterScreen() // Ini adalah panggilan fungsi @Composable yang benar
                }
            }
        }
    }
}

@Composable
fun RegisterScreen() {
    var NIM by remember { mutableStateOf ("") }
    var isFemale by remember { mutableStateOf(false) }
//    var username by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Register",
            fontSize = 32.sp

        )

        OutlinedTextField(
            value = NIM,
            onValueChange = { NIM = it},
            keyboardOptions = KeyboardOptions (keyboardType = KeyboardType.Text),
            label = { Text("Nama Lengkap") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Lock Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp)
                .padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = NIM,
            onValueChange = { NIM = it},
            keyboardOptions = KeyboardOptions (keyboardType = KeyboardType.Text),
            label = { Text("Tanggal Lahir") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Lock Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 6.dp)

        )

        OutlinedTextField(
            value = NIM,
            onValueChange = { NIM = it},
            keyboardOptions = KeyboardOptions (keyboardType = KeyboardType.Text),
            label = { Text("Alamat Rumah") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Lock Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 6.dp)

        )
        OutlinedTextField(
            value = NIM,
            onValueChange = { NIM = it},
            keyboardOptions = KeyboardOptions (keyboardType = KeyboardType.Text),
            label = { Text("Nomor Telepon") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Lock Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp)

        )

        Text(
            text = "Apakah Anda Mahasiswa Unida",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Agar teks dan switch terpisah
        ) {
            Text(
                text = if (isFemale) "Tidak" else "Iya",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isFemale,
                onCheckedChange = { isFemale = it } // Update state saat switch diubah
            )
        }

        Text(text = "Anda ${if (isFemale) "Seorang" else "Bukan"} Mahasiswa Unida")
        Spacer(modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                println("Tombol di Klik!")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("REGISTER")
        }
    }
}