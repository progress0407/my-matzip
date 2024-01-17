package io.philo.matzip.app

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class PlaceCollector {
}

fun main() {
    val jsonString = """
    {
        "20916775": {
            "periodList": [
                {
                    "periodName": "Operating period",
                    "timeList": [
                        {
                            "timeName": "영업시간",
                            "timeSE": "17:00 ~ 23:00",
                            "dayOfWeek": "화~금",
                            "timeEtc": null
                        },
                        {
                            "timeName": "영업시간",
                            "timeSE": "13:00 ~ 23:00",
                            "dayOfWeek": "토,일",
                            "timeEtc": null
                        }
                    ]
                }
            ]
        }, 
        "1234775": {
            "periodList": [
                {
                    "periodName": "Operating period",
                    "timeList": [
                        {
                            "timeName": "영업시간",
                            "timeSE": "17:00 ~ 23:00",
                            "dayOfWeek": "화~금",
                            "timeEtc": null
                        }
                    ]
                }
            ]
        }
    }
}
"""

    val objectMapper = jacksonObjectMapper()

    try {
        val root = objectMapper.readValue(jsonString, object : TypeReference<Map<String, PeriodData>>() {})
        val periodData = root["20916775"]
        val timeList = periodData?.periodList?.flatMap { it.timeList.orEmpty() }

        // Do something with the timeList
        timeList?.forEach { println(it) }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

private data class PeriodData  constructor(
    val periodList: List<Period>,
)

private data class Period constructor(
    val periodName: String = "",
    val timeList: List<Time>? = null,
)

private data class Time constructor(
    val timeName: String = "",
    val timeSE: String = "",
    val dayOfWeek: String = "",
    val timeEtc: String? = null, // Nullable as per your JSON structure
)


/*    val restClient: RestClient = RestClient.create();

        val url =
            "https://map.kakao.com/api/dapi/overall?confirmId=27478115%2C26947426%2C449062653%2C1712259768%2C17904909%2C20916775%2C1939002664%2C25975641%2C26486835%2C27327963%2C25648863%2C12363566%2C914137926%2C465772929%2C27306614&mode=standard\n"

        val entity: ResponseEntity<Dto> = restClient.get()
            .uri(url)
            .accept(APPLICATION_JSON)
            .retrieve()
            .toEntity(Dto::class.java)*/

/*
    """
    {
        "20916775": {
        "periodList": [
        {
            "periodName": "Operating period",
            "timeList": [
            {
                "timeName": "영업시간",
                "timeSE": "17:00 ~ 23:00",
                "dayOfWeek": "화~금",
                "timeEtc": null
            },
            {
                "timeName": "영업시간",
                "timeSE": "13:00 ~ 23:00",
                "dayOfWeek": "토,일",
                "timeEtc": null
            }
            ]
        }
        ]
    },
        "1712259768": {
        "periodList": [
        {
            "periodName": "Operating period",
            "timeList": [ ]
        }
        ]
    }
    }
    """
*/