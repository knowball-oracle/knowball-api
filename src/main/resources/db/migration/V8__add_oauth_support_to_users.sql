ALTER TABLE knowball_users
    MODIFY (password NULL);

ALTER TABLE knowball_users
    ADD auth_provider VARCHAR2(20 CHAR) DEFAULT 'LOCAL' NOT NULL;

ALTER TABLE knowball_users
    ADD google_id VARCHAR2(255 CHAR);

ALTER TABLE knowball_users
    ADD CONSTRAINT ck_knowball_users_provider
        CHECK (auth_provider IN ('LOCAL', 'GOOGLE'));

ALTER TABLE knowball_users
    ADD CONSTRAINT uk_knowball_users_google_id UNIQUE (google_id);

COMMENT ON COLUMN knowball_users.auth_provider IS 'Origem da conta: LOCAL (e-mail/senha) ou GOOGLE (OAuth2)';
COMMENT ON COLUMN knowball_users.google_id IS 'Subject (sub) do token do Google, usado para vincular a conta';