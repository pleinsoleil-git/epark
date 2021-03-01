DROP TABLE j_job CASCADE;


CREATE TABLE j_job
(
	id						BIGSERIAL,
	thread_nums				NUMERIC			DEFAULT 1,
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
