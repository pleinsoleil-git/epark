CREATE OR REPLACE FUNCTION pg_catalog.add_months
(
	TIMESTAMP WITH TIME ZONE,
	INT
) RETURNS date AS
$$
declare
begin
	RETURN DATE( $1 + ( $2::VARCHAR || ' month' )::INTERVAL );
end;
$$
LANGUAGE plpgsql
