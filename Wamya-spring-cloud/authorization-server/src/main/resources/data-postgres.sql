/*##########################################
# INSERTING ROLES
##########################################*/
INSERT INTO role(id, name, authorities) VALUES(1, 'ADMIN', 'journey:read,journey:write,offer:read,offer:write,profile:read,profile:write,vehicule:read,vehicule:write') ON CONFLICT DO NOTHING;
INSERT INTO role(id, name, authorities) VALUES(2, 'TRANSPORTER', 'journey:read,offer:read,offer:write,profile:read,profile:write,vehicule:read,vehicule:write') ON CONFLICT DO NOTHING;
INSERT INTO role(id, name, authorities) VALUES(3, 'CLIENT', 'journey:read,journey:write,offer:read,profile:read,profile:write,vehicule:read') ON CONFLICT DO NOTHING;

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
	'journey:read,journey:write,offer:read,offer:write,profile:read,profile:write,vehicule:read,vehicule:write',
	'client_credentials,password,authorization_code,refresh_token', 
	'http://anywhere.com', 
	'ROLE_USER', 
	36000, 
	36000, 
	'{"type":"Mobile App Client"}', 
	true)

ON CONFLICT DO NOTHING;

INSERT INTO oauth_client_details(client_id, client_secret) VALUES('wamya-backend-resource-server', '{noop}S3CR3T') ON CONFLICT DO NOTHING;

