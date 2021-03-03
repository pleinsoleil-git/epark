CREATE OR REPLACE FUNCTION pg_catalog.add_months
(
	TIMESTAMP WITH TIME ZONE
) RETURNS date AS
$$
declare
begin
	RETURN DATE( $1 + '1 month' );
end;
$$
LANGUAGE plpgsql
