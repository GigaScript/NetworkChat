# Многопоточный "Сетевой чат"
___
## Описание
___
Многопоточный "Сетевой чат" демонстрирует работу Клиент Серверной архитектуры, обеспечивая возможность обмена 
простыми текстовыми сообщениями между клиентом и сервером. Ввод и вывод сообщений осуществляется
с помощью консоли. Сервер обеспечивает вывод сообщений в консоль от любого клиента, всем подключённым к 
серверу клиентам.

Приложение рассчитано на работу в локальной сети. Взаимодействия между клиентом и 
сервером реализовано по протоколу TCP, с помощью класса Socket из пакета java.net.  

После старта сервер запускает первый поток ожидающий
подключение Клиента по IP(localhost) адресу указанному в файле настроек на порту 2020.

После подключение каждого нового клиента, сервер создает новый поток ожидающий нового клиента. Поток установивший
соединение с клиентом переходит в режим ожидания трансляции данных от клиента.

Завершение работы клиента обеспечивается отправкой команды /exit. Работа клиента может быть завершена принудительно, в
 случае если сервер разорвал соединение.

Приложением предусмотрена возможность логирования сообщений и факата соединений, как на стороне сервера, так и клиента.
Логированные данные сохраняются в файле log.txt, расположенного в коревой папке с приложением. Все данные логирования 
дописываются в файл не перезаписывая его.

Для обеспечения взаимодействия между клиентом и сервером необходимо чтобы в настройках у клиента и сервера были указаны
одинаковые IP адреса и порты.

## Файл настроек
___
Файл настроек находится в корне проекта resources/setting.properties. Файл позволяет изменять 3 параметра:
* ipAddress=localhost - IP адрес для сервера и клиента.
* port=2020 - порт для обмена сообщениями.
* logPath=./src/main/java/net/chat/resources/log.txt - путь дя хранения лог файла.

Выше представлены настройки по умолчанию. Для изменения настроек необходимо менять значение после символа =.

## Схема приложения
___
![](NetworkChatSchema.jpg)