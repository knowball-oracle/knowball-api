UPDATE knowball_users
SET password = '$2a$12$f2tlARC8s8k0siwB5CVy7O9GIg9jghbhuqZPDxHiI8sHoCYMEm6gO'
WHERE email = 'admin@knowball.com';

COMMIT;