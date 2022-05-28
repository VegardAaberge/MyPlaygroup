package com.myplaygroup.app.core.domain.repository

import com.myplaygroup.app.core.util.Resource

interface TokenRepository {
    suspend fun verifyRefreshTokenAndReturnMessage() : String

    suspend fun verifyRefreshToken() : Resource<Unit>
}