package com.example.dmc.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QuestionAnswer // <-- IKON UNTUK TANYA DOKTER
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.R
import com.example.dmc.ui.theme.DMCTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = intent.getStringExtra("USER_NAME") ?: "Pasien"
        val userNim = intent.getStringExtra("USER_NIM") ?: ""
        val pasienId = intent.getLongExtra("PASIEN_ID", -1L)

        println("DEBUG_ID: HomeActivity menerima pasienId: $pasienId")

        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val applicationContext = context.applicationContext

                    HomeScreen(
                        userName = userName,
                        onLogoutClick = {
                            Toast.makeText(applicationContext, "Anda telah Logout", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        },
                        onViewMedicalRecordsClick = {
                            Toast.makeText(applicationContext, "Membuka Jejak Rekam Medis...", Toast.LENGTH_SHORT).show()
                            println("DEBUG_ID: HomeActivity mengirim pasienId ke MedicalRecordsListActivity: $pasienId")
                            val intent = Intent(context, MedicalRecordsListActivity::class.java).apply {
                                putExtra("PASIEN_ID", pasienId)
                                putExtra("PATIENT_NAME", userName)
                            }
                            context.startActivity(intent)
                        },
                        onAddMedicalRecordClick = {
                            Toast.makeText(applicationContext, "Membuka Form Tambah Rekam Medis...", Toast.LENGTH_SHORT).show()
                            println("DEBUG_ID: HomeActivity mengirim pasienId ke AddMedicalRecordActivity: $pasienId")
                            val intent = Intent(context, AddMedicalRecordActivity::class.java).apply {
                                putExtra("PASIEN_ID", pasienId)
                                putExtra("PATIENT_NAME", userName)
                            }
                            context.startActivity(intent)
                        },
                        onDispenseMedicineClick = {
                            Toast.makeText(applicationContext, "Membuka Form Pemberian Obat...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, DispenseMedicineActivity::class.java)
                            context.startActivity(intent)
                        },
                        onAppointmentClick = {
                            Toast.makeText(applicationContext, "Membuka Buat Janji Temu...", Toast.LENGTH_SHORT).show()
                            // TODO: Implementasi Intent untuk Buat Janji Temu Activity (misal AppointmentActivity)
                        },
                        onMedicineInfoClick = {
                            Toast.makeText(applicationContext, "Membuka Informasi Obat...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, MedicineInfoActivity::class.java)
                            context.startActivity(intent)
                        },
                        onTanyaDokterClick = {
                            Toast.makeText(applicationContext, "Membuka Tanya Dokter...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, TanyaDokterActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "Pasien",
    onLogoutClick: () -> Unit = {},
    onViewMedicalRecordsClick: () -> Unit = {},
    onAddMedicalRecordClick: () -> Unit = {},
    onDispenseMedicineClick: () -> Unit = {},
    onAppointmentClick: () -> Unit = {},
    onMedicineInfoClick: () -> Unit = {},
    onTanyaDokterClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Selamat Datang, ${userName.split(" ").firstOrNull() ?: "Pasien"}!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Informasi Aplikasi", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Filled.Info, contentDescription = "Informasi Aplikasi")
                    }
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // --- Bagian Card / Banner Atas ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.HealthAndSafety,
                            contentDescription = "Health and Safety Icon",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(150.dp)
                                ,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Ayo Jaga Kesehatan Anda!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Bagian 4 Menu Tengah (Grid 2x2) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MenuItemCard(
                        icon = Icons.Filled.MedicalServices,
                        text = "Jejak Rekam Medis",
                        onClick = onViewMedicalRecordsClick,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    MenuItemCard(
                        icon = Icons.Filled.LocalHospital,
                        text = "Tambah Rekam Medis",
                        onClick = onAddMedicalRecordClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MenuItemCard(
                        icon = Icons.Filled.Medication,
                        text = "Pemberian Obat",
                        onClick = onDispenseMedicineClick,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    MenuItemCard(
                        icon = Icons.Filled.Medication,
                        text = "Informasi Obat",
                        onClick = onMedicineInfoClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Kurangi spacer di sini

            // --- Tombol Baru: Tanya Dokter (Lebih Kecil) ---
            // Menggunakan ElevatedButton untuk tampilan yang berbeda
            ElevatedButton(
                onClick = onTanyaDokterClick,
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Lebar 60%
                    .height(50.dp) // Tinggi 50dp
                    .align(Alignment.CenterHorizontally), // Pusatkan secara horizontal
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.QuestionAnswer,
                        contentDescription = "Tanya Dokter Icon",
                        modifier = Modifier.size(24.dp) // Ukuran ikon lebih kecil
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tanya Dokter",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp)) // Spacer setelah tombol Tanya Dokter

            // --- Bagian Card / Banner Bawah ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Kesehatan itu Investasi,\nMari Jaga Sejak Dini!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, end = 8.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = "Book Icon",
                            modifier = Modifier.size(90.dp).padding(end = 16.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

// MenuItemCard Composable tetap sama
@Composable
fun RowScope.MenuItemCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewHomeScreenWithAppBar() {
    DMCTheme {
        HomeScreen(userName = "Budi Santoso")
    }
}