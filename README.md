# Monitoring Service

Monitoring Service - это приложение для для подачи показаний счетчиков пользователей.

## Запуск приложения

1. Склонируйте репозиторий:
   git clone git@github.com:A1ekseiPanov/MonitoringService.git
2. Откройте проект в среде разработки.
3. Упаковываем проект в пакут war Build->Build Artifacts-> Monitoring-Service:war->Build
4. Создание и запуск контейнеров(файл docker-compose.yml).
-----------------------------

## Данные для тестирования:
### Запросы для User:
1. http://localhost:8080/registration - регистрация нового пользователя(метод POST)
```
{
"username": "user1234",
"password": "user1234"
}
```
-----------------------------
2. http://localhost:8080/login - аутентификация пользователя(метод POST)
```
{
"username": "user1",
"password": "user1"
}
```
-----------------------------
3. http://localhost:8080/logout - запрос для выхода пользователя из системы(метод GET)
-----------------------------
4. http://localhost:8080/meter_readings - запрос для получения истории показаний счетчиков(метод GET)
-----------------------------
5. http://localhost:8080/meter_readings - запрос для добавления новых показаний счетчиков(метод POST)
```
{
    "typeId": 2,
    "reading": 3
}
```
-----------------------------
6. http://localhost:8080/meter_readings/last - запрос для получения последних показаний счетчиков(метод GET)
-----------------------------
7. http://localhost:8080/meter_readings/types - запрос для получения всех типов счетчиков(метод GET)
-----------------------------
8. http://localhost:8080/meter_readings/date?month=1&year=2024 - запрос для получения всех типов счетчиков(метод GET)
-----------------------------

### Запросы для Admin:

Данные для аутенфтификации:
```
{
"username": "admin",
"password": "admin"
}
```
-----------------------------
1. http://localhost:8080/admin/meter_readings - запрос для получения показаний счетчиков всех пользователей(метод GET)
-----------------------------
2. http://localhost:8080/admin/audit - запрос для журнала действий пользоватетей(метод GET)
-----------------------------
3. http://localhost:8081/admin/meter_readings/types - запрос для добавления нового типа счетчика(метод POST)
```
{
    "title":"Газ"
}
```
-----------------------------

ДЗ №3: https://github.com/A1ekseiPanov/MonitoringService/pull/3
ДЗ №4: https://github.com/A1ekseiPanov/MonitoringService/pull/4
