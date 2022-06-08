package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_main.data.model.AppUserEntity
import com.myplaygroup.app.feature_main.domain.model.AppUser

fun AppUserEntity.toAppUser() : AppUser {

    return AppUser(
        id = id,
        clientId = clientId,
        email = email ?: Constants.NULL,
        locked = locked,
        phoneNumber = phoneNumber?: Constants.NULL,
        profileCreated = profileCreated,
        profileName = profileName?: Constants.NULL,
        userCredit = userCredit,
        username = username
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
        username = username
    )
}