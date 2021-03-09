package app.nakajo.a00100.job.a00100.load.job.request.load.usage;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m_", chain = false)
public class Record {
	String m_usageHistoryId;
	String m_mediaId;
	String m_service;
	String m_usageDate;
	String m_memberId;
	String m_evaluation;
	String m_usageType;
	String m_reserve1;
	String m_reserve2;
	String m_usageWithinLast2Year;
	String m_usageWithinLast1Year;
	String m_usageWithinLast6Month;
	String m_usageWithinAfter30Day;
	String m_usageWithinAfter60Day;
	String m_usageWithinAfter90Day;
	String m_usageWithinAfter120Day;
	String m_usageWithinAfter150Day;
	String m_usageWithinAfter180Day;
	String m_usageWithinAfter1Year;
	String m_usageWithinAfter2Year;
	String m_allUsageWithinLast6Month;
	String m_allUsageWithinAfter30Day;
	String m_allUsageWithinAfter60Day;
	String m_allUsageWithinAfter90Day;
	String m_allUsageWithinAfter120Day;
	String m_allUsageWithinAfter150Day;
	String m_allUsageWithinAfter180Day;
}
