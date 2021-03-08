DROP TABLE tmp_repeat_report CASCADE;


CREATE UNLOGGED TABLE tmp_repeat_report
(
	id										BIGSERIAL,
	usage_month								DATE,				-- 日付
	evaluation								VARCHAR( 512 ),	-- 評価
	channel									VARCHAR( 512 ),	-- 種別
	usage_within_last_2_year				NUMERIC,			-- 過去2年以内利用回数件数
	usage_within_last_1_year				NUMERIC,			-- 過去1年以内利用回数件数
	usage_within_last_6_month				NUMERIC,			-- 過去6ヵ月利用回数件数
	usage_within_after_30_day				NUMERIC,			-- その後30日以内利用回数件数
	usage_within_after_60_day				NUMERIC,			-- その後60日以内利用回数件数
	usage_within_after_90_day				NUMERIC,			-- その後90日以内利用回数件数
	usage_within_after_120_day				NUMERIC,			-- その後120日以内利用回数件数
	usage_within_after_150_day				NUMERIC,			-- その後150日以内利用回数件数
	usage_within_after_180_day				NUMERIC,			-- その後180日以内利用回数件数
	usage_within_after_1_year				NUMERIC,			-- その後1年以内利用件数
	usage_within_after_2_year				NUMERIC,			-- その後2年以内利用件数
	all_usage_within_last_6_month			NUMERIC,			-- （ALL）過去6ヵ月利用件数
	all_usage_within_after_30_day			NUMERIC,			-- （ALL）その後30日以内利用件数
	all_usage_within_after_60_day			NUMERIC,			-- （ALL）その後60日以内利用件数
	all_usage_within_after_90_day			NUMERIC,			-- （ALL）その後90日以内利用件数
	all_usage_within_after_120_day			NUMERIC,			-- （ALL）その後120日以内利用件数
	all_usage_within_after_150_day			NUMERIC,			-- （ALL）その後150日以内利用件数
	all_usage_within_after_180_day			NUMERIC,			-- （ALL）その後180日以内利用件数
	created_at								TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP
)
WITH
(
	FILLFACTOR = 100
)
TABLESPACE pg_default;
