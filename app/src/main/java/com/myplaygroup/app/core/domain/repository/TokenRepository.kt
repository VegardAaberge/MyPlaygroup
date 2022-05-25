package com.myplaygroup.app.core.domain.repository

interface TokenRepository {
    suspend fun verifyRefreshToken() : String
}