package app.nakajo.a00100.job.a00100.load.job.request.load;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import app.nakajo.a00100.job.a00100.load.job.request.Request;
import common.lang.StringUtils;
import lombok.val;

public class Reader implements AutoCloseable {
	int m_rowNum = 1;
	DataFormatter m_formatter;

	String getCellValue(final Cell cell) throws Exception {
		if (m_formatter == null) {
			m_formatter = new DataFormatter();
		}

		return StringUtils.trim(m_formatter.formatCellValue(cell));
	}

	Record read() throws Exception {
		val request = Request.getCurrent();
		val sheet = request.getSheet();
		val row = sheet.getRow(m_rowNum++);

		if (row != null) {
			return new Record() {
				{
					for (val cell : row) {
						val value = getCellValue(cell);

						switch (cell.getColumnIndex()) {
						case 0:
							m_usageHistoryId = value;
							break;
						case 1:
							m_mediaId = value;
							break;
						case 2:
							m_service = value;
							break;
						case 3:
							m_usageDate = value;
							break;
						case 4:
							m_memberId = value;
							break;
						case 5:
							m_evaluation = value;
							break;
						case 6:
							m_usageType = value;
							break;
						case 7:
							m_reserve1 = value;
							break;
						case 8:
							m_reserve2 = value;
							break;
						case 9:
							m_usageWithinLast2Year = value;
							break;
						case 10:
							m_usageWithinLast1Year = value;
							break;
						case 11:
							m_usageWithinLast6Month = value;
							break;
						case 12:
							m_usageWithinAfter30Day = value;
							break;
						case 13:
							m_usageWithinAfter60Day = value;
							break;
						case 14:
							m_usageWithinAfter90Day = value;
							break;
						case 15:
							m_usageWithinAfter120Day = value;
							break;
						case 16:
							m_usageWithinAfter150Day = value;
							break;
						case 17:
							m_usageWithinAfter180Day = value;
							break;
						case 18:
							m_usageWithinAfter1Year = value;
							break;
						case 19:
							m_usageWithinAfter2Year = value;
							break;
						case 20:
							m_allUsageWithinLast6Month = value;
							break;
						case 21:
							m_allUsageWithinAfter30Day = value;
							break;
						case 22:
							m_allUsageWithinAfter60Day = value;
							break;
						case 23:
							m_allUsageWithinAfter90Day = value;
							break;
						case 24:
							m_allUsageWithinAfter120Day = value;
							break;
						case 25:
							m_allUsageWithinAfter150Day = value;
							break;
						case 26:
							m_allUsageWithinAfter180Day = value;
							break;
						default:
							break;
						}
					}
				}
			};
		}

		return null;
	}

	@Override
	public void close() throws Exception {
	}
}
