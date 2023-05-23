package com.example.nakilcep

interface MyInterface {
    var prop: Int
    val propertyWithImplementation: Int get() = 12
}

class Impl : MyInterface {
    override var prop = 1
    override var propertyWithImplementation = 12
        get() = super.propertyWithImplementation + prop
        set(value) {
            field = value * 2
        }
}

fun main() {
    val impl = Impl()
    impl.prop = 3
    impl.propertyWithImplementation = 4
    print(impl.prop + impl.propertyWithImplementation)


}/*
fun main() {
    var a = 1
    var b = 2
    a = b.also { b = a }.apply { b *= 2 }
    print("a:$a, b:$b")
}*/