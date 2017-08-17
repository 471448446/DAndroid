package com.antonioleiva.weatherapp.domain.datasource

import com.antonioleiva.weatherapp.data.server.ForecastResult

interface ForecastDataSource {

    fun requestForecastByZipCode(zipCode: Long): ForecastResult?

}