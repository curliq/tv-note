### Migrations

When changing schema, add the manual sql migration file to resources/db/migrations 
and then run `./gradlew flywayMigrate`. If that fails then run `flywayRepair` which
clears the "FAILED" status from that migration so you can run it again. Then remove all the sql lines
that ran successfully (cna move them to the previews migration file), and fix the lines that caused issues.
Run `flywayMigrate` again and repeat the process until everything succeeds.


