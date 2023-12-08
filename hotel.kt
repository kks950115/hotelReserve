package com.example.kote

import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    var customs = arrayOf<Custom>()
    val hotel = Hotel()
    var reserveList = hotel.reserveList
    customs+= Custom("김유신",1623123,  9765)
    customs+= Custom("홍길동",312323,  87676)
    customs+= Custom( "김갑산",987823, 1234)
    reserveList.add(arrayListOf("김유신","201","20231130","20231210","10000","3"))
    reserveList+=arrayListOf("홍길동","202","20231230","20240110","23455","2")
    reserveList+=arrayListOf("김갑산","403","20240130","20240210","98753","1")

    while (true) {
        println("1. 방예약, 2. 예약목록 출력, 3. 예약목록 (정렬) 출력, 4. 시스템 종료, 5. 금액 입금-출금 내역 목록 출력, 6. 예약 변경/취소")
        var answer = isNumber()

        when (answer) {
            4 -> hotel.finish()
            1 -> {
                println("예약자 성함을 말씀해주세요")
                var inputName: String = isName()

                println("예약할 방번호를 입력해주세요")
                var roomNumber = isNumber()
                while (roomNumber !in 100 .. 999 ) {
                    println("예약할 수 있는 방은 100~999까지만 있습니다")
                    roomNumber = isNumber()
                }

                println("체크인 날짜를 입력해주세요. 표기형식:20230101")
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val today = current.format(formatter)

                var checkIn = isDate()
                while (today.toInt() > checkIn ) {
                    println("입력할 수 없는 날짜입니다. 표기형식:20230101, 이전 날짜는 입력할 수 없습니다.")
                    checkIn = isDate()
                }
                while(reserveList.count{ it[1].toInt()==roomNumber && (it[3].toInt() - checkIn) >= 0 }>=1) {
                    println("해당 날짜에 이미 방을 사용 중입니다. 다른 날짜를 입력해주세요 ")
                    println("체크인 날짜를 입력해주세요 ")
                    checkIn = isDate()
                }
                println("체크아웃 날짜를 입력해주세요. 표기형식:20230101")
                var checkOut = isDate()
                while (checkIn > checkOut ) {
                    println("입력할 수 없는 날짜입니다. 표기형식:20230101,  체크인 이전 날짜는 입력할 수 없습니다.")
                    checkOut = isDate()
                }
                var spendMoney = (50000..150000).random()

                if(customs.count { it.name.equals(inputName) }>=1){
                    customs.filter{ it -> it.name.equals(inputName)}.forEach{ it->
                        it.spendMoney+=spendMoney
                        it.spend(spendMoney)
                        println("호텔예약이 완료되었습니다. 남은 돈 ${it.inputMoney}")
                    }
                    reserveList += hotel.reserve(inputName, roomNumber.toString(), checkIn.toString(), checkOut.toString(),spendMoney.toString())
                } else{
                    val custom1 = Custom(inputName,(500000..900000).random())
                    val spendMoneyRandom = (50000..150000).random()
                    reserveList += hotel.reserve(inputName, roomNumber.toString(), checkIn.toString(), checkOut.toString(),spendMoney.toString())
                    custom1.spend(spendMoneyRandom)
                    custom1.spendMoney+=spendMoneyRandom
                    customs+=custom1
                    println("호텔예약이 완료되었습니다. 남은 돈 ${custom1.inputMoney}")
                }


            }
            2 -> {
                println("예약자 목록")
//                println(reserveList[0].contentToString())
//                println(reserveList[1].contentToString())
//                println(reserveList[2].contentToString())
                for (i in 0 until reserveList.size) {
                    println("${i+1}.예약자: ${reserveList[i][0]}, 방 번호: ${reserveList[i][1]}, 체크인: ${reserveList[i][2]}, 체크아웃: ${reserveList[i][3]}")
                }
            }
            3 -> {
                println("예약자 목록 정렬 오름차순")
                reserveList.sortedBy { it[2] }.forEachIndexed { index, s ->
                    println("${index+1}.예약자: ${s[0]}, 방 번호: ${s[1]}, 체크인: ${s[2]}, 체크아웃: ${s[3]}")
                }
            }
            5 -> {
                println("조회하실 사용자 이름을 입력하세요")
                var findName = isName()
                if(customs.count{ it.name==findName }>=1){
                    customs.filter { it.name==findName }.map {
                        println("1. 초기금액으로는 ${it.inputMoney}원 입금되었습니다")
                        println("2. 예약금액으로는 ${it.spendMoney}원 출금되었습니다")
                    }
                } else {
                    println("예약된 사용자를 찾지 못했습니다.")
                }
            }
            6 -> {
                println("예약을 변경할 사용자를 선택하세요.")
                var findReserve = isName()
                var tempArray = arrayListOf<ArrayList<String>>()
                if(reserveList.count{ it[0] == findReserve}>=1 ){
                    println("${findReserve}님이 예약한 목록입니다. 변경하실 예약번호를 말씀해주세요. (탈출은 0입력)")
                    reserveList.filter { it[0] ==findReserve }.forEachIndexed { index, strings ->
                        println("${index+1} 방 번호: ${strings[1]}, 체크인: ${strings[2]}, 체크아웃: ${strings[3]}")
                        tempArray+=strings
                    }
                    val choice = isNumber()
                    if (choice == 0) continue
                    println(tempArray[choice-1])
                    if(tempArray[choice-1].isNotEmpty() ) {
                        println("해당 예약을 어떻게 하시겠어요? 1. 변경, 2. 취소 / 0. 메뉴로 돌아가기 ")
                        var choice2 = isNumber()
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                        val today = current.format(formatter)
                        var roomNumber = tempArray[choice-1][1]
                        if(choice2 != 1 && choice2!=2 ) continue
                        if(choice2 == 1){
                            println("변경할 체크인 날짜를 입력해주세요. 표기형식:20230101")

                            var checkIn = isDate()
                            while (today.toInt() > checkIn ) {
                                println("입력할 수 없는 날짜입니다. 표기형식:20230101, 이전 날짜는 입력할 수 없습니다.")
                                checkIn = isDate()
                            }
                            while(reserveList.count{ it[1]==roomNumber && (it[3].toInt() - checkIn) >= 0 }>=1) {
                                println("해당 날짜에 이미 방을 사용 중입니다. 다른 날짜를 입력해주세요 ")
                                println("체크인 날짜를 입력해주세요 ")
                                checkIn = isDate()
                            }
                            println("체크아웃 날짜를 입력해주세요. 표기형식:20230101")
                            var checkOut = isDate()
                            while (checkIn > checkOut) {
                                println("입력할 수 없는 날짜입니다. 표기형식:20230101,  체크인 이전 날짜는 입력할 수 없습니다.")
                                checkOut = isDate()
                            }
                            var idx = reserveList.indexOfFirst {
                                it[0] == tempArray[choice-1][0] &&
                                        it[1] == tempArray[choice-1][1] &&
                                        it[2] == tempArray[choice-1][2] &&
                                        it[3] == tempArray[choice-1][3] }
                            reserveList[idx][2]=checkIn.toString()
                            reserveList[idx][3]=checkOut.toString()
                            println("변경되었습니다.")

                        } else if (choice2 == 2) {
                            println("[취소 유의사항]\n체크인 3일 이전 취소 예약금 환불 불가\n체크인 5일 이전 취소 예약금 환불 불가\n체크인 7일 이전 취소 예약금 환불 불가\n체크인 14일 이전 취소 예약금 환불 불가\n체크인 30일 이전 취소 예약금 환불 불가\n취소가 완료되었습니다.")
                            reserveList.removeAt(reserveList.indexOfFirst {
                                it[0] == tempArray[choice-1][0] &&
                                it[1] == tempArray[choice-1][1] &&
                                it[2] == tempArray[choice-1][2] &&
                                it[3] == tempArray[choice-1][3] })
                            //println(tempArray[choice-1])
                            var day: Int = tempArray[choice-1][2].toInt() - today.toInt()
                            when {
                                day <= 3-> println("예약금 환불 불가합니다.")
                                day in 4..5 -> {
                                    println("예약금의 30%를 환불받았습니다.")

                                    customs.filter { it.name == tempArray[choice-1][0] }
                                        .map { it.recall((tempArray[choice-1][4].toInt() * 0.3).toInt()) }
                                }

                                day in 6..7 -> {
                                    println("예약금의 50%를 환불받았습니다.")
                                    customs.filter { it.name == tempArray[choice-1][0] }
                                        .map { it.recall((tempArray[choice-1][4].toInt() * 0.5).toInt()) }
                                }

                                day in 8..14 -> {
                                    println("예약금의 80%를 환불받았습니다.")
                                    customs.filter { it.name == tempArray[choice-1][0] }
                                        .map { it.recall((tempArray[choice-1][4].toInt() * 0.8).toInt())  }
                                }

                                day > 15 -> {
                                    println("예약금의 100%를 환불받았습니다.")
                                    customs.filter { it.name == tempArray[choice-1][0] }
                                        .map { it.recall(tempArray[choice-1][4].toInt()) }
                                }
                            }
                            continue

                        } else if(choice2==0){
                            continue
                        }else {
                            continue
                        }

                    } else {
                        continue
                    }
                } else {
                    println("해당 이름을 가진 예약자를 찾지못했습니다..")
                }
            }
            else ->  println("아직 구현되지 않았습니다.")

        }
    }
}
fun isNumber() : Int{
    while (true) {
        try {
            var num = readln().toInt()
            return num
        } catch (e: NumberFormatException) {
            println("잘못된 입력값입니다.")
        }
    }
}

