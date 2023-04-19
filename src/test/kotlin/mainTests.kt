
fun main() {
    val ucname1 = JObjectAttribute("uc", JString("PA"))
    val etcs1 = JObjectAttribute("etcs", JNumber(6.0))
    val dt = JObjectAttribute("data-exame", JNull())
    val b = JObjectAttribute("s/n", JBoolean(true))
    val obj1 = JObject(listOf(ucname1, etcs1, dt,b))
    //obj1.listAttributes.forEach { println(it.attribute.first+ " "+ it.attribute.second.toString()) }


    val uc1 = JObjectAttribute("uc1", obj1)
    //println(uc1.toString())

    val ucname2 = JObjectAttribute("uc2", JString("PO"))
    val etcs2 = JObjectAttribute("etcs2", JNumber(3.0))
    val obj2 = JObject(listOf(ucname2, etcs2))

    val uc2 = JObjectAttribute("uc2", obj2)

    val allUCs = JObject(listOf(uc1, uc2))

   println(allUCs.toString())

}
