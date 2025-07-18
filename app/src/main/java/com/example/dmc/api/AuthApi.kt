package com.example.dmc.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.POST

interface AuthApi {
    @POST ("pasien")
    fun login(@Body request: LoginRequest): Call<LoginRequest>
}



data class Pasien(
    val id : Long,
    val nama : String,
    val tanggalLahir : String,
    val alamat: String,
    val nomorTelepon: String,
    val isMahasiswaUnida : Boolean,
    val nim : String
)

data class RekamMedis(
    val keluhan : String,
    val diagnosa : String,
    val tindakan : String,
    val resep : String
)

data class Obat(
    val id : Long,
    val namaObat: String,
    val deskripsi: String,
    val stok: Int
)

data class LoginRequest(
    val nim: String,
    val tanggalLahir: String
)