fun isName() : String{
    while (true) {
        try {
            var name = readln()
            while(name.equals("")){
                println("공백은 입력이 불가능합니다.")
                name = readln()
            }
            return name
        } catch (e: Exception) {
            println("잘못된 입력값입니다.")
        }
    }
}

fun isDate() : Int { //입력 ex) 2024년01월01일, 2023-12-31, 2024/03/03 . 출력: 20240101, 20230213402
    while(true){
        try {
            var date = readln()
            date = date.replace("\\D".toRegex(),"")
            var day = date.takeLast(2).toInt()
            date = date.dropLast(2)
            var month = date.takeLast(2).toInt()
            date = date.dropLast(2)
            var year = date.toInt()
            var localDate = LocalDate.of(year,month,day)
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            var result = localDate.format(formatter).toInt()
            return result
        } catch (e: DateTimeException) {
            println("잘못된 입력값입니다.")

        }
    }
}

class Hotel {
    var reserveList = arrayListOf<ArrayList<String>>()
    fun finish(){
        System.exit(0)
    }
    fun reserve (name:String,roomNumber:String,checkIn:String,checkOut:String,deposit:String ): ArrayList<String> {
        var array = arrayListOf(name,roomNumber,checkIn,checkOut,deposit)
        return array
    }
}

class Custom {


    var name : String =""
    var inputMoney:Int = 1000000
    var spendMoney:Int =0

    constructor(_str: String , _num: Int, _inputMoney: Int )  {
        name=_str
        spendMoney=_num
        inputMoney = _inputMoney
    }
    constructor(_name: String, _inputMoney: Int )  {
        name=_name
        inputMoney = _inputMoney
    }


    fun spend(smoney:Int){
        inputMoney -= smoney
    }
    fun recall(smoney: Int){
        inputMoney += smoney
        spendMoney -=smoney
    }



}
