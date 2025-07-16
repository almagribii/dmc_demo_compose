
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.activity.RegisterActivity
import com.example.dmc.ui.theme.DMCTheme

@Composable
fun LoginActivty() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login", fontSize = 18.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth() // Memenuhi seluruh lebar yang tersedia
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround, // Memberi jarak merata di antara elemen
            verticalAlignment = Alignment.CenterVertically // Elemen sejajar di tengah secara vertikal
        ) {
            Icon(Icons.Default.Home, contentDescription = "Home")
            Text(text = "Beranda",
                modifier = Modifier.clickable {
                val intent = Intent(context, RegisterActivity::class.java)
                context.startActivity(intent)
            })
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
        Box(
            modifier = Modifier
                .size(200.dp) // Mengatur ukuran Box
                .background(Color.Gray),
            contentAlignment = Alignment.Center // Memusatkan semua konten di dalam Box
        ) {
            // Ini akan jadi lapisan paling bawah
            Text(
                "Background",
                color = Color.LightGray,
                fontSize = 24.sp,
                    )
            // Ini akan jadi lapisan di atas "Background"
            Text("Overlay Text", color = Color.White, fontSize = 18.sp)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Jarak antar item
        ) {
            items(1) { index -> // Mengulang 100 kali
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(
                        text = "Item ke-$index",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Text(
            text = ""

        )

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DMCTheme {
        LoginActivty()
    }
}