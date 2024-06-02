# Shareit

### Описание проекта

Restful API back-end сервис для шеринга вещей.

#### Основные особенности проекта:

- Разработан с использованием фреймворка Spring Boot;
- База данных h2database. Общение с БД на Query Methods и JPQL;
- 20 End-points доступных для управления данными.

## Содержание:

1. [Стэк проекта](#стэк-проекта)
2. [Функционал](#функционал)
3. [ER-diagram](#er-diagram)
4. [Ход проекта](#ход-проекта)
5. [Инструкция](#пошаговая-инструкция-по-установке-и-запуску-проета)

## Стэк проекта

- Java 11, Spring Boot, Maven, Lombok, JPQL, QueryDSL, Mockito, Postman
- База данных: [schema.sql](server/src/main/resources/schema.sql)
- Зависимости: [pom.xml](pom.xml)
- Тесты: [tests](postman)

## Функционал

### EndPoints:

#### Пользователи (users):
+ GET /users - Получить всех пользователей;
+ GET /users/{id} - Получить пользователя по ID;
+ POST /users - Добавить пользователя;
+ PATCH /users/{userId} - Обновить пользователя по ID;
+ DELETE /users/{userId} - Удалить пользователя по ID.

#### Вещи (items):
+ GET /items - Получить все вещи;
+ GET /items/{itemId} - Получить вещь по ID;
+ GET /items/search - Найти вещь;
+ POST /items - Добавить вещь;
+ PATCH /items/{itemId} - Обновить вещь по ID;

#### Комментарии к вещи (comments):
+ POST /items/{itemId}/comment - Добавить комментарий к вещи по ID.

#### Бронирование (bookings):
+ GET /bookings/{bookingId} - Получить данные о бронировании по ID (включая его статус);
+ GET /bookings?state={state} - Получить списк бронирований для всех вещей текущего пользователя; 
  - Может принимать значения state: ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED (state default: "ALL")
+ GET /bookings/owner?state={state} - Получить списк бронирований для всех вещей текущего владельца;
  - Может принимать значения state: ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED (state default: "ALL")
+ POST /bookings - Добавить вещь;
+ PATCH /bookings/{bookingId}?approved={approved} - Подтвердить или отклонить запрос на бронирование вещи.
  - Может принимать значения approved: true, false

#### Запросы на вещи (item_requests):
+ GET /requests/from={}&size={} - Получить список личных запросов на вещи;
+ GET /requests/{requestId} - Получить запросе на вещь;
+ GET /requests/all?from={from}&size={size} - Получить список запросов, созданных другими пользователями 
(from - страница, size - количество записей);
+ POST /requests - Добавить новый запрос на вещь;

### ER-diagram

![](server/src/main/resources/ER-diagram.png)

## Ход проекта

#### График проекта

| Спринт    | Этап | Описание этапов                    | Отметка |
|-----------|----|------------------------------------|---------|
| Spring 13 | 1  | user, items entity and controllers | +       |
| Spring 14 | 2  | bookings and comments              | +       |
| Spring 15 | 3  | requests and mok-tests             |         |
| Spring 16 | 4  |                                    |         |


## Пошаговая инструкция по установке и запуску проета

1. Установите Git: Если у вас еще не установлен Git, загрузите и установите его с официального сайта
   Git: https://git-scm.com/.
2. Клонируйте репозиторий: Откройте командную строку или терминал и выполните команду клонирования для репозитория
   GitHub. Например:

```
git clone https://github.com/KoryRunoMain/java-shareit.git
```

3. Откройте проект в IDE: Откройте вашу среду разработки (IDE), такую как IntelliJ IDEA, Eclipse или NetBeans.
4. Импортируйте проект как Maven проект: Если вы используете IntelliJ IDEA,
   выберите File -> Open и выберите папку, в которую был склонирован репозиторий.
   IntelliJ IDEA должна автоматически распознать проект как Maven проект и импортировать его.
   В Eclipse вы можете выбрать File -> Import -> Existing Maven Projects и выбрать корневую папку проекта.
   В NetBeans вы можете выбрать File -> Open Project и выбрать папку проекта.
5. Запустите приложение: точка входа находится в классе [SharItApp](server/src/main/java/ru/practicum/shareit/ShareItApp.java) помеченном аннотацией
   @SpringBootApplication.
   Либо запустить через Maven:

```
mvn spring-boot:run
```