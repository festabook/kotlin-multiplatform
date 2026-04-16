package com.daedan.festabook.domain.model

// TODO(server-supports-placeId): 서버가 /waitings/me 응답에 placeId를 내려주면 이 래퍼 삭제
data class MyWaitingWithPlace(
    val myWaiting: MyWaiting,
    val placeId: Long?, // DataStore 캐시. 앱 재설치 등으로 없을 수 있음
)