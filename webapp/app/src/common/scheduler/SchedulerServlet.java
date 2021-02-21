package common.scheduler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.CascadingClassLoadHelper;
import org.quartz.xml.XMLSchedulingDataProcessor;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchedulerServlet extends HttpServlet {
	@Override
	public void init() throws ServletException {
		log.debug("SchedulerServlet init");

		try {
			val helper = new CascadingClassLoadHelper();
			helper.initialize();

			val scheduler = StdSchedulerFactory.getDefaultScheduler();
			val processor = new XMLSchedulingDataProcessor(helper);
			processor.processFileAndScheduleJobs("quartz.xml", scheduler);
			scheduler.start();
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
