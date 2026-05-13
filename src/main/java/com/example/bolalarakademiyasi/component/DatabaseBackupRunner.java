package com.example.bolalarakademiyasi.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseBackupRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // 1. DROP SCHEMA
        ProcessBuilder drop = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\18\\bin\\psql.exe",
                "-U", "postgres",
                "-d", "bolalar_academy",
                "-c", "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
        );
        drop.environment().put("PGPASSWORD", "root123");

        Process p1 = drop.start();
        int dropCode = p1.waitFor();
        System.out.println("Drop schema exit code: " + dropCode);

// 2. RESTORE
        ProcessBuilder restore = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\18\\bin\\psql.exe",
                "-U", "postgres",
                "-d", "bolalar_academy",
                "-f", "backup.sql"
        );
        restore.environment().put("PGPASSWORD", "root123");

        Process p2 = restore.start();
        int restoreCode = p2.waitFor();

        System.out.println("Restore finished. Exit code: " + restoreCode);

    }
}
