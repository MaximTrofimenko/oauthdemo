CREATE SCHEMA IF NOT EXISTS public;

DROP TABLE IF EXISTS visitor;

CREATE TABLE IF NOT EXISTS visitor(
    id UUID NOT NULL UNIQUE CONSTRAINT PK_visitor PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL ,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

INSERT INTO public.visitor (id, username, password, role) VALUES ('8aec477d-3b48-4425-8500-2d5c6996ec54', 'geekadmin', '$2a$08$RYZi2/Wt09ikVJcVwHhGPeAgFhMjTP2iw1MyhJAqPEamHDjE3Uqz2', 'ROLE_ADMIN');
INSERT INTO public.visitor (id, username, password, role) VALUES ('63e88e20-9fce-4f3c-b61a-0481775bf654', 'geekuser', '$2a$08$E/LV5wAViDqtu0EU6Z7IXudisw23zjsiYjT83ARGAum0RHxRNi1ai', 'ROLE_USER');

CREATE TABLE IF NOT EXISTS oauth_client_details (
    client_id VARCHAR(255) NOT NULL CONSTRAINT oauth_client_details_pkey PRIMARY KEY,
    resource_ids VARCHAR(255),
    client_secret VARCHAR(255),
    scope VARCHAR(255),
    authorized_grant_types VARCHAR(255),
    web_server_redirect_uri VARCHAR(255),
    authorities VARCHAR(255),
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    additional_information VARCHAR(4096),
    autoapprove VARCHAR(255)
);

INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('geekbrains-client', 'oauth-demo-api', '$2a$08$p/UbxR8tZHgMU84mj78.1ujBDAq4lb2Wii755vCfWxUY71jcE/vgy', 'guest', 'password,refresh_token,client_credentials', null, 'ROLE_ADMIN', 10000000, 10000000, null, null);

CREATE TABLE IF NOT EXISTS oauth_access_token (
    token_id VARCHAR(255),
    token bytea,
    authentication_id VARCHAR(255) NOT NULL CONSTRAINT PK_oauth_access_token_pkey PRIMARY KEY,
    user_name VARCHAR(255),
    client_id VARCHAR(255) CONSTRAINT FK_oauth_access_token_client_details REFERENCES oauth_client_details,
    authentication bytea,
    refresh_token VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
    token_id VARCHAR(255),
    token bytea,
    authentication bytea
);