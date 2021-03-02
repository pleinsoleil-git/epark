DROP TABLE j_export_job_status CASCADE;


CREATE TABLE j_export_job_status
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES j_export_job( id ) ON DELETE CASCADE,
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
