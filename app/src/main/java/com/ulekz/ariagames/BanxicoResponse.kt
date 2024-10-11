package com.ulekz.ariagames

data class BanxicoResponse(
    val bmx: BmxData
)

data class BmxData(
    val series: List<SerieData>
)

data class SerieData(
    val idSerie: String,
    val titulo: String,
    val datos: List<SerieDato>
)

data class SerieDato(
    val fecha: String,
    val dato: String
)
