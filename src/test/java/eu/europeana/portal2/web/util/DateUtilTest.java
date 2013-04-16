package eu.europeana.portal2.web.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

public class DateUtilTest {

	int DAY = (24 * 60 * 60 * 1000) - 1;
	int WEEK = (7 * 24 * 60 * 60 * 1000) - 1;

	@Test
	public void test() {
		DateTime now = new DateTime();
		
		DateTime yesterday = new DateTime().minusDays(1).toDateMidnight().toDateTime();
		DateTime start = new DateTime().toDateMidnight().toDateTime();
		System.out.println(now);
		System.out.println(new DateTime().toDateMidnight());
		System.out.println(yesterday);
		System.out.println(new Date(start.toDate().getTime()-1));
		System.out.println(getToday());
		System.out.println(getPastDay(1));
		System.out.println(getPastDay(10));
		System.out.println(getWeek());
		System.out.println(getThisWeek());
		System.out.println(getWeek(1));
		System.out.println(getWeek(2));
		System.out.println(getMonth(0));
		System.out.println(getMonth(1));
		System.out.println(getMonth(2));
	}

	private Interval getToday() {
		Date now = new Date();
		Date start = new DateTime().toDateMidnight().toDate();
		return new Interval(start, now);
	}

	private Interval getPastDay(int i) {
		Date start = new DateTime().minusDays(i).toDateMidnight().toDate();
		Date end = new Date(start.getTime() + DAY);
		return new Interval(start, end);
	}

	private Interval getWeek() {
		Date start = new DateTime().minusDays(7).toDateMidnight().toDate();
		Date end = new Date(start.getTime() + WEEK);
		return new Interval(start, end);
	}

	private Interval getThisWeek() {
		DateTime end = new DateTime();
		DateTime start = end.minusDays(end.getDayOfWeek() - 1).toDateMidnight().toDateTime();
		return new Interval(start.toDate(), end.toDate());
	}

	private Interval getWeek(int i) {
		DateTime now = new DateTime();
		DateTime start = now.minusDays(now.getDayOfWeek() - 1).toDateMidnight().minusWeeks(i).toDateTime();
		DateTime end = start.plusWeeks(1).minusSeconds(1);
		return new Interval(start.toDate(), end.toDate());
	}

	private Interval getMonth(int i) {
		DateTime now = new DateTime();
		DateTime start = now.minusDays(now.getDayOfMonth() - 1).toDateMidnight().minusMonths(i).toDateTime();
		DateTime end = start.plusMonths(1).minusSeconds(1);
		return new Interval(start.toDate(), end.toDate());
	}

	class Interval {
		Date begin;
		Date end;

		public Interval(Date begin, Date end) {
			this.begin = begin;
			this.end = end;
		}

		public Date getBegin() {
			return begin;
		}

		public void setBegin(Date begin) {
			this.begin = begin;
		}

		public Date getEnd() {
			return end;
		}

		public void setEnd(Date end) {
			this.end = end;
		}
		
		public String toString() {
			return begin.toString() + " - " + end.toString();
		}
	}
}
