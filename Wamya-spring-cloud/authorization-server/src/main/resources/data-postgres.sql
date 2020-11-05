/*##########################################
# INSERTING ROLES
##########################################*/
INSERT INTO role(id, name) VALUES(1, 'ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO role(id, name) VALUES(2, 'TRANSPORTER') ON CONFLICT DO NOTHING;
INSERT INTO role(id, name) VALUES(3, 'CLIENT') ON CONFLICT DO NOTHING;

/*##########################################
# INSERTING PRIVILEGES
##########################################*/
INSERT INTO privilege(id, name) VALUES(1, 'profile:read') ON CONFLICT DO NOTHING;
INSERT INTO privilege(id, name) VALUES(2, 'profile:write') ON CONFLICT DO NOTHING;
INSERT INTO privilege(id, name) VALUES(3, 'journery:read') ON CONFLICT DO NOTHING;
INSERT INTO privilege(id, name) VALUES(4, 'journery:write') ON CONFLICT DO NOTHING;
INSERT INTO privilege(id, name) VALUES(5, 'offer:read') ON CONFLICT DO NOTHING;
INSERT INTO privilege(id, name) VALUES(6, 'offer:write') ON CONFLICT DO NOTHING;


/*##########################################
# MAPPING PRIVILEGES TO TRANSPORTER ROLE
##########################################*/
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(2, 1) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(2, 2) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(2, 3) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(2, 5) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(2, 6) ON CONFLICT DO NOTHING;

/*##########################################
# MAPPING PRIVILEGES TO CLIENT ROLE
##########################################*/
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(3, 1) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(3, 2) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(3, 3) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(3, 4) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(3, 5) ON CONFLICT DO NOTHING;
INSERT INTO roles_privileges(role_id, privilege_id) VALUES(3, 6) ON CONFLICT DO NOTHING;


/*##########################################
# CREATE OAUTH_CLIENT_DETAILS TABLE
##########################################*/
CREATE TABLE IF NOT EXISTS oauth_client_details (
    client_id VARCHAR(256) PRIMARY KEY,
    resource_ids VARCHAR(256),
    client_secret VARCHAR(256),
    scope VARCHAR(256),
    authorized_grant_types VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities VARCHAR(256),
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    additional_information VARCHAR(4096),
    autoapprove VARCHAR(256)
);
    
/*##########################################
# INSERTING OAUTH CLIENT DETAILS
##########################################*/    
INSERT INTO oauth_client_details(
	client_id, 
	client_secret, 
	scope, 
	authorized_grant_types,
	web_server_redirect_uri, 
	authorities, 
	access_token_validity,
	refresh_token_validity, 
	additional_information, 
	autoapprove)

VALUES(
	'wamya-mobile-app', 
	'{noop}S3CR3T', 
	'user:read,user:write',
	'client_credentials,password,authorization_code,refresh_token', 
	'http://anywhere.com', 
	'ADMIN', 
	36000, 
	36000, 
	null, 
	true) 

ON CONFLICT DO NOTHING;



/*##########################################
# INSERTING ADMIN USER
##########################################*/   
INSERT INTO user_account(
	oauth_id, 
	firstname, 
	lastname, 
	email, 
	phone_number, 
	password, 
	is_account_non_expired, 
	is_account_non_locked, 
	is_credentials_non_expired,
	is_enabled
	) 
	VALUES(
	'd7cd23b8-991c-470f-ac63-d8fb106f391e',
	'admin',
	'admin',
	'admin@wamya.com',
	'+21628093418',
	'{noop}admin',
	true,
	true,
	true,
	true
	) 
	ON CONFLICT DO NOTHING;
	
/*##########################################
# INSERTING ADMIN USER ROLE
##########################################*/
	INSERT INTO users_roles(user_id, role_id)
	VALUES('d7cd23b8-991c-470f-ac63-d8fb106f391e', 1)
	ON CONFLICT DO NOTHING;