DROP TABLE j_request CASCADE;


CREATE TABLE j_request
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES j_job( id ) ON DELETE CASCADE,
	catalog_id				VARCHAR( 512 ),
	shopowner_id			VARCHAR( 512 ),
	prov_name				VARCHAR( 256 ),
	city					VARCHAR( 256 ),
	deleted					BOOLEAN			DEFAULT FALSE,
	created_at				TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY
	(
		id
	)
	WITH
	(
		FILLFACTOR = 100
	),
	UNIQUE
	(
		foreign_id, catalog_id
	)
)
WITH
(
	FILLFACTOR = 100
)
TABLESPACE pg_default;
