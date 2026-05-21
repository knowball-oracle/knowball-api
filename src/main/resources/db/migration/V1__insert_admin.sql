INSERT INTO knowball_users (name, email, password, role)
SELECT
    'Admin',
    'admin@knowball.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
    'ROLE_ADMIN'
FROM dual
WHERE NOT EXISTS (
    SELECT 1
    FROM knowball_users
    WHERE email = 'admin@knowball.com'
);

COMMIT;