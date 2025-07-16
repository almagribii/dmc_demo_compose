package com.example.dmc.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register/dokter")
    suspend fun registerDokter(@Body dokter: Dokter): Response<Dokter> // <--- HAPUS {} DI SINI


    @POST("auth/register/pasien")
    suspend fun registerPasien(@Body pasien: Pasien): Response<Pasien> // <--- HAPUS {} DI SINI

    @GET("dokter")
    suspend fun getListDokter(): Response<List<Dokter>> // <--- HAPUS {} DI SINI


    @GET("pasien")
    suspend fun getListPasien(): Response<List<Pasien>> // <--- HAPUS {} DI SINI


    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserAuthResult> // <--- HAPUS {} DI SINI
}

// Data class Anda (ObjectId, Kontak, ProdiInfo, Pasien, Dokter, LoginRequest, UserAuthResult)
// Tetap berada di sini dan definisinya sudah tepat.

data class ObjectId(
    @SerializedName("\$oid") val oid: String
)

data class Kontak(
    val email: String,
    val telepon: String,
    val alamat: String
)

data class ProdiInfo(
    val idProdi: ObjectId,
    val namaProdi: String
)

data class Pasien(
    @SerializedName("id") val id: String? = null,
    val passwordHash: String? = null,
    val jenisIdentitas: String,
    val nomorIdentitas: String,
    val namaLengkap: String,
    val tanggalLahir: String,
    val jenisKelamin: String,
    val golonganDarah: String,
    val kontak: Kontak,
    val statusCivitas: String,
    val programStudi: ProdiInfo,
    val tanggalDaftarDMC: String,
    val riwayatAlergi: List<String>,
    val riwayatPenyakitKronis: List<String>,
    val catatanTambahan: String
)

data class Dokter(
    @SerializedName("id") val id: String? = null,
    val passwordHash: String? = null,
    val nipPegawai: String,
    val namaLengkap: String,
    val spesialisasi: String,
    val nomorSIP: String,
    val kontak: Kontak,
    val statusAktif: Boolean
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class UserAuthResult(
    val identifier: String,
    val role: String?,
    val pasienType: String?,
    val isAuthenticated: Boolean,
    val isNewUser: Boolean
)