package labs.devotion.ipcloudsync.cloudflareclient

internal object MockResponses {
    val successZone = """
        {
            "result": [
                { "id": "$zoneId", "name": "$domain" }
            ],
            "errors": [],
            "messages": [],
            "success": true
        }
    """.trimIndent()

    val successDns = """
        {
            "result": [
                { "zone_id": "$zoneId", "zone_name": "$domain", "content": "$ipAddress", "id": "$recordId" }
            ],
            "errors": [],
            "messages": [],
            "success": true
        }
    """.trimIndent()

    val emptyResult = """
        {
            "result": [],
            "errors": [],
            "messages": [],
            "success": true
        }
    """.trimIndent()
}
