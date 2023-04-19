
fun main() {
    val ucname1 = JObjectAttributes(Pair("uc", JString("PA")))
    val etcs1 = JObjectAttributes(Pair("etcs", JNumber(6.0)))
    val dt = JObjectAttributes(Pair("data-exame", JNull()))
    val b = JObjectAttributes(Pair("s/n", JBoolean(true)))
    val obj1 = JObject(listOf(ucname1, etcs1, dt,b))
    //obj1.listAttributes.forEach { println(it.attribute.first+ " "+ it.attribute.second.toString()) }


    val uc1 = JObjectAttributes(Pair("uc1", obj1))
    println(uc1.toString())

    val ucname2 = JObjectAttributes(Pair("uc2", JString("PO")))
    val etcs2 = JObjectAttributes(Pair("etcs2", JNumber(3.0)))
    val obj2 = JObject(listOf(ucname2, etcs2))

    val uc2 = JObjectAttributes(Pair("uc2", obj2))

    val allUCs = JObject(listOf(uc1, uc2))

   println(allUCs.toString())

}
