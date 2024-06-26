## Отчет по итогам автоматизированного тестирования
### Выполнено:
* Проведено автоматизированное UI-тестирование всех запланированных позитивных и негативных сценариев для обоих вариантов взаимодействия с сервисом: "Оплата по карте" и "Кредит по данным карты"
* Проведено тестирование API сервиса для оплаты покупки тура с использованием Postman
* Проиведена корректная настройка работы БД MySQL и PostgeSQL с SUT
* Составлен отчет по итогам автоматизации
* Составлены 11 issues по найденным дефектам
___

### Сработавшие риски
* Из-за отсутствия документации по проекту было трудно догадаться, какой именно будет ответ при том или ином запросе
* В связи с необходимостью поддержки двух БД возникла сложность в запуске приложения и правильной передаче параметров при запуске приложения и при запуске авто-тестов.

### Причины, по которым что-то не было сделано

* Не получилось интегрировать систему с Appveyor CI, поскольку это оказалось слишком сложным и потребовало бы большего количества времени, чем запланировано.
___

### Общий итог по времени
1. Настройка SUT: запланировано 10 часов, фактически - 10. 
2. Разработка тест-плана: запланировано 13 часов, фактически - 13 часов. 
3. Написание авто-тестов и их прогон: запланировано 70 часов, фактически - 70 часов. 
4. Написание баг-репортов: запланировано 10 часов, фактически - 8 часа. 
5. Написание отчета по результатам автоматизации: запланировано 5 часов, фактически 3 часа. 
6. Настройка CI: 10 часов, фактически - 0

Итого: запланировано 118 ч., фактически 104 ч.
