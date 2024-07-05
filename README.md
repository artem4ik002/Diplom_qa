## Порядок запуска Дипломного проекта профессии "Тестировщик". 

1. Запустить Docker Desktop

2. Открыть тестовый проект в IntelliJ IDEA

3. В терминале IntelliJ IDEA выполнить команду `docker-compose up`, дождаться подъема контейнеров (*около 1 минуты*).

4. В терминале IntelliJ IDEA выполнить команду для запуска приложения:
- для MySQL:
 `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar`
 
- для Postgres:
`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`


5. В build.gradle выбрать адрес БД следующим образом: 
 

- для MySQL:
 `systemProperty 'datasource', System.getProperty ('datasource', 'jdbc:mysql://localhost:3306/app')`


- для Postgres:
 `systemProperty 'datasource', System.getProperty ('datasource', 'jdbc:postgresql://localhost:5432/app')`


6. В терминале IntelliJ IDEA выполнить команду для прогона автотестов: 

- для MySQL:
` ./gradlew clean test -Durl=jdbc:mysql://localhost:3306/app 
- для Postgres:
`./gradlew clean test -Durl=jdbc:postgresql://localhost:5432/app 

7. В терминале IntelliJ IDEA выполнить команду для получения отчета:
`.\gradlew allureServe `

**После завершения прогона автотестов и получения отчета:**
- Завершить обработку отчета сочетанием клавиш `CTRL + C`, в терминале нажать клавишу `Y`, нажать `Enter`.
- Закрыть приложение сочитанием клавиш `CTRL + C` в терминале запуска.
- Остановить работу контейнеров командой `docker-compose down`.

## Ссылки на документацию:
- [1) Дипломное задание](https://github.com/netology-code/qa-diploma/blob/master/README.md)
- [2) План автоматизации](https://github.com/artem4ik002/Diplom_qa/blob/main/docs/Plan.md)
- [3) Отчет по итогам тестрования](https://github.com/artem4ik002/Diplom_qa/blob/main/docs/Report.md)
- [4) Отчет о проведенной автоматизации](https://github.com/artem4ik002/Diplom_qa/blob/main/docs/Summary.md)

