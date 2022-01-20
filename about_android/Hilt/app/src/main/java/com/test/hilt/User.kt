package com.test.hilt

import javax.inject.Inject

data class User(
    var name: String
) {
    /**
     * 生成User的方式，使用默认的构造函数
     */
    @Inject
    constructor() : this("zhangsan")
}

//class User @Inject
//constructor() {
//
//}