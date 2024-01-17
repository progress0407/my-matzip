package io.philo.matzip.app

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestClient
import java.lang.Thread.sleep

fun main() {

    val restClient: RestClient = RestClient.create();

    val url = Url("https://place.map.kakao.com/main/v/1619702502")

    do {
        var isOkStatus = true
        println("# 탐색: ${url.value}")
        val entity: ResponseEntity<Dto> = restClient.get()
            .uri(url.value)
            .accept(APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError) { request, response ->
                isOkStatus = false
                println("is Not Status OK ${response.statusCode}")
            }
            .toEntity(Dto::class.java)

        if (isOkStatus.not() || isEmpty(entity)) {
            println("# empty ")
            println("status = ${entity.statusCode}")
            break
        }

        val comments = entity.body!!.comment!!.list!!
        println("comments = ${comments}")

        url.setLastUrl(comments.last().commentid)

        sleep(500)

    } while (entity.statusCode == HttpStatus.OK)
}

private fun extracted(isOkStatus: Boolean, response: ClientHttpResponse) {
    var isOkStatus1 = isOkStatus
    isOkStatus1 = false
    println("is Not Status OK ${response.statusCode}")
}

private fun isEmpty(entity: ResponseEntity<Dto>) =
    entity.body == null || entity.body!!.comment == null || entity.body!!.comment!!.list == null

private data class Url(var value: String) {

    var isFirstStatus = true

    fun setLastUrl(lastCommentId: Long) {
        if (isFirstStatus) {
            value = value.replace("/main", "/commentlist") + "/$lastCommentId"
            isFirstStatus = false
        } else {
            value = "\\d+$".toRegex().replace(value, lastCommentId.toString())
        }
    }
}

private data class Dto(val comment: DtoList?) {
    constructor() : this(null)
}

private data class DtoList(val list: List<DtoComment>?) {
    constructor() : this(null)
}

private data class DtoComment(val commentid: Long, val contents: String?) {
    constructor() : this(-1, null)
}