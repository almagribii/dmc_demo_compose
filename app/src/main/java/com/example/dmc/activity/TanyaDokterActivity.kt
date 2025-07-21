package com.example.dmc.activity // <-- PASTIKAN PACKAGE INI SESUAI DENGAN PROYEK DMC ANDA

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.ui.theme.DMCTheme // <-- IMPORT DMCTheme ANDA
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// SANGAT PENTING: Jangan menyimpan API Key langsung seperti ini di aplikasi yang akan di-release!
// Gunakan BuildConfig, local.properties, atau ambil dari backend.
private const val API_KEY = "AIzaSyDW1VZe5D6vf9hyzhXm5B8PRj4pEk0YwsY" // Ganti dengan API Key Anda

class TanyaDokterActivity : ComponentActivity() {

    private lateinit var generativeModel: GenerativeModel
    private lateinit var chat: Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = API_KEY,
            systemInstruction = content {
                text("Berperanlah sebagai seorang dokter yang ramah, informatif, dan profesional. Berikan saran medis umum dan informasi kesehatan berdasarkan pengetahuan kedokteran terkini. Jangan memberikan diagnosis atau resep spesifik, dan selalu sarankan untuk berkonsultasi langsung dengan dokter jika ada keluhan serius. Setiap respons harus ringkas dan mudah dipahami. jangan bilang kamu adalah program komputer")
            }
        )
        chat = generativeModel.startChat()

        setContent {
            // --- PERUBAHAN DI SINI: GUNAKAN DMCTheme ---
            DMCTheme { // <-- Panggil DMCTheme Anda di sini
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // MaterialTheme.colorScheme.background dari DMCTheme
                ) {
                    TanyaDokterScreen(
                        onBackClick = { finish() },
                        chatInstance = chat
                    )
                }
            }
            // --- AKHIR PERUBAHAN ---
        }
    }
}

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TanyaDokterScreen(
    onBackClick: () -> Unit,
    chatInstance: Chat
) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var questionInput by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    val composableScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            composableScope.launch {
                lazyColumnState.animateScrollToItem(messages.lastIndex)
                Log.d("TanyaDokter", "Scrolling to item: ${messages.lastIndex}")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tanya Dokter",
                        color = MaterialTheme.colorScheme.onSurface, // <-- Gunakan warna dari tema
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant // <-- Gunakan warna dari tema
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface) // <-- Gunakan warna dari tema
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .padding(bottom = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = questionInput,
                    onValueChange = { questionInput = it },
                    placeholder = { Text("Tanya Dokter...", color = MaterialTheme.colorScheme.onSurfaceVariant) }, // <-- Warna placeholder
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 50.dp, max = 150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant), // <-- Background dari tema
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary, // <-- Warna dari tema
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline, // <-- Warna dari tema
//                        textColor = MaterialTheme.colorScheme.onSurface // <-- Warna teks input
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        val question = questionInput.trim()
                        if (question.isNotEmpty() && !isSending) {
                            messages.add(ChatMessage(question, true))
                            Log.d("TanyaDokter", "User message added: $question")
                            questionInput = ""
                            sendPrompt(
                                promptText = question,
                                messages = messages,
                                chatInstance = chatInstance,
                                setIsSending = { isSending = it },
                                applicationContext = applicationContext,
                                composableScope = composableScope
                            )
                        } else if (question.isEmpty()) {
                            Toast.makeText(applicationContext, "Pertanyaan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.size(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary, // <-- Gunakan warna dari tema
                    shape = RoundedCornerShape(percent = 50)
                ) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Kirim Pesan",
                        tint = MaterialTheme.colorScheme.onPrimary // <-- Warna ikon dari tema
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background) // <-- Background utama dari tema
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                state = lazyColumnState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                }
            }

            if (isSending) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.primary // <-- Warna loading dari tema
                )
                Text(
                    text = "Dokter sedang mengetik...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // <-- Warna teks loading dari tema
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant // <-- Warna dari tema
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, // <-- Warna teks dari tema
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}


private fun sendPrompt(
    promptText: String,
    messages: MutableList<ChatMessage>,
    chatInstance: Chat,
    setIsSending: (Boolean) -> Unit,
    applicationContext: android.content.Context,
    composableScope: CoroutineScope
) {
    setIsSending(true)
    Log.d("TanyaDokter", "Sending prompt: $promptText")
    composableScope.launch(Dispatchers.IO) {
        try {
            val userContent = content { text(promptText) }
            val response = chatInstance.sendMessage(userContent)

            val generatedText = response.text
            Log.d("TanyaDokter", "Gemini response received. Text: $generatedText")

            withContext(Dispatchers.Main) {
                if (generatedText != null) {
                    messages.add(ChatMessage(generatedText, false))
                    Log.d("TanyaDokter", "AI message added: $generatedText")
                } else {
                    messages.add(ChatMessage("Dokter: Tidak ada respons dari Gemini.", false))
                    Log.d("TanyaDokter", "AI message added: No response from Gemini")
                }
            }
        } catch (e: Exception) {
            Log.e("TanyaDokterActivity", "Error calling Gemini API: ${e.message}", e)
            withContext(Dispatchers.Main) {
                messages.add(ChatMessage("Dokter: Terjadi kesalahan: ${e.message}", false))
                Log.d("TanyaDokter", "Error message added to UI: ${e.message}")
            }
        } finally {
            withContext(Dispatchers.Main) {
                setIsSending(false)
                Log.d("TanyaDokter", "Finished sending prompt. isSending set to false.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTanyaDokterScreen() {
    // --- PERUBAHAN DI SINI: GUNAKAN DMCTheme UNTUK PREVIEW ---
    DMCTheme { // <-- Panggil DMCTheme Anda di sini
        TanyaDokterScreen(onBackClick = {}, chatInstance = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = API_KEY, // Gunakan API_KEY dummy untuk preview
            systemInstruction = content { text("Preview Mode") }
        ).startChat())
    }
    // --- AKHIR PERUBAHAN ---
}