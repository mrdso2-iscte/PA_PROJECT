import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class mainTest{
    //UC 1
    val ucname1 = JObjectAttribute("uc", JString("PA"))
    val etcs1 = JObjectAttribute("etcs", JNumber(6.0))
    val dt = JObjectAttribute("data-exame", JNull())
    val b = JObjectAttribute("s/n", JBoolean(true))
    val obj1 = JObject(listOf(ucname1, etcs1, dt,b))

    val uc1 = JObjectAttribute("uc1", obj1)

    //UC 2
    val ucname2 = JObjectAttribute("uc", JString("PO"))
    val etcs2 = JObjectAttribute("etcs", JNumber(3.0))
    val obj2 = JObject(listOf(ucname2, etcs2))

    val uc2 = JObjectAttribute("uc2", obj2)

    //Lista de UCs
    val allUCs = JObject(listOf(uc1, uc2))


    @Test
    fun testGetValuesWithLabel(){
        val ucList = GetValuesWithLabel("uc")
        allUCs.accept(ucList)
        val a = listOf<JValue>(JString("PA"),JString("PO"))
        assertEquals(a.toString(), ucList.list.toString())

        val etcsList = GetValuesWithLabel("etcs")
        allUCs.accept(etcsList)
        val b = listOf<JValue>(JNumber(6.0),JNumber(3.0))
        assertEquals(b.toString(), etcsList.list.toString())

    }
    @Test
    fun testGetObjectsWithLabels(){
        val ucList = GetObjectsWithLabels(listOf("uc","etcs") )
        allUCs.accept(ucList)
        val a = listOf<JValue>(obj1, obj2)
        println(ucList.list.toString())
        //assertEquals(a.toString(), ucList.list.toString())



    }
}

