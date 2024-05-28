import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

abstract class Products(val name:String,var count:Int,var price:Double, var url:URL)
{
    abstract fun print()
}

class Fabric(
    name: String,
    count: Int,
    price: Double,
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
    price: Double,
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
            name = "бетонно-цементная смесь",
            count = 5,
            price = 12.99,
            url =URI("https://www.doitbest.com/product/264074/quikrete-commercial-grade-quick-setting-cement-264074/?member=main-building-materials").toURL(),
            a =20,
            b =30,
            c =10,
        ),
        Fabric(
            name = "Плитка",
            count = 7,
            price = 32.99,
            url =URI("https://www.doitbest.com/product/123862/dpi-aquatile-4-ft-x-8-ft-x-1-8-in-gray-encinitas-tileboard-wall-tile-123862/?member=main-building-materials").toURL(),
            a =30,
            b =50,
            c =20,
        ),
        Fabric(
            name = "Внутренний шуруп",
            count = 7,
            price = 10.99,
            url =URI("https://www.doitbest.com/product/201147/spax-10-x-3-in-flat-head-interior-multi-material-construction-screw-1-lb-box-201147/?member=main-building-materials").toURL(),
            a =30,
            b =50,
            c =20,
        ),
    )
    private val tools = mutableListOf<Tools>(
        Tools(
            name = "Пила",
            count = 4,
            price = 74.99,
            url=URI("https://www.doitbest.com/product/732893/remington-versa-saw-rm1645-16-in-12a-electric-chainsaw-732893/?member=main-building-materials").toURL(),
            description = "Ремингтон 16 дюймов. Электрическая бензопила 12А оснащена двигателем на 16 дюймов. стержень и цепь с низкой отдачей, удобный доступ к натяжителю цепи с помощью винта с накатанной головкой, автоматическая смазка, полностью охватываемая рукоятка для резки под любым углом, удобный обзор масляного резервуара и стальные выступающие зубья для лучшего рычага и контроля. Полностью собран. Вес: 9,5 фунтов. Ограниченная гарантия 2 года.",
            instructions = "Необходимо взять пилу и подключить к источнику электропитания, а затем пилить необходимую древесину",
        ),
        Tools(
            name = "Электрическая газонокосилка",
            count = 5,
            price = 219.99,
            url=URI("https://www.doitbest.com/product/701122/black-decker-20-in-13a-push-electric-lawn-mower-701122/?member=main-building-materials").toURL(),
            description = "Легкая электрическая газонокосилка проста в управлении и приводится в действие двигателем Black & Decker на 13 А. 20 В. Дека 3-в-1 для мульчирования, сбора в мешок или бокового выброса. Технология Edge Max для резки близко к кромке. Прочная, не ржавеющая палуба легко чистится и имеет пожизненную гарантию. Ручка легко складывается для компактного хранения. Конструкция сумки с подъемным механизмом облегчает опорожнение и прикрепление. Требуется минимальная сборка. В комплект входит задняя сумка в сборе. Дверца закрывается на задней части газонокосилки, образуя пробку для мульчи. Высота среза: 1-1/2 дюйма. до 4 В. с регулировкой высоты одним рычагом. Идеальный размер собственности: до 1/3 акра в пределах 100 футов. электрической розетки. 21,875 дюймов. Ш х 34,125 дюйма. Высота х 17,25 дюйма. D. Вес устройства: 46,85 фунтов. См. модель № CMB2000 для замены лезвия косилки.",
            instructions = "Необходимо взять газонокосилку и подключить к источнику электропитания, а косить траву",
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
        val count= readln().toInt()
        println("Цена стройматериала: ")
        val price = readln().toDouble()
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
        val count= readln().toInt()
        println("Цена инструмента: ")
        val price = readln().toDouble()
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

    fun downloadFile(url: URL, outputFileName: String) {
        url.openStream().use {
            Files.copy(it, Paths.get(outputFileName), StandardCopyOption.REPLACE_EXISTING)
        }

    }

    private fun loadURL() {
        println("Началась загрузка адресов.")
        runBlocking {
            tovar
                .asFlow()
                .onEach { tovar ->
                    downloadFile(tovar.url, "${tovar.name}.html")
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    println("Загрузка завершена: ${it.name}")
                }
        }
        runBlocking {
            tools
                .asFlow()
                .onEach { tools ->
                    downloadFile(tools.url, "${tools.name}.html")
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