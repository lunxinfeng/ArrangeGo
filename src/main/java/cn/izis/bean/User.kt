package cn.izis.bean

class User() {
    var index: String = "0"//编号
    var name: String = ""//名字
    var sex: String = ""//性别
    var company: String = ""//单位
    var phone: String = ""//电话
    var age: String = ""//年龄

    constructor(
        index: String,
        name: String,
        sex: String,
        company: String,
        phone: String,
        birthday: String
    ) : this() {
        this.index = index
        this.name = name
        this.sex = sex
        this.company = company
        this.phone = phone
        this.age = birthday
    }
}