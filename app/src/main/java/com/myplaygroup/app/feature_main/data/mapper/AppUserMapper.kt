package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.feature_main.data.model.AppUserEntity
import com.myplaygroup.app.feature_main.domain.model.AppUser

fun AppUserEntity.toAppUser() : AppUser {

    return AppUser(
        id = id,
        clientId = clientId,
        email = email ?: "",
        locked = locked,
        phoneNumber = phoneNumber?: "",
        profileCreated = profileCreated,
        profileName = profileName?: "",
        userCredit = userCredit,
        username = username,
        modified = modified
    )
}

fun AppUser.toAppUserEntity() : AppUserEntity {

    return AppUserEntity(
        id = id,
        clientId = clientId,
        email = email,
        locked = locked,
        phoneNumber = phoneNumber,
        profileCreated = profileCreated,
        profileName = profileName,
        userCredit = userCredit,
        username = username,
        modified = modified
    )
}