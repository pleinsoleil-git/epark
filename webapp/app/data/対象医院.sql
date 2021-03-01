TRUNCATE TABLE i_clinic CASCADE;


COPY i_clinic
(
	catalog_id
)
FROM 'D:/Temp/clinic.csv'


TRUNCATE TABLE j_request CASCADE;


WITH s_params AS
(
	SELECT 27::BIGINT AS job_id
)
INSERT INTO j_request
(
	foreign_id,
	catalog_id,
	shopowner_id,
	deleted
)
SELECT t10.job_id,
	t20.catalog_id,
	m10.shopowner_id,
	TRUE
FROM s_params AS t10
CROSS JOIN i_clinic AS t20
INNER JOIN m_clinic AS m10
	ON m10.foreign_id = t10.job_id
	AND m10.catalog_id = t20.catalog_id
ORDER BY t20.id;
