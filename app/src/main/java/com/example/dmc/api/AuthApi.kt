package com.example.dmc.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @POST ("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("pasien")
    fun registerPasien(@Body pasien: Pasien): Call<Pasien>

    @POST("rekam-medis/{id}")
    fun addRekamMedis(@Path("id") pasienId: Long, @Body rekamMedis: RekamMedis): Call<RekamMedis>

    @GET("obat")
    fun getObat() : Call<List<Obat>>

    @GET("rekam-medis/pasien/{pasienId}")
    fun getRekamMedisByPasienId(@Path("pasienId") pasienId: Long) : Call<List<RekamMedis>>

    @PUT("obat/{id}/dispense")
    fun dispenseObat(@Path("id") id: Long, @Query("quantity") quantity:Int): Call<Obat>
}

data class Pasien(
    val id : Long? = null,
    val nama : String,
    val tanggalLahir : String,
    val alamat: String,
    val nomorTelepon: String,
    val isMahasiswaUnida : Boolean,
    val nim : String
)

data class LoginRequest(
    val nim: String,
    val tanggalLahir: String
)

data class RekamMedis(
    val id: Long? = null,
    val keluhan: String,
    val diagnosa: String,
    val tindakan: String,
    val resep: String,
    // Pastikan Pasien data class diimport di sini jika Anda menggunakannya
    val pasien: Pasien? = null,
    val tanggalKonsultasi: String? = null, // <--- KEMBALIKAN MENJADI String?
    @SerializedName("createdAt") val createdAt: String? = null, // <--- KEMBALIKAN MENJADI String?
    @SerializedName("updatedAt") val updatedAt: String? = null // <--- KEMBALIKAN MENJADI String?
)


data class Obat(
    val id : Long,
    val namaObat: String,
    val deskripsi: String,
    val stok: Int
)

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("pasienId") val pasienId: Long,
    @SerializedName("nama") val nama: String,
    @SerializedName("nim") val nim: String
)

data class RegisterResponse(
    val message: String
)