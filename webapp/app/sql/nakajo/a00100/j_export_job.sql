DROP TABLE j_export_job CASCADE;


CREATE TABLE j_export_job
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES j_job( id ) ON DELETE CASCADE,
	output_file				VARCHAR( 1024 ),
	deleted					BOOLEAN			DEFAULT FALSE,
	created_at				TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY
	(
		id
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


CREATE INDEX ON j_export_job
(
	foreign_id
)
WITH
(
	FILLFACTOR = 100
);
