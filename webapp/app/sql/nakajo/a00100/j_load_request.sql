DROP TABLE j_load_request CASCADE;


CREATE TABLE j_load_request
(
	id						BIGSERIAL,
	foreign_id				BIGINT				REFERENCES j_load_job( id ) ON DELETE CASCADE,
	request_type			VARCHAR( 512 ),	-- USAGE：REQUEST
	data_type				VARCHAR( 512 ),	-- VOC：VOCデータ（メールアドレスのみ）：Silent
	input_file				VARCHAR( 1024 ),
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


CREATE INDEX ON j_load_request
(
	foreign_id
)
WITH
(
	FILLFACTOR = 100
);
