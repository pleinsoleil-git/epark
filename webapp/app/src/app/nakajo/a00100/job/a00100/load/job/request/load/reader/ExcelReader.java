package app.nakajo.a00100.job.a00100.load.job.request.load.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import app.nakajo.a00100.job.a00100.load.job.request.Request;
import app.nakajo.a00100.job.a00100.load.job.request.load.Record;
import common.lang.StringUtils;
import lombok.val;

class ExcelReader extends Reader {
	int m_rowNum = 1;
	DataFormatter m_formatter;

	String getCellValue(final Cell cell) throws Exception {
		if (m_formatter == null) {
			m_formatter = new DataFormatter();
		}

		return StringUtils.trim(m_formatter.formatCellValue(cell));
	}

	@Override
	public Record read() throws Exception {
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
							setUsageHistoryId(value);
							break;
						case 1:
							setMediaId(value);
							break;
						case 2:
							setService(value);
							break;
						case 3:
							setUsageDate(value);
							break;
						case 4:
							setMemberId(value);
							break;
						case 5:
							setEvaluation(value);
							break;
						case 6:
							setUsageType(value);
							break;
						case 7:
							setReserve1(value);
							break;
						case 8:
							setReserve2(value);
							break;
						case 9:
							setUsageWithinLast2Year(value);
							break;
						case 10:
							setUsageWithinLast1Year(value);
							break;
						case 11:
							setUsageWithinLast6Month(value);
							break;
						case 12:
							setUsageWithinAfter30Day(value);
							break;
						case 13:
							setUsageWithinAfter60Day(value);
							break;
						case 14:
							setUsageWithinAfter90Day(value);
							break;
						case 15:
							setUsageWithinAfter120Day(value);
							break;
						case 16:
							setUsageWithinAfter150Day(value);
							break;
						case 17:
							setUsageWithinAfter180Day(value);
							break;
						case 18:
							setUsageWithinAfter1Year(value);
							break;
						case 19:
							setUsageWithinAfter2Year(value);
							break;
						case 20:
							setAllUsageWithinLast6Month(value);
							break;
						case 21:
							setAllUsageWithinAfter30Day(value);
							break;
						case 22:
							setAllUsageWithinAfter60Day(value);
							break;
						case 23:
							setAllUsageWithinAfter90Day(value);
							break;
						case 24:
							setAllUsageWithinAfter120Day(value);
							break;
						case 25:
							setAllUsageWithinAfter150Day(value);
							break;
						case 26:
							setAllUsageWithinAfter180Day(value);
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
