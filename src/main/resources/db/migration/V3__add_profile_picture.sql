ALTER TABLE knowball_users
    ADD profile_picture CLOB DEFAULT NULL;

COMMENT ON COLUMN knowball_users.profile_picture IS 'Foto de perfil do usuário em Base64';

COMMIT;