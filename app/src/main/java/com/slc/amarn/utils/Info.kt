package com.slc.amarn.utils

import com.slc.amarn.models.User

object Info {
    var user: User = User()
    var photos: ArrayList<String> = ArrayList()
    var reloadPhotos: Boolean = false
}