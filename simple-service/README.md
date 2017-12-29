# SimpleService

How to start the SimpleService application
------------------------------------------

$ curl -X GET http://localhost:8080/api/events

[{"id":10,"name":"Birthday","description":"Please do not be on time!","location":"345B Baker Street","date":"2017-12-28T23:31-0500"}]

Health Check
---

$ curl http://localhost:8081/healthcheck

{"deadlocks":{"healthy":true}}