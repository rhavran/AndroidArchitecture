package com.androidarchitecture.data.local.user

import android.arch.lifecycle.LiveData
import com.androidarchitecture.entity.User

/**
 * Created by binary on 5/18/17.
 */
interface UserLocalAPI {

    fun findByName(first: String): LiveData<User>

    fun findById(uid: Int): LiveData<User>
}