import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

abstract class Products(val name:String,var count:Int,var price:Int, var url:URL)
{
    abstract fun print()
}

class Fabric(
    name: String,
    count: Int ,
    price: Int,
    url:URL,
    var a: Int = 0,
    var b: Int = 0,
    var c: Int = 0,
):Products(name,count,price,url)
{
    override fun print() {
        println("| $name | $count | $price | $a | $b | $c ")
    }
}

class Tools(
    name: String,
    count: Int ,
    price: Int,
    url:URL,
    var description: String,
    var instructions: String,
):Products(name,count,price,url) {

    override fun print() {
        println("| $name | $count | $price | $description | $instructions |")
    }
}
class CMain
{
    private fun outMenu()
    {
        println()
        println()
        println("Выберите требуемое действие: ")
        println("1. Вывести список стройматериалов.")
        println("2. Вывести список инструментов.")
        println("3. Добавить стройматериал.")
        println("4. Добавить инструмент.")
        println("5. Удалить стройматериал.")
        println("6. Удалить инструмент.")
        println("7. Фильтровать стройматериал .")
        println("8. Фильтровать инструмент .")
        println("9. Отсортировать стройматериал.")
        println("10. Отсортировать инструмент.")
        println("11. Скачать страницы товаров.")
        println("12. Выход.")
    }
    private fun getAction() : Int
    {
        var action : Int
        while (true) {
            outMenu()
            try{
                action = readln().toInt()
                if (action in 1..13)
                    return action
                println("Необходимо указать номер операции от 1 до 12!")
            }
            catch(e : Exception)
            {
                println("Необходимо указать номер операции от 1 до 12!")
            }
        }

    }
    private val tovar = mutableListOf<Fabric>(
        Fabric(
            name = "Бетон",
            count = 5,
            price = 40,
            url=URI("https://www.doitbest.com/main-building-materials/").toURL(),
            a=20,
            b=30,
            c=10,
        ),
        Fabric(
            name = "Плитка",
            count = 7,
            price = 30,
            url=URI("https://www.doitbest.com/main-building-materials/").toURL(),
            a=30,
            b=50,
            c=20,
        ),
    )
    private val tools = mutableListOf<Tools>(
        Tools(
            name = "Пила",
            count = 4,
            price = 40000,
            url=URI("https://www.doitbest.com/main-building-materials/").toURL(),
            description = " ",
            instructions = " ",
        ),
        Tools(
            name = "Коса",
            count = 5,
            price = 10000,
            url=URI("https://www.doitbest.com/main-building-materials/").toURL(),
            description = " ",
            instructions = " ",
        )
    )

    private fun outMaterial()
    {
        tovar.forEach { it.print() }
    }

    private fun outTools()
    {
        tools.forEach { it.print() }
    }

    private fun addFabric()
    {
        println("Наименование стройматериала: ")
        val name = readln()
        println("Количество стройматериала: ")
        var count= readln().toInt()
        println("Цена стройматериала: ")
        val price = readln().toInt()
        var url : String
        var urlCleared : URL
        while (true) {
            println("Ссылка на товар: ")
            url = readln()
            try{
                urlCleared = URI(url).toURL()
                break
            }
            catch (e : Exception)
            {
                println("Проверьте правильность указания http адреса!")
            }
        }
        tovar.add(
            Fabric(
                name,
                count,
                price,
                urlCleared,
            )
        )
    }

    private fun addTools()
    {
        println("Наименование инструмента: ")
        val name = readln()
        println("Количество инструмента: ")
        var count= readln().toInt()
        println("Цена инструмента: ")
        val price = readln().toInt()
        println("Описание инструмента: ")
        val description = readln()
        println("Инструкция инструмента: ")
        val instructions = readln()
        var url : String
        var urlCleared : URL
        while (true) {
            println("Ссылка на товар: ")
            url = readln()
            try{
                urlCleared = URI(url).toURL()
                break
            }
            catch (e : Exception)
            {
                println("Проверьте правильность указания http адреса!")
            }
        }
        tools.add(
            Tools(
                name,
                count,
                price,
                urlCleared,
                description,
                instructions,
            )
        )
    }

    private fun filterMaterial()
    {
        print("Введите наименование материала для поиска: ")
        val filterStr = readln()

        val filtered = tovar
            .filter {
                it.name.startsWith(filterStr, ignoreCase = true)
            }
        filtered
            .forEach { it.print() }
        if (filtered.isEmpty())
            println("По вашему запросу ничего не найдено!")

    }
    private fun filterTools()
    {
        print("Введите наименование инструмента для поиска: ")
        val filterStr = readln()

        val filtered = tools
            .filter {
                it.name.startsWith(filterStr, ignoreCase = true)
            }
        filtered
            .forEach { it.print() }
        if (filtered.isEmpty())
            println("По вашему запросу ничего не найдено!")
    }

    suspend fun downloadFile(url: URL, outputFileName: String) {
        //withContext(Dispatchers.IO) {
        url.openStream().use {
            Files.copy(it, Paths.get(outputFileName), StandardCopyOption.REPLACE_EXISTING)
        }
        //}
        //println(outputFileName)
    }
    private suspend fun downloadFileWC(url: URL, outputFileName: String) {
        withContext(Dispatchers.IO) {
            url.openStream().use {
                Files.copy(it, Paths.get(outputFileName), StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
    private fun loadURL()
    {
        println("Началась загрузка адресов.")
        runBlocking {
            tovar
                .asFlow()
                .onEach { tovar ->
                    downloadFile(tovar.url, tovar.name)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    println("Загрузка завершена: ${it.name}")
                }

        }
        println("Загрузка завершена.")
    }
    fun deleteMaterial()
    {
        println("Какой материал удалить?: ")
        val name = readln()
        tovar.removeIf { it.name==name }
        outMaterial()
    }
    fun deleteTools()
    {
        println("Какой инструмент удалить?: ")
        val name = readln()
        tools.removeIf { it.name==name }
        outTools()
    }
    fun sortingMaterial()
    {
        tovar.sortBy {it.name  }
        outMaterial()
    }
    fun sortingTools()
    {
        tools.sortBy{it.name}
        outTools()
    }
    fun main()
    {
        while(true) {
            when(getAction())
            {
                1 -> outMaterial()
                2 -> outTools()
                3 -> addFabric()
                4 -> addTools()
                5 -> deleteMaterial()
                6 -> deleteTools()
                7 -> filterMaterial()
                8 -> filterTools()
                9 -> sortingMaterial()
                10 -> sortingTools()
                11 -> loadURL()
                12 -> return
            }

        }
    }
}

fun main()
{
    CMain().main()
}