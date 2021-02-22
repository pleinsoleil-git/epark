DROP TABLE j_request_status CASCADE;


CREATE TABLE j_request_status
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES j_request( id ) ON DELETE CASCADE,
	status					NUMERIC,
	message					VARCHAR,
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
		foreign_id, status
	)
	WITH
	(
		FILLFACTOR = 100
	)
)
WITH
(
	FILLFACTOR = 100
)
TABLESPACE pg_default;
