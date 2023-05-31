import jsonValues.*

    //Data Objects
    internal val ins1 = Inscrito(101101, "Dave Farley",true)
    internal val ins2 = Inscrito(101102,"Martin Fowler",true)
    internal val ins3 = Inscrito(26503,"André Santos", false)

    internal val inscritos = listOf(ins1,ins2,ins3)


    internal val uc = UnidadeCurricular("PA", 6.0, null, inscritos   )

    // Json Objects
    internal val jUc = JObjectAttribute("uc", JString("PA"))
    internal val jEcts = JObjectAttribute("ects", JNumber(6.0))
    internal val jDt = JObjectAttribute("dataExame", JNull)

    internal val jIns1= JObject(
        listOf(
            JObjectAttribute("numero", JNumber(101101)),
            JObjectAttribute("nome", JString("Dave Farley")),
            JObjectAttribute("internacional", JBoolean(true))
        )
    )
    internal val jIns2= JObject(listOf(
        JObjectAttribute("numero", JNumber(101102)),
        JObjectAttribute("nome", JString("Martin Fowler")),
        JObjectAttribute("internacional", JBoolean(true))
    ))
    internal val jIns3= JObject(listOf(
        JObjectAttribute("numero", JNumber(26503)),
        JObjectAttribute("nome", JString("André Santos")),
        JObjectAttribute("internacional", JBoolean(false))
    ))
    internal val jInscritos = JArray(listOf(jIns1,jIns2,jIns3))

    internal val jIns = JObjectAttribute("inscritos", jInscritos)


    internal val myObject = JObject(listOf(jUc, jEcts, jDt, jIns) )
