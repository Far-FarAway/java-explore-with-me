# Explore with me

Приложение для поиска событий по интересам. 

Пользователи могут создавать собственные события или находить новые по интересам, используя фильтры и комментировать их.

Также в проекте реализован микросервис по обработке статистики просмотров каждого события, обращение к которому
происходит внутри основного сервера приложения.

Стек технологий: java, spring boot, hibernate, maven, postgreSQL, docker, postman.

### Установка
 Сначала выполнить в командной строке команды 

```
cd 'папка, где будет располагаться приложение'
git clone https://github.com/Far-FarAway/java-explore-with-me.git
```

Следом перейти в склонированный репозиторий и выполнить команду докера 
```
cd java-explore-with-me
docker compose up --build
```