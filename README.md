# HotelExplorer

## Описание

HotelExplorer — RESTful API для работы с отелями.  
Реализует методы, требуемые в тестовом задании.

- **Технологии:** Java 17, Spring Boot (Web, Data JPA, Validation, Test), Liquibase, H2, Lombok, MapStruct, Springdoc OpenAPI (Swagger UI), Maven
- **Порт:** 8092
- **Префикс всех методов:** `/property-view`
- **Swagger:** [http://localhost:8092/swagger-ui/index.html](http://localhost:8092/swagger-ui/index.html)
- **Unit-тесты:** покрывают основные компоненты приложения, запуск — через `mvn test`

---

## Запуск приложения

1. Склонируйте репозиторий
2. Соберите и запустите проект:
   ```
   mvn spring-boot:run
   ```
3. Приложение будет доступно по адресу:  
   [http://localhost:8092](http://localhost:8092)

> Для смены базы данных (например, на MySQL/PostgreSQL) — измените настройки в `application.properties` и перезапустите приложение.

---

## Реализованные REST API методы

### 1. Получить список всех отелей  
`GET /property-view/hotels`

**Пример ответа:**
```json
[
  {
    "id": 1,
    "name": "DoubleTree by Hilton Minsk",
    "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375 17 309-80-00"
  }
]
```

---

### 2. Получить расширенную информацию об отеле  
`GET /property-view/hotels/{id}`

**Пример ответа:**
```json
{
  "id": 1,
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  },
  "amenities": [
    "Free parking",
    "Free WiFi",
    "Non-smoking rooms",
    "Concierge",
    "On-site restaurant",
    "Fitness center",
    "Pet-friendly rooms",
    "Room service",
    "Business center",
    "Meeting rooms"
  ]
}
```

---

### 3. Поиск отелей  
`GET /property-view/search?city=minsk&brand=Hilton&amenities=Free WiFi`

**Поиск по параметрам:** name, brand, city, country, amenities  
**Пример ответа:**
```json
[
  {
    "id": 1,
    "name": "DoubleTree by Hilton Minsk",
    "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375 17 309-80-00"
  }
]
```

---

### 4. Создать новый отель  
`POST /property-view/hotels`

**Пример запроса:**
```json
{
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  }
}
```
**Пример ответа:**
```json
{
  "id": 1,
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
  "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
  "phone": "+375 17 309-80-00"
}
```

---

### 5. Добавить список amenities к отелю  
`POST /property-view/hotels/{id}/amenities`

**Пример запроса:**
```json
[
  "Free parking",
  "Free WiFi",
  "Non-smoking rooms",
  "Concierge",
  "On-site restaurant",
  "Fitness center",
  "Pet-friendly rooms",
  "Room service",
  "Business center",
  "Meeting rooms"
]
```

---

### 6. Получить гистограмму по параметру  
`GET /property-view/histogram/{param}`  
**Параметр:** brand, city, country, amenities

**Пример для city:**
```json
{
  "Minsk": 1,
  "Moscow": 2,
  "Mogilev": 1
}
```
**Пример для amenities:**
```json
{
  "Free parking": 1,
  "Free WiFi": 20,
  "Non-smoking rooms": 5,
  "Fitness center": 1
}
```

---

## Swagger-документация

Документация доступна по адресу:  
[http://localhost:8092/swagger-ui/index.html](http://localhost:8092/swagger-ui/index.html)

---

## Unit-тесты

В проекте реализованы unit-тесты для ключевых компонентов.  
Для запуска тестов выполните:
```
mvn test
```

---

## Примечания

- В приложении реализованы только методы, описанные в тестовом задании.
- Все методы API имеют общий префикс `/property-view`.

---

**Удачного тестирования!**
