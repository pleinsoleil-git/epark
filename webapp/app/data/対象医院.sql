TRUNCATE TABLE j_request CASCADE;


COPY j_request
(
	catalog_id
)
FROM 'D:/Temp/clinic.csv'
