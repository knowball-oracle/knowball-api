ALTER TABLE report ADD user_id NUMBER(19);

UPDATE report
SET user_id = (
    SELECT id
    FROM knowball_users
    WHERE email = 'admin@knowball.com'
)
WHERE user_id IS NULL;

ALTER TABLE report
    MODIFY (user_id NOT NULL);

ALTER TABLE report
    ADD CONSTRAINT fk_report_user
        FOREIGN KEY (user_id)
            REFERENCES knowball_users(id);