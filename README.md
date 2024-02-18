# Monitoring Service

Monitoring Service - это приложение для для подачи показаний счетчиков пользователей.

## Запуск приложения

1. Склонируйте репозиторий:
   git clone git@github.com:A1ekseiPanov/MonitoringService.git
2. Откройте проект в среде разработки.
3. Для запуска приложения, нужно предварительно запустить docker-compose.yml файл.
4. Необходимо скачать, установить, включить Tomcat сбилдить проект на http://localhost:8081.
-----------------------------

## Данные для тестирования:
1. http://localhost:8081/registration - регистрация нового пользователя(метод POST)
```
{
"username": "user1234",
"password": "user1234"
}
```
-----------------------------
2. http://localhost:8081/login - аутентификация пользователя(метод POST)
```
{
"username": "user2",
"password": "user2"
}
```
-----------------------------
3. http://localhost:8081/logout - запросы для выхода пользователя из системы(метод GET)
-----------------------------
4. http://localhost:8081/meter_readings - запрос для получения истории показаний счетчиков(метод GET)
-----------------------------
5. http://localhost:8081/meter_readings - запрос для добавления новых показаний счетчиков(метод POST)
```
{
    "typeId": 2,
    "reading": 3
}
```
-----------------------------
6. http://localhost:8081/meter_readings/last - запрос для получения последних показаний счетчиков(метод GET)
-----------------------------
7. http://localhost:8081/meter_readings/types - запрос для получения всех типов счетчиков(метод GET)
-----------------------------
8. http://localhost:8081/meter_readings/types - запрос для добавления нового типа счетчика(метод POST)
```
{
    "title":"Газ"
}
```
-----------------------------

ДЗ №3: https://github.com/A1ekseiPanov/MonitoringService/pull/3
ДЗ №4: https://github.com/A1ekseiPanov/MonitoringService/pull/4
