## Testando perfil dev com Postgresql no Docker
```
docker run -p 5432:5432 --name ms-user-pg12 --network rh-net -e POSTGRES_PASSWORD=1234567 -e POSTGRES_DB=db_ms_user postgres:12-alpine
```