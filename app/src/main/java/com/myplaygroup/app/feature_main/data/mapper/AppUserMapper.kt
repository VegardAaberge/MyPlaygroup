package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.feature_main.data.model.AppUserEntity
import com.myplaygroup.app.feature_main.domain.model.AppUser

fun AppUserEntity.toAppUser() : AppUser {

    return AppUser(
        id = id,
        clientId = clientId,
        locked = locked,
        phoneNumber = phoneNumber?: "",
        profileCreated = profileCreated,
        profileName = profileName?: "",
        username = username,
        modified = modified,
        resetPassword = resetPassword
    )
}

fun AppUser.toAppUserEntity() : AppUserEntity {

    return AppUserEntity(
        id = id,
        clientId = clientId,
        locked = locked,
        phoneNumber = phoneNumber,
        profileCreated = profileCreated,
        profileName = profileName,
        username = username,
        modified = modified,
        resetPassword = resetPassword
    )
}