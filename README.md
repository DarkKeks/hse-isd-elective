# Бэкенд сервиса конвертации видео форматов.

[![Build Status](https://travis-ci.com/DarkKeks/hse-isd-elective.svg?branch=master)](https://travis-ci.com/DarkKeks/hse-isd-elective)

Задание факкультатива [Методы разработки программного обеспечения](http://wiki.cs.hse.ru/ISDElective)

Веб фреймворк &mdash; Spring MVC, для конвертации используется обертка над ffmpeg &mdash; [Jave2](https://github.com/a-schild/jave2), для хранения лога операций используется mongodb

---

Готовый сервер запущен [здесь](https://isd.darkkeks.live/convert)

## Сборка и запуск

1. ### Docker
    Для запуска понадобится docker и docker-compose:
    ```shell script
    docker-compose up -d
    ```
    
    Либо без docker-compose:
    
    ```shell script
    docker build -t darkkeks/isd-backend .
    docker run --rm -d -p 27017:27017 -v mongo-data:/data/db mongo
    docker run --rm -p 8080:8080 darkkeks/isd-backend
    ```
    
    Можно так же задать переменную окружения `MONGODB_URL` чтобы указать путь к mongodb (по умолчанию `mongodb://localhost/app`):
    
    ```shell script
    docker run --rm -p 8080:8080 -e MONGODB_URI=mongodb://user:pass@host/db darkkeks/isd-backend
    ```

2. ### Своя jdk и mongodb

    Для сборки и запуска необходима jdk11 (или выше, скачать можно [сборку openjdk](https://jdk.java.net/java-se-ri/11), [сборку Oracle](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html), либо через пакетный менеджер вашего дистрибутива) и [mongodb](https://www.mongodb.com/download-center/community):
    #### Arch
    ```shell script
    # openjdk
    pacman -S jdk11-openjdk 
   
    # mongodb
    yay -S mongodb-bin  # или любой другой aur helper
    sudo systemctl start mongodb 
    ```
    #### Ubuntu
    (Не проверено, взято [отсюда](https://stackoverflow.com/a/52950746))
    ```shell script
    # openjdk
    sudo add-apt-repository ppa:openjdk-r/ppa \
    && sudo apt-get update -q \
    && sudo apt install -y openjdk-11-jdk
    
    # mongodb
    sudo apt install -y mongodb
    sudo systemctl start mongodb
    ```
    
    #### После установки jdk11 и запуска mongodb:
    ```shell script
    ./gradlew build  # или gradlew.bat на windows
    java -jar build/libs/isd-elective-backend-*.jar
    ```
    
    Можно так же задать переменную окружения `MONGODB_URI` (`set MONGODB_URI=mongodb://user:pass@host/db` для Windows или `export MONGODB_URI=mongodb://user:pass@host/db` для Linux)

## API Сервиса

-   `GET /convert/formats`
    
    Возвращает список поддерживаемых форматов, сервис гарантированно умеет конвертировать каждый из этих форматов в любой другой.
    Так же поддерживаются многие другие входные форматы, но корректность конвертации из них не гарантируется.
    
    ```json5
    [
      {
        "name": "flv",
        "encoderName": "flv",
        "extension": ".flv"
      },
      {
        "name": "mp4",
        "encoderName": "mp4",
        "extension": ".mp4"
      },
      {
        "name": "mkv",
        "encoderName": "matroska",
        "extension": ".mkv"
      },
      {
        "name": "mov",
        "encoderName": "mov",
        "extension": ".mov"
      }
    ]
    ```   

-   `POST /convert/{format}`

    `Content-Type: multipart/form-data`
    
    Наобходимо передать файл, который надо сконвертировать параметром с именем `file`
    
    При успешной конвертации возвращается `200` и файл результата.
    
    Иначе, `application/json` с информацией об ошибке, например
    
    ```json5
    {
      "timestamp": "2019-10-29T04:42:37.074+0000",
      "status": 500,
      "error": "Internal Server Error",
      "message": "Service is currently busy, try later",
      "path": "/convert/flv"
    }
    ``` 

## TODO
- [X] Базовая функциональность
- [X] Лог операций
- [ ] Временное хранилище сконвертированных файлов
- [X] Ограничения
    - [X] Ограничить кол-во одновременных операций конвертации
    - [X] Ограничение по времени
- [ ] Тесты 