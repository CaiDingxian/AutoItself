package blockman.server

import kotlin.random.Random
import kotlin.random.nextInt

fun main() {

    val re = Re()
    val time = System.currentTimeMillis()
    val fs = re.javaClass.methods// 110492
    val f = Re::class.java.getMethod("go", String::class.java, Int::class.java)

    val u = 0
//    for (i in 1..1000000000) {
//        //f.invoke(re,"123456789123456789123456789",1)
//        re.go("123456789123456789123456789",1)//21810 23000 28776
//    }
//    print(System.currentTimeMillis()-time)
}

class Re {
    fun go(a: String, b: Int): String {
        //val r=Random.nextInt(1..100)
        var result = ""
        for (i in (1..1)) {
            val str = a + """
在我们平时的工作或者面试中，都会经常遇到“反射”这个知识点，通过“反射”我们可以动态的获取到对象的信息以及灵活的调用对象方法等，但是在使用的同时又伴随着另一种声音的出现，那就是“反射”很慢，要少用。难道反射真的很慢？那跟我们平时正常创建对象调用方法比慢多少? 估计很多人都没去测试过，只是”道听途说“。下面我们就直接通过一些测试用例来直观的感受一下”反射“。
作者：深夜里的程序猿
链接：https://juejin.im/post/5cc16dd25188252d3f7e1712
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
        """.trimIndent()
            result += str[Random.nextInt(1..100)]
        }
        return result
    }
}