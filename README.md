# user-updater

### Local development
```bash
# Run PostgreSQL
docker run --name kuzds-postgres -p 5432:5432 -e POSTGRES_USER=test -e POSTGRES_PASSWORD=test -e POSTGRES_DB=test_db -d postgres:14-alpine
```