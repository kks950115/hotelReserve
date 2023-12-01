package com.example.kote

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    var customs = arrayOf<Custom>()
    val hotel = Hotel()
    var reserveList = hotel.reserveList
    customs+= Custom("김경식1",1623123,  9765)
    customs+= Custom("홍길동",312323,  87676)
    customs+= Custom( "김갑산",987823, 1234)
    reserveList+=arrayOf("김경식1","201","20231130","20231210")
    reserveList+=arrayOf("홍길동","202","20231230","20240110")
    reserveList+=arrayOf("김갑산","403","20240130","20240210")
    reserveList=reserveList.sliceArray(1..3)
    while (true) {
        println("1. 방예약, 2. 예약목록 출력, 3. 예약목록 (정렬) 출력, 4. 시스템 종료, 5. 금액 입금-출금 내역 목록 출력, 6. 예약 변경/취소")
        var answer: String = readLine()!!

        when (answer) {
            "4" -> hotel.finish()
            "1" -> {
                println("예약자 성함을 말씀해주세요")
                var name: String = readLine()!!
                println("예약할 방번호를 입력해주세요")
                var roomNumber: String = readLine()!!
                while (roomNumber.toInt() > 999 || roomNumber.toInt() < 100 ) {
                    println("예약할 수 있는 방은 100~999까지만 있습니다")
                    roomNumber = readLine()!!
                }

                println("체크인 날짜를 입력해주세요. 표기형식:20230101")
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val today = current.format(formatter)

                var checkIn: String = readLine()!!
                while (checkIn.length != 8 || today.toInt() > checkIn.toInt() ) {
                    println("입력할 수 없는 날짜입니다. 표기형식:20230101, 이전 날짜는 입력할 수 없습니다.")
                    checkIn = readLine()!!
                }
                while(reserveList.count{ it[1]==roomNumber && (it[3].toInt() - checkIn.toInt()) >= 0 }>=1) {
                    println("해당 날짜에 이미 방을 사용 중입니다. 다른 날짜를 입력해주세요 ")
                    println("체크인 날짜를 입력해주세요 ")
                    checkIn = readLine()!!
                }
                println("체크아웃 날짜를 입력해주세요. 표기형식:20230101")
                var checkOut: String = readLine()!!
                while (checkOut.length != 8 || checkIn.toInt() > checkOut.toInt() ) {
                    println("입력할 수 없는 날짜입니다. 표기형식:20230101,  체크인 이전 날짜는 입력할 수 없습니다.")
                    checkOut = readLine()!!
                }
                reserveList += hotel.reserve(name, roomNumber, checkIn, checkOut)
                var custom1 = Custom(name)
                var spendMoney = (50000..150000).random()
                custom1.spend(spendMoney)
                custom1.spendMoney+=spendMoney
                customs+=custom1
                println("호텔예약이 완료되었습니다. 남은 돈 ${custom1.inputMoney}")

            }
            "2" -> {
                println("예약자 목록")
//                println(reserveList[0].contentToString())
//                println(reserveList[1].contentToString())
//                println(reserveList[2].contentToString())
                for (i in 0 until reserveList.size) {
                    println("${i}.예약자: ${reserveList[i][0]}, 방 번호: ${reserveList[i][1]}, 체크인: ${reserveList[i][2]}, 체크아웃: ${reserveList[i][3]}")
                }
            }
            "3" -> {
                println("예약자 목록 정렬 오름차순")
                reserveList.sortedBy { it[2] }.forEachIndexed { index, s ->
                    println("${index+1}.예약자: ${s[0]}, 방 번호: ${s[1]}, 체크인: ${s[2]}, 체크아웃: ${s[3]}")
                }
//                for (i in 1 .. reserveList.size) {
//                    print("${i}.예약자: ${reserveList[i][0]}, 방 번호: ${reserveList[i][1]}, 체크인: ${reserveList[i][2]}, 체크아웃: ${reserveList[i][3]}")
//                }
            }
            "5" -> {
                println("조회하실 사용자 이름을 입력하세요")
                var findName = readLine()!!
                if(customs.count{ it.name==findName }>=1){
                    customs.filter { it.name==findName }.map {
                        println("1. 초기금액으로는 ${it.inputMoney}원 입금되었습니다")
                        println("2. 초기금액으로는 ${it.spendMoney}원 입금되었습니다")
                    }
                } else {
                    println("예약된 사용자를 찾지 못했습니다.")
                }
            }
            else ->  println("아직 구현되지 않았습니다.")

        }
    }
}

class Hotel {
    var reserveList =arrayOf(arrayOf<String>())
    fun finish(){
        System.exit(0)
    }
    fun reserve (name:String,roomNumber:String,checkIn:String,checkOut:String): Array<String> {
        var array = arrayOf(name,roomNumber,checkIn,checkOut)
        return array
    }
}
class Custom {


    var name : String =""
    var inputMoney:Int = 0
    var spendMoney:Int =0

    constructor(_str: String = "익명", _num: Int = 50000000, _num2: Int = 10000)  {
        name=_str
        spendMoney=_num2
        inputMoney = _num
    }
    constructor( _num: Int = 50000000, _num2: Int = 10000)  {
        spendMoney=_num2
        inputMoney = _num
    }
    constructor(_str: String = "익명", _num: Int = 50000000)  {
        name=_str
        inputMoney = _num
    }

    fun spend(smoney:Int){
        inputMoney -= smoney
    }
}
