# Shareit

### Описание проекта

Restful API back-end сервис для шеринга вещей.

#### Основные особенности проекта:

- Разработан с использованием фреймворка Spring Boot;
- База данных H2;
- 16 End-points доступных для управления данными.

## Содержание:

1. [Стэк проекта](#стэк-проекта)
2. [Функционал](#функционал)
3. [ER-diagram](#er-diagram)
4. [Ход проекта](#ход-проекта)

## Стэк проекта

- Java 11, Spring Boot, Maven, Lombok, SpringBootTest, Postman
- База данных: [schema.sql](src/main/resources/schema.sql)
- Зависимости: [pom.xml](pom.xml)
- Тесты: [tests](.postman)

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

### ER-diagram

![](src/main/resources/ER-diagram.png)


## Ход проекта

#### График проекта

| Спринт    | Этап | Описание этапов                    | Отметка |
|-----------|----|------------------------------------|---------|
| Spring 13 | 1  | user, items entity and controllers | +       |
| Spring 14 | 2  | bookings and comments              |         |
| Spring 15 | 3  | requests and mok-tests             |         |
| Spring 16 | 4  | ???                                |         |
