package com.example.kote

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    var customs = arrayOf<Custom>()
    val hotel = Hotel()
    var reserveList = hotel.reserveList
    customs+= Custom("김경식1",1623123,  9765,3)
    customs+= Custom("홍길동",312323,  87676,2)
    customs+= Custom( "김갑산",987823, 1234,1)
    reserveList+=arrayOf("김경식1","201","20231130","20231210","10000","3")
    reserveList+=arrayOf("홍길동","202","20231230","20240110","23455","2")
    reserveList+=arrayOf("김갑산","403","20240130","20240210","98753","1")
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

                var custom1 = Custom(name,(1..99999).random())
                var spendMoney = (50000..150000).random()
                reserveList += hotel.reserve(name, roomNumber, checkIn, checkOut,spendMoney.toString(),custom1.customNo.toString())
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
                    println("${i+1}.예약자: ${reserveList[i][0]}, 방 번호: ${reserveList[i][1]}, 체크인: ${reserveList[i][2]}, 체크아웃: ${reserveList[i][3]}")
                }
            }
            "3" -> {
                println("예약자 목록 정렬 오름차순")
                reserveList.sortedBy { it[2] }.forEachIndexed { index, s ->
                    println("${index+1}.예약자: ${s[0]}, 방 번호: ${s[1]}, 체크인: ${s[2]}, 체크아웃: ${s[3]}")
                }
            }
            "5" -> {
                println("조회하실 사용자 이름을 입력하세요")
                var findName = readLine()!!
                if(customs.count{ it.name==findName }>=1){
                    customs.filter { it.name==findName }.map {
                        println("1. 초기금액으로는 ${it.inputMoney}원 입금되었습니다")
                        println("2. 예약금액으로는 ${it.spendMoney}원 출금되었습니다")
                    }
                } else {
                    println("예약된 사용자를 찾지 못했습니다.")
                }
            }
            "6" -> {
                println("예약을 변경할 사용자를 선택하세요.")
                var findReserve = readLine()!!
                var tempArray = arrayOf(arrayOf<String>())
                if(reserveList.count{ it[0] == findReserve}>=1 ){
                    println("${findReserve}님이 예약한 목록입니다. 변경하실 예약번호를 말씀해주세요. (탈출은 exit입력)")
                    reserveList.filter { it[0] ==findReserve }.forEachIndexed { index, strings ->
                        println("${index+1} 방 번호: ${strings[1]}, 체크인: ${strings[2]}, 체크아웃: ${strings[3]}")
                        tempArray+=strings
                    }
                    var choice = readLine()!!
                    if (choice == "exit") continue
                    println(tempArray[choice.toInt()].contentToString())
                    if(tempArray[choice.toInt()].isNotEmpty() ) {
                        println("해당 예약을 어떻게 하시겠어요? 1. 변경, 2. 취소 / 이외 번호. 메뉴로 돌아가기 ")
                        var choice2 = readLine()!!
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                        val today = current.format(formatter)
                        var roomNumber = tempArray[choice.toInt()][1]
                        if(choice2 != "1"&& choice2!="2") continue
                        if(choice2 == "1"){
                            println("변경할 체크인 날짜를 입력해주세요. 표기형식:20230101")


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
                        } else if (choice2 == "2") {
                            println("[취소 유의사항]\n체크인 3일 이전 취소 예약금 환불 불가\n체크인 5일 이전 취소 예약금 환불 불가\n체크인 7일 이전 취소 예약금 환불 불가\n체크인 14일 이전 취소 예약금 환불 불가\n체크인 30일 이전 취소 예약금 환불 불가\n취소가 완료되었습니다.")
                            //checkin-today
                            println(tempArray[choice.toInt()].contentToString())
                            var day: Int = tempArray[choice.toInt()][2].toInt() - today.toInt()
                            when {
                                day <= 3-> println("예약금 환불 불가합니다.")
                                day in 4..5 -> {
                                    println("예약금의 30%를 환불받았습니다.")
                                    //동명이인 문제를 해결할 수 없어서 임시로 회원번호를 해놓음. 하지만 랜덤번호라서 중복이 될 가능성이 여전히 존재
                                    customs.filter { it.customNo == tempArray[choice.toInt()][5].toInt() }
                                        .map { it.inputMoney + (tempArray[choice.toInt()][4].toInt() * 0.3) }
                                    println(customs)
                                }

                                day in 6..7 -> {
                                    println("예약금의 50%를 환불받았습니다.")
                                    customs.filter { it.customNo == tempArray[choice.toInt()][5].toInt() }
                                        .map { it.inputMoney + (tempArray[choice.toInt()][4].toInt() * 0.5) }
                                    println(customs)
                                }

                                day in 8..14 -> {
                                    println("예약금의 80%를 환불받았습니다.")
                                    customs.filter { it.customNo == tempArray[choice.toInt()][5].toInt() }
                                        .map { it.inputMoney + (tempArray[choice.toInt()][4].toInt() * 0.8) }
                                    println(customs)
                                }

                                day > 15 -> {
                                    println("예약금의 100%를 환불받았습니다.")
                                    customs.filter { it.customNo == tempArray[choice.toInt()][5].toInt() }
                                        .map { it.inputMoney + tempArray[choice.toInt()][4].toInt() }
                                    println(customs)
                                }
                            }
                            continue

                        } else if(choice2=="exit"){
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

class Hotel {
    var reserveList =arrayOf(arrayOf<String>())
    fun finish(){
        System.exit(0)
    }
    fun reserve (name:String,roomNumber:String,checkIn:String,checkOut:String,deposit:String,customNo:String ): Array<String> {
        var array = arrayOf(name,roomNumber,checkIn,checkOut,deposit,customNo)
        return array
    }
}
class Custom {

    var customNo : Int =0
    var name : String =""
    var inputMoney:Int = 1000000
    var spendMoney:Int =0

    constructor(_str: String , _num: Int, _num2: Int ,_customNo:Int)  {
        name=_str
        spendMoney=_num2
        inputMoney = _num
        customNo = _customNo
    }
    constructor(_str: String, _num: Int ,_customNo:Int)  {
        name=_str
        inputMoney = _num
        customNo = _customNo
    }

    constructor(_str: String, _customNo: Int) {
        name=_str
        customNo = _customNo
    }

    fun spend(smoney:Int){
        inputMoney -= smoney
    }
}

