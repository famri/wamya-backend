INSERT INTO international_calling_code(id, value, country_name, flag_path, enabled) VALUES(1, '+216', 'TUNISIA', 'assets/images/icons/tunisia.png', TRUE) ON CONFLICT DO NOTHING;
INSERT INTO international_calling_code(id, value, country_name, flag_path, enabled) VALUES(2, '+33', 'FRANCE', 'assets/images/icons/france.png', FALSE) ON CONFLICT DO NOTHING;