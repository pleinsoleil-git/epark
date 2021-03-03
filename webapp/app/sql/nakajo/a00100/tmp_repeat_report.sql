DROP TABLE tmp_repeat_report CASCADE;


CREATE TEMPORARY TABLE tmp_repeat_report
(
	id										BIGSERIAL,
	data_type								VARCHAR( 512 ),	-- VOC：VOCデータ（メールアドレスのみ）：Silent
	service									VARCHAR( 512 ),	-- サービス
	usage_month								DATE,				-- 日付
	evaluation								VARCHAR( 512 ),	-- 評価
	usage_type								VARCHAR( 512 ),	-- 種別
	usage_within_last_2_year_count			NUMERIC,			-- 過去2年以内利用回数件数
	usage_within_last_1_year_count			NUMERIC,			-- 過去1年以内利用回数件数
	usage_within_last_6_month_count			NUMERIC,			-- 過去6ヵ月利用回数件数
	usage_within_after_30_day_count			NUMERIC,			-- その後30日以内利用回数件数
	usage_within_after_60_day_count			NUMERIC,			-- その後60日以内利用回数件数
	usage_within_after_90_day_count			NUMERIC,			-- その後90日以内利用回数件数
	usage_within_after_120_day_count		NUMERIC,			-- その後120日以内利用回数件数
	usage_within_after_150_day_count		NUMERIC,			-- その後150日以内利用回数件数
	usage_within_after_180_day_count		NUMERIC,			-- その後180日以内利用回数件数
	usage_within_after_1_year_count			NUMERIC,			-- その後1年以内利用件数
	usage_within_after_2_year_count			NUMERIC,			-- その後2年以内利用件数
	all_usage_within_last_6_month_count		NUMERIC,			-- （ALL）過去6ヵ月利用件数
	all_usage_within_after_30_day_count		NUMERIC,			-- （ALL）その後30日以内利用件数
	all_usage_within_after_60_day_count		NUMERIC,			-- （ALL）その後60日以内利用件数
	all_usage_within_after_90_day_count		NUMERIC,			-- （ALL）その後90日以内利用件数
	all_usage_within_after_120_day_count	NUMERIC,			-- （ALL）その後120日以内利用件数
	all_usage_within_after_150_day_count	NUMERIC,			-- （ALL）その後150日以内利用件数
	all_usage_within_after_180_day_count	NUMERIC			-- （ALL）その後180日以内利用件数
)
WITH
(
	FILLFACTOR = 100
)
TABLESPACE pg_default;
