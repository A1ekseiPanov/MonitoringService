Monitoring Service
-----------------------------

Проект сделан в рамках прохождения обучения на платформе [Ylab.](https://learning-platform.ylab.website/)

Monitoring Service - это приложение для подачи показаний счетчиков пользователей.

## Описание
Показания можно подавать один раз в месяц.
Ранее поданые показания редактировать запрещено.
Последние поданые показания считаются актуальными.
Пользователь может видеть только свои показания, администратор может видеть показания всех пользователей.

Stack: Java 17, Spring Boot 3.2.0, JDBC, Spring Security, Spring Validator, Spring AOP,
 Lombok, Swagger/Open API, Docker, Luiqbase, Test-containers, MapStruct
## Запуск приложения

1. Склонируйте репозиторий:
   git clone git@github.com:A1ekseiPanov/MonitoringService.git
2. Откройте проект в среде разработки.
3. Создание и запуск контейнеров(файл docker-compose.yml)
4. Запуск приложения.
-----------------------------

### [REST API documentation](http://localhost:8080/swagger-ui/index.html)