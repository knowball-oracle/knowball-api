ALTER TABLE team
    ADD logo_url VARCHAR2(500 CHAR);

COMMENT ON COLUMN team.logo_url IS
    'URL do escudo do time, obtida via sincronização com a API TheSportsDB.';