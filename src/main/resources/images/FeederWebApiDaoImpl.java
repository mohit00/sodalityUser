package com.radius.feederWebApi.dao;
     
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.radius.feederWebApi.model.Common;
import com.radius.feederWebApi.model.CommonReport;
import com.radius.feederWebApi.model.DailyReport;
import com.radius.feederWebApi.model.DataRequest;
import com.radius.feederWebApi.model.DiscomwiseBean;
import com.radius.feederWebApi.model.DistrictwiseBean;
import com.radius.feederWebApi.model.FeederContactDetails;
import com.radius.feederWebApi.model.FeederDailyStatisticFinalBean;
import com.radius.feederWebApi.model.FeederData;
import com.radius.feederWebApi.model.FeederDetailsEnergySupplied;
import com.radius.feederWebApi.model.FeederdetailsBean;
import com.radius.feederWebApi.model.Hcl152;
import com.radius.feederWebApi.model.IPDSDailySupply;
import com.radius.feederWebApi.model.InterruptDetailsModel;
import com.radius.feederWebApi.model.InterruptionBean;
import com.radius.feederWebApi.model.LoginResources;
import com.radius.feederWebApi.model.MasterData;
import com.radius.feederWebApi.model.MaxLoadModel;
import com.radius.feederWebApi.model.MeterReadBean;
import com.radius.feederWebApi.model.Meterdata1912Bean;
import com.radius.feederWebApi.model.MeterdataNABean;
import com.radius.feederWebApi.model.MonthlyVitalStatistics;
import com.radius.feederWebApi.model.NN16;
import com.radius.feederWebApi.model.Notification;
import com.radius.feederWebApi.model.RAPDRPAvgDataResponse;
import com.radius.feederWebApi.model.RAPDRPDataResponse;
import com.radius.feederWebApi.model.RAPDRPDataResponseNew;
import com.radius.feederWebApi.model.RapdrpPopUpDataBean;
import com.radius.feederWebApi.model.RapdrpTownBean;
import com.radius.feederWebApi.model.ReportTown8NN;
import com.radius.feederWebApi.model.Search;
import com.radius.feederWebApi.model.SensorData;
import com.radius.feederWebApi.model.SensorFeederDetails;
import com.radius.feederWebApi.model.SupplyReport;
import com.radius.feederWebApi.model.SupplyReportDetails;
import com.radius.feederWebApi.model.SupplyReportDiscomWise;
import com.radius.feederWebApi.model.SupplyReportResponse;
import com.radius.feederWebApi.model.SupplyReportResponseTownWiseData;
import com.radius.feederWebApi.model.SupplyReportTownWise;
import com.radius.feederWebApi.model.SupplyStatus;
import com.radius.feederWebApi.model.TodayVitalStatistics;
import com.radius.feederWebApi.model.TokenResonse;
import com.radius.feederWebApi.model.TotalInterruption;
import com.radius.feederWebApi.model.TownwiseBean;
import com.radius.feederWebApi.model.Week1Bean;
import com.radius.feederWebApi.model.Week2Bean;
import com.radius.feederWebApi.model.Week3Bean;
import com.radius.feederWebApi.model.Week4Bean;
import com.radius.feederWebApi.model.WeeklyReportModel;
import com.radius.feederWebApi.model.WeeklyReportResponse;
import com.radius.feederWebApi.utility.Utility;
import com.radius.feederWebApi.vos.Circle;
import com.radius.feederWebApi.vos.DeviceHistory;
import com.radius.feederWebApi.vos.Discom;
import com.radius.feederWebApi.vos.Division;
import com.radius.feederWebApi.vos.Feeder;
import com.radius.feederWebApi.vos.FeederDailyStatisticFinal;
import com.radius.feederWebApi.vos.FeederLogData;
import com.radius.feederWebApi.vos.Login;
import com.radius.feederWebApi.vos.Secure_interruption_data;
import com.radius.feederWebApi.vos.Sensor;
import com.radius.feederWebApi.vos.Site;
import com.radius.feederWebApi.vos.Zone;




@SuppressWarnings({ "unchecked", "unchecked" })
@Repository
public class FeederWebApiDaoImpl implements FeederWebApiDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public TokenResonse getTokenId(Login login) {

		TokenResonse response = new TokenResonse();
		ArrayList<LoginResources> loginResourceList = new ArrayList<LoginResources>();
		LoginResources lr = new LoginResources();
		response.setRc(-1);
		response.setMessage("ERROR :: Invalid login id or Password!!!");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Login.class);
		criteria.add(Restrictions.eq("login_id", login.getLogin_id()));
		Login l = (Login) criteria.uniqueResult();

		if (l == null) {
			return response;
		}

		String dbPassword = l.getPassword().toString().trim();
		String encriptPassword = Utility.passwordEncode(Utility.passwordEncode(login.getPassword()));
		if (dbPassword.equals(login.getPassword().toString().trim()) || dbPassword.equals(encriptPassword)) {
			Utility util = new Utility();
			String uuid = util.getUniqueId();
			String sql = "update login set 	reference_id=:refrence_id where login_id=:login_id and password=:password";
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			query.setString("refrence_id", uuid);
			query.setParameter("login_id", l.getLogin_id());
			query.setParameter("password", l.getPassword());
			query.executeUpdate();
			lr.setToken_id(uuid);
			lr.setBase_url("https://feeder.myxenius.com:8080/webapi/v1/");
			lr.setExpire_time("");
			loginResourceList.add(lr);
			response.setRc(0);
			response.setMessage("success");
			response.setLogin_id(login.getLogin_id());
			response.setAccess_area(l.getAccess_area());
			response.setRole(l.getRole());
			response.setProject(l.getProject());
			response.setResources(loginResourceList);
			response.setAutorefresh(l.getAutorefresh());
		}

		return response;
	}

	@Override
	public List<Sensor> getFeederStatistics() {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		Query query = sessionFactory.getCurrentSession()
			.createQuery("from Sensor where discom_id is not NULL  and type='AC_METER' and admin_status in ('N','S','U')");
		sensorList = query.list();
		return sensorList;
	}
	
	
	//pranay
	@Override
	public List<Sensor> getFeederStatisticsHistoryDown() {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		String sql = "";
		Query query;
		
		sql = "select id id, name name, project_id project_id,discom_id discomId, admin_status admin_status,last_packet_time last_packet_time from sensor where type='AC_METER' and admin_status in ('N','S','U') and (date(last_packet_time) <= date(NOW() - INTERVAL 1 WEEK) or last_packet_time is NULL) ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("id", new StringType()).addScalar("name", new StringType())
				.addScalar("project_id", new StringType()).addScalar("discomId", new StringType())
				.addScalar("admin_status", new StringType()).addScalar("last_packet_time", new StringType())
				.setResultTransformer(Transformers.aliasToBean(Sensor.class));
		sensorList = query.list();
		return sensorList;
	}
	
	@Override
	public List<Sensor> getFeederStatisticsHistoryUp() {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		String sql = "";
		Query query;
		
		sql = "select id id, name name, project_id project_id,discom_id discomId, admin_status admin_status,last_packet_time last_packet_time from sensor where type='AC_METER' and admin_status in ('N','S','U') and (date(last_packet_time) > date(NOW() - INTERVAL 1 WEEK)) ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("id", new StringType()).addScalar("name", new StringType())
				.addScalar("project_id", new StringType()).addScalar("discomId", new StringType())
				.addScalar("admin_status", new StringType()).addScalar("last_packet_time", new StringType())
				.setResultTransformer(Transformers.aliasToBean(Sensor.class));
		sensorList = query.list();
		return sensorList;
	}
	
	//pranay

	public List<Sensor> getFeederMap() {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from Sensor where type='AC_METER' and (admin_status='N' OR admin_status='S' OR admin_status='U')"
				+ " and (latitude >'-90' or latitude < '90')  and (longitude >'-180' or longitude < '180') order by longitude");
		
		sensorList = query.list();
		return sensorList;
	}

	@Override
	public List<Discom> getDiscomList(String project_id) {
		List<Discom> disom = new ArrayList<Discom>();
		String sql = "";
		Query query;
		if (project_id.equalsIgnoreCase("ALL")) {
			sql = "select id id,name name,short_name shortName from discom ";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setResultTransformer(Transformers.aliasToBean(Discom.class));

		} else {
			if (project_id.equalsIgnoreCase("NTPF")) {
				sql = "select id id,name name,short_name shortName from discom  where  id not in ('5a71b6d35156d7.70878980','5a71b8a355c2d4.24012768')";
			} else {
				sql = "select id id,name name,short_name shortName from discom  where  project_id LIKE '%"+project_id+"' or project_id LIKE '%ALL%' ";
			}
			query = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setResultTransformer(Transformers.aliasToBean(Discom.class));
			//query.setParameter(0, project_id);
		}
		Discom d=new Discom();
		/*d.setId("ALL");
		d.setName("ALL");
		d.setShortName("ALL");			
		*/disom = query.list();
		//disom.add(0, d);
		return disom;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeederDailyStatisticFinal> getInterruptionData(String date) {
		return sessionFactory.getCurrentSession().createCriteria(FeederDailyStatisticFinal.class)
				.add(Restrictions.eq("eventData", date)).add(Restrictions.ne("processStatus", "M"))
				.add(Restrictions.ne("processStatus", "D")).list();
	}

	@Override
	public Sensor getSensorDetails(String sensorId) {

		Sensor s = (Sensor) sessionFactory.getCurrentSession().createCriteria(Sensor.class)
				.add(Restrictions.eq("id", sensorId)).uniqueResult();
		return s;

	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<FeederDailyStatisticFinal>
	 * getInterruptionData(String date, String projectId) { String
	 * from=date.trim()+"-1"; String to=date.trim()+"-31";
	 * 
	 * Session session=sessionFactory.getCurrentSession();
	 * List<FeederDailyStatisticFinal>feederDailyStatisticFinal=
	 * session.createCriteria(FeederDailyStatisticFinal.class)
	 * .add(Restrictions.ge("eventDate", from))
	 * .add(Restrictions.le("eventDate", to)) .add(Restrictions.eq("projectId",
	 * projectId)) .add(Restrictions.ne("processStatus", "M"))
	 * .add(Restrictions.ne("processStatus", "D")).list(); return
	 * feederDailyStatisticFinal; }
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<FeederDailyStatisticFinalBean> getBestFeeder(String date, String projectId, String orderBy,
			int limitby) {
		String from = date.trim() + "-1";
		String to = date.trim() + "-31";
		String dateDifference = "31";
		Date date1 = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int month = (cal.get(Calendar.MONTH)) + 1;
		int dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfMonth = String.valueOf(dayofMonth);
		String dateString[] = date.split("-");
		int pmonth = Integer.parseInt(dateString[1]);
		if (month == pmonth) {
			to = date.trim() + "-" + dayOfMonth;
			dateDifference = dayOfMonth;
		} else {
			LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
			LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
			to = end.toString();
			String datedif[] = to.split("-");
			dateDifference = datedif[2];
		}

		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count, LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID "
				+ "from feeder_daily_statistics_final u use index (feederDailyStats) , "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by '"
				+ orderBy + " ' limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FeederDailyStatisticFinalBean> getBestFeeder(String projectId, String orderBy,
			int limitby,String... date) {
		String from=null;
		String to=null;
		int month=0;
		int dayofMonth=0;
		int pmonth=0;
		String dayOfMonth=null;
		String dateDifference = "31";
		Calendar cal = Calendar.getInstance();
		if(date[0]!=null && date[1]==null && date[2]==null)
		{
			from = date[0].trim() + "-1";
			to = date[0].trim() + "-31";
			Date date1 = new Date();
			cal.setTime(date1);
			month = (cal.get(Calendar.MONTH)) + 1;
			dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
			dayOfMonth = String.valueOf(dayofMonth);
			String dateString[] = from.split("-");
			pmonth = Integer.parseInt(dateString[1]);
			if (month == pmonth) {
				to = to.trim() + "-" + dayOfMonth;
				dateDifference = dayOfMonth;
			} else {
				LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
				LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
				to = end.toString();
				String datedif[] = to.split("-");
				dateDifference = datedif[2];
			}
		}
		else if(date[0]==null && date[1]!=null && date[2]!=null)
		{
				 /*	from = date[1].trim() + "-01";
					to = date[2].trim() + "-31";*/
					from = date[1].trim();
					to = date[2].trim();
					LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					dateDifference = String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate)+1);
				 	
		}
		

		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count, LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID "
				+ "from feeder_daily_statistics_final u use index (feederDailyStats) , "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + "  limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeederDailyStatisticFinalBean> getWorstFeeder(String date, String projectId, String orderBy,
			int limitby) {
		String from = date.trim() + "-1";
		String to = date.trim() + "-31";
		String dateDifference = "31";
		Date date1 = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int month = (cal.get(Calendar.MONTH)) + 1;
		int dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfMonth = String.valueOf(dayofMonth);
		String dateString[] = date.split("-");
		int pmonth = Integer.parseInt(dateString[1]);
		if (month == pmonth) {

			to = date.trim() + "-" + dayOfMonth;
			dateDifference = dayOfMonth;

		} else {

			LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
			LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
			to = end.toString();
			String datedif[] = to.split("-");
			dateDifference = datedif[2];
		}

		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count,LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID   "
				+ "from feeder_daily_statistics_final u use index (feederDailyStats) , "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + " desc limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}

	public List<FeederDailyStatisticFinalBean> getWorstFeeder(String projectId, String orderBy,
			int limitby,String... date) {
		String from=null;
		String to=null;
		int month=0;
		int dayofMonth=0;
		int pmonth=0;
		String dayOfMonth=null;
		String dateDifference = "31";
		Calendar cal = Calendar.getInstance();
		if(date[0]!=null && date[1]==null && date[2]==null)
		{
				from = date[0].trim() + "-1";
				to = date[0].trim() + "-31";
				Date date1 = new Date();
				cal.setTime(date1);
				month = (cal.get(Calendar.MONTH)) + 1;
				dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
				dayOfMonth = String.valueOf(dayofMonth);
				String dateString[] = date[0].split("-");
				pmonth = Integer.parseInt(dateString[1]);
				if (month == pmonth) {
					to = date[0].trim() + "-" + dayOfMonth;
					dateDifference = dayOfMonth;
				} else {
					LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
					LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
					to = end.toString();
					String datedif[] = to.split("-");
					dateDifference = datedif[2];
				}
		}
		else if(date[0]==null && date[1]!=null && date[2]!=null)
		{
			//from = date[1].trim() + "-01";
			//to = date[2].trim() + "-31";
			from = date[1].trim();
			to = date[2].trim();
			LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			dateDifference = String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate)+1);
		}
		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count,LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID   "
				+ "from feeder_daily_statistics_final u use index (feederDailyStats) , "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + " Desc limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validateToken(String tokenId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Login.class);
		criteria.add(Restrictions.eq("reference_id", tokenId));
		Login l = (Login) criteria.uniqueResult();
		if (l == null) {
			return true;
		}

		return true;
	}

	@Override
	public List<Zone> getZoneList(String discom_id) {
		List<Zone> zone = new ArrayList<Zone>();
		String sql = "";
		Query query;
		
		sql = "select id id,name name from zone  where discom_id=? ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(Zone.class));
		query.setParameter(0, discom_id);
		
		Zone z=new Zone();
		z.setId("ALL");
		z.setName("ALL");		
		zone = query.list();
		zone.add(0, z);
		return zone;
	}

	@Override
	public List<Circle> getCircleList(String zone_id) {
		List<Circle> circle = new ArrayList<Circle>();
		String sql = "";
		Query query;
		sql = "select id id,name name from circle  where zone_id=? ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(Circle.class));
		query.setParameter(0, zone_id);
		Circle c=new Circle();
		c.setId("ALL");
		c.setName("ALL");		
		circle = query.list();
		circle.add(0, c);
		return circle;
	}

	@Override
	public List<Division> getDivisionList(String circle_id) {
		List<Division> division = new ArrayList<Division>();
		String sql = "";
		Query query;
		sql = "select id id,name name from division where circle_id=? ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(Division.class));
		query.setParameter(0, circle_id);
		Division d=new Division();
		d.setId("ALL");
		d.setName("ALL");		
		division = query.list();
		division.add(0, d);
		return division ;
	}

	@Override
	public List<Site> getSubstationList(String division_id) {
		List<Site> site = new ArrayList<Site>();
		String sql = "";
		Query query;
		sql = "select id id,name name from site  where division_id=? ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(Site.class));
		query.setParameter(0, division_id);
		Site s=new Site();
		s.setId("ALL");
		s.setName("ALL");		
		site = query.list();
		site.add(0, s);
		return site;
	}

	@Override
	public List<FeederData> getFeederDataList(String discom_id, String zone_id, String circle_id, String division_id,
			String substation_id, String project_id) {
		List<FeederData> FeederDatalist = new ArrayList<FeederData>();
		String sql = "";
		Query query;
		sql = "select s.id,s.sub_type sub_type,device_type,s.remark,s.grid_load_alarm,s.dg_load_alarm,TRUNCATE(s.max_demand_KVA,2) as max_demand_KVA, "
				+ " s.dic_id dic_id ,s.device_state device_state,s.csq_signal_strength csq, s.dic_port dic_port,s.UOM uom,concat(site.name,' ',s.name) as name ,"
				+ " s.serial_no as serial_no,TRUNCATE(s.KWh,2) as KWh, TRUNCATE(s.KVAh,2) as KVAh,TRUNCATE(s.instant_R_KW,2) As instant_R_KW,"
				+ " TRUNCATE(s.instant_cum_KW,2) as instant_cum_KW, TRUNCATE(s.instant_cum_KVA,2) as instant_cum_KVA,"
				+ " s.R_Voltage R_Voltage,s.Y_Voltage Y_Voltage,s.B_Voltage B_Voltage,"
				+ " TRUNCATE(s.R_Current,2) as R_Current, TRUNCATE(s.Y_Current,2) as Y_Current, TRUNCATE(s.B_Current,2) as B_Current,"
				+ " s.R_PF,s.Y_PF,s.B_PF,s.cumm_pf,"
				+ " TRUNCATE(s.KWh1,2) KWh1,TRUNCATE(s.KWh2,2) KWh2,TRUNCATE(s.KWh3,2) KWh3,TRUNCATE(s.KWh4,2) KWh4, TRUNCATE(s.KWh5,2) KWh5,TRUNCATE(s.KWh6,2) KWh6,TRUNCATE(s.KWh7,2) KWh7,TRUNCATE(s.KWh8,2) KWh8,"
				+ " TRUNCATE(s.KVAh1,2) KVAh1,TRUNCATE(s.KVAh2,2) KVAh2,TRUNCATE(s.KVAh3,2) KVAh3, TRUNCATE(s.KVAh4,2) KVAh4,TRUNCATE(s.KVAh5,2) KVAh5,TRUNCATE(s.KVAh6,2) KVAh6, TRUNCATE(s.KVAh7,2) KVAh7, TRUNCATE(s.KVAh8,2) KVAh8,"
				+ " z.name zone_name,c.name circle_name,di.name division_name,site.name substation_name,"
				+ " s.last_reading_updated,s.frequency,s.connected,"
				+ " TRUNCATE(s.max_demand_KW,2) as max_demand_KW ,s.contactor_status,s.consumer_id,s.grid_load_sanctioned,s.last_packet_time,"
				+ " s.digital_input1,s.digital_input2,s.digital_input3,s.last_reading_updated_grid,"
				+ " TIMESTAMPDIFF(SECOND, s.last_reading_updated_grid, s.last_reading_updated)/3600 Hrs,RIGHT(d.MC_UID, 10) MC_UID,s.meter_ct_mf,s.CB_status_time,s.Incommer_status_time"
				+ " from sensor s , site site,datalogger d,zone z,circle c,division di " + " where s.utility=1 ";

		if (discom_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and s.discom_id = s.discom_id  ";
		} else {
			sql = sql + " and s.discom_id ='" + discom_id + "'  ";
		}
		if (zone_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and s.zone_id = s.zone_id ";
		} else {
			sql = sql + " and s.zone_id ='" + zone_id + "'  ";
		}
		if (circle_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and s.circle_id = s.circle_id  ";
		} else {
			sql = sql + " and s.circle_id ='" + circle_id + "'  ";
		}
		if (division_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and s.division_id = s.division_id ";
		} else {
			sql = sql + " and s.division_id ='" + division_id + "'  ";
		}
		if (substation_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and s.site_id = s.site_id ";
		} else {
			sql = sql + " and s.site_id ='" + substation_id + "'  ";
		}
		if (project_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and s.project_id = s.project_id ";
		} else {
			sql = sql + " and trim(s.project_id) = '" + project_id + "'  ";
		}
		sql = sql
				+ " and d.id=s.data_logger_id  and z.id=s.zone_id and c.id=s.circle_id and di.id=s.division_id and s.type = 'AC_METER' and s.admin_status in ('N','S') and site.id = s.site_id order by name ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("id", new StringType())
				.addScalar("sub_type", new StringType()).addScalar("device_type", new StringType())
				.addScalar("remark", new StringType()).addScalar("grid_load_alarm", new StringType())
				.addScalar("dg_load_alarm", new StringType()).addScalar("max_demand_KVA", new StringType())
				.addScalar("max_demand_KW", new StringType()).addScalar("dic_id", new StringType())
				.addScalar("device_state", new StringType()).addScalar("csq", new StringType())
				.addScalar("dic_port", new StringType()).addScalar("uom", new StringType())
				.addScalar("name", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("KWh", new StringType()).addScalar("KVAh", new StringType())
				.addScalar("instant_R_KW", new StringType()).addScalar("instant_cum_KW", new StringType())
				.addScalar("instant_cum_KVA", new StringType()).addScalar("R_Voltage", new StringType())
				.addScalar("Y_Voltage", new StringType()).addScalar("B_Voltage", new StringType())
				.addScalar("R_Current", new StringType()).addScalar("Y_Current", new StringType())
				.addScalar("B_Current", new StringType()).addScalar("R_PF", new StringType())
				.addScalar("Y_PF", new StringType()).addScalar("B_PF", new StringType())
				.addScalar("cumm_pf", new StringType()).addScalar("KWh1", new StringType())
				.addScalar("KWh2", new StringType()).addScalar("KWh3", new StringType())
				.addScalar("KWh4", new StringType()).addScalar("KWh5", new StringType())
				.addScalar("KWh6", new StringType()).addScalar("KWh7", new StringType())
				.addScalar("KWh8", new StringType()).addScalar("KVAh1", new StringType())
				.addScalar("KVAh2", new StringType()).addScalar("KVAh3", new StringType())
				.addScalar("KVAh4", new StringType()).addScalar("KVAh5", new StringType())
				.addScalar("KVAh6", new StringType()).addScalar("KVAh7", new StringType())
				.addScalar("KVAh8", new StringType()).addScalar("hrs", new StringType())
				.addScalar("MC_UID", new StringType()).addScalar("meter_ct_mf", new StringType())
				.addScalar("last_reading_updated", new StringType()).addScalar("frequency", new StringType())
				.addScalar("connected", new StringType()).addScalar("contactor_status", new StringType())
				.addScalar("consumer_id", new StringType()).addScalar("grid_load_sanctioned", new StringType())
				.addScalar("last_packet_time", new StringType()).addScalar("digital_input1", new StringType())
				.addScalar("digital_input2", new StringType()).addScalar("digital_input3", new StringType())
				.addScalar("last_reading_updated_grid", new StringType())
				.addScalar("incommer_status_time", new StringType()).addScalar("CB_status_time", new StringType())
				.addScalar("zone_name", new StringType()).addScalar("circle_name", new StringType())
				.addScalar("division_name", new StringType()).addScalar("substation_name", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederData.class));
		return FeederDatalist = query.list();
	}

	@Override
	public List<Feeder> getFeederList(String substation_id) {
		List<Feeder> feeders = new ArrayList<Feeder>();
		String sql = "";
		Query query;
		sql = "select id id,name name from sensor  where site_id=? and type='AC_METER' and admin_status in ('N','S' ,'U') ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(Feeder.class));
		query.setParameter(0, substation_id);
		return feeders = query.list();
	}

	@Override
	public List<FeederDailyStatisticFinal> getInterruptionDataOutage(String fromTime, String toTime) {
		List<FeederDailyStatisticFinal> FeederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinal>();
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from FeederDailyStatisticFinal where eventDate between '" + fromTime + "' and '" + toTime
						+ "' and processStatus not in ('M','D') ");
		FeederDailyStatisticFinal = query.list();
		return FeederDailyStatisticFinal;
	}

	@Override
	public List<SensorFeederDetails> getFeederDetailsData( String feeder_id) {
		List<SensorFeederDetails> sensor = new ArrayList<SensorFeederDetails>();
		String sql = "";
		Query query;
		sql = "select s.id,s.digital_input2 ,s.digital_input3,s.CB_status_time,s.Incommer_status_time,s.serial_no,d.MC_UID,"
				+ "s.csq_signal_strength,s.last_reading_updated,s.kwh,s.kvah,s.last_packet_time,s.device_state,s.uom,"
				+ "s.max_demand_KVA,IFNULL( (s.max_demand_time) ,'NA') max_demand_time,s.instant_cum_KVA,s.meter_ct_mf "
				+ "from sensor s,datalogger d where s.id=? and d.id=s.data_logger_id ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("id", new StringType())
				.addScalar("digital_input2", new StringType()).addScalar("digital_input3", new StringType())
				.addScalar("CB_status_time", new StringType()).addScalar("Incommer_status_time", new StringType())
				.addScalar("serial_no", new StringType()).addScalar("MC_UID", new StringType())
				.addScalar("csq_signal_strength", new StringType()).addScalar("last_reading_updated", new StringType())
				.addScalar("kwh", new StringType()).addScalar("kvah", new StringType())
				.addScalar("last_packet_time", new StringType()).addScalar("device_state", new StringType())
				.addScalar("uom", new StringType()).addScalar("max_demand_KVA", new StringType())
				.addScalar("max_demand_time", new StringType()).addScalar("instant_cum_KVA", new StringType())
				.addScalar("meter_ct_mf", new StringType())
				.setResultTransformer(Transformers.aliasToBean(SensorFeederDetails.class));
		query.setParameter(0, feeder_id);
		return sensor = query.list();
	}

	@Override
	public List<FeederContactDetails> getFeederDataContactDetails(String feeder_id) {
		List<FeederContactDetails> contactdetails = new ArrayList<FeederContactDetails>();
		String sql = "";
		Query query;
		sql = "select site.name2 sdo_name,site.designation2 sdo_designation,site.phone_no2 sdo_mob,site.email_id2 sdo_email, "
				+ "site.name1 je_name,site.designation1 je_designation,site.phone_no1 je_mob,site.email_id1 je_email, "
				+ "site.name3 ee_name,site.designation3 ee_designation,site.phone_no3 ee_mob,site.email_id3 ee_email, "
				+ "site.name0 sso_name,site.designation sso_designation,site.phone_no sso_mob,site.email_id sso_email "
				+ " from site where id = (select site_id from sensor where id=? ) ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sdo_name", new StringType())
				.addScalar("sdo_designation", new StringType()).addScalar("sdo_mob", new StringType())
				.addScalar("sdo_email", new StringType())

				.addScalar("je_name", new StringType()).addScalar("je_designation", new StringType())
				.addScalar("je_mob", new StringType()).addScalar("je_email", new StringType())

				.addScalar("ee_name", new StringType()).addScalar("ee_designation", new StringType())
				.addScalar("ee_mob", new StringType()).addScalar("ee_email", new StringType())

				.addScalar("sso_name", new StringType()).addScalar("sso_designation", new StringType())
				.addScalar("sso_mob", new StringType()).addScalar("sso_email", new StringType())

				.setResultTransformer(Transformers.aliasToBean(FeederContactDetails.class));
		query.setParameter(0, feeder_id);
		return contactdetails = query.list();
	}

	@Override
	public TodayVitalStatistics getTodayVitalStatisticsDetails(String project_id, String feeder_id) {		
		TodayVitalStatistics todayVitalData = new TodayVitalStatistics();
		String sql = "";
		Query query;
		sql = "select count(a.id) outage_count,LEFT(SEC_TO_TIME(sum(a.event_data)),8) AS outage_duration_min ,  "
				+ "round(sum(a.KVAh_loss_EUL1*a.meter_ct_mf)/1000,3) KVAh_loss_EUL1 , "
				+ "round(sum(a.KVAh_loss_EUL2*a.meter_ct_mf)/1000,3) KVAh_loss_EUL2,  "
				+ "round(sum(a.KVAh_loss_EUL3*a.meter_ct_mf)/1000,3) KVAh_loss_EUL3 "
				+ "from feeder_daily_statistics_final a, processor_master_control b "
				+ "where a.parent_sensor_id=? and  a.event_date=date(now()) "
				+ "and a.status not in ('D', 'M') and a.process_status not in ('M','D') "
				+ "and  a.event_data >= b.uppcl_outage_consider_sec ";
		if (project_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and project_id = project_id";
		} else {
			sql = sql + " and trim(project_id) = '" + project_id + "'  ";
		}
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("outage_count", new StringType())
				.addScalar("KVAh_loss_EUL1", new StringType()).addScalar("KVAh_loss_EUL2", new StringType())
				.addScalar("KVAh_loss_EUL3", StandardBasicTypes.INTEGER)
				.addScalar("outage_duration_min", new StringType());
		query.setParameter(0, feeder_id);

		List<Object[]> rows = query.list();
		for (Object[] row : rows) {
			todayVitalData.setOutage_count(row[0].toString());
			if (row[1] == null) {
				todayVitalData.setOutage_duration_min("00:00:00");
			} else {
				todayVitalData.setOutage_duration_min(row[1].toString());
			}
			if (row[2] == null) {
				todayVitalData.setKVAh_loss_EUL1("0");
			} else {
				todayVitalData.setKVAh_loss_EUL1(row[2].toString());
			}
			if (row[3] == null) {
				todayVitalData.setKVAh_loss_EUL2("0");
			} else {
				todayVitalData.setKVAh_loss_EUL2(row[3].toString());
			}
			if (row[4] == null) {
				todayVitalData.setKVAh_loss_EUL3("0");
			} else {
				todayVitalData.setKVAh_loss_EUL3(row[4].toString());
			}
		}
		return todayVitalData;
	}

	private static String calculateTime(int totalSecs)
	{
	    int hours = totalSecs / 3600;
	    int minutes = (totalSecs % 3600) / 60;
	    int seconds = totalSecs % 60;

	    String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
	    return timeString;
	}
	
	@Override
	public MonthlyVitalStatistics getMonthlyVitalStatisticsDetails(String year_month, String feeder_id) {
		String sql = "";
		Query query;		
		String[] parts = year_month.split("-");
		String year = parts[0]; 
		String month = parts[1]; 
		MonthlyVitalStatistics monthlyVitalData = new MonthlyVitalStatistics();
		
		sql = "select count(a.id) no_of_interuption,sum(event_data) no_of_interuption_duration from feeder_daily_statistics_final a, processor_master_control b   "
				+ "where a.parent_sensor_id=?  and a.status not in ('D', 'M') and a.process_status not in ('M','D') "
				+ "and month(a.event_date)=? and year(a.event_date)=? "
				+ "and a.event_data >= b.uppcl_outage_consider_sec " + "and event_data >= b.uppcl_outage_consider_sec ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("no_of_interuption", new StringType())
				.addScalar("no_of_interuption_duration", new StringType());
		query.setParameter(0, feeder_id);
		query.setParameter(1, month);
		query.setParameter(2, year);

		List<Object[]> rows = query.list();
		for (Object[] row : rows) {
			monthlyVitalData.setNo_of_interuption(row[0].toString());
			if (row[1] == null) {
				monthlyVitalData.setNo_of_interuption_duration("00:00:00");
			} else {
				 	String time = row[1].toString();
		            Double dTime = Double.valueOf(time);
		            int intTime = (int) dTime.doubleValue();
		            String nTime = calculateTime(intTime);
				monthlyVitalData.setNo_of_interuption_duration(nTime);
			}
		}
		sql = "select count(a.id) outagelessthan5min, sum(event_data) outagelessthan5min_duration"
				+ " from feeder_daily_statistics_final a , processor_master_control b   "
				+ "where a.parent_sensor_id=? and a.status not in ('D', 'M') and a.process_status not in ('M','D') "
				+ "and month(a.event_date)=? and year(a.event_date)=? "
				+ "and a.event_data >= b.uppcl_outage_consider_sec " + "and a.event_data <= 300.00 ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("outagelessthan5min", new StringType())
				.addScalar("outagelessthan5min_duration", new StringType());
		query.setParameter(0, feeder_id);
		query.setParameter(1, month);
		query.setParameter(2, year);

		List<Object[]> rowss = query.list();
		for (Object[] row : rowss) {
			monthlyVitalData.setOutagelessthan5min(row[0].toString());
			if (row[1] == null) {
				monthlyVitalData.setOutagelessthan5min_duration("00:00:00");
			} else {
				String time = row[1].toString();
	            Double dTime = Double.valueOf(time);
	            int intTime = (int) dTime.doubleValue();
	            String nTime = calculateTime(intTime);
				monthlyVitalData.setOutagelessthan5min_duration(nTime);
			}			
		}
		
		sql = "select count(id) outageduration5to30min,sum(event_data) outageduration5to30min_duration from feeder_daily_statistics_final"
				+ " where parent_sensor_id=? and status not in ('D', 'M') and process_status not in ('M','D') "
				+ " and month(event_date)=? and year(event_date)=? "
				+ "and event_data > 300.00 and event_data <= 1800.00 ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("outageduration5to30min", new StringType())
				.addScalar("outageduration5to30min_duration", new StringType());
		query.setParameter(0, feeder_id);
		query.setParameter(1, month);
		query.setParameter(2, year);

		List<Object[]> rowsss = query.list();
		for (Object[] row : rowsss) {
			monthlyVitalData.setOutageduration5to30min(row[0].toString());
			if (row[1] == null) {
				monthlyVitalData.setOutageduration5to30min_duration("00:00:00");
			} else {
				String time = row[1].toString();
	            Double dTime = Double.valueOf(time);
	            int intTime = (int) dTime.doubleValue();
	            String nTime = calculateTime(intTime);
				monthlyVitalData.setOutageduration5to30min_duration(nTime);
			}
		}		
		
		sql = "select count(id) outagemorethan30min,sum(event_data) outagemorethan30min_duration from feeder_daily_statistics_final "
				+ " where parent_sensor_id=? and status not in ('D', 'M') and process_status not in ('M','D')"
				+ " and month(event_date)=? and year(event_date)=? "
				+ " and event_data > 1800.00 ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("outagemorethan30min", new StringType())
				.addScalar("outagemorethan30min_duration", new StringType());
		query.setParameter(0, feeder_id);
		query.setParameter(1, month);
		query.setParameter(2, year);

		List<Object[]> rowssss = query.list();
		for (Object[] row : rowssss) {
			monthlyVitalData.setOutagemorethan30min(row[0].toString());
			if (row[1] == null) {
				monthlyVitalData.setOutagemorethan30min_duration("00:00:00");
			} else {
				String time = row[1].toString();
	            Double dTime = Double.valueOf(time);
	            int intTime = (int) dTime.doubleValue();
	            String nTime = calculateTime(intTime);
				monthlyVitalData.setOutagemorethan30min_duration(nTime);
			}
		}		
		return monthlyVitalData;
	}

	@Override
	public FeederDetailsEnergySupplied getFeederDetailsMonthlyEnergySupplied(String year_month, String feeder_id,
			String project_id) {
		String sql = "";
		Query query;		
		String[] parts = year_month.split("-");
		String year = parts[0]; 
		String month = parts[1]; 
		FeederDetailsEnergySupplied feederDetailsEnergySupplied = new FeederDetailsEnergySupplied();		
		
		sql = "select (closing_KWh - opening_KWh) consumed_KWh, (closing_KVAh - opening_KVAh) consumed_KVAh, "
				+ " day(meter_date) as day, uom from feeder_data_daily_log where status != 'M' and sensor_id=? "
				+ " and month(meter_date)=?  and year(meter_date)=?  ";
			
		if (project_id.equalsIgnoreCase("ALL")) {
			sql = sql + " and project_id = project_id ";
		} else {
			sql = sql + " and trim(project_id) = '" + project_id + "' ";
		}
		
		sql = sql + " order by meter_date ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("consumed_KWh", new StringType())
				.addScalar("consumed_KVAh", new StringType())
				.addScalar("day", new StringType())
				.addScalar("uom", new StringType());
		query.setParameter(0, feeder_id);
		query.setParameter(1, month);
		query.setParameter(2, year);

		List<Object[]> rowss = query.list();
		List<String> kwh = new ArrayList<String>();
		List<String> kvah = new ArrayList<String>();
		List<String> day = new ArrayList<String>();
		List<String> uom = new ArrayList<String>();
		for (Object[] row : rowss) {
			kwh.add(row[0].toString());
			kvah.add(row[1].toString());
			day.add(row[2].toString());
			uom.add(row[3].toString());					
		}	
		feederDetailsEnergySupplied.setKWh(kwh);
		feederDetailsEnergySupplied.setKVAh(kvah);
		feederDetailsEnergySupplied.setDate(day);
		feederDetailsEnergySupplied.setUom(uom);	
		return feederDetailsEnergySupplied;
	}

	@Override
	public List<DeviceHistory> getViewRemarkList(String feeder_id) {
		List<DeviceHistory> viewremark = new ArrayList<DeviceHistory>();
		String sql = "";
		Query query;
		sql = "select id id,log_type log_type,created_by_profile_id created_by_profile_id,remark remark from device_history where sensor_id=? and remark_type='EXTERNAL' ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setResultTransformer(Transformers.aliasToBean(DeviceHistory.class));
		query.setParameter(0, feeder_id);
		return viewremark = query.list();
	}

	@Override
	public List<Object[]> feeder_data_daily_log(String fromTime,String toTime) {
		String sql = "select di.name,round(sum((fddl.closing_KVAh - fddl.opening_KVAh)*fddl.meter_ct_mf*fddl.meter_MWh_mf)/1000,2) as KVAH,fddl.project_id,di.short_name,fddl.meter_ct_mf,fddl.meter_MWh_mf,di.id from discom di,feeder_data_daily_log fddl  where di.id=fddl.discom_id and fddl.status !='U'";  
		if(fromTime==null && toTime==null)
		{
			 fromTime=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			 toTime=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		sql+=" and fddl.meter_date between '"+fromTime+"' and '"+toTime+"'";
		sql+=" group by fddl.project_id , fddl.discom_id";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		List<Object[]> list=query.list();
		return list;
	}
	
	@Override
	public List<TotalInterruption> getTotalInterruption(String discom_id, String date, String interruption_type, String project_id) {
		String sql = "";
		Query query;	
		List<TotalInterruption> todayInterruption = new ArrayList<TotalInterruption>();		
		sql = "select z.name zone_name ,concat(c.name,'-',dd.name,'-',ss.name,'-',s.name) feeder_name,s.serial_no meter_no,"
				+ "RIGHT(dl.MC_UID, 10) modem_no,a.from_time from_time,a.event_date date,a.to_time to_time,TIME_FORMAT(TIMEDIFF((a.to_time),"
				+ " (a.from_time)), '%T') AS duration "				
				+ "from processor_master_control b ,zone z , circle c , division dd , site ss , datalogger dl,sensor s, "
				+ " feeder_daily_statistics_final a  use index (feederDailyStats) "				
				+ "where a.discom_id = ? and a.zone_id= a.zone_id and a.circle_id=a.circle_id "
				+ "and a.division_id=a.division_id and a.site_id=a.site_id "				
				+ "and a.sensor_id=a.sensor_id "								
				+ "and s.id=a.parent_sensor_id "
				+ "and a.event_date = ? and a.status not in ('D', 'M') and a.process_status not in ('D', 'M')";		
		
				if (project_id.equalsIgnoreCase("ALL")) {
					sql = sql + " and a.project_id = a.project_id ";
				} else {
					sql = sql + " and trim(a.project_id) = '" + project_id + "' ";
				}
				
				sql = sql + "and z.id=a.zone_id and c.id=a.circle_id and dd.id=a.division_id "
						+ "and ss.id=a.site_id and dl.id=s.data_logger_id ";
				
				if (interruption_type.equalsIgnoreCase("TOTAL_INTERRUPTION")) {
					sql = sql + " and a.event_data >= b.uppcl_outage_consider_sec order by a.event_date ";
				} else if(interruption_type.equalsIgnoreCase("LESS_THAN_5")){
					sql = sql + " and a.event_data <= 300.00 and a.event_data >= b.uppcl_outage_consider_sec  order by a.event_date ";
				} else if(interruption_type.equalsIgnoreCase("BETWEEN_5_TO_30")){
					sql = sql + " and a.event_data > 300.00 and a.event_data <= 1800.00 order by a.event_date ";
				} else if(interruption_type.equalsIgnoreCase("MORE_THAN_30")){
					sql = sql + " and a.event_data > 1800.00  order by a.event_date ";
				}
				
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("zone_name", new StringType())
				.addScalar("feeder_name", new StringType())
				.addScalar("meter_no", new StringType())
				.addScalar("modem_no", new StringType())
				.addScalar("date", new StringType())
				.addScalar("from_time", new StringType())
				.addScalar("to_time", new StringType())
				.addScalar("duration", new StringType())
				.setResultTransformer(Transformers.aliasToBean(TotalInterruption.class));;
		query.setParameter(0, discom_id);
		query.setParameter(1, date);

		return todayInterruption = query.list();
	}

	@Override
	public List<FeederLogData> getLogRecord(String dicId, Integer dicport, String sensorId, String utility,
			String discomId, String zoneId, String circle_id, String divisionId, String siteId, String... date) {
		String sql=" select  s.deploy_type deployType,s.UOM UOM,abs(u.cumm_PF) cummPF, u.dic_id dicId, u.port port, u.R_PF RPF, u.Y_PF YPF, ";
        sql +=" u.B_PF BPF, s.serial_no meterId,s.dg_reading dgReading,s.KVAh_dg kvahdg,s.kvah kvah,s.kwh kwh, s.name Name, s.meter_ct_mf meterCTMF,";
        sql +=" abs(u.instant_cum_KW)  instantcumKW, abs(u.instant_cum_KVA) instantcumKVA, ";
        sql+=" u.R_Voltage RVoltage, u.Y_Voltage YVoltage, u.B_Voltage BVoltage,u.R_Current RCurrent, u.Y_Current YCurrent,";
        sql+=" u.B_Current BCurrent, u.creation_time creationTime,u.digital_input1 digitalInput1,u.digital_input2 digitalInput2,u.digital_input3 digitalInput3 ";
        sql+=" from feeder_log u , sensor s ";
        sql+=" where u.utility= '"+utility+"'";
        sql+=" and u.discom_id= '"+discomId+"'";
        sql+=" and u.zone_id= '"+zoneId+"'";
        sql+=" and u.circle_id= '"+circle_id+"'";
        sql+=" and u.division_id = '"+divisionId+"'";
        sql+=" and u.site_id = '"+siteId+"'";
        sql+=" and u.sensor_id='"+sensorId+"'";
        sql+=" and u.status in ('P','Y')";
        if(date.length>1)
        {
        	sql+=" and u.creation_time between '"+date[0].trim()+" 00:00:00' and '"+date[1].trim()+" 23:59:59'";
        }
        else
        {
        	sql+=" and u.creation_time between '"+date[0].trim()+" 00:00:00' and '"+date[0].trim()+" 23:59:59'";
        }
        sql+=" and u.sensor_id=s.id ";
        sql+=" and s.type = 'AC_METER' ";
        sql+=" order by u.creation_time";
        Query q=sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(FeederLogData.class));
		return q.list();
	}

	@Override
	public List<Notification> getNotificationDetails(String projectId, String accessArea,String notificationType) {
		String sql=null;
		String projectIdSql=null;
		String condition=" where ss.id = s.site_id";
		String condition1=" and s.type = 'AC_METER'";
		String condition2=" and ABS(s.cumm_PF) < 0.90  and s.cumm_PF != 0.0 and s.admin_status in ('N','S')";
		Query q=null;
		if(projectId.equalsIgnoreCase("All"))
		{
			projectIdSql="  and s.project_id = s.project_id ";
		}
		else
		{
			projectIdSql="  and trim(s.project_id) ='"+projectId+"'";
		}
		
		if(notificationType.equalsIgnoreCase("DownFeeder"))
		{
			 sql="select concat(ss.name,' | ',s.name) name ,' Feeder Down ' value,s.last_reading_updated from sensor s, site ss where ss.id = s.site_id";
			 sql+=" and  ((s.digital_input2 < 1) or (s.digital_input3 < 1))";
			 sql+=condition1;
			 sql+=projectIdSql;
		}
		else if(notificationType.equalsIgnoreCase("LeanLoadFeeder"))
		{
			sql="select concat('Low Performing Feeder ',ss.name,' | ',s.name) name ,(s.R_Current+s.Y_Current+s.B_Current) value,s.last_reading_updated from sensor s, site ss";
	        sql+=condition;
			sql+=" and  (s.R_Current+s.Y_Current+s.B_Current) < s.grid_load_sanctioned*0.30";
	        sql+=" and s.digital_input2 > 0 and s.digital_input3 > 0";
	        sql+=" and (s.R_Current+s.Y_Current+s.B_Current) > 0 ";
	        sql+=" and (s.R_Current+s.Y_Current+s.B_Current) < 200 ";
	        sql+=condition1;
	        sql+=projectIdSql;
		}
		else if(notificationType.equalsIgnoreCase("LowPerformingFeeder"))
		{
			sql="select concat('Low PF Feeder ',ss.name,' | ',s.name) name ,s.cumm_PF value,s.last_reading_updated from sensor s, site ss";
	        sql+=condition;
			sql+=condition2;
			sql+=condition1;
			sql+=projectIdSql;
		}
		else if(notificationType.equalsIgnoreCase("All"))
		{
			sql="select concat('Low PF Feeder ',ss.name,' | ',s.name) name ,s.cumm_PF value,s.last_reading_updated from sensor s, site ss";
	        sql+=condition;
			sql+=condition2;
			sql+=condition1+projectIdSql;
			sql+=" UNION ALL";
			sql+=" select concat('Low Performing Feeder ',ss.name,' | ',s.name) name ,(s.R_Current+s.Y_Current+s.B_Current) value,s.last_reading_updated from sensor s, site ss";
	        sql+=condition;
			sql+=" and  (s.R_Current+s.Y_Current+s.B_Current) < s.grid_load_sanctioned*0.30";
	        sql+=" and s.digital_input2 > 0 and s.digital_input3 > 0";
	        sql+=" and (s.R_Current+s.Y_Current+s.B_Current) > 0 ";
	        sql+=" and (s.R_Current+s.Y_Current+s.B_Current) < 200 ";
	        sql+=condition1+projectIdSql+" UNION ALL";
	        sql+=" select concat('Low PF Feeder ',ss.name,' | ',s.name) name ,s.cumm_PF value,s.last_reading_updated from sensor s, site ss";
	        sql+=condition;
			sql+=condition2;
			sql+=condition1;
			sql+=projectIdSql;
		}
		
		q=sessionFactory.getCurrentSession().createSQLQuery(sql);
		List<Object[]>ls= q.list();
		List<Notification> listData=new ArrayList<>();
		Notification notifyObj=null;
		for(Object[] obj: ls)
		{
			notifyObj=new Notification();
			if(obj[0]!=null) {
			notifyObj.setName(obj[0].toString());}
			if(obj[1]!=null) {
			notifyObj.setValue(obj[1].toString());}
			if(obj[2]!=null) {
			notifyObj.setLastReadingUpdated(obj[2].toString());}
			listData.add(notifyObj);
		}
		return listData;
	}
	

 public Double meterOutage(String sensorId,String eventDate) {
   Double durationSec =0.00;
   String sql = "SELECT SUM(event_data) duration FROM feeder_daily_statistics WHERE parent_sensor_id='"+sensorId+"'";
   sql+= " and event_date='"+eventDate+"' and event_data > 60 and ref_port in (26,37) and status not in ('D','M') and process_status not in ('D','M') ;";
   Query q=sessionFactory.getCurrentSession().createSQLQuery(sql);
   List ls=q.list();
   if(ls.get(0)!=null)
   {
	   durationSec=(Double.parseDouble(ls.get(0).toString()))/60;
       if (durationSec > 1440) { 
       durationSec=1440.00;  }
    }
	return durationSec/100.0;
 	}

	public List<SensorData> getFeederSLA(String feederId, String date,Sensor obj) {
		SensorData modelObj=null; 
		List<SensorData> listObj=new ArrayList<>();
		if(date==null)
		 {
			 Date date1=new Date();
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(date1);
			 int year = cal.get(Calendar.YEAR);
			 int month = cal.get(Calendar.MONTH);
			 date=year+"-"+month+01;
		 }
		 String sql ="select fd.name,fd.device_sr_no,fd.sensor_id,fd.meter_date, if(fd.data_packet_count > round(3600*24/p.feeder_packet_interval_sec,0),round(3600*24/p.feeder_packet_interval_sec,0),fd.data_packet_count) data_packet_count,"
                 +"round(3600*24/p.feeder_packet_interval_sec,0) requiredPacket"
                 +" from processor_master_control p , feeder_data_daily_log fd "
                 +" where fd.discom_id='" +obj.getDiscomId()+ "' and fd.zone_id='" +obj.getZoneId()+ "' and fd.circle_id='" +obj.getCircle_id()+ "'"
                 +" and fd.division_id='" +obj.getDivisionId()+ "'  and fd.site_id='" +obj.getSiteId()+ "' and fd.sensor_id='" +feederId+ "'"
                 +" and fd.meter_date like '%"+date+"%' order by meter_date";
		 Query q=sessionFactory.getCurrentSession().createSQLQuery(sql);
		 List<Object[]> ls=q.list();
		 for(Object[] s:ls)
		 {
			 modelObj=new SensorData();
			 modelObj.setName(s[0].toString());
			 modelObj.setMeterNo(s[1].toString());
			 modelObj.setMeterDate(s[3].toString());
			 modelObj.setDataPacketCount(s[4].toString());
			 modelObj.setRequiredPacket(s[5].toString());
			 modelObj.setDuration((meterOutage(feederId,s[3].toString()).toString()));
			 listObj.add(modelObj);
		 }
		 return listObj;
	}

	
	public List<Common> getAVGOutageReport(DataRequest dataRequest) {
			String discom=null;
			String zone=null;
			String circle=null;
			String division=null;
			String substation=null;
			String sensor=null;
			String fromDate=null;
			String toDate=null;
			int numOfDays=0;
			String outageType=null;
			String outage=null;
			String projectType=null;
		   if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
			   discom = "  u.discom_id ='" +dataRequest.getDiscom_id()+ "' ";}
		   else {
			   discom = "  u.discom_id = u.discom_id ";}
		   if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
			   zone = " and u.zone_id ='" +dataRequest.getZone_id()+ "' ";}
		   else {
			   zone = " and u.zone_id = u.zone_id ";}
		   if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
			   circle = " and u.circle_id ='"+ dataRequest.getCircle_id()+"' ";
		   else
			   circle = " and u.circle_id = u.circle_id ";
		
		   if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
			   division = " and u.division_id ='"+dataRequest.getDivision_id()+ "' ";
		   else
			   division = " and u.division_id = u.division_id ";
		
		   if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
			   substation = " and u.site_id ='"+dataRequest.getSubstation_id()+ "' ";
		   else
			   substation = " and u.site_id = u.site_id ";
		   if (dataRequest.getFeeder_id()!=null && !dataRequest.getFeeder_id().equalsIgnoreCase("ALL"))
			   sensor = " and u.parent_sensor_id ='" +dataRequest.getFeeder_id()+ "' ";
		   else
			   sensor = " and u.parent_sensor_id = u.parent_sensor_id ";
		   if (dataRequest.getFromTime()!=null)
			   fromDate = dataRequest.getFromTime();
		   if (dataRequest.getToTime()!=null)
			   toDate =dataRequest.getToTime();
		   
           outageType = dataRequest.getOutageType();
           if (outageType.equalsIgnoreCase("TOTAL_INTERRUPTION")) {
        	   outage = " and u.event_data >= p.uppcl_outage_consider_sec ";
           }
           if (outageType.equalsIgnoreCase("LESS_THAN_15")) {
        	   outage = " and u.event_data <= 900.00 and u.event_data >= p.uppcl_outage_consider_sec  ";
           }
           if (outageType.equalsIgnoreCase("BETWEEN_15_TO_60")) {
        	   outage = " and u.event_data > 900.00 and u.event_data <= 3600.00";
           }
           if (outageType.equalsIgnoreCase("MORE_THAN_60")) {
        	   outage = " and u.event_data > 3600.00 and u.event_data >= p.uppcl_outage_consider_sec";
           }
           if (!dataRequest.getProject_id().equalsIgnoreCase("ALL"))
               projectType = " and u.project_id = trim('"+dataRequest.getProject_id()+"') ";
           else
        	   projectType = " and u.project_id = u.project_id ";
		    try {
		    	Date date1=null;
		    	Date date2=null;
		    	
				 if(fromDate==null && toDate==null)
		    	 {
		    		 fromDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		    		 toDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		    	 }
				 if(fromDate!=null && toDate!=null)
			    	{
					  date1=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
					  date2=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
					  long diff = date2.getTime()-date1.getTime();
					  numOfDays = (int) (diff/(1000*60*60*24));
			    	}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}  
		  
            
           
		String sql="select sen.id sensorId,"
				+ "sen.project_id, "
				+ "d.name discomName , "
				+ "z.name zoneName ,"
				+ "c.name circleName, "
				+ "di.name divisionName,"
				+ "s.name ssName,"
				+ "sen.name feederName, "
				+ "number_of_gateway feederCount, "
				  +"u.event_date eventDate,"
				  + "count(event_date) OutageCount , "
				  + "SEC_TO_TIME(sum(event_data)) totalOutageDuration, "
				  + "'"+numOfDays+"' numOfDays , "
				  		+ "'" +fromDate+"' fromDate , "
				  				+ "'"+toDate+"' toDate , " 
		          +"count(event_date)/"+numOfDays+ " avgOutageCount,"
		          		+ "LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avgOutageDuration,"
		          		+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/"+numOfDays+ ")),8) avgOutageDurationPerDay,"
		          				+ "sen.serial_no as serialNo,"
		          				+ "RIGHT(sen.appliance_id, 10) mcUID "    
		          +"from feeder_daily_statistics_final u ,site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c   where ";
			   sql+=discom+zone+circle+division+substation+sensor;    
			   sql+=" and u.discom_id = d.id and u.zone_id = z.id and u.division_id = di.id and u.circle_id = c.id and u.event_date between '" +fromDate+"' and '"+toDate+"' and u.site_id=s.id and u.status not in ('D','M') and u.process_status not in ('D','M','U') "
				  +" and sen.id=u.parent_sensor_id	 and sen.type='AC_METER' ";
			   sql+=outage+ projectType+ " group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id , u.parent_sensor_id";
			   
	   Query qry=sessionFactory.getCurrentSession().createSQLQuery(sql);
	   List<Object[]> ls=qry.list();
	   List<Common> dataList=new ArrayList();
	   Iterator itr=ls.iterator();
	   Common dataObj=null;
	   while(itr.hasNext())
	   {
		   dataObj=new Common();
		   Object[] ob=(Object[])itr.next();
		   if(ob[0]!=null) {
			   dataObj.setSensorId(ob[0].toString());}
		   if(ob[1]!=null) {
		   dataObj.setProjectName(ob[1].toString());}
		   if(ob[2]!=null){
			dataObj.setDiscomName(ob[2].toString());  }
		   if(ob[3]!=null) {
			dataObj.setZoneName(ob[3].toString()); }
		   if(ob[4]!=null) {
			dataObj.setCircleName(ob[4].toString());}
		   if(ob[5]!=null) {
			dataObj.setDivisionName(ob[5].toString());}
		   if(ob[6]!=null) {
			dataObj.setSsName(ob[6].toString());}
		   if(ob[18]!=null) {
				dataObj.setMeterType(ob[18].toString());
				dataObj.setSerialNo(ob[18].toString());
		   }
		   if(ob[19]!=null) {
				dataObj.setMcUID(ob[19].toString());}
		   if(ob[7]!=null)
			   dataObj.setFeederName(ob[7].toString());
		   if(ob[10]!=null)
			   dataObj.setOutageCount(ob[10].toString());
		   if(ob[11]!=null)
			   dataObj.setAvgOutageDuration(ob[11].toString());
		   if(ob[15]!=null) {
			   dataObj.setAvgOutageCount(ob[15].toString());
			   dataObj.setRoundAvgOutageCount(Math.round(Float.parseFloat(ob[15].toString())));
		   }
		   if(ob[17]!=null)
			   dataObj.setAvgOutageDurationPerDay(ob[17].toString());
		   if(ob[16]!=null)
			   dataObj.setAvgOutageDuration(ob[16].toString());
		   dataObj.setFromDate(fromDate);
		   dataObj.setToDate(toDate);
		   dataObj.setNumOfDays(String.valueOf(numOfDays));
	   }
	   dataList.add(dataObj);
	   return dataList;
	}
	
	public List<Common> getGatewayReport(DataRequest dataRequest) {
		String discom=null;
		String zone=null;
		String circle=null;
		String division=null;
		String substation=null;
		String sensor=null;
		int numOfDays=0;
		String outage=null;
		String projectType=null;
		Common commonObj=null;
		List<Common> commonList=new ArrayList();
		
		 if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
			   discom = "  s.discom_id ='" +dataRequest.getDiscom_id()+ "'";}
		   else {
			   discom = "  s.discom_id = s.discom_id ";}
		   if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
			   zone = " and s.zone_id ='" +dataRequest.getZone_id()+ "'";}
		   else {
			   zone = " and s.zone_id = s.zone_id ";}
		   if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
			   circle = " and s.circle_id ='"+ dataRequest.getCircle_id()+"'";
		   else
			   circle = " and s.circle_id = s.circle_id";
		
		   if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
			   division = " and s.division_id ='"+dataRequest.getDivision_id()+ "'";
		   else
			   division = " and s.division_id = s.division_id ";
		
		   if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
			   substation = " and s.site_id ='"+dataRequest.getSubstation_id()+ "'";
		   else
			   substation = " and s.site_id=s.site_id ";
		   
		   if (!dataRequest.getProject_id().equalsIgnoreCase("ALL"))
			   projectType = " and s.project_id = trim('"+dataRequest.getProject_id()+ "')";
           else
        	   projectType = " and s.project_id = s.project_id ";
		String sql = "select ds.name discomName, z.name zoneName,c.name circleName ,d.name divisionName, concat(si.name,'-',s.name) ssName ,s.meter_ct_mf meterCTMF, s.data_logger_id DL_ID,RIGHT(dl.MC_UID,10) IMEI_NO,"
                 +" s.serial_no Meter_sr_no,s.project_id,s.UOM Meter_type,s.instant_cum_KVA kva,s.name feede_name,s.last_reading_updated_grid,s.installation_time,s.last_packet_time,"
                 +" s.max_demand_KVA md,s.admin_status"
                 +" FROM  sensor s ,zone z ,circle c ,division d  , datalogger dl , site si ,discom ds"
                 +" where "
                 +" s.application='FEDR' and "
                 + discom;
                 if(!dataRequest.getDiscom_id().equalsIgnoreCase("ALL"))
                 {
                	 sql+=" and ds.id='" +dataRequest.getDiscom_id()+"' ";
                 }
                 sql+=zone+circle+division
                 +" and s.type='AC_METER' "
                 +" and s.admin_status in ('N','S','U','M') "
                 +  projectType
                 +  substation
                 +" and dl.id=s.data_logger_id "
                 +"  and si.id = s.site_id    and dl.id=s.data_logger_id  and ds.id=s.discom_id  and z.id=s.zone_id  and c.id=s.circle_id  and d.id=s.division_id order by s.discom_id,s.zone_id,s.circle_id,s.division_id,s.site_id";
		Query qry=sessionFactory.getCurrentSession().createSQLQuery(sql);
		List<Object[]> ls=qry.list();
		Iterator itr=ls.iterator();
		String adminStatus=null;
		while(itr.hasNext())
		{
			commonObj=new Common();
			Object[] obj=(Object[])itr.next();
			if(obj[0]!=null) {
			commonObj.setDiscomName(obj[0].toString());}
			if(obj[1]!=null) {
			commonObj.setZoneName(obj[1].toString());}
			if(obj[2]!=null) {
			commonObj.setCircleName(obj[2].toString());}
			if(obj[3]!=null) {
			commonObj.setDivisionName(obj[3].toString());}
			if(obj[4]!=null) {
			commonObj.setSsName(obj[4].toString());}
			if(obj[5]!=null) {
			commonObj.setMeterCTMF(obj[5].toString());}
			if(obj[6]!=null) {
			commonObj.setDataLoggerId(obj[6].toString());}
			if(obj[7]!=null) {
			commonObj.setImeiNo(obj[7].toString());}
			if(obj[8]!=null) {
			commonObj.setSerialNo(obj[8].toString());}
			if(obj[9]!=null) {
			commonObj.setProjectName(obj[9].toString());}
			if(obj[10]!=null) {
			commonObj.setMeterType(obj[10].toString());}
			if(obj[11]!=null) {
			commonObj.setInstantkva(obj[11].toString());}
			if(obj[12]!=null) {
			commonObj.setFeederName(obj[12].toString());}
			if(obj[13]!=null) {
			commonObj.setLastReadingUpdated(obj[13].toString());}
			if(obj[14]!=null) {
			commonObj.setInstallationTime(obj[14].toString());}
			if(obj[15]!=null) {
			commonObj.setLastPacketTime(obj[15].toString());}
			if(obj[16]!=null) {
			commonObj.setMaxDemandKVA(obj[16].toString());}
			if(obj[17]!=null) {
			if(obj[17].toString().equalsIgnoreCase("N") || obj[17].toString().equalsIgnoreCase("S"))
			{adminStatus="Live";}
			else if(obj[17].toString().equalsIgnoreCase("U"))
			{adminStatus="LM";}
			else
			{adminStatus="Maintenance";}}
			commonObj.setStatus(adminStatus);
			commonList.add(commonObj);
		}
		   return commonList;
	}

	@Override
	public List<Common> getNoConumptionFeederDetails(DataRequest dataRequest) {
		String discom=null;
		String zone=null;
		String circle=null;
		String division=null;
		String substation=null;
		String sensor=null;
		int numOfDays=0;
		String outage=null;
		String projectType=null;
		Common commonObj=null;
		List<Common> commonList=new ArrayList();
		
		 if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
			   discom = "  s.discom_id ='" +dataRequest.getDiscom_id()+ "'";}
		 else {
			   discom = "  s.discom_id = s.discom_id";}
		 if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
			   zone = " and s.zone_id ='" +dataRequest.getZone_id()+ "'";}
		 else {
			   zone = " and s.zone_id = s.zone_id ";}
		 if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
			   circle = " and s.circle_id ='"+ dataRequest.getCircle_id()+"'";
		 else
			   circle = " and s.circle_id = s.circle_id ";
		
		 if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
			   division = " and s.division_id ='"+dataRequest.getDivision_id()+ "'";
		 else
			   division = " and s.division_id = s.division_id ";
		
		 if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
			   substation = " and s.site_id ='"+dataRequest.getSubstation_id()+ "'";
		 else
			   substation = " and s.site_id = s.site_id ";
		   
		 if (!dataRequest.getProject_id().equalsIgnoreCase("ALL"))
			   projectType = " and s.project_id = trim('"+dataRequest.getProject_id()+ "')";
         else
        	   projectType = " and s.project_id = s.project_id ";
		   
		   String sql ="select s.project_id,concat(site.name,' ',s.name) name,s.serial_no as serial_no,RIGHT(d.MC_UID, 10),s.last_reading_updated,s.last_packet_time,s.digital_input2,s.digital_input3,s.CB_status_time,s.Incommer_status_time,s.id";
		          sql+= " from sensor s , site site,datalogger d,zone z,circle c,division di where s.utility=1 and";
		 sql+=discom+zone+circle+division+substation+projectType;       
         sql+=" and  d.id=s.data_logger_id and  z.id=s.zone_id and  c.id=s.circle_id  and  di.id=s.division_id ";
         sql+= " and s.type = 'AC_METER' and s.admin_status in ('N','S')  and site.id = s.site_id  and TIMESTAMPDIFF(SECOND, last_reading_updated_grid, last_reading_updated)/3600 >72 order by name ";
		 Query qry=sessionFactory.getCurrentSession().createSQLQuery(sql);
		 List<Object[]> ls=qry.list();
		 for(Object[] data: ls)
		 {
			 commonObj=new Common();
			 commonObj.setName(data[1].toString()!=""?data[1].toString():"");
			 commonObj.setProjectName(data[0].toString()!=""?data[0].toString():"");
			 //commonObj.setMeterType(data[2].toString()!=""?data[2].toString():"");
			 commonObj.setSerialNo(data[2].toString()!=""?data[2].toString():"");
			 commonObj.setMcUID(data[3].toString()!=""?data[3].toString():"");
			 commonObj.setModemNo(data[3].toString()!=""?data[3].toString():"");
			 commonObj.setLastReadingUpdated(data[4].toString()!=""?data[4].toString():"");
			 commonObj.setIncrementalData(data[4].toString()!=""?data[4].toString():"");
			 commonObj.setLastPacketTime(data[5].toString()!=""?data[5].toString():"");
			 commonObj.setLastUpdated(data[5].toString()!=""?data[5].toString():"");
			 commonObj.setIncomer(data[6].toString()!=""?data[6].toString():"");
			 commonObj.setCB(data[7].toString()!=""?data[7].toString():"");
			 commonObj.setIncommerStatusTime(data[8].toString()!=""?data[8].toString():"");
			 commonObj.setCbStatusTime(data[9].toString()!=""?data[9].toString():"");
			 commonObj.setSensorId(data[10].toString()!=""?data[10].toString():"");
			 commonList.add(commonObj);
		 }
	      return commonList;
	}
	
	
	@Override
	public List<Object[]> getRAPDRP() {
		String query="select meter_date from secure_interruption_data order by meter_date desc limit 1";
        Query qry=sessionFactory.getCurrentSession().createSQLQuery(query);
        String meterDate=qry.uniqueResult().toString();
        if(meterDate==null && meterDate.isEmpty())
        {
        	meterDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	
        }
        String qryToGetTotalInstalled="select count(id) total from secure_interruption_data where meter_date='"+meterDate+"'";
        Query installedqry=sessionFactory.getCurrentSession().createSQLQuery(qryToGetTotalInstalled);
        String install=installedqry.uniqueResult().toString();
        
        String qryToGetTotalUp="select count(id) total from secure_interruption_data where meter_date='"+meterDate+"' and trim(remark)='OK'";
        Query upQry=sessionFactory.getCurrentSession().createSQLQuery(qryToGetTotalUp);
        String up=upQry.uniqueResult().toString();
        
        String qryToGetTotalDown="select count(id) total from secure_interruption_data where meter_date='"+meterDate+"' and trim(remark)!='OK'";
        Query downQuery=sessionFactory.getCurrentSession().createSQLQuery(qryToGetTotalDown);
        String down=downQuery.uniqueResult().toString();
        
        Object obj[]=new Object[3];
        List listObj=new ArrayList<>();
        obj[0]=install;
        obj[2]=down;
        obj[1]=up;
        listObj.add(obj);
		return listObj;
	}
	
	@Override
	public List<Secure_interruption_data> getRAPDRPData() {
		String query="select meter_date from secure_interruption_data order by meter_date desc limit 1";
        Query qry=sessionFactory.getCurrentSession().createSQLQuery(query);
        String meterDate=qry.uniqueResult().toString();
        if(meterDate==null && meterDate.isEmpty())
        {
        	meterDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	
        }
		List<Secure_interruption_data> secureList = new ArrayList<Secure_interruption_data>();
		Query qury = sessionFactory.getCurrentSession()
				.createQuery("From Secure_interruption_data where meterDate='"+meterDate+"' and trim(town) in ('ALIGARH','FIROZABAD','JHANSI','MATHURA','BAREILLY','FAIZABAD','LUCKNOW','SHAHJAHANPUR','ALLAHABAD','GORAKHPUR','VARANASI','GHAZIABAD','MEERUT','MORADABAD','NOIDA','SAHARANPUR')");
		secureList = qury.list();
		return secureList;
	}

	@Override
    public RAPDRPDataResponse getRAPDRPDataDetails(final DataRequest dataRequest) {
        String sql = "";
        String meterDate = "";
        if (dataRequest.getDate() == null) {
            sql = "select meter_date from secure_interruption_data order by meter_date desc limit 1";
            final Query query = (Query)this.sessionFactory.getCurrentSession().createSQLQuery(sql);
            meterDate = query.uniqueResult().toString();
            if (meterDate == null && meterDate.isEmpty()) {
                meterDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
        }
        else {
            meterDate = dataRequest.getDate();
        }
        List<InterruptionBean> interruptionBeanList = new ArrayList<InterruptionBean>();
        List<MeterReadBean> meterReadBeanList = new ArrayList<MeterReadBean>();
        List<MeterdataNABean> meterdataNABeanList = new ArrayList<MeterdataNABean>();
        List<Meterdata1912Bean> meterdata1912BeanList = new ArrayList<Meterdata1912Bean>();
        List<RapdrpPopUpDataBean> RapdrpPopUpDataBeanList = new ArrayList<RapdrpPopUpDataBean>();
        final RAPDRPDataResponse dr = new RAPDRPDataResponse();
        dr.setRc(-1);
        dr.setMessage("Error ");
       	sql = "SELECT  s.discom_name, s.meter_date meterDate,s.town ,count(DISTINCT s.meter_serial_no) total_feeder, round(sum(s.total_interruption_count)/count(DISTINCT s.meter_serial_no),2) interruption_count,round(sum(s.total_interruption_duration)/count(DISTINCT s.meter_serial_no),2) interruption_duration, round(sum(s.total_interruption_duration),2) total_interruption_duration,  round(sum(s.total_interruption_count),2) total_interruption_count, r.complain_registered  complain_registered , r.closed_registered closed_registered, r.pending_complain pending_complain FROM secure_interruption_data s , rapdrp_complain_log r  where s.meter_date = '"
    				+ meterDate
    				+ "' and s.discom_name=s.discom_name  and r.ss_name = s.ss_name  and s.fedeer_type='OUTGOING' and r.town_name=s.town and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') group by s.discom_name , s.town ";
     	Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("discom_name", new StringType()).addScalar("town", new StringType())
				.addScalar("meterDate", new StringType()).addScalar("total_feeder", new StringType())
				.addScalar("interruption_count", new StringType()).addScalar("interruption_duration", new StringType())
				.addScalar("total_interruption_duration", new StringType())
				.addScalar("total_interruption_count", new StringType())
				.addScalar("complain_registered", new StringType()).addScalar("closed_registered", new StringType())
				.addScalar("pending_complain", new StringType())
				.setResultTransformer(Transformers.aliasToBean(InterruptionBean.class));
	  interruptionBeanList = (List<InterruptionBean>)query.list();
        sql = "SELECT discom_name , town, count(meter_serial_no) notripcount  FROM `secure_interruption_data` where  meter_date = '" + meterDate + "'  and discom_name=discom_name and     trim(remark)='No Tripping Data Available' and fedeer_type='OUTGOING'  and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER')  group by discom_name,town";
        query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom_name",  new StringType()).addScalar("town",  new StringType()).addScalar("notripcount",  new StringType()).setResultTransformer(Transformers.aliasToBean(MeterReadBean.class));
        meterReadBeanList = (List<MeterReadBean>)query.list();
        sql = "SELECT discom_name , town , count(meter_serial_no) nodatacount  FROM `secure_interruption_data` where meter_date = '" + meterDate + "' and discom_name=discom_name and  trim(remark) in ('Meter data not available','Meter Not Read') and fedeer_type='OUTGOING'  and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') group by discom_name , town ";
        query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom_name",  new StringType()).addScalar("town",  new StringType()).addScalar("nodatacount",  new StringType()).setResultTransformer(Transformers.aliasToBean(MeterdataNABean.class));
        meterdataNABeanList = (List<MeterdataNABean>)query.list();
        sql = "select town_name , complain_status ,  count(id) total,  sum(case when complain_status = 'CLOSED' then 1 else 0 end) CLOSED , sum(case when complain_status in ('PENDING BEYOND','PENDING WITHIN') then 1 else 0 end) pending from rapdrp_complain_log  where date(registration_date)  = '" + meterDate + "' group by town_name ";
        query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("town_name",  new StringType()).addScalar("complain_status",  new StringType()).addScalar("total",  new StringType()).addScalar("CLOSED",  new StringType()).addScalar("pending",  new StringType()).setResultTransformer(Transformers.aliasToBean(Meterdata1912Bean.class));
        meterdata1912BeanList = (List<Meterdata1912Bean>)query.list();
        if (!"ALL".equalsIgnoreCase(dataRequest.getTown()) && dataRequest.getTown() != null && dataRequest.getTown().length() >1 ) {
        	sql = "SELECT  town project_area ,ss_name ss_name,feeder_name feeder_name,meter_serial_no meter_sr_no,remark remark,total_interruption_count interruption_count ,total_interruption_duration interruption_duration,last_reading_timestamp FROM secure_interruption_data where  meter_date = '" + meterDate + "' and fedeer_type='OUTGOING' and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') and trim(town) = '"+dataRequest.getTown()+"'";	
        }else {
        	sql = "SELECT  town project_area ,ss_name ss_name,feeder_name feeder_name,meter_serial_no meter_sr_no,remark remark,total_interruption_count interruption_count ,total_interruption_duration interruption_duration,last_reading_timestamp FROM secure_interruption_data where  meter_date = '" + meterDate + "' and fedeer_type='OUTGOING' and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') and town in ('ALIGARH','FIROZABAD','JHANSI','MATHURA','BAREILLY','FAIZABAD','LUCKNOW','SHAHJAHAN','ALLAHABAD','GORAKHPUR','VARANASI','GHAZIABAD','MEERUT','MORADABAD','NOIDA','SAHARANPU')";
        }
        query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project_area",  new StringType()).addScalar("ss_name",  new StringType()).addScalar("feeder_name",  new StringType()).addScalar("meter_sr_no",  new StringType()).addScalar("remark",  new StringType()).addScalar("interruption_count",  new StringType()).addScalar("interruption_duration",  new StringType()).setResultTransformer(Transformers.aliasToBean(RapdrpPopUpDataBean.class));
        RapdrpPopUpDataBeanList = (List<RapdrpPopUpDataBean>)query.list();
        dr.setRc(0);
        dr.setMessage("Success");
        dr.setInterruption(interruptionBeanList);
        dr.setMeterRead(meterReadBeanList);
        dr.setMeterdataNA(meterdataNABeanList);
        dr.setMeterdata1912(meterdata1912BeanList);
        dr.setPopupdata(RapdrpPopUpDataBeanList);
        return dr;
    }
    

	/*@Override
	public List<MaxLoad> getMaxLoadStatus(DataRequest dataRequest) {
		String sql="select meter_date,sum(consumed_KVAh) KVAh , sum(instant_cum_KVA_Max) max from feeder_data_daily_log where month(meter_date)=11 and year(meter_date)=2018 group by meter_date";
		Criteria crt=sessionFactory.getCurrentSession().createCriteria(MaxLoad.class);
		Criterion cr=Restrictions.eq("month(meter_date)", 11);
		Criterion cr1=Restrictions.eq("year(meter_date)", 2018);
		ProjectionList p=Projections.projectionList();
		Projection p1=Projections.groupProperty("meterDate");
		Projection p2=Projections.sum("instantCumKVAMax");
		Projection p3=Projections.sum("consumedKVAh");
		Projection p4=Projections.property("meter_date");
		p.add(p1);
		p.add(p2);
		//p.add(p3);
		//p.add(p4);
		//crt.add(cr);
		//crt.add(cr1);
		//List<MaxLoad> ls=crt.setProjection(p).setResultTransformer(Transformers.aliasToBean(MaxLoad.class)).list();
		List<MaxLoad> ls=crt.setProjection(p).list();
		return ls;
	}*/
	
	public List<MaxLoadModel> getMaxLoadStatus(DataRequest dataRequest) {
		// String sql="select meter_date meterDate,sum(consumed_KVAh)
		// consumedKVAh , sum(instant_cum_KVA_Max) instantCumKVAMax from
		// feeder_data_daily_log where month(meter_date)=11 and
		// year(meter_date)=2018 group by meter_date";
		String month[] = null;
		Calendar now = Calendar.getInstance();
		int currentMonth = (now.get(Calendar.MONTH));
		int currentMonthDaily = (now.get(Calendar.MONTH) + 1);
		int currentDate = now.get(Calendar.DATE);
		int currentYear = now.get(Calendar.YEAR);
		int previousDate = now.get(Calendar.DATE) - 1;
		String currentMonth1 = null;
		String previousDate1 = null;
		String CurrentmonthDaily = null;
		if (currentMonth < 10) {
			currentMonth1 = "0" + String.valueOf(currentMonth);
		} else {
			currentMonth1 = String.valueOf(currentMonth);
		}
		if (currentMonthDaily < 10) {
			CurrentmonthDaily = "0" + String.valueOf(currentMonthDaily);
		} else {
			CurrentmonthDaily = String.valueOf(currentMonthDaily);
		}
		if (previousDate < 10) {
			previousDate1 = "0" + String.valueOf(previousDate);
		} else {
			previousDate1 = String.valueOf(previousDate);
		}
		previousDate1 = currentYear + "-" + CurrentmonthDaily + "-" + previousDate1;
		String discom = null;
		String project = null;
		if (dataRequest.getMonth() != null) {
			month = dataRequest.getMonth().split("-");
			currentYear = Integer.parseInt(month[0].toString());
			currentMonth = Integer.parseInt(month[1].toString());
		}
		if (dataRequest.getDiscom_id().equalsIgnoreCase("All")) {
			discom = "f.discom_id = d.id";
		} else {
			discom = "f.discom_id = d.id && d.id='" + dataRequest.getDiscom_id() + "'";
		}
// Getting project Wise Data
		if (dataRequest.getProject_id() != null) {
			if ("ALL".equalsIgnoreCase(dataRequest.getProject_id())) {
				project = "f.project_id = f.project_id";
			} else {
				project = "f.project_id = '" + dataRequest.getProject_id() + "'";
			}
		} else {
			project = "f.project_id = f.project_id";
		}

		String sql = "select f.meter_date Date , sum(instant_cum_KW_Max*meter_ct_mf*meter_mwh_mf)/1000 load1,sum(consumed_KVAh*meter_ct_mf*meter_mwh_mf)/1000 energySupplied,d.name disom from feeder_data_daily_log  f , discom d where month(meter_date)="
				+ currentMonth + " and year(meter_date)=" + currentYear + " and " + discom + " and " + project
				+ " group by meter_date , discom_id";
		if (dataRequest.getType().equalsIgnoreCase("daily")) {
			sql = "select f.meter_date Date , sum(instant_cum_KW_Max*meter_ct_mf*meter_mwh_mf)/1000 load1,sum(consumed_KVAh*meter_ct_mf*meter_mwh_mf)/1000 energySupplied,d.name disom from feeder_data_daily_log  f , discom d where meter_date='"
					+ previousDate1 + "'  and " + discom + " and " + project + " group by meter_date , d.discom_id";
		}
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("Date", new StringType())
				.addScalar("load1", new DoubleType()).addScalar("energySupplied", new DoubleType())
				.addScalar("disom", new StringType())
				.setResultTransformer(Transformers.aliasToBean(MaxLoadModel.class));
		return query.list();
	}
	
	public List<Common> getSuspiciousOutage(DataRequest dataRequest) {
		String discom=null;
		String zone=null;
		String circle=null;
		String division=null;
		String substation=null;
		String sensor=null;
		String fromDate=null;
		String toDate=null;
		int numOfDays=0;
		String outageType=null;
		String outage=null;
		String projectType=null;
	   if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
		   discom = "  u.discom_id ='" +dataRequest.getDiscom_id()+ "' ";}
	   else {
		   discom = "  u.discom_id = u.discom_id ";}
	   if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
		   zone = " and u.zone_id ='" +dataRequest.getZone_id()+ "' ";}
	   else {
		   zone = " and u.zone_id = u.zone_id ";}
	   if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
		   circle = " and u.circle_id ='"+ dataRequest.getCircle_id()+"' ";
	   else
		   circle = " and u.circle_id = u.circle_id ";
	
	   if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
		   division = " and u.division_id ='"+dataRequest.getDivision_id()+ "' ";
	   else
		   division = " and u.division_id = u.division_id ";
	
	   if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
		   substation = " and u.site_id ='"+dataRequest.getSubstation_id()+ "' ";
	   else
		   substation = " and u.site_id = u.site_id ";
	   if (dataRequest.getFeeder_id()!=null && !dataRequest.getFeeder_id().equalsIgnoreCase("ALL"))
		   sensor = " and u.parent_sensor_id ='" +dataRequest.getFeeder_id()+ "' ";
	   else
		   sensor = " and u.parent_sensor_id = u.parent_sensor_id ";
	   if (dataRequest.getFromTime()!=null)
		   fromDate = dataRequest.getFromTime();
	   if (dataRequest.getToTime()!=null)
		   toDate =dataRequest.getToTime();
	   
       outageType = dataRequest.getOutageType();
       if (outageType.equalsIgnoreCase("TOTAL_INTERRUPTION")) {
    	   outage = " and u.event_data >= p.uppcl_outage_consider_sec ";
       }
       if (outageType.equalsIgnoreCase("LESS_THAN_15")) {
    	   outage = " and u.event_data <= 900.00 and u.event_data >= p.uppcl_outage_consider_sec  ";
       }
       if (outageType.equalsIgnoreCase("BETWEEN_15_TO_60")) {
    	   outage = " and u.event_data > 900.00 and u.event_data <= 3600.00";
       }
       if (outageType.equalsIgnoreCase("MORE_THAN_60")) {
    	   outage = " and u.event_data > 3600.00 and u.event_data >= p.uppcl_outage_consider_sec";
       }
       
       if (outageType.equalsIgnoreCase("MORE_THAN_15")) {
    	   outage = " and u.event_data > 3240000.00 and u.event_data >= p.uppcl_outage_consider_sec";
       }
       
       if (!dataRequest.getProject_id().equalsIgnoreCase("ALL"))
           projectType = " and u.project_id = trim('"+dataRequest.getProject_id()+"') ";
       else
    	   projectType = " and u.project_id = u.project_id ";
	    try {
	    	Date date1=null;
	    	Date date2=null;
	    	
			 if(fromDate==null && toDate==null)
	    	 {
	    		 fromDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	    		 toDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	    	 }
			 if(fromDate!=null && toDate!=null)
		    	{
				  date1=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
				  date2=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
				  long diff = date2.getTime()-date1.getTime();
				  numOfDays = (int) (diff/(1000*60*60*24));
		    	}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	  
        
       
	String sql="select sen.id sensorId,"
			+ "sen.project_id, "
			+ "d.name discomName , "
			+ "z.name zoneName ,"
			+ "c.name circleName, "
			+ "di.name divisionName,"
			+ "s.name ssName,"
			+ "sen.name feederName, "
			+ "number_of_gateway feederCount, "
			  +"u.event_date eventDate,"
			  + "count(event_date) OutageCount , "
			  + "SEC_TO_TIME(sum(event_data)) totalOutageDuration, "
			  + "'"+numOfDays+"' numOfDays , "
			  		+ "'" +fromDate+"' fromDate , "
			  				+ "'"+toDate+"' toDate , " 
	          +"count(event_date)/"+numOfDays+ " avgOutageCount,"
	          		+ "LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avgOutageDuration,"
	          		+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/"+numOfDays+ ")),8) avgOutageDurationPerDay,"
	          				+ "sen.serial_no as serialNo,"
	          				+ "RIGHT(sen.appliance_id, 10) mcUID "    
	          +"from feeder_daily_statistics_final u ,site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c   where ";
		   sql+=discom+zone+circle+division+substation+sensor;    
		   sql+=" and u.discom_id = d.id and u.zone_id = z.id and u.division_id = di.id and u.circle_id = c.id and u.event_date between '" +fromDate+"' and '"+toDate+"' and u.site_id=s.id and u.status not in ('D','M') and u.process_status not in ('D','M','U','V') "
			  +" and sen.id=u.parent_sensor_id	 and sen.type='AC_METER' ";
		   sql+=outage+ projectType+ " group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id , u.parent_sensor_id";
		   
   Query qry=sessionFactory.getCurrentSession().createSQLQuery(sql);
   List<Object[]> ls=qry.list();
   List<Common> dataList=new ArrayList();
   Iterator itr=ls.iterator();
   Common dataObj=null;
   while(itr.hasNext())
   {
	   dataObj=new Common();
	   Object[] ob=(Object[])itr.next();
	   if(ob[0]!=null) {
		   dataObj.setSensorId(ob[0].toString());}
	   if(ob[1]!=null) {
	   dataObj.setProjectName(ob[1].toString());}
	   if(ob[2]!=null){
		dataObj.setDiscomName(ob[2].toString());  }
	   if(ob[3]!=null) {
		dataObj.setZoneName(ob[3].toString()); }
	   if(ob[4]!=null) {
		dataObj.setCircleName(ob[4].toString());}
	   if(ob[5]!=null) {
		dataObj.setDivisionName(ob[5].toString());}
	   if(ob[6]!=null) {
		dataObj.setSsName(ob[6].toString());}
	   if(ob[18]!=null) {
			dataObj.setMeterType(ob[18].toString());
			dataObj.setSerialNo(ob[18].toString());
	   }
	   if(ob[19]!=null) {
			dataObj.setMcUID(ob[19].toString());}
	   if(ob[7]!=null)
		   dataObj.setFeederName(ob[7].toString());
	   if(ob[10]!=null)
		   dataObj.setOutageCount(ob[10].toString());
	   if(ob[11]!=null)
		   dataObj.setAvgOutageDuration(ob[11].toString());
	   if(ob[15]!=null) {
		   dataObj.setAvgOutageCount(ob[15].toString());
		   dataObj.setRoundAvgOutageCount(Math.round(Float.parseFloat(ob[15].toString())));
	   }
	   if(ob[17]!=null)
		   dataObj.setAvgOutageDurationPerDay(ob[17].toString());
	   if(ob[16]!=null)
		   dataObj.setAvgOutageDuration(ob[16].toString());
	   dataObj.setFromDate(fromDate);
	   dataObj.setToDate(toDate);
	   dataObj.setNumOfDays(String.valueOf(numOfDays));
   }
   dataList.add(dataObj);
   return dataList;
}
	
	public List<FeederDailyStatisticFinalBean> getTenWorstFeeder(String projectId, String orderBy,
			int limitby) {
		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = " select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ " u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, "
				+ " 1 no_of_day , subdate(current_date, 1) from_date , subdate(current_date, 1) to_date , "
				+ " count(event_date) avg_outage_count" 
				+ " ,LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ " LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/1"
				+ " )),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID   "
				+ " from feeder_daily_statistics_final u , "
				+ " site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ " u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ " and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ " and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ " and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ " and u.circle_id = c.id " + "and u.event_date between subdate(current_date, 1) and  subdate(current_date, 1) "
				+ " and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ " and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ " and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + " Desc limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}
	

	public List<FeederDailyStatisticFinalBean> findSixWorstFeeder(String projectId, String orderBy,
			int limitby,String... date) {
		String from=null;
		String to=null;
		int month=0;
		int dayofMonth=0;
		int pmonth=0;
		String dayOfMonth=null;
		String dateDifference = "31";
		Calendar cal = Calendar.getInstance();
		if(date[0]!=null && date[1]==null && date[2]==null)
		{
				from = date[0].trim() + "-1";
				to = date[0].trim() + "-31";
				Date date1 = new Date();
				cal.setTime(date1);
				month = (cal.get(Calendar.MONTH)) + 1;
				dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
				dayOfMonth = String.valueOf(dayofMonth);
				String dateString[] = date[0].split("-");
				pmonth = Integer.parseInt(dateString[1]);
				if (month == pmonth) {
					to = date[0].trim() + "-" + dayOfMonth;
					dateDifference = dayOfMonth;
				} else {
					LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
					LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
					to = end.toString();
					String datedif[] = to.split("-");
					dateDifference = datedif[2];
				}
		}
		else if(date[0]==null && date[1]!=null && date[2]!=null)
		{
			from = date[1].trim() + "-01";
			to = date[2].trim() + "-31";
			LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			dateDifference = String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate)+1);
		}
		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count,LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID   "
				+ "from feeder_daily_statistics_final u  , "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + " Desc limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FeederDailyStatisticFinalBean> findSixWorstFeeder(String date, String projectId,String orderBy,
			int limitby) {
		String from = date.trim() + "-1";
		String to = date.trim() + "-31";
		String dateDifference = "31";
		Date date1 = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int month = (cal.get(Calendar.MONTH)) + 1;
		int dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfMonth = String.valueOf(dayofMonth);
		String dateString[] = date.split("-");
		int pmonth = Integer.parseInt(dateString[1]);
		if (month == pmonth) {

			to = date.trim() + "-" + dayOfMonth;
			dateDifference = dayOfMonth;

		} else {

			LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
			LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
			to = end.toString();
			String datedif[] = to.split("-");
			dateDifference = datedif[2];
		}

		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count,LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID   "
				+ "from feeder_daily_statistics_final u use index (feederDailyStats) , "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id    " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + " desc limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	
}

	public List<Search> getMeterSearch(String meterNo) {

		String sql = "select u.id id,u.data_logger_id data_logger_id, u.serial_no meter_sr_no , dis.short_name discom_name, z.name zone_name,"
				+ " c.name circle_name,di.name division_name,subs.name substation_name,u.contactor_status contactor_status, "
				+ "u.meter_rtc_cal meter_rtc_cal,u.kWh KWh ,u.kVAh KVAh,u.uom uom,u.meter_ct_mf meter_ct_mf,"
				+ "u.max_demand_KVA max_demand_KVA,u.max_demand_time max_demand_time,d.MC_UID MC_UID,(case when u.admin_status='M' then 'Maintenance'  when u.admin_status='U' then 'LM' when u.admin_status='S' then 'Live' "
				+ "when u.admin_status='N' then 'Live' when u.admin_status='F' then 'Faulty' when u.admin_status='D' then 'Discard' when u.admin_status='R' then 'Relocation State'  "
				+ "when u.admin_status='C' then 'Not Communicating' else 'Not Defined' end) admin_status,u.project_id project,u.name feeder_name,u.digital_input2  Incommer_status,u.digital_input3 CB_status,u.last_packet_time last_packet_time "
				+ "from sensor u,datalogger d, discom dis,zone z,circle c,division di,site subs where u.data_logger_id=d.id  and u.discom_id = dis.id  and u.zone_id = z.id  and u.circle_id = c.id  and u.division_id = di.id  and u.site_id = subs.id "
				+ "and u.type='AC_METER'  and (u.serial_no like '%" + meterNo + "%' or d.MC_UID like '%" + meterNo
				+ "%' or u.data_logger_id like '%" + meterNo + "%')";
		Query qry = sessionFactory.getCurrentSession().createSQLQuery(sql).
				addScalar("id",new StringType()).addScalar("data_logger_id",new StringType()).addScalar("meter_sr_no",new StringType())
		.addScalar("discom_name",new StringType())
		.addScalar("zone_name",new StringType())
		.addScalar("circle_name",new StringType())
		.addScalar("division_name",new StringType())
		.addScalar("feeder_name",new StringType())
		.addScalar("project",new StringType())
		.addScalar("KWh",new StringType())
		.addScalar("KVAh",new StringType())
		.addScalar("uom",new StringType())
	    .addScalar("substation_name",new StringType())
	    .addScalar("max_demand_time",new StringType())
	    .addScalar("last_packet_time",new StringType())
	    .addScalar("meter_ct_mf",new StringType())       
	    .addScalar("max_demand_KVA",new StringType())
	    .addScalar("contactor_status",new StringType())
	    .addScalar("CB_status",new StringType())
	    .addScalar("Incommer_status",new StringType())
	    .addScalar("MC_UID",new StringType())
	    .addScalar("meter_rtc_cal",new StringType()).setResultTransformer(Transformers.aliasToBean(Search.class));;
		List<Search> Search =qry.list();
		return Search;
	}
	
	@Override
	public List<Sensor> getFeederStatisticsDown() {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		String sql = "";
		Query query;
		
		sql = "select id id, name name, project_id project_id,discom_id discomId, admin_status admin_status,last_packet_time last_packet_time from sensor s where type='AC_METER' and admin_status in ('N','S','U') and (last_packet_time < NOW() - INTERVAL 1 WEEK)";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("id", new StringType()).addScalar("name", new StringType())
				.addScalar("project_id", new StringType()).addScalar("discomId", new StringType())
				.addScalar("admin_status", new StringType()).addScalar("last_packet_time", new StringType())
				.setResultTransformer(Transformers.aliasToBean(Sensor.class));
		sensorList = query.list();
		return sensorList;
	}
	
	@Override
	public List<Sensor> getFeederStatisticsUp() {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		String sql = "";
		Query query;
		
		sql = "select id id, name name, project_id project_id,discom_id discomId, admin_status admin_status,last_packet_time last_packet_time from sensor s where type='AC_METER' and admin_status in ('N','S','U') and (last_packet_time > NOW() - INTERVAL 1 WEEK) ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("id", new StringType()).addScalar("name", new StringType())
				.addScalar("project_id", new StringType()).addScalar("discomId", new StringType())
				.addScalar("admin_status", new StringType()).addScalar("last_packet_time", new StringType())
				.setResultTransformer(Transformers.aliasToBean(Sensor.class));
		sensorList = query.list();
		return sensorList;
	}
	
	@Override
	public List<Sensor> getFeederStatisticsDownList() {
		
		List<Sensor> sensorList = new ArrayList<Sensor>();
		String sql = "";
		Query query;
		
		sql = "select * from sensor s where type='AC_METER' and admin_status in ('N','S','U') and (last_packet_time < NOW() - INTERVAL 1 WEEK)";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(Sensor.class);
		
		sensorList = query.list();
		return sensorList;
	}

	@Override
	public List<Sensor> getFeederStatisticsUpList() {
	
		List<Sensor> sensorList = new ArrayList<Sensor>();
		String sql = "";
		Query query;
		
		sql = "select * from sensor s where type='AC_METER' and admin_status in ('N','S','U') and (last_packet_time > NOW() - INTERVAL 1 WEEK)";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(Sensor.class);
			
		sensorList = query.list();
		return sensorList;
	}

	@Override
	public List<Sensor> getSensorList(String fromLimit, String toLimit) {
		List<Sensor> sensors = new ArrayList<Sensor>();
		
		String sql = "";
		Query query;
		/*sql = "select name name, serial_no serial_no, appliance_id appliance_id, KWh KWh, KVAh KVAh,"
		+"max_demand_KW md_kw, max_demand_KVA md_kVa, instant_cum_KW instant_cum_KW,"
		+"R_Voltage R_Voltage, Y_Voltage Y_Voltage, B_Voltage B_Voltage, R_Current R_Current, Y_Current Y_Current, B_Current B_Current,"
		+"R_PF R_PF , Y_PF Y_PF, B_PF B_PF, meter_rtc_cal meter_rtc_cal from sensor where remark != 'Success' and admin_status in ('N','S','U') and type='AC_METER' order by remark"
		+" limit " + fromLimit + "," + toLimit + "";*/
		sql = "FROM Sensor where remark != 'Success' and admin_status in ('N','S','U') and type='AC_METER' order by remark";
				
		query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setFirstResult(Integer.parseInt(fromLimit));
		query.setMaxResults(Integer.parseInt(toLimit));
		
		
		 sensors = query.list();
	
	return sensors;
		
		}
	
	@Override
	public List<SupplyReport> supplyReport(String date) {
		String sql = "";
		sql = "select s.discom_id discomId , s.project_id project , s.name name ,s.serial_no serialNo, s.appliance_id gatewayId, left(SEC_TO_TIME(24*60*60),8) totalSupply,"
				+ " case when sum(f.event_data) "
				+ " is null  then 0 else left(SEC_TO_TIME(sum(f.event_data)),8) end as outageDuration, case "
				+ " when sum(f.event_data) is null  then 24*60*60 when sum(f.event_data) > 24*60*60  then 24*60*60 "
				+ " else (24*60*60-sum(f.event_data)) end as totalOutage,case when sum(f.event_data) is null  "
				+ " then left(SEC_TO_TIME(24*60*60),8) else left(SEC_TO_TIME(24*60*60-sum(f.event_data)),8) end as supplyDuration from sensor s left join feeder_daily_statistics_final f "
				+ " on  f.parent_sensor_id=s.id and f.event_date='" + date + "' where s.type='AC_METER'"
				+ " and  s.admin_status in ('N','S') and s.meter_type='2' and f.process_status not in ('M','D') group by s.id";
		System.out.println(sql);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("discomId", new StringType()).addScalar("project", new StringType())
				.addScalar("name", new StringType()).addScalar("serialNo", new StringType())
				.addScalar("gatewayId", new StringType()).addScalar("totalSupply", new StringType())
				.addScalar("outageDuration", new StringType()).addScalar("totalOutage", new StringType())
				.addScalar("supplyDuration", new StringType()).setResultTransformer(Transformers.aliasToBean(SupplyReport.class));
		//Query query = sessionFactory.getCurrentSession().createQuery(sql);
        List<SupplyReport> ls = query.list();
        return ls;

	}
	
	@Override
	public RAPDRPDataResponse getPendingComplainDetails() {
		String sql = "";
		Query query;		
		
		sql="select meter_date from secure_interruption_data order by meter_date desc limit 1";
		query=sessionFactory.getCurrentSession().createSQLQuery(sql);
        Object meterDate=query.uniqueResult();
        System.out.println("**********+:" + meterDate);
        if(meterDate==null)
        {
        	meterDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());        	
        }
        
		List<InterruptionBean> interruptionBeanList = new ArrayList<InterruptionBean>();
		List<MeterReadBean> meterReadBeanList = new ArrayList<MeterReadBean>();
		List<MeterdataNABean> meterdataNABeanList = new ArrayList<MeterdataNABean>();
		List<Meterdata1912Bean> meterdata1912BeanList = new ArrayList<Meterdata1912Bean>();
		RAPDRPDataResponse dr = new RAPDRPDataResponse();
		dr.setRc(-1);
		dr.setMessage("Error ");
		
		sql = "SELECT  s.discom_name, s.town ,count(DISTINCT s.meter_serial_no) total_feeder, sum(s.total_interruption_count)/count(DISTINCT s.meter_serial_no) interruption_count,sum(s.total_interruption_duration)/count(DISTINCT s.meter_serial_no) interruption_duration,"
                +" sum(s.total_interruption_duration) total_interruption_duration, "
                +" sum(s.total_interruption_count) total_interruption_count,"
                +" r.complain_registered  complain_registered , r.closed_registered closed_registered, r.pending_complain pending_complain"
                +" FROM secure_interruption_data s , rapdrp_complain_log r  where s.meter_date = '" + meterDate + "' and s.discom_name=s.discom_name "
                +" and r.ss_name = s.ss_name  and r.town_name=s.town group by s.discom_name , s.town ";
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom_name", new StringType())
				.addScalar("town", new StringType()).addScalar("total_feeder", new StringType())
				.addScalar("interruption_count", new StringType()).addScalar("interruption_duration", new StringType())
				.addScalar("total_interruption_duration", new StringType()).addScalar("total_interruption_count", new StringType())
				.addScalar("complain_registered", new StringType()).addScalar("closed_registered", new StringType())
				.addScalar("pending_complain", new StringType())
				.setResultTransformer(Transformers.aliasToBean(InterruptionBean.class));
		interruptionBeanList = query.list();		
		
		sql = "SELECT discom_name , town, count(meter_serial_no) notripcount "
                +" FROM `secure_interruption_data` where  meter_date = '" + meterDate + "'  and discom_name=discom_name and   "
                +"  trim(remark)='No Tripping Data Available' "
                +"  group by discom_name,town";
		
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom_name", new StringType())
				.addScalar("town", new StringType()).addScalar("notripcount", new StringType())				
				.setResultTransformer(Transformers.aliasToBean(MeterReadBean.class));
		meterReadBeanList = query.list();		
		
		sql = "SELECT discom_name , town , count(meter_serial_no) nodatacount "
                +" FROM `secure_interruption_data` where meter_date = '" + meterDate + "' and discom_name=discom_name and "
                +" trim(remark) in ('Meter data not available','Meter Not Read') "
                +" group by discom_name , town ";
		
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom_name", new StringType())
				.addScalar("town", new StringType()).addScalar("nodatacount", new StringType())				
				.setResultTransformer(Transformers.aliasToBean(MeterdataNABean.class));
		meterdataNABeanList = query.list();			
		
		sql = "select town_name , complain_status ,  count(id) total, "
                +" sum(case when complain_status = 'CLOSED' then 1 else 0 end) CLOSED , sum(case when complain_status in ('out of timeline','within timeline') then 1 else 0 end) pending from rapdrp_complain_log "
                +" where date(registration_date)  = '" + meterDate + "' group by town_name ";
		
		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("town_name", new StringType())
				.addScalar("complain_status", new StringType()).addScalar("total", new StringType())
				.addScalar("CLOSED", new StringType()).addScalar("pending", new StringType())
				.setResultTransformer(Transformers.aliasToBean(Meterdata1912Bean.class));
		meterdata1912BeanList = query.list();	
		
	    dr.setRc(0);
		dr.setMessage("Success");
		dr.setInterruption(interruptionBeanList);
		dr.setMeterRead(meterReadBeanList);
		dr.setMeterdataNA(meterdataNABeanList);
		dr.setMeterdata1912(meterdata1912BeanList);
		
		return dr;		
		
	}

	@Override
    public RAPDRPDataResponse getRAPDRP1912DataDetails(final DataRequest dataRequest) {
        String sql = "";
        sql = "select registration_date from rapdrp_complain_log order by registration_date desc limit 1";
        Query query = (Query)this.sessionFactory.getCurrentSession().createSQLQuery(sql);
        String meterDate = query.uniqueResult().toString();
        if (meterDate == null && meterDate.isEmpty()) {
            meterDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        List<Meterdata1912Bean> meterdata1912BeanList = new ArrayList<Meterdata1912Bean>();
        final RAPDRPDataResponse dr = new RAPDRPDataResponse();
        dr.setRc(-1);
        dr.setMessage("Error ");
        if ("DETAILS".equalsIgnoreCase(dataRequest.getNotificationType())) {
            if ("ALL".equalsIgnoreCase(dataRequest.getDiscom_id())) {
                sql = "select discom_name , town_name ,  count(id) total,  sum(case when complain_status = 'CLOSED' then 1 else 0 end) CLOSED , sum(case when complain_status in ('PENDING BEYOND','PENDING WITHIN') then 1 else 0 end) pending, "
                		+ " sum(case when complain_status='PENDING BEYOND' then 1 else 0 end) pending_beyond , sum(case when complain_status ='PENDING WITHIN' then 1 else 0 end) pending_within from rapdrp_complain_log  where discom_name = discom_name and month(registration_date)  = month('" + meterDate + "') group by discom_name , town_name ";
            }
            else {
                sql = "select discom_name , town_name ,  count(id) total,  sum(case when complain_status = 'CLOSED' then 1 else 0 end) CLOSED , sum(case when complain_status in ('PENDING BEYOND','PENDING WITHIN') then 1 else 0 end) pending,"
                		+ " sum(case when complain_status='PENDING BEYOND' then 1 else 0 end) pending_beyond , sum(case when complain_status ='PENDING WITHIN' then 1 else 0 end) pending_within from rapdrp_complain_log  where discom_name = '" + dataRequest.getDiscom_id() + "' and  month(registration_date)  = month('" + meterDate + "') group by discom_name , town_name ";
            }
            query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom_name",new StringType())
            		.addScalar("town_name",  new StringType()).addScalar("total", new StringType())
            		.addScalar("CLOSED",  new StringType())
            		.addScalar("pending",  new StringType())
            		.addScalar("pending_beyond",  new StringType())
            		.addScalar("pending_within",  new StringType())
            		.setResultTransformer(Transformers.aliasToBean(Meterdata1912Bean.class));
        }
        else {
            sql = "select discom_name ,  count(distinct town_name) total,  sum(case when complain_status = 'CLOSED' then 1 else 0 end) CLOSED , sum(case when complain_status in ('PENDING BEYOND','PENDING WITHIN') then 1 else 0 end) pending,"
            		+ " sum(case when complain_status='PENDING BEYOND' then 1 else 0 end) pending_beyond , sum(case when complain_status ='PENDING WITHIN' then 1 else 0 end) pending_within from rapdrp_complain_log  where month(registration_date)  = month('" + meterDate + "') group by discom_name ";
            query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
            		.addScalar("discom_name",  new StringType())
            		.addScalar("total",  new StringType())
            		.addScalar("CLOSED",  new StringType())
            		.addScalar("pending",  new StringType())
            		.addScalar("pending_beyond",  new StringType())
            		.addScalar("pending_within",  new StringType())
            		.setResultTransformer(Transformers.aliasToBean(Meterdata1912Bean.class));
        }
        meterdata1912BeanList = (List<Meterdata1912Bean>)query.list();
        dr.setRc(0);
        dr.setMessage("Success");
        dr.setMeterdata1912(meterdata1912BeanList);
        return dr;
    }

	   @Override
	    public List<Object[]> getRECData() {
	        final String qryToGetTotalInstalled = "select count(rec.id) from rec_sensor s  , rec_meter_master rec where s.discom_name = s.discom_name and s.circle_name = s.circle_name and s.division_name = s.division_name and s.admin_status in ('N','S') and rec.id=s.serial_no and rec.id NOT LIKE '%.%' order by name;";
	        final Query installedqry = (Query)this.sessionFactory.getCurrentSession().createSQLQuery(qryToGetTotalInstalled);
	        final String install = installedqry.uniqueResult().toString();
	        final String qryToGetTotalUp = "select count(s.id) from rec_sensor s  , rec_meter_master rec where s.discom_name = s.discom_name and s.circle_name = s.circle_name and s.division_name = s.division_name and s.admin_status in ('N','S') and rec.id=s.serial_no and s.id NOT LIKE '%.%' and date(s.last_reading_updated) = date(now()) order by name";
	        final Query upQry = (Query)this.sessionFactory.getCurrentSession().createSQLQuery(qryToGetTotalUp);
	        final String up = upQry.uniqueResult().toString();
	        final String qryToGetTotalDown = "select count(rec.id) from rec_sensor s  , rec_meter_master rec where 1 and s.discom_name = s.discom_name and s.circle_name = s.circle_name and s.division_name = s.division_name and s.admin_status in ('N','S') and rec.id=s.serial_no and rec.id NOT LIKE '%.%' and date(s.last_reading_updated) < date(now()) order by name;";
	        final Query downQuery = (Query)this.sessionFactory.getCurrentSession().createSQLQuery(qryToGetTotalDown);
	        final String down = downQuery.uniqueResult().toString();
	        final Object[] obj = new Object[3];
	        final List listObj = new ArrayList();
	        obj[0] = install;
	        obj[1] = up;
	        obj[2] = down;
	        listObj.add(obj);
	        return (List<Object[]>)listObj;
	    }

	
	/*@SuppressWarnings("unchecked")
	@Override
	public List<FeederDailyStatisticFinalBean> getBestFeederHeirarchy(String projectId, String orderBy,
			int limitby,String... date) {
		String from=null;
		String to=null;
		int month=0;
		int dayofMonth=0;
		int pmonth=0;
		String dayOfMonth=null;
		String dateDifference = "31";
		Calendar cal = Calendar.getInstance();
		if(date[0]!=null && date[1]==null && date[2]==null)
		{
			from = date[0].trim() + "-1";
			to = date[0].trim() + "-31";
			Date date1 = new Date();
			cal.setTime(date1);
			month = (cal.get(Calendar.MONTH)) + 1;
			dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
			dayOfMonth = String.valueOf(dayofMonth);
			String dateString[] = from.split("-");
			pmonth = Integer.parseInt(dateString[1]);
			if (month == pmonth) {
				to = to.trim() + "-" + dayOfMonth;
				dateDifference = dayOfMonth;
			} else {
				LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
				LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
				to = end.toString();
				String datedif[] = to.split("-");
				dateDifference = datedif[2];
			}
		}
		else if(date[0]==null && date[1]!=null && date[2]!=null)
		{
				 	from = date[1].trim() + "-01";
					to = date[2].trim() + "-31";
					LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					dateDifference = String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate)+1);
				 	
		}
		

		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count, LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID "
				+ "from feeder_daily_statistics_final u , "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + "  limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}*/
	
	public List<FeederDailyStatisticFinalBean> getWorstFeederHeirarchy(String projectId, String orderBy,
			int limitby,String... date) {
		String from=null;
		String to=null;
		int month=0;
		int dayofMonth=0;
		int pmonth=0;
		String dayOfMonth=null;
		String dateDifference = "31";
		Calendar cal = Calendar.getInstance();
		if(date[0]!=null && date[1]==null && date[2]==null)
		{
				from = date[0].trim() + "-1";
				to = date[0].trim() + "-31";
				Date date1 = new Date();
				cal.setTime(date1);
				month = (cal.get(Calendar.MONTH)) + 1;
				dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
				dayOfMonth = String.valueOf(dayofMonth);
				String dateString[] = date[0].split("-");
				pmonth = Integer.parseInt(dateString[1]);
				if (month == pmonth) {
					to = date[0].trim() + "-" + dayOfMonth;
					dateDifference = dayOfMonth;
				} else {
					LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
					LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
					to = end.toString();
					String datedif[] = to.split("-");
					dateDifference = datedif[2];
				}
		}
		else if(date[0]==null && date[1]!=null && date[2]!=null)
		{
			from = date[1].trim() + "-01";
			to = date[2].trim() + "-31";
			LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			dateDifference = String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate)+1);
		}
		List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
		String sql = "select * from (select sen.id sensor_id, d.name Discom_name , z.name Zone_name ,c.name Circle_name, di.name Division_name,s.name SS_name,sen.name feeder_name, number_of_gateway feeder_count,"
				+ "u.event_date event_date,count(event_date) Outage_count ,sum(event_data) Total_Outage_Duration, '"
				+ dateDifference + "' no_of_day , '" + from + "' from_date , '" + to + "' to_date , "
				+ " count(event_date)/'" + dateDifference
				+ "' avg_outage_count,LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avg_outage_duration,"
				+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/'" + dateDifference
				+ "')),8) avg_outage_duration_per_day,sen.serial_no as serial_no,RIGHT(sen.appliance_id, 10) MC_UID   "
				+ "from feeder_daily_statistics_final u, "
				+ "site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c " + "where "
				+ "u.discom_id =u.discom_id " + "and u.zone_id = u.zone_id " + "and u.circle_id = u.circle_id  "
				+ "and u.division_id = u.division_id " + "and u.site_id = u.site_id  "
				+ "and u.parent_sensor_id = u.parent_sensor_id     " + "and u.discom_id = d.id "
				+ "and u.zone_id = z.id " + "and u.project_id='" + projectId + "' " + "and u.division_id = di.id "
				+ "and u.circle_id = c.id " + "and u.event_date between '" + from + "' and  '" + to + "' "
				+ "and u.site_id=s.id " + "and u.status not in ('D', 'M') and u.process_status not in ('M','D') "
				+ "and sen.id=u.parent_sensor_id " + "and sen.type='AC_METER' "
				+ "and u.project_id = u.project_id  group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id) record order by "
				+ orderBy + " Desc limit " + limitby + "";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType())
				.addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType())
				.addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType())
				.addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType())
				.addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType())
				.addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType())
				.addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType())
				.addScalar("avg_outage_duration", new StringType())
				.addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("MC_UID", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederDailyStatisticFinalBean.class));
		feederDailyStatisticFinal = query.list();
		return feederDailyStatisticFinal;
	}

	
	
	@Override
	public List<SupplyStatus> supplyStatus(String date , String project) {
		String projectType="";
		String sql = "";
		if (!project.equalsIgnoreCase("ALL"))
	           projectType = " and s.project_id = trim('"+project+"') ";
	       else
	    	   projectType = " and s.project_id = s.project_id ";
	
	 sql= " select project, discom_id,d.short_name discom , left(SEC_TO_TIME(avg(seconds)),8) supply_duration ,left(SEC_TO_TIME(24*60*60-avg(seconds)),8) Outage_duration, count(id) count ";
     sql+= " from (select s.discom_id discom_id , s.project_id project , s.name,s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) ";
     sql+= " is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds,  case when sum(f.event_data) is null ";  
     sql+= " then 0 else SEC_TO_TIME(sum(f.event_data)) end as duration from sensor s left join feeder_daily_statistics_final f ";  
     sql+= " on f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id ";  
     sql+= " and f.site_id = s.site_id and f.sensor_id = f.sensor_id and f.event_date='"+date+"'and f.event_type is null "; 
     sql+= " and f.parent_sensor_id=s.id where s.discom_id = s.discom_id and s.zone_id = s.zone_id and s.circle_id = s.circle_id ";  
     sql+= " and s.division_id = s.division_id and s.site_id = s.site_id and s.type='AC_METER'";
 	 sql+= " "+projectType+" and  s.admin_status in ('N','S') and s.meter_type='2' group by s.id) as outage , discom d ";  
     sql+= " where d.id=outage.discom_id group by outage.discom_id order by discom";

    Query supply = sessionFactory.getCurrentSession().createSQLQuery(sql)
			.addScalar("project", new StringType()).addScalar("discom_id", new StringType())
			.addScalar("discom", new StringType())
    		.addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType())
    		.addScalar("count", new StringType())
			.setResultTransformer(Transformers.aliasToBean(SupplyStatus.class));
	
    List<SupplyStatus> ls = supply.list();
    
    return ls;
	
	}
	
	@Override
	public List<SupplyStatus> MastersupplyStatus(String fromDate,String toDate , String discom , String Hour,String ReportType,String project) {
		String discomName="";
		String sql = "";
		String dateDifference = "1";
		String projectName="";
		 final LocalDate from = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
         final LocalDate to = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		 dateDifference = String.valueOf(ChronoUnit.DAYS.between(from, to) + 1L);
		 System.out.println(dateDifference);
		if (!discom.equalsIgnoreCase("ALL"))
			   discomName = " discom_name = UPPER(trim('"+discom+"')) ";
	       else
	    	   discomName = "discom_name = discom_name ";
		if(project!=null && project.equalsIgnoreCase("all"))
			projectName=" ";
		else if(project!=null && project.equalsIgnoreCase("all"))
			projectName=" and project_id='"+project+"' ";
		else if(project!=null && project.equalsIgnoreCase("16 NN"))
			projectName=" and remark like '%"+project+"%'";
		else if(project!=null && project.equalsIgnoreCase("152"))
			projectName=" and remark like '"+project+"%' ";
		else 
			projectName=" and project_id='"+project+"' ";
		if (ReportType.equalsIgnoreCase("currentDay")) {
			if (Integer.parseInt(Hour) != 0) {
				int supplyhour = Integer.parseInt(Hour) * 60;
				sql = "select discom_id,discom_name,SUM(IF(id=id, 1, 0))/"+dateDifference+" AS totalRec,project_id project,round(avg(interruption_duration)/"+dateDifference+",2) Outage_duration , "
						+ "round(avg(supply_duration)/"+dateDifference+",2) supply_duration,count(id) count , round(SUM(IF(round(supply_duration) < '" + supplyhour+"' , 1, 0))/SUM(IF(id=id, 1, 0))*100,2) as percentage,"
						+ "round(sum(interruption_count)/" + dateDifference
						+ ",0) totInttCount from uppcl_master_data_log  where feeder_type='OUTGOING' and   event_date between '"
						+ fromDate + "' AND '" + toDate + "' " + "and " + discomName + projectName
						+ "  and supply_duration <= " + supplyhour + " group by discom_name";

			} else {

				sql = "select discom_id,discom_name,SUM(IF(id=id, 1, 0))/"+dateDifference+" AS totalRec,project_id project,"
						+ "round(avg(interruption_duration)/"+dateDifference+",2) Outage_duration , "
						+ "round(avg(supply_duration)/"+dateDifference+",2) supply_duration,count(id) count , 1 as percentage, "
						+ "round(sum(interruption_count)/" + dateDifference
						+ ",0) totInttCount from uppcl_master_data_log  where  feeder_type='OUTGOING' and  event_date between '"
						+ fromDate + "' AND '" + toDate + "' " + " and " + discomName + projectName
						+ " group by discom_name ";

			}

		}
		
		if (ReportType.equalsIgnoreCase("PastDaysAvg")) {
			if (Integer.parseInt(Hour) != 0) {
				int supplyhour = Integer.parseInt(Hour) * 60;
				sql = " select discom_id,discom_name,Rd,Sd,project, round((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0))/"
						+ dateDifference
						+ ",0) Outage_duration , round(((24*60)-((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)))/"
						+ dateDifference
						+ "),0) supply_duration, count(id) count,(IFNULL(count1,0) +IFNULL(intcount1,0)) as totInttCount  from (select umd.id,umd.discom_id discom_id, umd.project_id project, umd.discom_name discom_name, umd.serial_no ,";
				sql += " fds.duration Rd, sid.duration Sd,fds.count1 count1,sid.intcount intcount1 from uppcl_master_data umd   LEFT JOIN (SELECT parent_sensor_id, "
						+ " round(sum(event_data)/60) as duration,count(id) count1 ";
				sql += " FROM feeder_daily_statistics_final ";
				sql += " where ";
				sql += " parent_sensor_id=parent_sensor_id";
				sql += " and  event_date between '" + fromDate + "-INTERVAL 1 WEEK' and '" + toDate + "' ";
				sql += " and creation_time=creation_time and status='P' GROUP BY parent_sensor_id ) fds ON fds.parent_sensor_id = trim(umd.parent_sensor_id)";
				sql += " LEFT JOIN (SELECT meter_serial_no, sum(total_interruption_duration) ";
				sql += " as duration,sum(total_interruption_count) as intcount FROM secure_interruption_data where meter_date between '"
						+ fromDate + "-INTERVAL 1 WEEK' and '" + toDate + "' GROUP BY meter_serial_no )";
				sql += " sid ON sid.meter_serial_no = trim(umd.serial_no) where (round(24*60)-(IFNULL(fds.duration, 0)+ IFNULL(sid.duration, 0))/"
						+ dateDifference + ",0) <= '" + supplyhour + "') ) as master  where " + discomName
						+ "   group by discom_name;";

			} else {
				sql = " select discom_id,discom_name,project, (IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)/" + dateDifference
						+ ") Outage_duration , ((24*60)-((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)))/" + dateDifference
						+ ") supply_duration, count(id) count,(IFNULL(count1,0) +IFNULL(intcount1,0)) as totInttCount  from (select umd.id,umd.discom_id discom_id, umd.project_id project, umd.discom_name discom_name, umd.serial_no ,";
				sql += " fds.duration Rd, sid.duration Sd,fds.count1 count1,sid.intcount intcount1 from uppcl_master_data umd   LEFT JOIN (SELECT parent_sensor_id, "
						+ " round(sum(event_data)/60) as duration,count(id) count1 ";
				sql += " FROM feeder_daily_statistics_final ";
				sql += " where ";
				sql += " parent_sensor_id=parent_sensor_id";
				sql += " and  event_date between '" + fromDate + "-INTERVAL 1 WEEK' and '" + toDate + "' ";
				sql += " and creation_time=creation_time and status='P' GROUP BY parent_sensor_id ) fds ON fds.parent_sensor_id = trim(umd.parent_sensor_id)";
				sql += " LEFT JOIN (SELECT meter_serial_no, sum(total_interruption_duration) ";
				sql += " as duration,sum(total_interruption_count) as intcount FROM secure_interruption_data where meter_date between '"
						+ fromDate + "-INTERVAL 1 WEEK' and '" + toDate + "' GROUP BY meter_serial_no )";
				sql += " sid ON sid.meter_serial_no = trim(umd.serial_no)) as master  where " + discomName
						+ " group by discom_name;";

			}
		}
	    	Query supply = sessionFactory.getCurrentSession().createSQLQuery(sql)
			.addScalar("project", new StringType()).addScalar("discom_id", new StringType())
			.addScalar("discom_name", new StringType())
    		.addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType())
    		.addScalar("count", new StringType())
    		.addScalar("totInttCount", new StringType())
    		.setResultTransformer(Transformers.aliasToBean(SupplyStatus.class));
		
    List<SupplyStatus> ls = supply.list();
			
    return ls;
		
	}
	

	@Override
	public List<FeederData> getFeederStatHistorySensorDetails(String project_id, String feederStatus) {

		List<FeederData> FeederDatalist = new ArrayList<FeederData>();
		String sql = "";
		String con1 = "";
		String con2 = "";
		Query query;
		if ("UP".equalsIgnoreCase(feederStatus)) {
			con1 = " (s.last_packet_time > NOW() - INTERVAL 1 WEEK)  ";
		} else if ("DN".equalsIgnoreCase(feederStatus)) {
			con1 = " (s.last_packet_time < NOW() - INTERVAL 1 WEEK)  ";
		}
		if ("Industrial".equalsIgnoreCase(project_id)) {
			con2 = " s.project_id='EODB' ";
		} else if ("Urban".equalsIgnoreCase(project_id)) {
			con2 = " s.project_id in ('IPDS','EODB')";
		} else {
			con2 = " s.project_id=s.project_id";
		}
		sql = "select s.id,s.sub_type sub_type,device_type,s.remark,s.grid_load_alarm,s.dg_load_alarm,TRUNCATE(s.max_demand_KVA,2) as max_demand_KVA, "
				+ " s.dic_id dic_id ,s.device_state device_state,s.csq_signal_strength csq, s.dic_port dic_port,s.UOM uom,concat(site.name,' ',s.name) as name ,"
				+ " s.serial_no as serial_no,TRUNCATE(s.KWh,2) as KWh, TRUNCATE(s.KVAh,2) as KVAh,TRUNCATE(s.instant_R_KW,2) As instant_R_KW,"
				+ " TRUNCATE(s.instant_cum_KW,2) as instant_cum_KW, TRUNCATE(s.instant_cum_KVA,2) as instant_cum_KVA,"
				+ " s.R_Voltage R_Voltage,s.Y_Voltage Y_Voltage,s.B_Voltage B_Voltage,"
				+ " TRUNCATE(s.R_Current,2) as R_Current, TRUNCATE(s.Y_Current,2) as Y_Current, TRUNCATE(s.B_Current,2) as B_Current,"
				+ " s.R_PF,s.Y_PF,s.B_PF,s.cumm_pf,"
				+ " TRUNCATE(s.KWh1,2) KWh1,TRUNCATE(s.KWh2,2) KWh2,TRUNCATE(s.KWh3,2) KWh3,TRUNCATE(s.KWh4,2) KWh4, TRUNCATE(s.KWh5,2) KWh5,TRUNCATE(s.KWh6,2) KWh6,TRUNCATE(s.KWh7,2) KWh7,TRUNCATE(s.KWh8,2) KWh8,"
				+ " TRUNCATE(s.KVAh1,2) KVAh1,TRUNCATE(s.KVAh2,2) KVAh2,TRUNCATE(s.KVAh3,2) KVAh3, TRUNCATE(s.KVAh4,2) KVAh4,TRUNCATE(s.KVAh5,2) KVAh5,TRUNCATE(s.KVAh6,2) KVAh6, TRUNCATE(s.KVAh7,2) KVAh7, TRUNCATE(s.KVAh8,2) KVAh8,"
				+ " z.name zone_name,c.name circle_name,di.name division_name,site.name substation_name,"
				+ " s.last_reading_updated,s.frequency,s.connected,"
				+ " TRUNCATE(s.max_demand_KW,2) as max_demand_KW ,s.contactor_status,s.consumer_id,s.grid_load_sanctioned,s.last_packet_time,"
				+ " s.digital_input1,s.digital_input2,s.digital_input3,s.last_reading_updated_grid,"
				+ " TIMESTAMPDIFF(SECOND, s.last_reading_updated_grid, s.last_reading_updated)/3600 Hrs,RIGHT(s.applaince_id, 10) MC_UID,s.meter_ct_mf,s.CB_status_time,s.Incommer_status_time"
				+ " from sensor s , site site,zone z,circle c,division di " + " where s.utility=1 "
				+ " and d.id=s.data_logger_id and z.id=s.zone_id and c.id=s.circle_id and  di.id=s.division_id and s.type = 'AC_METER' "
				+ " and s.admin_status in ('N','S','U') and " + con1 + " and " + con2
				+ " and ABS(s.cumm_PF) < 0.90 and site.id = s.site_id order by name ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("id", new StringType())
				.addScalar("sub_type", new StringType()).addScalar("device_type", new StringType())
				.addScalar("remark", new StringType()).addScalar("grid_load_alarm", new StringType())
				.addScalar("dg_load_alarm", new StringType()).addScalar("max_demand_KVA", new StringType())
				.addScalar("max_demand_KW", new StringType()).addScalar("dic_id", new StringType())
				.addScalar("device_state", new StringType()).addScalar("csq", new StringType())
				.addScalar("dic_port", new StringType()).addScalar("uom", new StringType())
				.addScalar("name", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("KWh", new StringType()).addScalar("KVAh", new StringType())
				.addScalar("instant_R_KW", new StringType()).addScalar("instant_cum_KW", new StringType())
				.addScalar("instant_cum_KVA", new StringType()).addScalar("R_Voltage", new StringType())
				.addScalar("Y_Voltage", new StringType()).addScalar("B_Voltage", new StringType())
				.addScalar("R_Current", new StringType()).addScalar("Y_Current", new StringType())
				.addScalar("B_Current", new StringType()).addScalar("R_PF", new StringType())
				.addScalar("Y_PF", new StringType()).addScalar("B_PF", new StringType())
				.addScalar("cumm_pf", new StringType()).addScalar("KWh1", new StringType())
				.addScalar("KWh2", new StringType()).addScalar("KWh3", new StringType())
				.addScalar("KWh4", new StringType()).addScalar("KWh5", new StringType())
				.addScalar("KWh6", new StringType()).addScalar("KWh7", new StringType())
				.addScalar("KWh8", new StringType()).addScalar("KVAh1", new StringType())
				.addScalar("KVAh2", new StringType()).addScalar("KVAh3", new StringType())
				.addScalar("KVAh4", new StringType()).addScalar("KVAh5", new StringType())
				.addScalar("KVAh6", new StringType()).addScalar("KVAh7", new StringType())
				.addScalar("KVAh8", new StringType()).addScalar("hrs", new StringType())
				.addScalar("MC_UID", new StringType()).addScalar("meter_ct_mf", new StringType())
				.addScalar("last_reading_updated", new StringType()).addScalar("frequency", new StringType())
				.addScalar("connected", new StringType()).addScalar("contactor_status", new StringType())
				.addScalar("consumer_id", new StringType()).addScalar("grid_load_sanctioned", new StringType())
				.addScalar("last_packet_time", new StringType()).addScalar("digital_input1", new StringType())
				.addScalar("digital_input2", new StringType()).addScalar("digital_input3", new StringType())
				.addScalar("last_reading_updated_grid", new StringType())
				.addScalar("incommer_status_time", new StringType()).addScalar("CB_status_time", new StringType())
				.addScalar("zone_name", new StringType()).addScalar("circle_name", new StringType())
				.addScalar("division_name", new StringType()).addScalar("substation_name", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederData.class));
		return FeederDatalist = query.list();
	}

	@Override
	public RAPDRPDataResponseNew getRAPDRPTownDetails(DataRequest dataRequest) {
	    String sql = "";
        String meterDate = "";
        if (dataRequest.getDate() == null) {
            sql = "select meter_date from secure_interruption_data order by meter_date desc limit 1";
            final Query query = (Query)this.sessionFactory.getCurrentSession().createSQLQuery(sql);
            meterDate = query.uniqueResult().toString();
            if (meterDate == null && meterDate.isEmpty()) {
                meterDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
        }
        else {
            meterDate = dataRequest.getDate();
        }
		
  	  List<RapdrpPopUpDataBean> RapdrpPopUpDataBeanList = new ArrayList<RapdrpPopUpDataBean>();
      final RAPDRPDataResponseNew dr = new RAPDRPDataResponseNew();
      dr.setRc(-1);
      dr.setMessage("Error ");
      if (!"ALL".equalsIgnoreCase(dataRequest.getTown()) && dataRequest.getTown() != null && dataRequest.getTown().length() >1 ) {
      	sql = "SELECT  town project_area ,ss_name ss_name,feeder_name feeder_name,meter_serial_no meter_sr_no,remark remark,total_interruption_count interruption_count ,total_interruption_duration interruption_duration,last_reading_timestamp FROM secure_interruption_data where  meter_date = '" + meterDate + "' and fedeer_type='OUTGOING' and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') and trim(town) = '"+dataRequest.getTown()+"'";	
      }else {
      	sql = "SELECT  town project_area ,ss_name ss_name,feeder_name feeder_name,meter_serial_no meter_sr_no,remark remark,total_interruption_count interruption_count ,total_interruption_duration interruption_duration,last_reading_timestamp FROM secure_interruption_data where  meter_date = '" + meterDate + "' and fedeer_type='OUTGOING' and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') and town in ('ALIGARH','FIROZABAD','JHANSI','MATHURA','BAREILLY','FAIZABAD','LUCKNOW','SHAHJAHAN','ALLAHABAD','GORAKHPUR','VARANASI','GHAZIABAD','MEERUT','MORADABAD','NOIDA','SAHARANPU')";
      }
  Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project_area",  new StringType()).addScalar("ss_name",  new StringType()).addScalar("feeder_name",  new StringType()).addScalar("meter_sr_no",  new StringType()).addScalar("remark",  new StringType()).addScalar("interruption_count",  new StringType()).addScalar("interruption_duration",  new StringType()).setResultTransformer(Transformers.aliasToBean(RapdrpPopUpDataBean.class));
      RapdrpPopUpDataBeanList = (List<RapdrpPopUpDataBean>)query.list();
      
      sql = "select distinct town from secure_interruption_data where meter_date = '" + meterDate + "' "; 
      query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
  List<RapdrpTownBean> RapdrpTownBeanList = query.list();
  dr.setRc(0);
  dr.setMessage("Success");
  dr.setPopupdata(RapdrpPopUpDataBeanList);
  dr.setRapdrptown(RapdrpTownBeanList);
	return dr;
        
	}

	
	
	
	@Override
	public RAPDRPAvgDataResponse getRAPDRPTownAvgDetails(DataRequest dataRequest) {
		String sql = "";
        String meterDate = "";
        if (dataRequest.getDate() == null) {
            sql = "select meter_date from secure_interruption_data order by meter_date desc limit 1";
            final Query query = (Query)this.sessionFactory.getCurrentSession().createSQLQuery(sql);
            meterDate = query.uniqueResult().toString();
            if (meterDate == null && meterDate.isEmpty()) {
                meterDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
        }
        else {
            meterDate = dataRequest.getDate();
        }
		
  	  List<RapdrpPopUpDataBean> RapdrpPopUpDataBeanList = new ArrayList<RapdrpPopUpDataBean>();
      final RAPDRPAvgDataResponse dr = new RAPDRPAvgDataResponse();
      dr.setRc(-1);
      dr.setMessage("Error ");
      if (!"ALL".equalsIgnoreCase(dataRequest.getTown()) && dataRequest.getTown() != null && dataRequest.getTown().length() >1 ) {
      	sql = "SELECT  town project_area ,ss_name ss_name,feeder_name feeder_name,meter_serial_no meter_sr_no,remark remark,round(avg(total_interruption_count),2) interruption_count ,round(avg(total_interruption_duration),2) interruption_duration , (1440-round(avg(total_interruption_duration),2)) supply_duration,last_reading_timestamp FROM secure_interruption_data where meter_date = '" + meterDate + "' and fedeer_type='OUTGOING' and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') and trim(town) = '"+dataRequest.getTown()+"' group by town";	
      }else {
      	sql = "SELECT  town project_area ,ss_name ss_name,feeder_name feeder_name,meter_serial_no meter_sr_no,remark remark,round(avg(total_interruption_count),2) interruption_count ,round(AVG(total_interruption_duration),2) interruption_duration,(1440-round(avg(total_interruption_duration),2)) supply_duration,last_reading_timestamp FROM secure_interruption_data where meter_date = '" + meterDate + "' and fedeer_type='OUTGOING' and trim(feeder_name) not in ('SUBSTATION NO.2','SUB STATION','SUB STATION FEEDER','NOTAP/SPARE','SUB STATION','SUB- STATION','SUBSTAION','SUB-STATION','SUBSTATION FEEDER','SUB-STATION FEEDER','SUB.STATION FEEDER','Spare/Rural','Substation-4','Substation Colony','Substation','NOTAP','Sub Station','SUBSTATION','RURAL','Rural','SPARE/RURAL','SUBSTATION FEEDER') and town in ('ALIGARH','FIROZABAD','JHANSI','MATHURA','BAREILLY','FAIZABAD','LUCKNOW','SHAHJAHAN','ALLAHABAD','GORAKHPUR','VARANASI','GHAZIABAD','MEERUT','MORADABAD','NOIDA','SAHARANPU')  group by town";
      }
  Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project_area",  new StringType()).addScalar("ss_name",  new StringType()).addScalar("feeder_name",  new StringType()).addScalar("meter_sr_no",  new StringType()).addScalar("remark",  new StringType()).addScalar("interruption_count",  new StringType()).addScalar("interruption_duration",  new StringType()).addScalar("supply_duration",  new StringType()).setResultTransformer(Transformers.aliasToBean(RapdrpPopUpDataBean.class));
      RapdrpPopUpDataBeanList = (List<RapdrpPopUpDataBean>)query.list();
  
  dr.setRc(0);
  dr.setMessage("Success");
  dr.setPopupdata(RapdrpPopUpDataBeanList);
  
	return dr;
		
	}
	
	

	@Override
	public List<DailyReport> getDailyEnergySuppliedDetails(DataRequest dataRequest) {
		
		String discom=null;
		String zone=null;
		String circle=null;
		String division=null;
		String substation=null;
		String uom=null;
		String fromDate=null;
		String toDate=null;
		int numOfDays=0;
		/*CommonReport commonObj=null;
		List<CommonReport> commonList=new ArrayList();*/
		
		 if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
			   discom = "  u.discom_id ='" +dataRequest.getDiscom_id()+ "'";}
		 else {
			   discom = "  u.discom_id = u.discom_id";}
		 if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
			   zone = " and u.zone_id ='" +dataRequest.getZone_id()+ "'";}
		 else {
			   zone = " and u.zone_id = u.zone_id ";}
		 if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
			   circle = " and u.circle_id ='"+ dataRequest.getCircle_id()+"'";
		 else
			   circle = " and u.circle_id = u.circle_id ";
		
		 if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
			   division = " and u.division_id ='"+dataRequest.getDivision_id()+ "'";
		 else
			   division = " and u.division_id = u.division_id ";
		
		 if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
			   substation = " and u.site_id ='"+dataRequest.getSubstation_id()+ "'";
		 else
			   substation = " and u.site_id = u.site_id ";
		   
		 if (dataRequest.getUom()!=null && !dataRequest.getUom().equalsIgnoreCase("ALL"))
			   uom = " and u.UOM ='"+dataRequest.getUom()+ "'";
		 else
			   uom = " and u.UOM = u.UOM ";
		   if (dataRequest.getFromTime()!=null)
			   fromDate = dataRequest.getFromTime();
		   if (dataRequest.getToTime()!=null)
			   toDate =dataRequest.getToTime();
		    try {
		    	Date date1=null;
		    	Date date2=null;
		    	
				 if(fromDate==null && toDate==null)
		    	 {
		    		 fromDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		    		 toDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		    	 }
				 if(fromDate!=null && toDate!=null)
			    	{
					  date1=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
					  date2=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
					  long diff = date2.getTime()-date1.getTime();
					  numOfDays = (int) (diff/(1000*60*60*24));
			    	}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		   
	
		    
		    String sql ="select u.name name,d.name discom_name,z.name zone, c.name circle,di.name division,s.name substation, ";
		    	   sql+=" u.meter_date date1,u.meter_id,ss.meter_reading_mf ct_ratio, SUBSTR(ss.appliance_id,6) data_logger_id,ss.serial_no,"; 
		    	   sql+=" ss.uom,u.opening_KWh opening_KVAh,u.closing_KWh closing_KVAh,((u.closing_KWh-u.opening_KWh)*ss.meter_ct_mf*u.meter_MWh_mf)/1000 consumed_KVAh,"; 
		    	   sql+=" (u.max_demand_KVA) max_demand_KVA,s.id site_id,u.device_sr_no,u.meter_ct_mf meter_ct_mf,ss.asset_id"; 
		    		sql+=" from  feeder_data_daily_log u , discom d, zone z,circle c, division di, site s,sensor ss where "+discom+zone; 
		    		sql+=circle+division+substation+uom+" and u.discom_id=d.id and u.zone_id=z.id "; 
		    		sql+=" and u.circle_id=c.id and u.division_id=di.id and u.site_id=s.id and u.sensor_id = ss.id"; 
		    		sql+=" and u.status!='U' and ss.meter_type='2' and u.meter_date between '"+fromDate+"' and '"+toDate+"' ";
		    		sql+=" order by d.name,z.name,c.name,di.name,s.name,u.meter_date,u.meter_id";

		    		
		    	    Query report = sessionFactory.getCurrentSession().createSQLQuery(sql)
		    				.addScalar("name", new StringType()).addScalar("discom_name", new StringType())
		    				.addScalar("zone", new StringType())
		    	    		.addScalar("circle", new StringType()).addScalar("division", new StringType())
		    	    		.addScalar("substation", new StringType()).addScalar("date1", new StringType())
		    	    		.addScalar("meter_id", new StringType()).addScalar("ct_ratio", new StringType())
		    	    		.addScalar("data_logger_id", new StringType()).addScalar("serial_no", new StringType())
		    	    		.addScalar("uom", new StringType()).addScalar("opening_KVAh", new StringType())
		    	    		.addScalar("closing_KVAh", new StringType()).addScalar("consumed_KVAh", new StringType())
		    	    		.addScalar("max_demand_KVA", new StringType()).addScalar("site_id", new StringType())
		    	    		.addScalar("device_sr_no", new StringType()).addScalar("meter_ct_mf", new StringType())
		    	    		.addScalar("asset_id", new StringType())
		    				.setResultTransformer(Transformers.aliasToBean(DailyReport.class));
		    		
		    	    List<DailyReport> ls = report.list();
		    	    
		    	    return ls;
	}

	@Override
	public List<CommonReport> getFeederMonthlyEnergyDetails(DataRequest dataRequest) {
		
		String sql = "";
		String discom=null;
		String zone=null;
		String circle=null;
		String division=null;
		String substation=null;
		String uom=null;
		String projectType =null;
		String year_month= null;
		Query query;		
		CommonReport commonObj=null;
		List<CommonReport> commonList=new ArrayList();
		
		

		if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
			   discom = " u.discom_id ='" +dataRequest.getDiscom_id()+ "'";}
		 else {
			   discom = " u.discom_id = u.discom_id";}
		 if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
			   zone = " and u.zone_id ='" +dataRequest.getZone_id()+ "'";}
		 else {
			   zone = " and u.zone_id = u.zone_id ";}
		 if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
			   circle = " and u.circle_id ='"+ dataRequest.getCircle_id()+"'";
		 else
			   circle = " and u.circle_id = u.circle_id ";
		
		 if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
			   division = " and u.division_id ='"+dataRequest.getDivision_id()+ "'";
		 else
			   division = " and u.division_id = u.division_id ";
		
		 if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
			   substation = " and u.site_id ='"+dataRequest.getSubstation_id()+ "'";
		 else
			   substation = " and u.site_id = u.site_id ";
		   
		 if (dataRequest.getUom()!=null && !dataRequest.getUom().equalsIgnoreCase("ALL"))
			   uom = " and u.UOM ='"+dataRequest.getUom()+ "'";
		 else
			   uom = " and u.UOM = u.UOM ";
		    if (!dataRequest.getProject_id().equalsIgnoreCase("ALL"))
	              projectType = " and u.project_id = trim('"+dataRequest.getProject_id()+"') ";
	          else
	       	   projectType = " and u.project_id = u.project_id ";
		      if (dataRequest.getYear_month()!=null)
				   year_month =dataRequest.getYear_month();

		      
			     sql ="select u.name name,d.name discom_name,z.name zone, c.name circle,di.name division,s.name substation, ";
		    	   sql+=" u.meter_date date1,u.meter_id,ss.meter_reading_mf ct_ratio, SUBSTR(ss.appliance_id,6) data_logger_id,ss.serial_no,"; 
		    	   sql+=" ss.uom,u.opening_KWh opening_KVAh,u.closing_KWh closing_KVAh,((u.closing_KWh-u.opening_KWh)*ss.meter_ct_mf*u.meter_MWh_mf)/1000 consumed_KVAh,"; 
		    	   sql+=" (u.max_demand_KVA) max_demand_KVA,s.id site_id,u.device_sr_no,u.meter_ct_mf meter_ct_mf,ss.asset_id"; 
		    		sql+=" from  feeder_data_monthly_log u , discom d, zone z,circle c, division di, site s,sensor ss where "+discom+zone; 
		    		sql+=circle+division+substation+uom+" and u.discom_id=d.id and u.zone_id=z.id "; 
		    		sql+=" and u.circle_id=c.id and u.division_id=di.id and u.site_id=s.id and u.sensor_id = ss.id"; 
		    		sql+=" and u.status!='U' and ss.meter_type='2' and concat(u.meter_year,'-',LPAD(u.meter_month,2,0))='"+year_month+"' ";
		    		sql+=" order by d.name,z.name,c.name,di.name,s.name,u.meter_date,u.meter_id";
		    Query qry=sessionFactory.getCurrentSession().createSQLQuery(sql);
		      

		    List<Object[]> ls=qry.list();
			 for(Object[] data: ls)
			 {
				 commonObj=new CommonReport();
				 
				 commonObj.setName(data[0].toString()!=""?data[0].toString():"");
				 commonObj.setDiscomName(data[1].toString()!=""?data[0].toString():"");
				 commonObj.setZoneName(data[2].toString()!=""?data[0].toString():"");
				 commonObj.setCircleName(data[3].toString()!=""?data[0].toString():"");
				 commonObj.setDivisionName(data[4].toString()!=""?data[0].toString():"");
				 commonObj.setSsName(data[5].toString()!=""?data[0].toString():"");
				 commonObj.setMeterDate(data[6].toString()!=""?data[0].toString():"");
				 commonObj.setMeterId(data[7].toString()!=""?data[0].toString():"");
				 commonObj.setCt_ratio(data[8].toString()!=""?data[0].toString():"");//ct_ratio
				 commonObj.setDataLoggerId(data[9].toString()!=""?data[0].toString():"");
				 commonObj.setSerialNo(data[10].toString()!=""?data[0].toString():"");
				 commonObj.setUOM(data[11].toString()!=""?data[0].toString():"");
				 commonObj.setOpening_kvah(data[12].toString()!=""?data[0].toString():"");//opening
				 commonObj.setClosing_kvah(data[13].toString()!=""?data[0].toString():"");//closed
				 commonObj.setConsumed_kvah(data[14].toString()!=""?data[0].toString():"");
				 commonObj.setMaxDemandKVA(data[15].toString()!=""?data[0].toString():"");
				 commonObj.setSiteId(data[16].toString()!=""?data[0].toString():"");//siteid
				 commonObj.setDeviceSerialNo(data[17].toString()!=""?data[0].toString():"");
				 commonObj.setMeterCTMF(data[18].toString()!=""?data[0].toString():"");
				 commonObj.setAssetId(data[19].toString()!=""?data[0].toString():"");
				 commonList.add(commonObj);
			 }
		      return commonList;
	}

	
	
/*	@Override
	public List<Common> getMonthlyEnergySupplied(DataRequest dataRequest) {
		
		String discom=null;
		String zone=null;
		String circle=null;
		String division=null;
		String substation=null;
		String projectType=null;
		//Common commonObj=null;
		String[] parts = year_month.split("-");
		String year = parts[0]; 
		String month = parts[1]; 
		String year_month=null;
		
		List<Common> commonList=new ArrayList();
		
		 if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
			   discom = "  u.discom_id ='" +dataRequest.getDiscom_id()+ "'";}
		 else {
			   discom = "  u.discom_id = u.discom_id";}
		 if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
			   zone = " and u.zone_id ='" +dataRequest.getZone_id()+ "'";}
		 else {
			   zone = " and u.zone_id = u.zone_id ";}
		 if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
			   circle = " and u.circle_id ='"+ dataRequest.getCircle_id()+"'";
		 else
			   circle = " and u.circle_id = u.circle_id ";
		
		 if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
			   division = " and u.division_id ='"+dataRequest.getDivision_id()+ "'";
		 else
			   division = " and u.division_id = u.division_id ";
		
		 if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
			   substation = " and u.site_id ='"+dataRequest.getSubstation_id()+ "'";
		 else
			   substation = " and u.site_id = u.site_id ";
		 
	      if (!dataRequest.getProject_id().equalsIgnoreCase("ALL"))
              projectType = " and u.project_id = trim('"+dataRequest.getProject_id()+"') ";
          else
       	   projectType = " and u.project_id = u.project_id ";
	      if (dataRequest.getYear_month()!=null)
			   year_month =dataRequest.getYear_month();
	      
	      String sql ="select u.name name, d.name discom, z.name zone, c.name circle, di.name division, s.name substation,u.meter_date date,u.meter_id serial_no1, ";
                  sql+=" u.project_id project,u.name feeder_name,if (ss.uom='null','',ss.uom) UOM, SUBSTR(ss.appliance_id,6) data_logger_id,ss.serial_no,";
                  sql+=" concat(u.meter_year,'-',LPAD(u.meter_month,2,0)) month_year, u.opening_KWh opening_KWh,u.closing_KWh closing_KWh,";
                 sql+=" (u.closing_KWh-u.opening_KWh)*ss.meter_ct_mf*u.meter_MWh_mf consumed_KWh,(u.max_demand_KW) max_demand_KW,round((u.R_Voltage_min+u.Y_Voltage_min+u.B_Voltage_min)/3,2) min_voltage,";
                  sql+=" round((u.R_Current_max+u.Y_Current_max+u.B_Current_max)/3,2) max_current, u.meter_ct_mf meter_ct_mf,s.id site_id,u.device_sr_no";
                  sql+=" from feeder_data_monthly_log u , discom d, zone z,circle c, division di, site s,sensor ss where"; 
                  sql+= discom+zone+circle+division+substation+ "and u.discom_id=d.id and u.zone_id=z.id and u.circle_id=c.id and u.division_id=di.id";
                  sql+=" and u.site_id=s.id and u.sensor_id=ss.id and u.status!='U' and concat(u.meter_year,'-',LPAD(u.meter_month,2,0))='"+year_month+"' ";
                  sql+= projectType+ "order by d.name,z.name,c.name,di.name,s.name,u.meter_id";
                  Query qry=sessionFactory.getCurrentSession().createSQLQuery(sql);
                  List<Object[]> ls=qry.list();
           	   List<Common> dataList=new ArrayList();
           	   Iterator itr=ls.iterator();
           	   Common dataObj=null;
           	   while(itr.hasNext())
           	   {
           		   dataObj=new Common();
           		   Object[] ob=(Object[])itr.next();
           		   if(ob[0]!=null) {
           			   dataObj.setName(ob[0].toString());}
           		   if(ob[1]!=null) {
           		   dataObj.setDiscomName(ob[1].toString());}
           		   if(ob[2]!=null){
           			dataObj.setZoneName(ob[2].toString());  }
           		   if(ob[3]!=null) {
           			dataObj.setCircleName(ob[3].toString()); }
           		   if(ob[4]!=null) {
           			dataObj.setDivisionName(ob[4].toString());}
           		   if(ob[5]!=null) {
           			dataObj.setSsName(ob[5].toString());}
           		   if(ob[6]!=null) {
           			dataObj.setdate(ob[6].toString());}
           		 
           		   if(ob[7]!=null) {
           				dataObj.setSerialNo(ob[7].toString());}
           		   if(ob[8]!=null)
           			   dataObj.setProjectName(ob[8].toString());
           		   if(ob[9]!=null)
           			   dataObj.setFeederName(ob[9].toString());
           		   if(ob[10]!=null)
           			   dataObj.setUOM(ob[10].toString());
           		   if(ob[11]!=null)
           			   dataObj.setUOM(ob[11].toString());
           		   if(ob[12]!=null)
           			   dataObj.setDataLoggerId(ob[12].toString());
           		   if(ob[13]!=null)
           			   dataObj.setserial(ob[13].toString());
           		   if(ob[14]!=null)
           			   dataObj.setmonthyear(ob[14].toString());
           	
           	   }
           	   dataList.add(dataObj);
           	   return dataList;
	}*/

	@Override
	public List<CommonReport> getAVGOutageReportSlabWise(DataRequest dataRequest) {
		
		String discom=null;
		String zone=null;
		String circle=null;
		String division=null;
		String substation=null;
		String sensor=null;
		String fromDate=null;
		String toDate=null;
		int numOfDays=0;
		String outageType=null;
		String outage=null;
		String projectType=null;
	   if (dataRequest.getDiscom_id()!=null && !dataRequest.getDiscom_id().equalsIgnoreCase("ALL")) {
		   discom = "  u.discom_id ='" +dataRequest.getDiscom_id()+ "' ";}
	   else {
		   discom = "  u.discom_id = u.discom_id ";}
	   if (dataRequest.getZone_id()!=null && !dataRequest.getZone_id().equalsIgnoreCase("ALL")) {
		   zone = " and u.zone_id ='" +dataRequest.getZone_id()+ "' ";}
	   else {
		   zone = " and u.zone_id = u.zone_id ";}
	   if (dataRequest.getCircle_id()!=null && !dataRequest.getCircle_id().equalsIgnoreCase("ALL"))
		   circle = " and u.circle_id ='"+ dataRequest.getCircle_id()+"' ";
	   else
		   circle = " and u.circle_id = u.circle_id ";
	
	   if (dataRequest.getDivision_id()!=null && !dataRequest.getDivision_id().equalsIgnoreCase("ALL"))
		   division = " and u.division_id ='"+dataRequest.getDivision_id()+ "' ";
	   else
		   division = " and u.division_id = u.division_id ";
	
	   if (dataRequest.getSubstation_id()!=null && !dataRequest.getSubstation_id().equalsIgnoreCase("ALL"))
		   substation = " and u.site_id ='"+dataRequest.getSubstation_id()+ "' ";
	   else
		   substation = " and u.site_id = u.site_id ";
	   if (dataRequest.getFeeder_id()!=null && !dataRequest.getFeeder_id().equalsIgnoreCase("ALL"))
		   sensor = " and u.parent_sensor_id ='" +dataRequest.getFeeder_id()+ "' ";
	   else
		   sensor = " and u.parent_sensor_id = u.parent_sensor_id ";
	   if (dataRequest.getFromTime()!=null)
		   fromDate = dataRequest.getFromTime();
	   if (dataRequest.getToTime()!=null)
		   toDate =dataRequest.getToTime();
	   
       outageType = dataRequest.getOutageType();
       if (outageType.equalsIgnoreCase("TOTAL_INTERRUPTION")) {
    	   outage = " and u.event_data >= p.uppcl_outage_consider_sec ";
       }
  
       if (!dataRequest.getProject_id().equalsIgnoreCase("ALL"))
           projectType = " and u.project_id = trim('"+dataRequest.getProject_id()+"') ";
       else
    	   projectType = " and u.project_id = u.project_id ";
	    try {
	    	Date date1=null;
	    	Date date2=null;
	    	
			 if(fromDate==null && toDate==null)
	    	 {
	    		 fromDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	    		 toDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	    	 }
			 if(fromDate!=null && toDate!=null)
		    	{
				  date1=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
				  date2=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
				  long diff = date2.getTime()-date1.getTime();
				  numOfDays = (int) (diff/(1000*60*60*24));
		    	}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	  
        
       
	String sql="select sen.id sensorId,"
			+ "sen.project_id, "
			+ "d.name discomName , "
			+ "z.name zoneName ,"
			+ "c.name circleName, "
			+ "di.name divisionName,"
			+ "s.name ssName,"
			+ "sen.name feederName, "
			+ "number_of_gateway feederCount, "
			  +"u.event_date eventDate,"
			  + "count(event_date) OutageCount , "
			  + "SEC_TO_TIME(sum(event_data)) totalOutageDuration, "
			  + "'"+numOfDays+"' numOfDays , "
			  		+ "'" +fromDate+"' fromDate , "
			  				+ "'"+toDate+"' toDate , " 
	          +"count(event_date)/"+numOfDays+ " avgOutageCount,"
	          		+ "LEFT(SEC_TO_TIME(sum(event_data)/count(event_date)),8) avgOutageDuration,"
	          		+ "LEFT(SEC_TO_TIME((sum(event_data)/count(event_date))*(count(event_data)/"+numOfDays+ ")),8) avgOutageDurationPerDay,"
	          				+ "sen.serial_no as serialNo,"
	          				+ "RIGHT(sen.appliance_id, 10) mcUID,count(if(u.event_data <= 300,1,null)) count_15min , count(if(u.event_data > 300 and u.event_data <= 1800,1,null)) count_15_60min , "
	          				+ "count(if(u.event_data > 1800,1,null)) count_60min,"
	          				 
	          				+ "SEC_TO_TIME((24*60*60)*"+numOfDays+"  - sum(event_data)) Total_supply_Duration,LEFT(SEC_TO_TIME(24*60*60-(sum(event_data)/"+numOfDays+")),8) avg_supply_duration_per_day," 
	          				+  "LEFT(SEC_TO_TIME(sum(if(u.event_data <= 300,u.event_data,null))),8) duration_15min , LEFT(SEC_TO_TIME(sum(if(u.event_data > 300 and u.event_data <= 1800,u.event_data,null))),8) duration_15_60min,"  
	          				+ "LEFT(SEC_TO_TIME(sum(if(u.event_data > 1800,u.event_data,null))),8) duration_60min "    
	          +"from feeder_daily_statistics_final u ,site s,sensor sen , processor_master_control p ,discom d ,zone z, division di , circle c   where ";
		   sql+=discom+zone+circle+division+substation+sensor;    
		   sql+=" and u.discom_id = d.id and u.zone_id = z.id and u.division_id = di.id and u.circle_id = c.id and u.event_date between '" +fromDate+"' and '"+toDate+"' and u.site_id=s.id and u.status not in ('D','M') and u.process_status not in ('D','M','U') "
			  +" and sen.id=u.parent_sensor_id	 and sen.type='AC_METER' ";
		   sql+=outage+ projectType+ " group by u.discom_id , u.zone_id, u.circle_id , u.division_id , u.site_id , u.parent_sensor_id";
		   
   Query qry=sessionFactory.getCurrentSession().createSQLQuery(sql);
   List<Object[]> ls=qry.list();
   List<CommonReport> dataList=new ArrayList();
   Iterator itr=ls.iterator();
   CommonReport dataObj=null;
   while(itr.hasNext())
   {
	   dataObj=new CommonReport();
	   Object[] ob=(Object[])itr.next();
	   if(ob[0]!=null) {
		   dataObj.setSensorId(ob[0].toString());}
	   if(ob[1]!=null) {
	   dataObj.setProjectName(ob[1].toString());}
	   if(ob[2]!=null){
		dataObj.setDiscomName(ob[2].toString());  }
	   if(ob[3]!=null) {
		dataObj.setZoneName(ob[3].toString()); }
	   if(ob[4]!=null) {
		dataObj.setCircleName(ob[4].toString());}
	   if(ob[5]!=null) {
		dataObj.setDivisionName(ob[5].toString());}
	   if(ob[6]!=null) {
		dataObj.setSsName(ob[6].toString());}
	   if(ob[7]!=null)
		   dataObj.setFeederName(ob[7].toString());
	  
	   //
	   
	   if(ob[8]!=null)
		   dataObj.setFeederCount(ob[8].toString());
	   if(ob[9]!=null)
		   dataObj.setEventDate(ob[9].toString());
	   if(ob[10]!=null)
		   dataObj.setOutageCount(ob[10].toString());
	   if(ob[11]!=null)
		   dataObj.setTotalOutageDuration(ob[11].toString());
	/*   if(ob[12]!=null)
		   dataObj.setNumOfDays(ob[12].toString());
	   if(ob[13]!=null)
		   dataObj.setFromDate(ob[13].toString());
	   if(ob[14]!=null)
		   dataObj.setToDate(ob[14].toString());*/
	   if(ob[15]!=null) {
		   dataObj.setAvgOutageCount(ob[15].toString());
		   dataObj.setRoundAvgOutageCount(Math.round(Float.parseFloat(ob[15].toString())));
	   if(ob[16]!=null)
		   dataObj.setAvgOutageDuration(ob[16].toString());
	   if(ob[17]!=null)
		   dataObj.setAvgOutageDurationPerDay(ob[17].toString());
	
	   
	   //
	   
	   if(ob[18]!=null) {
			dataObj.setSerialNo(ob[18].toString());
	   }
	   if(ob[19]!=null) {
			dataObj.setMcUID(ob[19].toString());}
	  //
	   if(ob[20]!=null) {
			dataObj.setCount_15min(ob[20].toString());}
	   
	   if(ob[21]!=null) {
			dataObj.setCount_15_60min(ob[21].toString());}
	   if(ob[22]!=null) {
			dataObj.setCount_60min(ob[22].toString());}
	   if(ob[23]!=null) {
			dataObj.setTotalSupplyDuration(ob[23].toString());}
	   if(ob[24]!=null) {
			dataObj.setAvgSupplyDurationPerDay(ob[24].toString());}
	   if(ob[25]!=null) {
			dataObj.setDuration_15min(ob[25].toString());}
	   if(ob[26]!=null) {
			dataObj.setDuration_15_60min(ob[26].toString());}
	   if(ob[27]!=null) {
			dataObj.setDuration_60min(ob[27].toString());}
	 
	   
	//
	
	   }
	 
	   dataObj.setFromDate(fromDate);
	   dataObj.setToDate(toDate);
	   dataObj.setNumOfDays(String.valueOf(numOfDays));
   }
   dataList.add(dataObj);
   return dataList;
	}

	
	
	@Override
	public List<IPDSDailySupply> supplyStatus8NN(String date, String project_id) {
		String projectType="";
		String sql = "";
		if (!project_id.equalsIgnoreCase("ALL"))
	           projectType = " and s.project_id = trim('"+project_id+"') ";
	       else
	    	   projectType = " and s.project_id = s.project_id ";
/*	
	 sql= " select project, discom_id,d.short_name discom , left(SEC_TO_TIME(avg(seconds)),8) supply_duration ,left(SEC_TO_TIME(24*60*60-avg(seconds)),8) Outage_duration, count(id) count ";
     sql+= " from (select s.discom_id discom_id , s.project_id project , s.name,s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) ";
     sql+= " is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds,  case when sum(f.event_data) is null ";  
     sql+= " then 0 else SEC_TO_TIME(sum(f.event_data)) end as duration from sensor s left join feeder_daily_statistics_final f ";  
     sql+= " on f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id ";  
     sql+= " and f.site_id = s.site_id and f.sensor_id = f.sensor_id and f.event_date='"+date+"'and f.event_type is null "; 
     sql+= " and f.parent_sensor_id=s.id where s.discom_id = s.discom_id and s.zone_id = s.zone_id and s.circle_id = s.circle_id ";  
     sql+= " and s.division_id = s.division_id and s.site_id = s.site_id and s.type='AC_METER'";
 	 sql+= " "+projectType+" and  s.admin_status in ('N','S') and s.meter_type='2' group by s.id) as outage , discom d ";  
     sql+= " where d.id=outage.discom_id group by outage.discom_id order by discom";*/
		
		sql=" select d.id discom_id , project , d.name discom , SEC_TO_TIME(avg(seconds)) avg_outage_dur,avg(seconds) avg_outage_sec, ";
       sql+= " count(id) count from"; 
        sql+=" (select s.discom_id discom_id , s.project_id project , s.name,s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) ";
         sql+="is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds,  case when sum(f.event_data) is null ";  
         sql+=" then 0 else SEC_TO_TIME(sum(f.event_data)) end as duration from sensor s left join feeder_daily_statistics_final f"; 
       
        sql+="and f.sensor_id = f.sensor_id and f.event_date='"+date+"' and f.event_type is null and f.parent_sensor_id=s.id ";   
       //  sql+=" where $Sdiscom  $Szone $Scircle $Sdivision $Ssubstation ";
        sql+= " and s.type='AC_METER' " + projectType; 
         sql+=" and  s.admin_status in ('N','S') and s.meter_type='2' group by s.id) as outage , discom d ";  
         sql+=" where d.id=outage.discom_id and seconds < 79200  group by outage.discom_id order by discom ";
		

    Query supply = sessionFactory.getCurrentSession().createSQLQuery(sql)
			.addScalar("project", new StringType()).addScalar("discom_id", new StringType())
			.addScalar("discom", new StringType())
    		.addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType())
    		.addScalar("count", new StringType())
			.setResultTransformer(Transformers.aliasToBean(SupplyStatus.class));
	
    List<IPDSDailySupply> ls = supply.list();
    
    return ls;
		
	}

	
	
	@Override
	public SupplyReportResponse getSupplyReportSummary(String discom_id,String project_id,String date,String reportType) {
		String sql = "";
		Query query;
		String project=null;
        String group = "";
        Date mDate =null;
        String discom=null;
        String Sdiscom=null;
        String circle=null;
        String Scircle=null;
        String zone=null;
        String Szone=null;
        String division=null;
        String Sdivision=null;
        String substation=null;
        String Ssubstation=null;
        
		List<FeederdetailsBean> feederdetailsList = new ArrayList<FeederdetailsBean>();
		List<DiscomwiseBean> discomwiseList = new ArrayList<DiscomwiseBean>();
		List<DistrictwiseBean> districtwiseList = new ArrayList<DistrictwiseBean>();
		List<TownwiseBean> townwiseList = new ArrayList<TownwiseBean>();
		SupplyReportResponse dr = new SupplyReportResponse();
		dr.setRc(-1);
		dr.setMessage("Error ");
		
		 if(project_id!=null)
	            project="and s.project_id='"+project_id+"'";
		   if(reportType.equalsIgnoreCase("DISTRICT")) {
	            group="group by project,discom,district ";
	        }else if(reportType.equalsIgnoreCase("TOWN")) {
	            group="group by project,discom,district,town ";
	        }else if(reportType.equalsIgnoreCase("DISCOM")) {
	            group="group by project,discom ";
	        }
		   try {
			mDate=new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        //$mDate=date("Y-m-d",strtotime($mDate));
		   
		   if (reportType.equalsIgnoreCase("SUMMARY")) {
	            if (discom_id!=null && !discom_id.equalsIgnoreCase("ALL")) {
	                discom = "  f.discom_id ='"+discom_id+ "' ";
	                Sdiscom = " s.discom_id ='"+discom_id+ "' ";
	            }else{
	                discom = "  f.discom_id = s.discom_id ";
	                Sdiscom = " s.discom_id = s.discom_id ";
	            }
	        
	                zone = " and f.zone_id = s.zone_id ";
	                Szone = " and s.zone_id = s.zone_id ";
	        
	        
	                circle = " and f.circle_id = s.circle_id ";
	                Scircle = " and s.circle_id = s.circle_id ";
	        
	        
	                division = " and f.division_id = s.division_id ";
	                Sdivision = " and s.division_id = s.division_id ";
	        
	        
	                substation = " and f.site_id = s.site_id ";
	                Ssubstation = " and s.site_id = s.site_id ";
	        
	 
	        sql = "select project,d.name discom ,sid, z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name," ;
	           sql+="1 count ,Dstatus,serial_no,RIGHT(appliance_id,10) appliance_id,supply_duration,outagecount,outage_duration from "; 
	           sql+= "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,"; 
	          sql+= "s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project,"; 
	         sql+= "s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id,"; 
	              sql+=" case when sum(f.event_data) is null then 24*60*60 else (24*60*60 - sum(f.event_data)) end as supply_duration,";
	               sql+=" case when count(f.id) is null then 0 else count(f.id) end as outagecount,"; 
	                sql+="case when sum(f.event_data) is null then 0 else sum(f.event_data) end as outage_duration ";
	               sql+=" from sensor s left join feeder_daily_statistics_final f on ";  
	                 sql+=discom+zone+circle+division+substation + " and f.sensor_id = f.sensor_id and f.event_date='"+mDate+"' "; 
	                sql+="and f.event_type is null and f.parent_sensor_id=s.id  where" + Sdiscom+Szone+Scircle+Sdivision+Ssubstation;
	                 sql+="and s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type!='1' "+project;
	                 sql+="and s.location_no is not null group by s.id) as outage , discom d, zone z , circle c , division di , site si ";
	                 sql+="where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id "; 
	                 sql+="and si.id=outage.site_id and supply_duration <=  86400 order by project,outage.discom_id,location_id";
	        //date(s.maintenance_to_live) <= '".$mDate."'
	        //$result['avgSupply'] = $this->get_map_by_query($sql);
	                 
	         		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
	        				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
	        				.addScalar("circle", new StringType()).addScalar("division", new StringType())
	        				.addScalar("ss", new StringType()).addScalar("district", new StringType())
	        				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
	        				.addScalar("count", new StringType()).addScalar("Dstatus", new StringType())
	        				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType())
	        				.addScalar("supply_duration", new StringType()).addScalar("outagecount", new StringType())
	        				.addScalar("outage_duration", new StringType())
	        				.setResultTransformer(Transformers.aliasToBean(FeederdetailsBean.class));
	         		feederdetailsList = query.list();
	                 
	        sql = "select project,d.name discom , z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,";
	               sql+= "Dstatus,serial_no,LEFT(appliance_id,10) appliance_id,sid,outagecount from"; 
	              sql+= "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,"; 
	              sql+= "s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"; 
	             sql+=  "s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id ,"; 
	              sql+= "case when count(f.id) is null then 0 else count(f.id) end as outagecount "; 
	                sql+="from sensor s left join feeder_daily_statistics_final f on" +discom +zone +circle +division +substation; 
	                 sql+="and f.sensor_id = f.sensor_id  and f.event_date='"+mDate+"' and f.event_type is null  and f.parent_sensor_id=s.id ";
	                 sql+="and f.event_data <= 300 where" +Sdiscom +Szone +Scircle +Sdivision+Ssubstation;
	                sql+= "and s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type!='1' "; 
	                sql+= project+ "and s.location_no is not null group by sid) as outage , discom d, zone z , circle c , division di , site si ";
	                 sql+= "where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id  and di.id=outage.division_id "; 
	                 sql+= "and si.id=outage.site_id order by project,outage.discom_id,location_id";
	        //$result['supply'] = $this->get_map_by_query($sql);
	                 
	            		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
		        				.addScalar("discom", new StringType()).addScalar("zone", new StringType())
		        				.addScalar("circle", new StringType()).addScalar("division", new StringType())
		        				.addScalar("ss", new StringType()).addScalar("district", new StringType())
		        				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
		        				.addScalar("Dstatus", new StringType())
		        				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType()).addScalar("sid", new StringType())
		        				.addScalar("outagecount", new StringType())
		        				
		        				.setResultTransformer(Transformers.aliasToBean(DiscomwiseBean.class));
	            		discomwiseList = query.list();
	                 
	       }
		   
		   else {
			     if (discom_id!=null && !discom_id.equalsIgnoreCase("ALL")) {
		                discom = "  f.discom_id ='"+discom_id+ "' ";
		                Sdiscom = " s.discom_id ='"+discom_id+ "' ";
		            }else{
		                discom = "  f.discom_id = s.discom_id ";
		                Sdiscom = " s.discom_id = s.discom_id ";
		            }
		        
		                zone = " and f.zone_id = s.zone_id ";
		                Szone = " and s.zone_id = s.zone_id ";
		        
		        
		                circle = " and f.circle_id = s.circle_id ";
		                Scircle = " and s.circle_id = s.circle_id ";
		        
		        
		                division = " and f.division_id = s.division_id ";
		                Sdivision = " and s.division_id = s.division_id ";
		        
		        
		                substation = " and f.site_id = s.site_id ";
		                Ssubstation = " and s.site_id = s.site_id ";
	        //$result['message'] = "Success";
	        sql = "select project,d.name discom ,sid, z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,";
	        sql+= "count(sid) count ,Dstatus,serial_no,RIGHT(appliance_id,10) appliance_id,avg(supply_duration) supply_duration,sum(outagecount) outagecount,avg(outage_duration) outage_duration from"; 
	        sql+="(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,"; 
	        sql+=" s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project,"; 
	        sql+="s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id ,"; 
	        sql+="case when sum(f.event_data) is null then 24*60*60 else (24*60*60 - sum(f.event_data)) end as supply_duration, ";
	        sql+="case when count(f.id) is null then 0 else count(f.id) end as outagecount, "; 
	        sql+="case when sum(f.event_data) is null then 0 else sum(f.event_data) end as outage_duration "; 
	        sql+="from sensor s left join feeder_daily_statistics_final f on ";  
	        sql+= discom+zone+circle+division+substation+"and f.sensor_id = f.sensor_id ";
	        sql+="and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id where ";   
	        sql+=Sdiscom+Szone+Scircle+Sdivision+Ssubstation+ "and s.type='AC_METER' "; 
	        sql+="and  s.admin_status in ('N','S','U')  and s.meter_type='2'" +project;
	        sql+="and s.deployed_type !='SPARE' and s.location_no is not null group by s.id) as outage , discom d, zone z , circle c , division di , site si ";
	        sql+="where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id ";
	        sql+="and si.id=outage.site_id and supply_duration <=  86400 "+group+ "order by project,outage.discom_id,location_id";
	        //date(s.maintenance_to_live) <= '".$mDate."'
	        //$result['avgSupply'] = $this->get_map_by_query($sql);
	        
	   		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
    				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
    				.addScalar("circle", new StringType()).addScalar("division", new StringType())
    				.addScalar("ss", new StringType()).addScalar("district", new StringType())
    				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
    				.addScalar("count", new StringType()).addScalar("Dstatus", new StringType())
    				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType())
    				.addScalar("supply_duration", new StringType()).addScalar("outagecount", new StringType())
    				.addScalar("outage_duration", new StringType())
    				.setResultTransformer(Transformers.aliasToBean(DistrictwiseBean.class));
	   		districtwiseList = query.list();
	        
	        sql = "select project,d.name discom , z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,";
	         sql+= " Dstatus,serial_no,LEFT(appliance_id,10) appliance_id,sid,count(sid) outagecount,SEC_TO_TIME(avg(seconds)) supply from ";
	         sql+= "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,"; 
	        sql+= "s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"; 
	       sql+= "s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id , ";
	        sql+= "case when count(f.id) is null then 0 else count(f.id) end as outagecount,";
	        sql+= "case when sum(f.event_data) is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds ";
	        sql+=  " from sensor s left join feeder_daily_statistics_final f on "; 
	        sql+=  discom+zone+circle+division+substation +"and f.sensor_id = f.sensor_id and f.event_date='"+mDate+"' and f.event_type is null ";
	        sql+= "and f.parent_sensor_id=s.id where" +Sdiscom+Szone+Scircle+Sdivision+Ssubstation;
	        sql+= "and s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type='2' and s.deployed_type !='SPARE' ";
	        sql+= project+ "and s.location_no is not null group by sid) as outage , discom d, zone z , circle c , division di , site si ";
	        sql+= "where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id ";
	        sql+= "and si.id=outage.site_id and seconds < 79200 " + group+ " order by project,outage.discom_id,location_id ";
	        //$result['supply'] = $this->get_map_by_query($sql);
	        
	   		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
    				.addScalar("discom", new StringType()).addScalar("zone", new StringType())
    				.addScalar("circle", new StringType()).addScalar("division", new StringType())
    				.addScalar("ss", new StringType()).addScalar("district", new StringType())
    				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
    				.addScalar("Dstatus", new StringType())
    				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType())
    				.addScalar("sid", new StringType())
    				.addScalar("outagecount", new StringType())
    				.addScalar("supply", new StringType())
    				.setResultTransformer(Transformers.aliasToBean(TownwiseBean.class));
	   		townwiseList = query.list();
	       }
	
		    dr.setRc(0);
			dr.setMessage("Success");
			dr.setDiscomwise(discomwiseList);
			dr.setDistrictwise(districtwiseList);
			dr.setFeederdetails(feederdetailsList);
			dr.setTownwise(townwiseList);
			
			return dr;
	}

	@Override
	public WeeklyReportResponse getWeeklyReportSummary(String month,String projectId) {
	/*Session session = 	sessionFactory.getCurrentSession();
		List<Week1Bean> week1BeanList = new ArrayList<Week1Bean>();
		List<Week2Bean> week2BeanList = new ArrayList<Week2Bean>();
		List<Week3Bean> week3BeanList = new ArrayList<Week3Bean>();
		List<Week4Bean> week4BeanList = new ArrayList<Week4Bean>();
		WeeklyReportResponse dr = new WeeklyReportResponse();
		
		String projectType="";
		String sql = "";
		if (!projectId.equalsIgnoreCase("ALL"))
	           projectType = " and s.project_id = trim('"+projectId+"') ";
	       else
	    	   projectType = " and s.project_id = s.project_id ";
	
	 sql= " select project, discom_id1,feedername,town,d.short_name discom , left(SEC_TO_TIME(avg(seconds)),8) supply_duration ,left(SEC_TO_TIME(24*60*60-avg(seconds)),8) Outage_duration, count(id) count ";
     sql+= " from (select s.discom_id discom_id1 , s.project_id project , s.name feedername,s.location_no town,s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) ";
     sql+= " is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds,  case when sum(f.event_data) is null ";  
     sql+= " then 0 else SEC_TO_TIME(sum(f.event_data)) end as duration from sensor s left join feeder_daily_statistics_final f ";  
     sql+= " on f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id ";  
     sql+= " and f.site_id = s.site_id and f.sensor_id = f.sensor_id and f.event_date between '"+month+"-01 ' AND '"+month+"-07 ' and f.event_type is null "; 
     sql+= " and f.parent_sensor_id=s.id where s.discom_id = s.discom_id and s.zone_id = s.zone_id and s.circle_id = s.circle_id ";  
     sql+= " and s.division_id = s.division_id and s.site_id = s.site_id and s.type='AC_METER'";
 	 sql+= " "+projectType+" and  s.admin_status in ('N','S') and s.meter_type='2' group by s.id) as outage , discom d ";  
     sql+= " where d.id=outage.discom_id1 group by outage.discom_id1 order by discom";

    Query supply = session.createSQLQuery(sql)
			.addScalar("project", new StringType()).addScalar("discom_id1", new StringType())
			.addScalar("feedername", new StringType()).addScalar("town", new StringType()).addScalar("discom", new StringType())
    		.addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType())
    		.addScalar("count", new StringType())
			.setResultTransformer(Transformers.aliasToBean(Week1Bean.class));
    week1BeanList=supply.list();
    
	 sql= " select project, discom_id1,feedername,town,d.short_name discom , left(SEC_TO_TIME(avg(seconds)),8) supply_duration ,left(SEC_TO_TIME(24*60*60-avg(seconds)),8) Outage_duration, count(id) count ";
     sql+= " from (select s.discom_id discom_id1 , s.project_id project , s.name feedername,s.location_no town,s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) ";
     sql+= " is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds,  case when sum(f.event_data) is null ";  
     sql+= " then 0 else SEC_TO_TIME(sum(f.event_data)) end as duration from sensor s left join feeder_daily_statistics_final f ";  
     sql+= " on f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id ";  
     sql+= " and f.site_id = s.site_id and f.sensor_id = f.sensor_id and f.event_date between '"+month+"-08 ' AND '"+month+"-15 ' and f.event_type is null "; 
     sql+= " and f.parent_sensor_id=s.id where s.discom_id = s.discom_id and s.zone_id = s.zone_id and s.circle_id = s.circle_id ";  
     sql+= " and s.division_id = s.division_id and s.site_id = s.site_id and s.type='AC_METER'";
 	 sql+= " "+projectType+" and  s.admin_status in ('N','S') and s.meter_type='2' group by s.id) as outage , discom d ";  
     sql+= " where d.id=outage.discom_id1 group by outage.discom_id1 order by discom";

    Query supply1 = session.createSQLQuery(sql)
			.addScalar("project", new StringType()).addScalar("discom_id1", new StringType())
			.addScalar("feedername", new StringType()).addScalar("town", new StringType()).addScalar("discom", new StringType())
    		.addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType())
    		.addScalar("count", new StringType())
			.setResultTransformer(Transformers.aliasToBean(Week2Bean.class));
    week2BeanList=supply1.list();
    
	 sql= " select project, discom_id1,feedername,town,d.short_name discom , left(SEC_TO_TIME(avg(seconds)),8) supply_duration ,left(SEC_TO_TIME(24*60*60-avg(seconds)),8) Outage_duration, count(id) count ";
     sql+= " from (select s.discom_id discom_id1 , s.project_id project , s.name feedername,s.location_no town,s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) ";
     sql+= " is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds,  case when sum(f.event_data) is null ";  
     sql+= " then 0 else SEC_TO_TIME(sum(f.event_data)) end as duration from sensor s left join feeder_daily_statistics_final f ";  
     sql+= " on f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id ";  
     sql+= " and f.site_id = s.site_id and f.sensor_id = f.sensor_id and f.event_date between '"+month+"-16 ' AND '"+month+"-23 ' and f.event_type is null "; 
     sql+= " and f.parent_sensor_id=s.id where s.discom_id = s.discom_id and s.zone_id = s.zone_id and s.circle_id = s.circle_id ";  
     sql+= " and s.division_id = s.division_id and s.site_id = s.site_id and s.type='AC_METER'";
 	 sql+= " "+projectType+" and  s.admin_status in ('N','S') and s.meter_type='2' group by s.id) as outage , discom d ";  
     sql+= " where d.id=outage.discom_id1 group by outage.discom_id1 order by discom";

    Query supply2 = session.createSQLQuery(sql)
			.addScalar("project", new StringType()).addScalar("discom_id1", new StringType())
			.addScalar("feedername", new StringType()).addScalar("town", new StringType()).addScalar("discom", new StringType())
    		.addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType())
    		.addScalar("count", new StringType())
			.setResultTransformer(Transformers.aliasToBean(Week3Bean.class));
    week3BeanList=supply2.list();
    
	 sql= " select project, discom_id1,feedername,town,d.short_name discom , left(SEC_TO_TIME(avg(seconds)),8) supply_duration ,left(SEC_TO_TIME(24*60*60-avg(seconds)),8) Outage_duration, count(id) count ";
     sql+= " from (select s.discom_id discom_id1 , s.project_id project , s.name feedername,s.location_no town,s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) ";
     sql+= " is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds,  case when sum(f.event_data) is null ";  
     sql+= " then 0 else SEC_TO_TIME(sum(f.event_data)) end as duration from sensor s left join feeder_daily_statistics_final f ";  
     sql+= " on f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id ";  
     sql+= " and f.site_id = s.site_id and f.sensor_id = f.sensor_id and f.event_date between '"+month+"-24 ' AND '"+month+"-31 ' and f.event_type is null "; 
     sql+= " and f.parent_sensor_id=s.id where s.discom_id = s.discom_id and s.zone_id = s.zone_id and s.circle_id = s.circle_id ";  
     sql+= " and s.division_id = s.division_id and s.site_id = s.site_id and s.type='AC_METER'";
 	 sql+= " "+projectType+" and  s.admin_status in ('N','S') and s.meter_type='2' group by s.id) as outage , discom d ";  
     sql+= " where d.id=outage.discom_id1 group by outage.discom_id1 order by discom";

    Query supply3 = session.createSQLQuery(sql)
			.addScalar("project", new StringType()).addScalar("discom_id1", new StringType())
			.addScalar("feedername", new StringType()).addScalar("town", new StringType()).addScalar("discom", new StringType())
    		.addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType())
    		.addScalar("count", new StringType())
			.setResultTransformer(Transformers.aliasToBean(Week4Bean.class));
    week4BeanList=supply3.list();
		
		
    dr.setRc(0);
 			dr.setMessage("Success");
 			dr.setWeek1(week1BeanList);
 			dr.setWeek2(week2BeanList);
 			dr.setWeek3(week3BeanList);
 			dr.setWeek4(week4BeanList);*/
			return null;
 			
 			//return WeeklyReportResponse;
	}

	@Override
	public List<SupplyReportResponseTownWiseData> getSupplyReportTownWise(DataRequest dataRequest) {
		String mDate = dataRequest.getDate();
		String projectId=dataRequest.getProject_id();
		String discomId=dataRequest.getDiscom_id();
		String reportType=dataRequest.getReportType();
		String discom=null;
		String Sdiscom=null;
		String project=null;
		String group=null;
		String sql=null;
		Query query=null;
		List<SupplyReportResponseTownWiseData> completeData=new ArrayList<>();
		if(projectId!=null) {
			project="and s.project_id='"+projectId+"'";}
		else if(projectId.equalsIgnoreCase("All")) {
			project="and s.project_id IN('EODB','IPDS','NTPF')";}
		
		if(reportType.equalsIgnoreCase("DISTRICT")) {
            group="group by project,discom,district ";
        }else if(reportType.equalsIgnoreCase("TOWN")) {
            group="group by project,discom,district,town ";
        }else if(reportType.equalsIgnoreCase("DISCOM")) {
            group="group by project,discom ";
        }
		  if (discomId!=null && !discomId.equalsIgnoreCase("ALL")) {
              discom = "  f.discom_id ='"+discomId+"' and f.zone_id = s.zone_id and f.circle_id = s.circle_id  and f.division_id = s.division_id and f.site_id = s.site_id ";
              Sdiscom = " s.discom_id ='"+discomId+"' and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id and f.site_id = s.site_id";
          }else{
              discom = "  f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id  and f.division_id = s.division_id and f.site_id = s.site_id ";
              Sdiscom = " f.discom_id = s.discom_id and f.zone_id = s.zone_id and f.circle_id = s.circle_id and f.division_id = s.division_id and f.site_id = s.site_id";
          }
		
		 if (reportType.equalsIgnoreCase("DETAILS")) {
	         sql ="select project,d.name discom ,sid, z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name, " + 
	        		" 1 count ,Dstatus,serial_no,RIGHT(appliance_id,10) appliance_id,supply_duration,outagecount,outage_duration,"
	        		+ " CASE WHEN supply_duration<79200 THEN 'YES' ELSE 'NO' END AS 'outageLessThan22' from "+ 
	        		" (select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,"+ 
	        		"s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project, " + 
	        		"s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id ,case when sum(f.event_data) is null then 24*60*60 else (24*60*60 - sum(f.event_data)) end as supply_duration, " + 
	        		"case when count(f.id) is null then 0 else count(f.id) end as outagecount, case when sum(f.event_data) is null then 0 else sum(f.event_data) end as outage_duration " + 
	        		" from sensor s left join feeder_daily_statistics_final f  on " +discom+" and f.sensor_id = s.id  and f.event_date='"+mDate+"' " + 
	        		"and f.event_type is null and f.parent_sensor_id=s.id where s.type='AC_METER' and  s.admin_status in ('N','S','U')  " + 
	        		"and s.meter_type!='1'"+project+" and s.location_no is not null group by s.id) as outage , discom d, zone z , circle c , division di , site si  " + 
	        		"where d.id=outage.discom_id and z.id=outage.zone_id  and c.id=outage.circle_id and di.id=outage.division_id  and si.id=outage.site_id " + 
	        		"and supply_duration <=  86400 order by project,outage.discom_id,location_id";
	        
	         query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
	  				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
	  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
	  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
	  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
	  				.addScalar("Dstatus", new StringType())
	  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType()).addScalar("supply_duration",new StringType())
	  				.addScalar("sid", new StringType())
	  				.addScalar("outagecount", new StringType())
	  				.addScalar("outage_duration", new StringType())
	  				.addScalar("outageLessThan22", new StringType())
	  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
	         List<SupplyReportResponseTownWiseData> ls=query.list();
	         completeData.addAll(ls);
		 }
		 if (reportType.equalsIgnoreCase("TOWN")) {
			 sql="select project,d.name discom ,sid, z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name," + 
			 	" count(sid) count ,Dstatus,serial_no,RIGHT(appliance_id,10) appliance_id,round(avg(supply_duration),2) supply_duration,sum(outagecount) outagecount,avg(outage_duration) outage_duration from " + 
			 		"(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id," + 
			 		"s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ," + 
			 		"s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id ," + 
			 		"case when sum(f.event_data) is null then 24*60*60 else (24*60*60 - sum(f.event_data)) end as supply_duration," + 
			 		"case when count(f.id) is null then 0 else count(f.id) end as outagecount," + 
			 		"case when sum(f.event_data) is null then 0 else sum(f.event_data) end as outage_duration" + 
			 		" from sensor s left join feeder_daily_statistics_final f  on "+discom+" and f.sensor_id = s.id and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id " + 
			 		" where s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type='2'" +project+
			 		" and s.deployed_type !='SPARE' and s.location_no is not null group by s.id) as outage , discom d, zone z , circle c , division di , site si " + 
			 		" where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id " + 
			 		" and supply_duration <=  86400 "+group +" order by project,outage.discom_id,location_id";
			 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
		  				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
		  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
		  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
		  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
		  				.addScalar("Dstatus", new StringType())
		  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType())
		  				.addScalar("sid", new StringType())
		  				.addScalar("outagecount", new StringType())
		  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
		        List<SupplyReportResponseTownWiseData> ls1=query.list();
			 sql= "select project,d.name discom , z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,"+
		           "Dstatus,serial_no,LEFT(appliance_id,10) appliance_id,sid,count(sid) outagecount,SEC_TO_TIME(avg(seconds)) supply from"+ 
		           "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,"+ 
		             "s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"+ 
		              "  s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id , case when count(f.id) is null then 0 else count(f.id) end as outagecount,"+
		               " case when sum(f.event_data) is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds  from sensor s left join feeder_daily_statistics_final f "+ 
		               "  on "+discom+"  and f.sensor_id = f.sensor_id and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id"+
		               "  where type='AC_METER' and  s.admin_status in ('N','S','U')   and s.meter_type='2' and s.deployed_type !='SPARE'"+project+" and s.location_no is not null group by sid) as outage , discom d, zone z , circle c , division di , site si"+
		               "  where d.id=outage.discom_id  and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id and seconds < 79200 "+group+" order by project,outage.discom_id,location_id ";
	        /*sql="select project,d.name discom , z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,Dstatus,serial_no,LEFT(appliance_id,10) appliance_id,sid,outagecount from "+
	               "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"+ 
	               "s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id ,case when count(f.id) is null then 0 else count(f.id) end as outagecount from sensor s left join feeder_daily_statistics_final f on "+
	               discom+" and f.sensor_id = s.id and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id and f.event_data <= 300  where"+
	               "  s.type='AC_METER' and  s.admin_status in ('N','S','U')  and s.meter_type!='1' "+project+" and s.location_no is not null group by sid) as outage , discom d, zone z , circle c , division di , site si"+
	               " where d.id=outage.discom_id and z.id=outage.zone_id  and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id order by project,outage.discom_id,location_id";*/
	        query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
	  				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
	  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
	  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
	  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
	  				.addScalar("Dstatus", new StringType())
	  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType())
	  				.addScalar("sid", new StringType())
	  				.addScalar("outagecount", new StringType())
	  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
	        List<SupplyReportResponseTownWiseData> ls2=query.list();
	        Iterator itr=ls1.iterator();
	        Iterator itr1=ls2.iterator();
	        while(itr.hasNext())
	        {
	        	SupplyReportResponseTownWiseData obj=(SupplyReportResponseTownWiseData)itr.next();
	        	obj.setOutageLessThan22Count("0");
	        	obj.setOutageLessThan22Duration("0");
	        	obj.setPercentage("0");
	        	while(itr1.hasNext())
	        	{
	        		SupplyReportResponseTownWiseData obj1=(SupplyReportResponseTownWiseData)itr1.next();
	        		if(obj.getTown().equalsIgnoreCase(obj1.getTown()))
	        		{
	        			obj.setOutageLessThan22Count(obj1.getOutagecount());
	        			obj.setOutageLessThan22Duration(obj1.getSupply_duration().substring(0,8));
	        			obj.setPercentage(String.valueOf((Math.round(Integer.parseInt(obj1.getOutagecount())/Integer.parseInt(obj.getCount()))*100)/100));
	        		}
	        	}
	        }
	        	completeData.addAll(ls1);
		 }
		 if (reportType.equalsIgnoreCase("DISTRICT")) {
			 sql = "select project,d.name discom ,sid, z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,"+
		           "count(sid) count ,Dstatus,serial_no,RIGHT(appliance_id,10) appliance_id,round(avg(supply_duration),2) supply_duration,sum(outagecount) outagecount,avg(outage_duration) outage_duration from "+
		           " (select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"+ 
		           " s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id , case when sum(f.event_data) is null then 24*60*60 else (24*60*60 - sum(f.event_data)) end as supply_duration,"+
		           " case when count(f.id) is null then 0 else count(f.id) end as outagecount,case when sum(f.event_data) is null then 0 else sum(f.event_data) end as outage_duration from sensor s left join feeder_daily_statistics_final f"+  
		           " on "+discom+" and f.sensor_id = s.id and f.event_date='"+mDate+ "'  and f.event_type is null  and f.parent_sensor_id=s.id where"+
		           "  s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type='2'"+project+" and s.deployed_type !='SPARE' and s.location_no is not null group by s.id) as outage , discom d, zone z , circle c , division di , site si"+
		           " where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id and supply_duration <=  86400 "+group+" order by project,outage.discom_id,location_id";
			 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
		  				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
		  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
		  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
		  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
		  				.addScalar("Dstatus", new StringType())
		  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType()).addScalar("supply_duration",new StringType())
		  				.addScalar("sid", new StringType())
		  				.addScalar("outagecount", new StringType())
		  				.addScalar("outage_duration", new StringType())
		  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
			 List<SupplyReportResponseTownWiseData> ls=query.list();
			 sql="select project,d.name discom , z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,"+
		                   "Dstatus,serial_no,LEFT(appliance_id,10) appliance_id,sid,count(sid) outagecount,SEC_TO_TIME(avg(seconds)) supply from "+ 
		                   "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id, "+ 
		                   "s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"+ 
		                   "s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id , case when count(f.id) is null then 0 else count(f.id) end as outagecount,"+
		                   "case when sum(f.event_data) is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds from sensor s left join feeder_daily_statistics_final f"+ 
		                   " on "+discom+" and f.sensor_id = f.sensor_id  and f.event_date='"+mDate+"' and f.event_type is null  and f.parent_sensor_id=s.id  where "+
		                  " s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type='2' and s.deployed_type !='SPARE' "+project+
		                 " and s.location_no is not null group by sid) as outage , discom d, zone z , circle c , division di , site si"+
		                 " where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id"+ 
		                 " and seconds < 79200 " + group+ "  order by project,outage.discom_id,location_id "; 
			 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
		  				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
		  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
		  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
		  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
		  				.addScalar("Dstatus", new StringType())
		  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType()).addScalar("supply_duration",new StringType())
		  				.addScalar("sid", new StringType())
		  				.addScalar("outagecount", new StringType())
		  				.addScalar("outage_duration", new StringType())
		  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
			 List<SupplyReportResponseTownWiseData> ls1=query.list();
			 Iterator itr=ls.iterator();
			 Iterator itr1=ls1.iterator();
			 while(itr.hasNext())
			 {
				 SupplyReportResponseTownWiseData obj=(SupplyReportResponseTownWiseData)itr.next();
				 while(itr1.hasNext())
				 {
					 SupplyReportResponseTownWiseData obj1=(SupplyReportResponseTownWiseData)itr1.next();
					 obj.setOutageLessThan22Count("0");
					 obj.setOutageLessThan22Duration("0");
					 obj.setPercentage("0");
					 if(obj.getDistrict().equalsIgnoreCase(obj1.getDistrict()))
					 {
						 obj.setOutageLessThan22Count(obj1.getOutageLessThan22Count());
						 obj.setOutageLessThan22Duration(obj1.getSupply_duration().substring(0,8));
						 obj.setPercentage(String.valueOf((Math.round(Integer.parseInt(obj1.getOutagecount())/Integer.parseInt(obj.getCount()))*100)/100));
					 }
				 }
			 }
			 completeData.addAll(ls);
		 }
		 else if(reportType.equalsIgnoreCase("DISCOM"))
		 {
			 sql="select project,d.name discom ,sid, z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,"+
             "count(sid) count ,Dstatus,serial_no,RIGHT(appliance_id,10) appliance_id,round(avg(supply_duration),2) supply_duration,sum(outagecount) outagecount,avg(outage_duration) outage_duration from"+ 
            "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"+ 
            "s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id ,case when sum(f.event_data) is null then 24*60*60 else (24*60*60 - sum(f.event_data)) end as supply_duration,"+
            " case when count(f.id) is null then 0 else count(f.id) end as outagecount,case when sum(f.event_data) is null then 0 else sum(f.event_data) end as outage_duration"+ 
             " from sensor s left join feeder_daily_statistics_final f on"+discom+" and f.sensor_id = s.id and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id "+    
             " where s.type='AC_METER' and  s.admin_status in ('N','S','U')  and s.meter_type='2' "+project+" and s.deployed_type !='SPARE' and s.location_no is not null group by s.id) as outage , discom d, zone z , circle c , division di , site si"+
             " where d.id=outage.discom_id  and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id and supply_duration <=  86400 "+group+" order by project,outage.discom_id,location_id";
			 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
		  				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
		  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
		  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
		  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
		  				.addScalar("Dstatus", new StringType())
		  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType()).addScalar("supply_duration",new StringType())
		  				.addScalar("sid", new StringType())
		  				.addScalar("outagecount", new StringType())
		  				.addScalar("outage_duration", new StringType())
		  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
			 List<SupplyReportResponseTownWiseData> ls=query.list();
			 
			 sql="select project,d.name discom , z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,"+
		          "Dstatus,serial_no,LEFT(appliance_id,10) appliance_id,sid,count(sid) outagecount,SEC_TO_TIME(avg(seconds)) supply from "+ 
		          "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,"+
		          " s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"+ 
		          " s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id , case when count(f.id) is null then 0 else count(f.id) end as outagecount,"+
		          " case when sum(f.event_data) is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds from sensor s left join feeder_daily_statistics_final f "+  
		          " on "+discom+ " and f.sensor_id = f.sensor_id and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id where"+
		          " s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type='2' and s.deployed_type !='SPARE' "+project+"  and s.location_no is not null group by sid) as outage , discom d, zone z , circle c , division di , site si"+
		          " where d.id=outage.discom_id  and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id and seconds < 79200 "+group+" order by project,outage.discom_id,location_id ";
			 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
		  				.addScalar("discom", new StringType()).addScalar("sid", new StringType()).addScalar("zone", new StringType())
		  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
		  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
		  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
		  				.addScalar("Dstatus", new StringType())
		  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType()).addScalar("supply_duration",new StringType())
		  				.addScalar("sid", new StringType())
		  				.addScalar("outagecount", new StringType())
		  				.addScalar("outage_duration", new StringType())
		  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
			 List<SupplyReportResponseTownWiseData> ls1=query.list();
			 Iterator itr=ls.iterator();
			 Iterator itr1=ls1.iterator();
			 while(itr.hasNext())
			 {
				 SupplyReportResponseTownWiseData obj=(SupplyReportResponseTownWiseData)itr.next();
				 while(itr1.hasNext())
				 {
					 SupplyReportResponseTownWiseData obj1=(SupplyReportResponseTownWiseData)itr1.next();
					 obj.setOutageLessThan22Count("0");
					 obj.setOutageLessThan22Duration("0");
					 obj.setPercentage("0");
					 if(obj.getDiscom().equalsIgnoreCase(obj1.getDiscom()))
					 {
						 obj.setOutageLessThan22Count(obj1.getOutageLessThan22Count());
						 obj.setOutageLessThan22Duration(obj1.getSupply_duration().substring(0,8));
						 obj.setPercentage(String.valueOf((Math.round(Integer.parseInt(obj1.getOutagecount())/Integer.parseInt(obj.getCount()))*100)/100));
					 }
				 }
			 }
			 
			 /*sql = "select project,d.name discom ,sid, z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,"+
		           "count(sid) count ,Dstatus,serial_no,RIGHT(appliance_id,10) appliance_id,avg(supply_duration) supply_duration,sum(outagecount) outagecount,avg(outage_duration) outage_duration from "+ 
		           "(select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project ,"+ 
		           "s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id ,case when sum(f.event_data) is null then 24*60*60 else (24*60*60 - sum(f.event_data)) end as supply_duration,"+
		           "case when count(f.id) is null then 0 else count(f.id) end as outagecount,case when sum(f.event_data) is null then 0 else sum(f.event_data) end as outage_duration"+ 
		           " from sensor s left join feeder_daily_statistics_final f on "+discom+" and f.sensor_id = f.sensor_id and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id"+    
		           " where s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type='2' "+project+" and s.deployed_type !='SPARE' and s.location_no is not null group by s.id) as outage , discom d, zone z , circle c , division di , site si "+
		           " where d.id=outage.discom_id  and z.id=outage.zone_id and c.id=outage.circle_id and di.id=outage.division_id and si.id=outage.site_id and supply_duration <=  86400 "+group+" order by project,outage.discom_id,location_id";
			 
	         completeData.put("avgSupply", ls);
	         
			 sql = "select project,d.name discom , z.name zone , c.name circle , di.name division ,si.name ss ,district,town,feeder_name,Dstatus,serial_no,LEFT(appliance_id,10) appliance_id,sid,count(sid) outagecount,SEC_TO_TIME(avg(seconds)) supply from "+
		           " (select s.id sid,s.location_id location_id , s.discom_id discom_id , s.zone_id zone_id, s.circle_id circle_id,s.division_id division_id, s.site_id site_id, s.location_id district, s.location_no town, s.project_id project,s.name feeder_name ,s.device_state Dstatus, s.serial_no serial_no, s.appliance_id,"+ 
		           "case when count(f.id) is null then 0 else count(f.id) end as outagecount,case when sum(f.event_data) is null  then 24*60*60 else (24*60*60 - sum(f.event_data)) end as seconds from sensor s left join feeder_daily_statistics_final f  on "+discom+ 
		           " and f.sensor_id = f.sensor_id and f.event_date='"+mDate+"' and f.event_type is null and f.parent_sensor_id=s.id where s.type='AC_METER' and  s.admin_status in ('N','S','U') and s.meter_type='2' and s.deployed_type !='SPARE' "+project+
		           "and s.location_no is not null group by sid) as outage , discom d, zone z , circle c , division di , site si where d.id=outage.discom_id and z.id=outage.zone_id and c.id=outage.circle_id  and di.id=outage.division_id and si.id=outage.site_id and seconds < 79200 "+group+" order by project,outage.discom_id,location_id ";
			 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType())
		  				.addScalar("discom", new StringType()).addScalar("zone", new StringType())
		  				.addScalar("circle", new StringType()).addScalar("division", new StringType())
		  				.addScalar("ss", new StringType()).addScalar("district", new StringType())
		  				.addScalar("town", new StringType()).addScalar("feeder_name", new StringType())
		  				.addScalar("Dstatus", new StringType())
		  				.addScalar("serial_no", new StringType()).addScalar("appliance_id", new StringType())
		  				.addScalar("sid", new StringType())
		  				.addScalar("outagecount", new StringType())
		  				.addScalar("supply", new StringType())
		  				.setResultTransformer(Transformers.aliasToBean(SupplyReportResponseTownWiseData.class));
			 List<SupplyReportResponseTownWiseData> ls1=query.list();
		     completeData.put("supply", ls1);*/
			 completeData.addAll(ls);
		 }
		
		return completeData;
	}


	@Override
	public List<SupplyReportDetails> getSupplyReportTownWiseData(DataRequest dataRequest) {
		String mDate = dataRequest.getDate();
		String projectId = dataRequest.getProject_id();
		String discomId = dataRequest.getDiscom_id();
		String reportType = dataRequest.getReportType();
		String discom = null;
		String Sdiscom = null;
		String project = null;
		String group = null;
		String sql = null;
		Query query = null;
		String project1 = null;
		List<SupplyReportDetails> ls = null;
		if (projectId != null && projectId.equalsIgnoreCase("16NN")) {
			project = " and trim(umd.project_id) IN('R-APDRP','IPDS','EODB','R-APDRP KANPUR')";
			project1 = "  where total.project IN('R-APDRP','IPDS','EODB','R-APDRP KANPUR') ";
		} else if (projectId != null && !projectId.equalsIgnoreCase("all") && !projectId.equalsIgnoreCase("16NN")) {
			project = " and umd.project_id='" + projectId + "'";
			project1 = " where total.project ='" + projectId + "'";
		} else if (projectId.equalsIgnoreCase("All")) {
			project = " and umd.project_id IN('EODB','IPDS','NTPF')";
			project1 = "where total.project IN('EODB','IPDS','NTPF')";
		}
		if (reportType.equalsIgnoreCase("DISTRICT")) {
			group = "group by project,discom,district ";
		} else if (reportType.equalsIgnoreCase("TOWN")) {
			group = "group by project,discom,district,town ";
		} else if (reportType.equalsIgnoreCase("DISCOM")) {
			group = "group by project,discom ";
		}
		if (discomId != null && discomId.equalsIgnoreCase("ALL")) {
			discom = "   discom_id =discom_id ";
		} else {
			discom = "   discom_id ='" + discomId+ "' ";
		}

		if (reportType.equalsIgnoreCase("DETAILS")) {
			sql = "select feeder_id feederId , discom_id discomId,project_id project,discom_name discom,zone_name zone,circle_name circle,town_name town, "
					+ "site_name substation,feeder_name feeder,serial_no serial_no,feeder_gis_code gis_code,remark remark, "
					+ "interruption_count totInttCount,interruption_duration inttInMin,round(interruption_duration/60) inttInHour, '24' defaultSupplyInHour, "
					+ "supply_duration avgSupplyHour from uppcl_master_data_log where event_date='" + mDate
					+ "' and remark='16 NN REPORT' and "+discom+" ";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
					.addScalar("discomId", new StringType()).addScalar("project", new StringType())
					.addScalar("discom", new StringType()).addScalar("zone", new StringType()).addScalar("circle", new StringType())
					.addScalar("town", new StringType()).addScalar("substation", new StringType())
					.addScalar("feeder", new StringType()).addScalar("serial_no", new StringType())
					.addScalar("gis_code", new StringType()).addScalar("remark", new StringType())
					.addScalar("totInttCount", new IntegerType()).addScalar("inttInMin", new IntegerType())
					.addScalar("inttInHour", new DoubleType()).addScalar("defaultSupplyInHour", new StringType())
					.addScalar("avgSupplyHour", new DoubleType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
			ls = query.list();

		}
		
		if (reportType.equalsIgnoreCase("TOWN")) {
			sql = "select feeder_id feederId , discom_id discomId,count(distinct town_name) No_of_town,town_name town, count(id) Total_fedr ,"
					+ " count(id) Live_fedr,project_id project,"
					+ "discom_name discom,remark remark, avg(interruption_count) totInttCount,avg(interruption_duration) inttInMin,"
					+ "round(avg(interruption_duration)/60,2) inttInHour, '24' defaultSupplyInHour, "
					+ "SUM(IF(round(supply_duration) <= 22*60 , 1, 0)) AS num,"
					+ "round(SUM(IF(round(supply_duration) < 22*60 , 1, 0))/SUM(IF(id=id, 1, 0))*100,2) as percentage,"
					+ "round(sum(IF(round(supply_duration) < 22*60 , supply_duration/60, 0))/SUM(IF(round(supply_duration) < 22*60 , 1, 0)),2) as avgSupplyLessThan22,"
					+ "round(avg(supply_duration)/60,2) avgSupplyHour from uppcl_master_data_log where event_date='" + mDate
					+ "' and remark='16 NN REPORT' and "+discom+" group by discom_name,town_name";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
					.addScalar("No_of_town", new IntegerType())
					.addScalar("town", new StringType())
					.addScalar("discom", new StringType())
					.addScalar("Total_fedr", new IntegerType())
					.addScalar("Live_fedr", new IntegerType())
					.addScalar("avgSupplyHour", new DoubleType())
					.addScalar("defaultSupplyInHour", new StringType())
					.addScalar("num", new IntegerType())
					.addScalar("percentage", new DoubleType())
					.addScalar("avgSupplyLessThan22", new DoubleType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
			ls = query.list();
			/*String sql1 = "select feeder_id feederId , discom_id discomId,count(distinct town_name) No_of_town, town_name town, count(id) num , count(id) Live_fedr,project_id project,"
					+ "discom_name discom,remark remark, sum(interruption_count) totInttCount,sum(interruption_duration) inttInMin,"
					+ "interruption_duration inttInHour, '24' defaultSupplyInHour, "
					+ "round(((24*60-avg(interruption_duration))/60),2) avgSupplyHour from uppcl_master_data_log where event_date='" + mDate
					+ "' and remark='16 NN REPORT' and "+discom+" and round(supply_duration/60,2) <= 22 group by discom_name,town_name";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql1).addScalar("num", new IntegerType())
					.addScalar("avgSupplyHour", new DoubleType())
					.addScalar("discom", new StringType()).addScalar("town", new StringType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
			List<SupplyReportDetails> ls1 = query.list();
			Iterator itr = ls.iterator();
			
			while (itr.hasNext()) {
				SupplyReportDetails details = (SupplyReportDetails) itr.next();
				Iterator itr1 = ls1.iterator();
				while (itr1.hasNext()) {
					SupplyReportDetails details1 = (SupplyReportDetails) itr1.next();
					if (details.getTown().trim().equalsIgnoreCase(details1.getTown().trim())) {
						details.setNum((details1.getNum()));
						details.setPercentage((double)(Math.round(((double)details1.getNum()/(double)details.getTotal_fedr())*100.0)));
						details.setAvgSupplyLessThan22(details1.getAvgSupplyHour());
					}
				}

			}*/
		} else if (reportType.equalsIgnoreCase("DISCOM")) {
			sql = "select feeder_id feederId , discom_id discomId,count(distinct town_name) No_of_town, count(id) Total_fedr , count(id) Live_fedr,project_id project,"
					+ "discom_name discom,remark remark, avg(interruption_count) totInttCount,avg(interruption_duration) inttInMin,"
					+ "round(sum(interruption_duration)/60,2) inttInHour, '24' defaultSupplyInHour, "
					+ "SUM(IF(round(supply_duration) < 22*60 , 1, 0)) AS num,"
					+ "round(SUM(IF(round(supply_duration) < 22*60 , 1, 0))/SUM(IF(id=id, 1, 0))*100,2) as percentage,"
					+ "round(sum(IF(round(supply_duration) < 22*60 , supply_duration/60, 0))/SUM(IF(round(supply_duration) < 22*60 , 1, 0)),2) as avgSupplyLessThan22,"
					+ "round(avg(supply_duration)/60,2) avgSupplyHour from uppcl_master_data_log where event_date='" + mDate
					+ "' and remark='16 NN REPORT' and "+discom+" group by discom_name";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
					.addScalar("No_of_town", new IntegerType()).addScalar("discom", new StringType())
					.addScalar("Total_fedr", new IntegerType()).addScalar("Live_fedr", new IntegerType())
					.addScalar("avgSupplyHour", new DoubleType())
					.addScalar("defaultSupplyInHour", new StringType())
					.addScalar("num", new IntegerType())
					.addScalar("percentage", new DoubleType())
					.addScalar("avgSupplyLessThan22", new DoubleType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
			ls = query.list();
			/*String sql1 = "select feeder_id feederId , discom_id discomId,count(distinct town_name) No_of_town, count(id) num , count(id) Live_fedr,project_id project,"
					+ "discom_name discom,remark remark, sum(interruption_count) totInttCount,sum(interruption_duration) inttInMin,"
					+ "interruption_duration inttInHour, '24' defaultSupplyInHour, "
					+ "round(((24*60-avg(interruption_duration))/60),2) avgSupplyHour from uppcl_master_data_log where event_date='" + mDate
					+ "' and remark='16 NN REPORT' and "+discom+" and round(supply_duration/60,2) <= 22 group by discom_name";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql1).addScalar("num", new IntegerType())
					.addScalar("avgSupplyHour", new DoubleType())
					.addScalar("discom", new StringType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
			List<SupplyReportDetails> ls1 = query.list();
			Iterator itr = ls.iterator();
			
			while (itr.hasNext()) {
				SupplyReportDetails details = (SupplyReportDetails) itr.next();
				Iterator itr1 = ls1.iterator();
				while (itr1.hasNext()) {
					SupplyReportDetails details1 = (SupplyReportDetails) itr1.next();
					
					if (details.getDiscom().trim().equalsIgnoreCase(details1.getDiscom().trim())) {
					 
						details.setNum((details1.getNum()));
						//details.setPercentage(Math.round(details1.getNum()/details.getTotal_fedr()));
						details.setPercentage((double)(Math.round(((double)details1.getNum()/(double)details.getTotal_fedr())*100.0)));
						details.setAvgSupplyLessThan22(details1.getAvgSupplyHour());
					}
				}

			}*/
		}

		return ls;
	}

	public List<FeederData> getFeederStatDetails(String project_id, String feederStatus) {

		List<FeederData> FeederDatalist = new ArrayList<FeederData>();
		String sql = "";
		String con1 = "";
		String con2 = "";
		Query query;
		if ("OK".equalsIgnoreCase(feederStatus)) {
			con1 = "device_state='UP'";
		} else if ("NOTOK".equalsIgnoreCase(feederStatus)) {
			con1 = " device_state in ('NC','NP')";
		} else if ("OUTAGE".equalsIgnoreCase(feederStatus)) {
			con1 = "device_state='DN' ";
		}
		if ("Industrial".equalsIgnoreCase(project_id)) {
			con2 = " s.project_id='EODB' ";
		} else if ("Urban".equalsIgnoreCase(project_id)) {
			con2 = " s.project_id in ('IPDS','EODB')";
		} else {
			con2 = " s.project_id=s.project_id";
		}
		sql = "select s.id,s.sub_type sub_type,device_type,s.remark,s.grid_load_alarm,s.dg_load_alarm,TRUNCATE(s.max_demand_KVA,2) as max_demand_KVA, "
				+ " s.dic_id dic_id ,s.device_state device_state,s.csq_signal_strength csq, s.dic_port dic_port,s.UOM uom,concat(site.name,' ',s.name) as name ,"
				+ " s.serial_no as serial_no,TRUNCATE(s.KWh,2) as KWh, TRUNCATE(s.KVAh,2) as KVAh,TRUNCATE(s.instant_R_KW,2) As instant_R_KW,"
				+ " TRUNCATE(s.instant_cum_KW,2) as instant_cum_KW, TRUNCATE(s.instant_cum_KVA,2) as instant_cum_KVA,"
				+ " s.R_Voltage R_Voltage,s.Y_Voltage Y_Voltage,s.B_Voltage B_Voltage,"
				+ " TRUNCATE(s.R_Current,2) as R_Current, TRUNCATE(s.Y_Current,2) as Y_Current, TRUNCATE(s.B_Current,2) as B_Current,"
				+ " s.R_PF,s.Y_PF,s.B_PF,s.cumm_pf,"
				+ " TRUNCATE(s.KWh1,2) KWh1,TRUNCATE(s.KWh2,2) KWh2,TRUNCATE(s.KWh3,2) KWh3,TRUNCATE(s.KWh4,2) KWh4, TRUNCATE(s.KWh5,2) KWh5,TRUNCATE(s.KWh6,2) KWh6,TRUNCATE(s.KWh7,2) KWh7,TRUNCATE(s.KWh8,2) KWh8,"
				+ " TRUNCATE(s.KVAh1,2) KVAh1,TRUNCATE(s.KVAh2,2) KVAh2,TRUNCATE(s.KVAh3,2) KVAh3, TRUNCATE(s.KVAh4,2) KVAh4,TRUNCATE(s.KVAh5,2) KVAh5,TRUNCATE(s.KVAh6,2) KVAh6, TRUNCATE(s.KVAh7,2) KVAh7, TRUNCATE(s.KVAh8,2) KVAh8,"
				+ " z.name zone_name,c.name circle_name,di.name division_name,site.name substation_name,"
				+ " s.last_reading_updated,s.frequency,s.connected,"
				+ " TRUNCATE(s.max_demand_KW,2) as max_demand_KW ,s.contactor_status,s.consumer_id,s.grid_load_sanctioned,s.last_packet_time,"
				+ " s.digital_input1,s.digital_input2,s.digital_input3,s.last_reading_updated_grid,"
				+ " TIMESTAMPDIFF(SECOND, s.last_reading_updated_grid, s.last_reading_updated)/3600 Hrs,RIGHT(s.applaince_id, 10) MC_UID,s.meter_ct_mf,s.CB_status_time,s.Incommer_status_time"
				+ " from sensor s , site site,zone z,circle c,division di " + " where s.utility=1 "
				+ " and d.id=s.data_logger_id and z.id=s.zone_id and c.id=s.circle_id and  di.id=s.division_id and s.type = 'AC_METER' "
				+ " and s.admin_status in ('N','S','U') and " + con1 + " and " + con2
				+ " and ABS(s.cumm_PF) < 0.90 and site.id = s.site_id order by name ";

		query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("id", new StringType())
				.addScalar("sub_type", new StringType()).addScalar("device_type", new StringType())
				.addScalar("remark", new StringType()).addScalar("grid_load_alarm", new StringType())
				.addScalar("dg_load_alarm", new StringType()).addScalar("max_demand_KVA", new StringType())
				.addScalar("max_demand_KW", new StringType()).addScalar("dic_id", new StringType())
				.addScalar("device_state", new StringType()).addScalar("csq", new StringType())
				.addScalar("dic_port", new StringType()).addScalar("uom", new StringType())
				.addScalar("name", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("KWh", new StringType()).addScalar("KVAh", new StringType())
				.addScalar("instant_R_KW", new StringType()).addScalar("instant_cum_KW", new StringType())
				.addScalar("instant_cum_KVA", new StringType()).addScalar("R_Voltage", new StringType())
				.addScalar("Y_Voltage", new StringType()).addScalar("B_Voltage", new StringType())
				.addScalar("R_Current", new StringType()).addScalar("Y_Current", new StringType())
				.addScalar("B_Current", new StringType()).addScalar("R_PF", new StringType())
				.addScalar("Y_PF", new StringType()).addScalar("B_PF", new StringType())
				.addScalar("cumm_pf", new StringType()).addScalar("KWh1", new StringType())
				.addScalar("KWh2", new StringType()).addScalar("KWh3", new StringType())
				.addScalar("KWh4", new StringType()).addScalar("KWh5", new StringType())
				.addScalar("KWh6", new StringType()).addScalar("KWh7", new StringType())
				.addScalar("KWh8", new StringType()).addScalar("KVAh1", new StringType())
				.addScalar("KVAh2", new StringType()).addScalar("KVAh3", new StringType())
				.addScalar("KVAh4", new StringType()).addScalar("KVAh5", new StringType())
				.addScalar("KVAh6", new StringType()).addScalar("KVAh7", new StringType())
				.addScalar("KVAh8", new StringType()).addScalar("hrs", new StringType())
				.addScalar("MC_UID", new StringType()).addScalar("meter_ct_mf", new StringType())
				.addScalar("last_reading_updated", new StringType()).addScalar("frequency", new StringType())
				.addScalar("connected", new StringType()).addScalar("contactor_status", new StringType())
				.addScalar("consumer_id", new StringType()).addScalar("grid_load_sanctioned", new StringType())
				.addScalar("last_packet_time", new StringType()).addScalar("digital_input1", new StringType())
				.addScalar("digital_input2", new StringType()).addScalar("digital_input3", new StringType())
				.addScalar("last_reading_updated_grid", new StringType())
				.addScalar("incommer_status_time", new StringType()).addScalar("CB_status_time", new StringType())
				.addScalar("zone_name", new StringType()).addScalar("circle_name", new StringType())
				.addScalar("division_name", new StringType()).addScalar("substation_name", new StringType())
				.setResultTransformer(Transformers.aliasToBean(FeederData.class));
		return FeederDatalist = query.list();
	}

	public List<FeederDailyStatisticFinalBean> getBestFeederMaster(final String projectId, final String orderBy, final int limitby, final String... date) {
        String from = null;
        String to = null;
        int month = 0;
        int dayofMonth = 0;
        int pmonth = 0;
        String dayOfMonth = null;
        String dateDifference = "31";
        String project="";
        final Calendar cal = Calendar.getInstance();
        if (date[0] != null && date[1] == null && date[2] == null) {
            from = date[0].trim() + "-1";
            to = date[0].trim() + "-31";
            final Date date2 = new Date();
            cal.setTime(date2);
            month = cal.get(2) + 1;
            dayofMonth = cal.get(5);
            dayOfMonth = String.valueOf(dayofMonth);
            final String[] dateString = from.split("-");
            pmonth = Integer.parseInt(dateString[1]);
            if (month == pmonth) {
                to = to.trim() + "-" + dayOfMonth;
                dateDifference = dayOfMonth;
            }
            else {
                final LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
                final LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
                to = end.toString();
                final String[] datedif = to.split("-");
                dateDifference = datedif[2];
            }
        }
        else if (date[0] == null && date[1] != null && date[2] != null) {
           // from = date[1].trim() + "-01";
            //to = date[2].trim() + "-31";
        	from = date[1].trim();
        	to = date[2].trim();
            final LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            final LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dateDifference = String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate) + 1L);
        }
        if(projectId!=null && !projectId.equalsIgnoreCase("all") && projectId.equalsIgnoreCase("152")) {
        	 project=" where remark like '152%'";
        }
        else if(projectId!=null && !projectId.equalsIgnoreCase("all") && projectId.equalsIgnoreCase("16 NN")) {
       	 project=" where remark like '16 NN%'";
       }
        else if(projectId!=null && !projectId.equalsIgnoreCase("all")) 
        {
        	project=" where project_id='"+projectId+"'";
        }
        else
        {
        	project="";
        }
       
        
        List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
        String sql = " select id sensor_id ,discom_name Discom_name, zone_name Zone_name,district_name,town_name,circle_name Circle_name,division_name Division_name,site_name SS_name,feeder_name,";
        sql = sql + "1 feeder_count,'" + from + "' event_date ,modem_no MC_UID, serial_no,round((IFNULL(Rd, 0)))+round(IFNULL(Sd, 0)) Total_Outage_Duration, round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0)) Outage_count,avg(round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0))) avg_outage_count,";
        sql = sql + dateDifference + " no_of_day ,'" + from + "' from_date , '" + to + "' to_date ,  round((IFNULL(RavgDuration, 0)))+round(IFNULL(SavgDuration, 0)) avg_outage_duration, ";
        sql = sql + "LEFT(SEC_TO_TIME((round((IFNULL(Rd, 0)))+round(IFNULL(Sd, 0))/round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0)))*(round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0)))/'" + dateDifference + "'),8) avg_outage_duration_per_day from ";
        sql += "(select umd.*,fds.duration Rd,sid.duration Sd,fds.Rc,sid.Sc,RavgDuration,SavgDuration from uppcl_master_data umd   ";
        sql += "LEFT JOIN (SELECT parent_sensor_id, round(sum(event_data)/60) as duration , count(id) Rc , avg(round((event_data)/60)) RavgDuration ";
        sql += "FROM feeder_daily_statistics_final ";
        sql += "where ";
        sql += "parent_sensor_id=parent_sensor_id ";
        sql = sql + "and  event_date between '" + from + "' and '" + to + "' ";
        sql += "and creation_time=creation_time GROUP BY parent_sensor_id ) fds ";
        sql += "ON fds.parent_sensor_id = trim(umd.parent_sensor_id) ";
        sql += "LEFT JOIN ";
        sql += "(SELECT meter_serial_no, sum(total_interruption_duration) ";
        sql = sql + "as duration , sum(total_interruption_count) Sc , avg(total_interruption_duration) SavgDuration FROM secure_interruption_data where meter_date between '" + from + "' and '" + to + "' GROUP BY meter_serial_no ) ";
        sql += "sid ON sid.meter_serial_no = trim(umd.serial_no)"+project+") as master  ";
        
        
        if(orderBy.equalsIgnoreCase("Outage_count")) {
        	sql += "where discom_name=discom_name and round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0)) > 9 and round((IFNULL(Rd, 0)))+round(IFNULL(Sd, 0)) > 9 group by id order by ";
            	
        } else {
        	sql += "where discom_name=discom_name and round((IFNULL(Rd, 0)))+round(IFNULL(Sd, 0)) > 9 group by id order by ";
            
        }
        sql = sql + orderBy + "  limit " + limitby + "";
        final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id",new StringType()).addScalar("Discom_name", new StringType()).addScalar("Zone_name",new StringType()).addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType()).addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType()).addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType()).addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType()).addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType()).addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType()).addScalar("avg_outage_duration", new StringType()).addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType()).addScalar("MC_UID", new StringType()).setResultTransformer(Transformers.aliasToBean((Class)FeederDailyStatisticFinalBean.class));
        feederDailyStatisticFinal = (List<FeederDailyStatisticFinalBean>)query.list();
        return feederDailyStatisticFinal;
    }
    
    public List<FeederDailyStatisticFinalBean> getWorstFeederMaster(final String projectId, final String orderBy, final int limitby, final String... date) {
        String from = null;
        String to = null;
        int month = 0;
        int dayofMonth = 0;
        int pmonth = 0;
        String dayOfMonth = null;
        String dateDifference = "31";
        String project="";
        if(projectId!=null && !projectId.equalsIgnoreCase("all") && projectId.equalsIgnoreCase("152")) {
       	 project=" where remark like '152%'";
       }
       else if(projectId!=null && !projectId.equalsIgnoreCase("all") && projectId.equalsIgnoreCase("16 NN")) {
      	 project=" where remark like '16 NN%'";
      }
       else if(projectId!=null && !projectId.equalsIgnoreCase("all")) 
       {
       	project=" where project_id='"+projectId+"'";
       }
       else
       {
       	project="";
       }
        final Calendar cal = Calendar.getInstance();
        if (date[0] != null && date[1] == null && date[2] == null) {
            from = date[0].trim() + "-1";
            to = date[0].trim() + "-31";
            final Date date2 = new Date();
            cal.setTime(date2);
            month = cal.get(2) + 1;
            dayofMonth = cal.get(5);
            dayOfMonth = String.valueOf(dayofMonth);
            final String[] dateString = from.split("-");
            pmonth = Integer.parseInt(dateString[1]);
            if (month == pmonth) {
                to = to.trim() + "-" + dayOfMonth;
                dateDifference = dayOfMonth;
            }
            else {
                final LocalDate initial = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 1);
                final LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
                to = end.toString();
                final String[] datedif = to.split("-");
                dateDifference = datedif[2];
            }
        }
        else if (date[0] == null && date[1] != null && date[2] != null) {
           // from = date[1].trim() + "-01";
           // to = date[2].trim() + "-31";
        	from = date[1].trim();
        	to = date[2].trim();
            final LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            final LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dateDifference = String.valueOf(ChronoUnit.DAYS.between(fromDate, toDate) + 1L);
        }
        List<FeederDailyStatisticFinalBean> feederDailyStatisticFinal = new ArrayList<FeederDailyStatisticFinalBean>();
        String sql = " select id sensor_id ,discom_name Discom_name, zone_name Zone_name,district_name,town_name,circle_name Circle_name,division_name Division_name,site_name SS_name,feeder_name,";
        sql = sql + "1 feeder_count,'" + from + "' event_date ,modem_no MC_UID, serial_no,round((IFNULL(Rd, 0)))+round(IFNULL(Sd, 0)) Total_Outage_Duration, round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0)) Outage_count,avg(round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0))) avg_outage_count,";
        sql = sql + dateDifference + " no_of_day ,'" + from + "' from_date , '" + to + "' to_date ,  round((IFNULL(RavgDuration, 0)))+round(IFNULL(SavgDuration, 0)) avg_outage_duration, ";
        sql = sql + "LEFT(SEC_TO_TIME((round((IFNULL(Rd, 0)))+round(IFNULL(Sd, 0))/round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0)))*(round((IFNULL(Rc, 0)))+round(IFNULL(Sc, 0)))/'" + dateDifference + "'),8) avg_outage_duration_per_day from ";
        sql += "(select umd.*,fds.duration Rd,sid.duration Sd,fds.Rc,sid.Sc,RavgDuration,SavgDuration from uppcl_master_data umd   ";
        sql += "LEFT JOIN (SELECT parent_sensor_id, round(sum(event_data)/60) as duration , count(id) Rc , avg(round((event_data)/60)) RavgDuration ";
        sql += "FROM feeder_daily_statistics_final ";
        sql += "where ";
        sql += "parent_sensor_id=parent_sensor_id ";
        sql = sql + "and  event_date between '" + from + "' and '" + to + "' ";
        sql += "and creation_time=creation_time GROUP BY parent_sensor_id ) fds ";
        sql += "ON fds.parent_sensor_id = trim(umd.parent_sensor_id) ";
        sql += "LEFT JOIN ";
        sql += "(SELECT meter_serial_no, sum(total_interruption_duration) ";
        sql = sql + "as duration , sum(total_interruption_count) Sc , avg(total_interruption_duration) SavgDuration FROM secure_interruption_data where meter_date between '" + from + "' and '" + to + "' GROUP BY meter_serial_no ) ";
        sql += "sid ON sid.meter_serial_no = trim(umd.serial_no)"+project+") as master  ";
        sql += "where discom_name=discom_name and round((IFNULL(Rd, 0)))+round(IFNULL(Sd, 0)) > 9 group by id order by ";
        sql = sql + orderBy + " desc  limit " + limitby + "";
        final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sensor_id", new StringType()).addScalar("Discom_name", new StringType()).addScalar("Zone_name", new StringType()).addScalar("Circle_name", new StringType()).addScalar("Division_name", new StringType()).addScalar("SS_name", new StringType()).addScalar("feeder_name", new StringType()).addScalar("feeder_count", new StringType()).addScalar("event_date", new StringType()).addScalar("Outage_count", new StringType()).addScalar("Total_Outage_Duration", new StringType()).addScalar("no_of_day", new StringType()).addScalar("from_date", new StringType()).addScalar("to_date", new StringType()).addScalar("avg_outage_count", new StringType()).addScalar("avg_outage_duration", new StringType()).addScalar("avg_outage_duration_per_day", new StringType()).addScalar("serial_no", new StringType()).addScalar("MC_UID", new StringType()).setResultTransformer(Transformers.aliasToBean((Class)FeederDailyStatisticFinalBean.class));
        feederDailyStatisticFinal = (List<FeederDailyStatisticFinalBean>)query.list();
        return feederDailyStatisticFinal;
    }

	public List<MasterData> MastersupplyStatusData(final String fromDate, final String toDate, final String discom_id,
			final String hour, final String reportType, String projectId) {
		String discomName = "";
		String sql = "";
		String project = "";
		String dateDifference = "1";
		final LocalDate from = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		final LocalDate to = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		dateDifference = String.valueOf(ChronoUnit.DAYS.between(from, to) + 1L);
		if (!discom_id.equalsIgnoreCase("ALL")) {
			discomName = " discom_name = UPPER(trim('" + discom_id + "')) ";
		} else {
			discomName = "discom_name = discom_name ";
		}
		if (!projectId.equalsIgnoreCase("all")) {
			project = " && project_id='" + projectId + "'";
		}
		if (reportType.equalsIgnoreCase("currentDay") || reportType == null) {
			if (Integer.parseInt(hour) != 0) {
				final int supplyhour = Integer.parseInt(hour) * 60;
				sql = "select discom_id,discom_name discom,zone_name zone,division_name division,circle_name circle,town_name town,district_name district,"
						+ "project_id project,serial_no meterNo,site_name ss, feeder_name, round(avg(interruption_duration)/"
						+ dateDifference + ",2) Outage_duration ," + "round((avg(interruption_duration)/60)/"+dateDifference+",2) inttInHour ,"
						+ "round((avg(supply_duration)/60)/"+dateDifference+",2) avgSupplyHour,count(id) count , "
						+ "avg(interruption_count)/"+dateDifference+" totInttCount from uppcl_master_data_log  where  event_date between '"
						+ fromDate + "' AND '" + toDate + "' " + " and supply_duration <=" + supplyhour
						+ " and feeder_type='OUTGOING' and " + "  " + discomName + " group by serial_no ";
			} else {
				sql = "select discom_id,discom_name discom,zone_name zone,division_name division,circle_name circle,town_name town,district_name district,"
						+ "project_id project,serial_no meterNo,site_name ss, feeder_name ,round(avg(interruption_duration)/"+dateDifference+",2) Outage_duration , round((avg(interruption_duration)/60)/"+dateDifference+",2) inttInHour,"
						+ "round((avg(supply_duration)/60)/"+dateDifference+",2) avgSupplyHour,count(id) count , "
						+ "avg(interruption_count)/"+dateDifference+" totInttCount from uppcl_master_data_log  where  event_date between '"
						+ fromDate + "' AND '" + toDate + "' " + " and  feeder_type='OUTGOING' and " + "  " + discomName
						+ " " + project + " group by serial_no ";
			}
		}
		if (reportType.equalsIgnoreCase("PastDaysAvg")) {
			if (Integer.parseInt(hour) != 0) {
				final int supplyhour = Integer.parseInt(hour) * 60;
				sql = " select avg((24-(IFNULL(Rd,0)+IFNULL(Sd,0))/60))  avgSupplyHour,((IFNULL(Rd,0)+IFNULL(Sd,0))/60) inttInHour,id sensor_id,discom_name discom,feeder_type,zone_name zone,district_name district,town_name town,circle_name circle,division_name division, site_name ss,feeder_name,serial_no meterNo,project_id project, round((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)),0) Outage_duration , round(((24*60)-((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)))),0) supply_duration, count(id) count  from (select umd.*,";
				sql += " fds.duration Rd, sid.duration Sd from uppcl_master_data umd   LEFT JOIN (SELECT parent_sensor_id,  round(sum(event_data)/60) as duration";
				sql += " FROM feeder_daily_statistics_final ";
				sql += " where ";
				sql += " parent_sensor_id=parent_sensor_id";
				sql = sql + " and  event_date between '" + fromDate + "-INTERVAL 1 WEEK' and '" + toDate + "' ";
				sql += " and creation_time=creation_time GROUP BY parent_sensor_id ) fds ON fds.parent_sensor_id = trim(umd.parent_sensor_id)";
				sql += " LEFT JOIN (SELECT meter_serial_no, sum(total_interruption_duration) ";
				sql = sql + " as duration FROM secure_interruption_data where meter_date between '" + fromDate
						+ "-INTERVAL 1 WEEK' and '" + toDate + "' GROUP BY meter_serial_no )";
				sql = sql
						+ " sid ON sid.meter_serial_no = trim(umd.serial_no) where round(((24*60)-((IFNULL((fds.duration), 0)+ IFNULL((sid.duration), 0)))),0)  <= '"
						+ supplyhour + "') as master  where " + discomName + "  "
						+ "and feeder_type='OUTGOING' group by id;";
			} else {
				sql = " select avg((24-(IFNULL(Rd,0)+IFNULL(Sd,0))/60))  avgSupplyHour,((IFNULL(Rd,0)+IFNULL(Sd,0))/60) inttInHour, id sensor_id,discom_name discom,feeder_type,zone_name zone,district_name district,town_name town,circle_name circle,division_name division, site_name ss,feeder_name,serial_no meterNo,project_id project, (IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)) Outage_duration , ((24*60)-((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)))) supply_duration, count(id) count  from (select umd.*,";
				sql += " fds.duration Rd, sid.duration Sd from uppcl_master_data umd   LEFT JOIN (SELECT parent_sensor_id,  round(sum(event_data)/60) as duration";
				sql += " FROM feeder_daily_statistics_final ";
				sql += " where ";
				sql += " parent_sensor_id=parent_sensor_id";
				sql = sql + " and  event_date between '" + fromDate + "-INTERVAL 1 WEEK' and '" + toDate + "' ";
				sql += " and creation_time=creation_time GROUP BY parent_sensor_id ) fds ON fds.parent_sensor_id = trim(umd.parent_sensor_id)";
				sql += " LEFT JOIN (SELECT meter_serial_no, sum(total_interruption_duration) ";
				sql = sql + " as duration FROM secure_interruption_data where meter_date between '" + fromDate
						+ "-INTERVAL 1 WEEK' and '" + toDate + "' GROUP BY meter_serial_no )";
				sql = sql
						+ " sid ON sid.meter_serial_no = trim(umd.serial_no) where  round(IFNULL(fds.duration, 0)+ IFNULL(sid.duration, 0),0) > 0) as master  "
						+ "where " + discomName + " and feeder_type='OUTGOING' group by id;";
			}
		}
		final Query supply = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("avgSupplyHour", new StringType()).addScalar("inttInHour", new StringType())
				.addScalar("discom", new StringType()).addScalar("zone", new StringType())
				.addScalar("circle", new StringType()).addScalar("district", new StringType())
				.addScalar("town", new StringType()).addScalar("project", new StringType())
				.addScalar("ss", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("division", new StringType()).addScalar("meterNo", new StringType())
				.setResultTransformer(Transformers.aliasToBean((Class) MasterData.class));
		final List<MasterData> ls = (List<MasterData>) supply.list();
		return ls;
	}
    
    public List<MasterData> getMasterData() {
        List<MasterData> masterDataList = new ArrayList<MasterData>();
        final String sql = "select serial_no meterNo, admin_status,discom_name discom, zone_name zone, district_name district, "
        		+ "circle_name circle, town_name town, division_name division,site_name ss , feeder_name ,remark, project_id project "
        		+ "from uppcl_master_data ";
        final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom", new StringType()).addScalar("zone", new StringType()).addScalar("circle", new StringType()).
        		addScalar("district", new StringType()).addScalar("town", new StringType())
        		.addScalar("project", new StringType()).addScalar("ss", new StringType())
        		.addScalar("feeder_name", new StringType())
        		.addScalar("division", new StringType())
        		.addScalar("meterNo", new StringType())
        		.addScalar("admin_status", new StringType())
        		.addScalar("remark", new StringType())
        		.setResultTransformer(Transformers.aliasToBean((Class)MasterData.class));
        masterDataList = (List<MasterData>)query.list();
        return masterDataList;
    }
    
    public List<MasterData> getMasterDataNtpf() {
        List<MasterData> masterDataNNList = new ArrayList<MasterData>();
        final String sql = "select serial_no meterNo, discom_name discom, zone_name zone, district_name district, circle_name circle, town_name town, division_name division,site_name ss , feeder_name , remark project "
        		+ "from uppcl_master_data where remark in ('16 NN REPORT','152 R-APDRP') and trim(project_id) !='R-APDRP Kanpur'";
        final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("discom", new StringType()).addScalar("zone", new StringType()).addScalar("circle", new StringType()).addScalar("district", new StringType()).addScalar("town", new StringType()).addScalar("project", new StringType()).addScalar("ss", new StringType()).addScalar("feeder_name", new StringType()).addScalar("division", new StringType()).addScalar("meterNo", new StringType()).setResultTransformer(Transformers.aliasToBean((Class)MasterData.class));
        masterDataNNList = (List<MasterData>)query.list();
        return masterDataNNList;
    }
    
    public List<SupplyStatus> MapsupplyStatus() {
        List<SupplyStatus> SupplyStatus = new ArrayList<SupplyStatus>();
        String sql = " select discom_id,discom_name,Rd,Sd,project, round((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)),0) Outage_duration , round(((24*60)-((IFNULL(AVG(Rd), 0)+ IFNULL(AVG(Sd), 0)))),0) supply_duration, count(id) count  from (select umd.id,umd.discom_id discom_id, umd.project_id project, umd.discom_name discom_name, umd.serial_no ,";
        sql += " fds.duration Rd, sid.duration Sd from uppcl_master_data umd   LEFT JOIN (SELECT parent_sensor_id,  round(sum(event_data)/60) as duration";
        sql += " FROM feeder_daily_statistics_final ";
        sql += " where ";
        sql += " parent_sensor_id=parent_sensor_id";
        sql += " and  event_date='CURDATE()' ";
        sql += " and creation_time=creation_time GROUP BY parent_sensor_id ) fds ON fds.parent_sensor_id = trim(umd.parent_sensor_id)";
        sql += " LEFT JOIN (SELECT meter_serial_no, sum(total_interruption_duration) ";
        sql += " as duration FROM secure_interruption_data where meter_date=CURDATE() GROUP BY meter_serial_no )";
        sql += " sid ON sid.meter_serial_no = trim(umd.serial_no)) as master  group by discom_name;";
        final Query supply = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("project", new StringType()).addScalar("discom_id", new StringType()).addScalar("discom_name", new StringType()).addScalar("supply_duration", new StringType()).addScalar("Outage_duration", new StringType()).addScalar("count", new StringType()).setResultTransformer(Transformers.aliasToBean((Class)SupplyStatus.class));
        SupplyStatus = (List<SupplyStatus>)supply.list();
        return SupplyStatus;
    }

	@Override
	public List<ReportTown8NN> getSupplyReport8NNData(DataRequest dataRequest) {
		String mDate = dataRequest.getDate();
		String discomId = dataRequest.getDiscom_id();
		String reportType = dataRequest.getReportType();
		String sql = null;
		Query query = null;
		String remark = "-";
		String percent = "%";
		List<ReportTown8NN> ls = null;
		String discomName = "";
		if (!discomId.equalsIgnoreCase("ALL")) {
			discomName = " and discom_id = trim('" + discomId + "') ";
		} else {
			discomName = " and discom_id = discom_id ";
		}

		if (reportType.equalsIgnoreCase("DETAILS")) {
			/*
			 * sql =
			 * "select feeder_id feederId , discom_id discomId,project_id project,discom_name discom,zone_name zone,circle_name circle,town_name town, "
			 * +
			 * "site_name substation,feeder_name feeder,serial_no serial_no,feeder_gis_code gis_code,remark remark, "
			 * +
			 * "interruption_count totInttCount,(interruption_duration/60) inttInHour,interruption_duration inttInMin, '24' defaultSupplyInHour, "
			 * +
			 * "supply_duration avgSupplyHour,remark remark,AVG(supply_duration) avgSupplyDuration,event_date eventDate,(interruption_duration/60) avgSupplyBet2223  from uppcl_master_data_log where event_date >= DATE_ADD('"
			 * +mDate+"',INTERVAL -2 DAY) " +
			 * " and trim(town_name) IN('LUCKNOW','VARANASI','GORAKHPUR','PRAYAGRAJ','GHAZIABAD','NOIDA','MATHURA','KANPUR') and interruption_duration between 22*60 and 23*60 group by id,event_date"
			 * ; query =
			 * sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar(
			 * "feederId", new StringType()) .addScalar("discomId", new
			 * StringType()).addScalar("project", new StringType())
			 * .addScalar("discom", new StringType()).addScalar("zone", new
			 * StringType()).addScalar("circle", new StringType())
			 * .addScalar("town", new StringType()).addScalar("substation", new
			 * StringType()) .addScalar("feeder", new
			 * StringType()).addScalar("serial_no", new StringType())
			 * .addScalar("gis_code", new StringType()).addScalar("remark", new
			 * StringType()) .addScalar("totInttCount", new IntegerType())
			 * .addScalar("inttInHour", new DoubleType()).addScalar("inttInMin",
			 * new IntegerType()).addScalar("defaultSupplyInHour", new
			 * StringType()) .addScalar("avgSupplyHour", new
			 * DoubleType()).addScalar("remark",new
			 * StringType()).addScalar("avgSupplyDuration",new
			 * StringType()).addScalar("eventDate",new
			 * StringType()).addScalar("avgSupplyBet2223",new DoubleType())
			 * .setResultTransformer(Transformers.aliasToBean(
			 * SupplyReportDetails.class)); ls = query.list();
			 * 
			 * String sql1 =
			 * "select feeder_id feederId , discom_id discomId,project_id project,discom_name discom,zone_name zone,circle_name circle,town_name town, "
			 * +
			 * "site_name substation,feeder_name feeder,serial_no serial_no,feeder_gis_code gis_code,remark remark, "
			 * +
			 * "interruption_count totInttCount,(interruption_duration/60) inttInHour,interruption_duration inttInMin, '24' defaultSupplyInHour, "
			 * +
			 * "supply_duration avgSupplyHour,remark remark,AVG(supply_duration) avgSupplyDuration,event_date eventDate,(interruption_duration/60) avgSupplyLessThan22  from uppcl_master_data_log where event_date >= DATE_ADD('"
			 * +mDate+"',INTERVAL -2 DAY) " +
			 * " and trim(town_name) IN('LUCKNOW','VARANASI','GORAKHPUR','PRAYAGRAJ','GHAZIABAD','NOIDA','MATHURA','KANPUR') and interruption_duration<22*60 group by id,event_date"
			 * ; Query query1 =
			 * sessionFactory.getCurrentSession().createSQLQuery(sql1).addScalar
			 * ("feederId", new StringType()) .addScalar("discomId", new
			 * StringType()).addScalar("project", new StringType())
			 * .addScalar("discom", new StringType()).addScalar("zone", new
			 * StringType()).addScalar("circle", new StringType())
			 * .addScalar("town", new StringType()).addScalar("substation", new
			 * StringType()) .addScalar("feeder", new
			 * StringType()).addScalar("serial_no", new StringType())
			 * .addScalar("gis_code", new StringType()).addScalar("remark", new
			 * StringType()) .addScalar("totInttCount", new IntegerType())
			 * .addScalar("inttInHour", new DoubleType()).addScalar("inttInMin",
			 * new IntegerType()).addScalar("defaultSupplyInHour", new
			 * StringType()) .addScalar("avgSupplyHour", new
			 * DoubleType()).addScalar("remark",new
			 * StringType()).addScalar("avgSupplyDuration",new
			 * StringType()).addScalar("eventDate",new
			 * StringType()).addScalar("avgSupplyLessThan22",new DoubleType())
			 * .setResultTransformer(Transformers.aliasToBean(
			 * SupplyReportDetails.class)); List<SupplyReportDetails> ls1 =
			 * query1.list(); for(SupplyReportDetails srd: ls) {
			 * for(SupplyReportDetails srd1: ls1) {
			 * if(srd.getSerial_no().equalsIgnoreCase(srd1.getSerial_no()) &&
			 * srd.getEventDate().equalsIgnoreCase(srd1.getEventDate())) {
			 * srd.setAvgSupplyLessThan22(srd1.getAvgSupplyLessThan22()); } } }
			 */}
		if (reportType.equalsIgnoreCase("TOWN")) {
			sql = "select town_name town,event_date eventDate,'"+remark+"' remark,100 workingPer, SUM(IF(id=id, 1, 0)) AS workingModem,SUM(IF(id=id, 1, 0)) AS feederWithModem,SUM(IF(id=id, 1, 0)) AS totalFeeder, SUM(IF(round(supply_duration) between 22*60 and 23*60  , 1, 0)) AS avgSupplyBet2223, "
					+ "SUM(IF(round(supply_duration) between 23*60 and 24*60  , 1, 0)) AS avgSupplyBet2324,SUM(IF(round(supply_duration) <= 22*60 , 1, 0)) AS avgSupplyLessThan22,"
					+ "concat(round(SUM(IF(round(supply_duration) between 22*60 and 23*60  , 1, 0))/SUM(IF(id=id, 1, 0))*100,2),'"+percent+"') perAvgSupplyBet2223,"
					+ "concat(round(SUM(IF(round(supply_duration) between 23*60 and 24*60  , 1, 0))/SUM(IF(id=id, 1, 0))*100,2),'"+percent+"') perAvgSupplyBet2324,"
					+ "concat(round(SUM(IF(round(supply_duration) < 22*60 , 1, 0))/SUM(IF(id=id, 1, 0))*100,2),'"+percent+"') perAvgSupplyLessThan22"
					+ " from uppcl_master_data_log where event_date >= DATE_ADD('" + mDate
					+ "',INTERVAL -2 DAY) and event_date <= '" + mDate + "' "
					+ " and trim(town_name) IN ('LUCKNOW','VARANASI','GORAKHPUR','ALLAHABAD','GHAZIABAD','NOIDA','MATHURA','KANPUR')  "
					+ discomName + " group by event_date,town_name order by town_name ,event_date desc";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("town", new StringType())
					.addScalar("totalFeeder", new StringType()).addScalar("feederWithModem", new StringType())
					.addScalar("workingModem", new StringType()).addScalar("workingPer", new StringType())
					.addScalar("eventDate", new StringType()).addScalar("avgSupplyBet2324", new StringType())
					.addScalar("perAvgSupplyBet2324", new StringType()).addScalar("avgSupplyBet2223", new StringType())
					.addScalar("perAvgSupplyBet2223", new StringType())
					.addScalar("avgSupplyLessThan22", new StringType())
					.addScalar("perAvgSupplyLessThan22", new StringType())
					.addScalar("remark", new StringType())
					.setResultTransformer(Transformers.aliasToBean(ReportTown8NN.class));
			ls = query.list();

		}

		return ls;
	}

	@Override
	public Integer getSecure16TownData() {
		String sql="select count(*) as total from secure_interruption_data where town IN('ALIGARH','FIROZABAD','JHANSI','MATHURA','BAREILLY','FAIZABAD','LUCKNOW','SHAHJAHAN','ALLAHABAD','GORAKHPUR','VARANASI','GHAZIABAD','MEERUT','MORADABAD','NOIDA','SAHARANPU')";
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
        List ls=qry.list();
        Integer a=Integer.parseInt(ls.get(0).toString());
        return a;
	}
	
	@Override
	public Integer getKescoData() {
		String sql="select count(*) as total from `uppcl_master_data` where project_id='R-APDRP KANPUR'";
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		 List ls=qry.list();
	     Integer a=Integer.parseInt(ls.get(0).toString());
	     return a;
	}
	
	public Double getTotalFromMaster(String discom)
	{
		String sql="select count(*) as total from uppcl_master_data where discom_name='"+discom+"'";
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		List ls=qry.list();
	    Double a=Double.parseDouble(ls.get(0).toString());
		return a;
	}

	@Override
	public List<String> GetDateofRapDrp() {
		SQLQuery qry=sessionFactory.getCurrentSession().createSQLQuery("select meter_date from  secure_interruption_data order by meter_date desc limit 1");
		List<String> ls=qry.list();
		return ls;
	}

	@Override
	public List<SupplyReportDetails> getMapDetails(DataRequest dataRequest) {
		String projectId=dataRequest.getProject_id();
		String project="";
		String sql="";
		Query query = null;
		 List<SupplyReportDetails> ls=null;
		 String query5="select event_date from uppcl_master_data_log order by event_date desc limit 1";
	        Query qry=sessionFactory.getCurrentSession().createSQLQuery(query5);
	        String meterDate=qry.uniqueResult().toString();
	        if(meterDate==null && meterDate.isEmpty())
	        {
	        	meterDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        	
	        }
		if(projectId!=null && !projectId.equalsIgnoreCase("All")) 
			project= dataRequest.getProject_id();
		if(dataRequest.getDistrict()!=null)
		{
			
			sql="select site_name sitename,feeder_id feederId,discom_name discom,count(id) Total_fedr,"
					+ " count(id) Live_fedr,'100' Percentage,round(Avg(supply_duration)/60,2) avgSupplyHour,"
					+ " '24' defaultSupplyInHour,district_name district,'NA' maxLoad,'NA' energySupplied,'0' consumerCount "
					+ "  from uppcl_master_data_log where event_date='"+meterDate+"' and district_name= '"+dataRequest.getDistrict()+"' group by site_name";
					 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("sitename",new StringType()).addScalar("feederId", new StringType())
						.addScalar("discom", new StringType()).addScalar("Total_fedr", new IntegerType()).addScalar("Live_fedr", new IntegerType())
						.addScalar("Percentage", new DoubleType()).addScalar("avgSupplyHour", new DoubleType())
						.addScalar("defaultSupplyInHour", new StringType()).addScalar("district",new StringType())
						.addScalar("maxLoad",new StringType()).addScalar("energySupplied", new StringType()).addScalar("consumerCount",new IntegerType())
						.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
				  ls = query.list();
		}
		if (dataRequest.getProject_id() != null && dataRequest.getProject_id().equalsIgnoreCase("all")) {
			sql = "select  site_id feederId,count(id) Total_fedr,count(id) Live_fedr,count(DISTINCT town_name) No_of_town,"
					+ "discom_name discom,round(avg(supply_duration)/60,2) avgSupplyHour,'1' Percentage,'24' defaultSupplyInHour,"
					+ "district_name district, 'NA' maxLoad, 'NA' energySupplied,'0' consumerCount from"
					+ " uppcl_master_data_log where event_date='"+meterDate+"' group by discom_id";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
					.addScalar("No_of_town", new IntegerType()).addScalar("discom", new StringType())
					.addScalar("Total_fedr", new IntegerType()).addScalar("Live_fedr", new IntegerType())
					.addScalar("Percentage", new DoubleType()).addScalar("avgSupplyHour", new DoubleType())
					.addScalar("defaultSupplyInHour", new StringType()).addScalar("district", new StringType())
					.addScalar("maxLoad", new StringType()).addScalar("energySupplied", new StringType())
					.addScalar("consumerCount", new IntegerType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
			ls = query.list();
			/*String sql1 = "select 'All' discom,SUM(Total_fedr) as Total_fedr,avgSupplyHour,'NA' maxLoad,'NA' energySupplied,'0' consumerCount from ( select feederId,count(DISTINCT town) No_of_town,discom  discom, count(feeder) Total_fedr,count(feeder) Live_fedr ,"
					+ "'100' Percentage, avgSupplyHour avgSupplyHour,defaultSupplyInHour,'' district from (select  umd.feeder_id feederId,umd.discom_id discomId, umd.project_id project, umd.discom_name discom,umd.circle_name circle,umd.town_name town,umd.site_name substation,umd.feeder_name feeder,umd.serial_no serial_no ,umd.feeder_gis_code gis_code,umd.remark remark, fds.duration radiusDuration, sid.duration secureDuration,(IFNULL(fds.count1,0) +IFNULL(sid.totInterCount,0)) totInttCount,(IFNULL(fds.duration,0)+IFNULL(sid.duration,0))  inttInMin,((IFNULL(fds.duration,0)+IFNULL(sid.duration,0))/60) inttInHour,'24' defaultSupplyInHour,(24-(AVG(IFNULL(fds.duration,0))+AVG(IFNULL(sid.duration,0)))/60)  avgSupplyHour from uppcl_master_data umd LEFT JOIN "
					+ "(SELECT parent_sensor_id,count(id) count1, round(sum(event_data)/60) as duration FROM feeder_daily_statistics_final  where parent_sensor_id=parent_sensor_id  and creation_time=creation_time GROUP BY parent_sensor_id ) fds ON fds.parent_sensor_id = trim(umd.parent_sensor_id) LEFT JOIN  (SELECT meter_serial_no, sum(total_interruption_duration) as duration, count(total_interruption_count) as totInterCount,total_interruption_duration totintdurInMin FROM secure_interruption_data GROUP BY meter_serial_no ) sid ON sid.meter_serial_no = trim(umd.serial_no) group by umd.serial_no) as total group BY discom) as discomdata";
			Query query1 = sessionFactory.getCurrentSession().createSQLQuery(sql1).addScalar("discom", new StringType())
					.addScalar("Total_fedr", new IntegerType()).addScalar("avgSupplyHour", new DoubleType())
					.addScalar("maxLoad", new StringType()).addScalar("energySupplied", new StringType())
					.addScalar("consumerCount", new IntegerType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
			List<SupplyReportDetails> ls1 = query1.list();
			ls.addAll(ls1);*/

		}else if(dataRequest.getProject_id()!=null && !dataRequest.getProject_id().equalsIgnoreCase("all"))
		{
			sql = "select  site_id feederId,count(id) Total_fedr,count(id) Live_fedr,count(DISTINCT town_name) No_of_town,"
					+ "discom_name discom,round(avg(supply_duration)/60,2) avgSupplyHour,'1' Percentage,'24' defaultSupplyInHour,"
					+ "district_name district, 'NA' maxLoad, 'NA' energySupplied,'0' consumerCount from"
					+ " uppcl_master_data_log where event_date='"+meterDate+"' and discom_name='"+ project +"' group by discom_id,district_name";
			
			
			/*sqle = "select feederId,count(DISTINCT town) No_of_town,discom  discom, count(feeder) Total_fedr,count(feeder) Live_fedr , "
			 		+ "'100' Percentage, avgSupplyHour avgSupplyHour,defaultSupplyInHour,district,'NA' maxLoad,'NA' energySupplied,'0' consumerCount  from "
			 				+ "(select  umd.feeder_id feederId,umd.discom_id discomId, umd.project_id project, umd.discom_name discom,umd.district_name district,"
			 				+ "umd.circle_name circle,umd.town_name town,umd.site_name substation,umd.feeder_name feeder,umd.serial_no serial_no,umd.feeder_gis_code gis_code,umd.remark remark, fds.duration radiusDuration, "
			 				+ "sid.duration secureDuration,(IFNULL(fds.count1,0) +IFNULL(sid.totInterCount,0)) totInttCount,(IFNULL(fds.duration,0)+IFNULL(sid.duration,0))  inttInMin,((IFNULL(fds.duration,0)+IFNULL(sid.duration,0))/60) inttInHour,'24'  defaultSupplyInHour,"
			 				+ "avg(24-(IFNULL(fds.duration,0)+IFNULL(sid.duration,0))/60)  avgSupplyHour from uppcl_master_data umd LEFT JOIN (SELECT parent_sensor_id,count(id) count1, round(sum(event_data)/60) as duration FROM feeder_daily_statistics_final  "
			 				+ "where parent_sensor_id=parent_sensor_id  and creation_time=creation_time GROUP BY parent_sensor_id ) fds ON fds.parent_sensor_id = trim(umd.parent_sensor_id) "
			 				+ "LEFT JOIN (SELECT meter_serial_no, sum(total_interruption_duration) as duration, count(total_interruption_count) as totInterCount,total_interruption_duration totintdurInMin  FROM secure_interruption_data GROUP BY meter_serial_no ) sid "
			 				+ "ON sid.meter_serial_no = trim(umd.serial_no) group by umd.serial_no) as total where discom='"+ project +"' group BY discom,district";
		*/	 query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
						.addScalar("No_of_town", new IntegerType()).addScalar("discom", new StringType())
						.addScalar("Total_fedr", new IntegerType()).addScalar("Live_fedr", new IntegerType())
						.addScalar("Percentage", new DoubleType()).addScalar("avgSupplyHour", new DoubleType())
						.addScalar("defaultSupplyInHour", new StringType()).addScalar("district",new StringType())
						.addScalar("maxLoad",new StringType()).addScalar("energySupplied", new StringType()).addScalar("consumerCount",new IntegerType())
						.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
				  ls = query.list();
		}
		
		return ls;
	}

	@Override
	public Integer getTotalFeeders() {
		String sql="select count(*) as total from `uppcl_master_data`";
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		 List ls=qry.list();
	     Integer a=Integer.parseInt(ls.get(0).toString());
	     return a;
	}

	@Override
	public List<Hcl152> getHclMasterData() {
		
		String query5 = "select event_date from uppcl_master_data_log order by event_date desc limit 1";
		Query qry = sessionFactory.getCurrentSession().createSQLQuery(query5);
		String meterDate = qry.uniqueResult().toString();
		if (meterDate == null && meterDate.isEmpty()) {
			meterDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		}
	
        List<Hcl152> masterDataList = new ArrayList<Hcl152>();
        
        final String sql = "select SUM(if(id=id,1,0))-SUM(IF(str_to_date(last_meter_read,'%m-%d-%Y') >= DATE('"+meterDate+"' - INTERVAL 7 DAY), 1, 0)) dn ,"
        		+ " SUM(IF(str_to_date(last_meter_read,'%m-%d-%Y') >= DATE('"+meterDate+"' - INTERVAL 7 DAY), 1, 0)) up,discom_name discom from uppcl_master_data_log "
        		+ "where event_date='"+meterDate+"' and trim(remark)='152 R-APDRP'";
        
         final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
        		.addScalar("dn", new StringType())
        		.addScalar("up", new StringType())
        		.addScalar("discom", new StringType())
        		.setResultTransformer(Transformers.aliasToBean((Class)Hcl152.class));
        masterDataList = (List<Hcl152>)query.list();
        return masterDataList;
    }

	@Override
	public List<NN16> getNNMasterData() {
		
		String query5 = "select event_date from uppcl_master_data_log order by event_date desc limit 1";
		Query qry = sessionFactory.getCurrentSession().createSQLQuery(query5);
		String meterDate = qry.uniqueResult().toString();
		if (meterDate == null && meterDate.isEmpty()) {
			meterDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		}
	
        List<NN16> masterDataList = new ArrayList<NN16>();
        final String sql = "select SUM(if(id=id,1,0))-SUM(IF(str_to_date(last_meter_read,'%m-%d-%Y') >= DATE('"+meterDate+"' - INTERVAL 7 DAY), 1, 0)) dn ,"
        		+ " SUM(IF(str_to_date(last_meter_read,'%m-%d-%Y') >= DATE('"+meterDate+"' - INTERVAL 7 DAY), 1, 0)) up,discom_name discom from uppcl_master_data_log "
        		+ "where event_date='"+meterDate+"' and trim(remark)='16 NN REPORT'";
        final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
        		.addScalar("dn", new StringType())
        		.addScalar("up", new StringType())
        		.addScalar("discom", new StringType())
        		.setResultTransformer(Transformers.aliasToBean((Class)NN16.class));
        masterDataList = (List<NN16>)query.list();
        return masterDataList;
    }
	
	@Override
	public List<Hcl152> getRapDrpData() {
		
		String query5 = "select event_date from uppcl_master_data_log order by event_date desc limit 1";
		Query qry = sessionFactory.getCurrentSession().createSQLQuery(query5);
		String meterDate = qry.uniqueResult().toString();
		if (meterDate == null && meterDate.isEmpty()) {
			meterDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		}
	
        List<Hcl152> masterDataList = new ArrayList<Hcl152>();
        
        final String sql = "select SUM(if(id=id,1,0))-SUM(IF(str_to_date(last_meter_read,'%m-%d-%Y') >= DATE('"+meterDate+"' - INTERVAL 7 DAY), 1, 0)) dn ,"
        		+ " SUM(IF(str_to_date(last_meter_read,'%m-%d-%Y') >= DATE('"+meterDate+"' - INTERVAL 7 DAY), 1, 0)) up,discom_name discom from uppcl_master_data_log "
        		+ "where event_date='"+meterDate+"' and (trim(remark)='152 R-APDRP' || trim(remark)='16 NN REPORT') group by discom";
        
         final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
        		.addScalar("dn", new StringType())
        		.addScalar("up", new StringType())
        		.addScalar("discom", new StringType())
        		.setResultTransformer(Transformers.aliasToBean((Class)Hcl152.class));
        masterDataList = (List<Hcl152>)query.list();
        return masterDataList;
    }
	
	@Override
	public List<Sensor> getIpdsStatistics() {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		final String sql = "select serial_no , project_id , admin_status , device_state from sensor where discom_id is not NULL and serial_no in (select serial_no from uppcl_master_data where project_id='IPDS')"
				+ " and type='AC_METER' and admin_status in ('N','S','U')";
		 final Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
				 .addScalar("serial_no", new StringType())
				 .addScalar("project_id", new StringType())
				 .addScalar("admin_status", new StringType())
				 .addScalar("device_state", new StringType())
				 .setResultTransformer(Transformers.aliasToBean((Class)Sensor.class));
			sensorList = query.list();
		return sensorList;
	}

	@Override
	public List<SupplyReportDetails> getDailyReport(DataRequest dataRequest) {

		String projectId=dataRequest.getProject_id();
		String discomId=dataRequest.getDiscom_id();
		String date=dataRequest.getDate();
		String reportType=dataRequest.getReportType();
		String dateDifference = "1";
		String discomName="";
		String projectName="";
		Query query=null;
		String sql="";
			if(!discomId.equalsIgnoreCase("all"))	
				discomName=" and discom_id='"+discomId+"'";
			if(!projectId.equalsIgnoreCase("all"))
			{
				if(projectId.equalsIgnoreCase("eodb") || projectId.equalsIgnoreCase("ipds") || projectId.equalsIgnoreCase("ntpf"))
				projectName=" and project_id='"+projectId+"'";
				if(projectId.equalsIgnoreCase("16 NN") || projectId.equalsIgnoreCase("152"))
				projectName=" and remark like '"+projectId+"%'";
			}
		if(reportType.equalsIgnoreCase("DETAILS"))
		{
		 sql = "select discom_id,discom_name discom,zone_name zone,division_name division,circle_name circle,town_name town,district_name district,"
				+ "project_id project,serial_no serial_no,site_name ss, feeder_name,supply_duration , round(avg(interruption_duration)/"
				+ dateDifference + ",2) outage_duration ," + "round((avg(interruption_duration)/60)/"+dateDifference+",2) inttInHour ,"
				+ "round((avg(supply_duration)/60)/"+dateDifference+",2) avgSupplyHour,count(id) count , "
				+ "avg(interruption_count)/"+dateDifference+" totInttCount,interruption_count outagecount from uppcl_master_data_log  where  event_date ='"+date+"'"
						+discomName+projectName+ "  group by serial_no ";
		 query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("avgSupplyHour", new StringType()).addScalar("inttInHour", new StringType())
				.addScalar("discom", new StringType()).addScalar("zone", new StringType())
				.addScalar("circle", new StringType()).addScalar("district", new StringType())
				.addScalar("town", new StringType()).addScalar("project", new StringType())
				.addScalar("ss", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("division", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("supply_duration", new StringType()).addScalar("outage_duration", new StringType())
				.addScalar("outagecount",new StringType())
				.setResultTransformer(Transformers.aliasToBean((Class) MasterData.class));
		}
		if(reportType.equalsIgnoreCase("town"))
		{
			 sql = "select feeder_id feederId , discom_id discomId,count(distinct town_name) No_of_town,town_name town, count(id) Total_fedr ,"
					+ " count(id) Live_fedr,project_id project,"
					+ "discom_name discom,remark remark, avg(interruption_count) totInttCount,avg(interruption_duration) inttInMin,"
					+ "round(avg(interruption_duration)/60,2) inttInHour, '24' defaultSupplyInHour, "
					+ "SUM(IF(round(supply_duration) <= 22*60 , 1, 0)) AS num,"
					+ "round(SUM(IF(round(supply_duration) < 22*60 , 1, 0))/SUM(IF(id=id, 1, 0))*100,2) as percentage,"
					+ "round(sum(IF(round(supply_duration) < 22*60 , supply_duration/60, 0))/SUM(IF(round(supply_duration) < 22*60 , 1, 0)),2) as avgSupplyLessThan22,"
					+ "round(avg(supply_duration)/60,2) avgSupplyHour from uppcl_master_data_log where event_date='" + date
					+ "' "+projectName+discomName+" group by discom_name,town_name";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
					.addScalar("No_of_town", new IntegerType())
					.addScalar("town", new StringType())
					.addScalar("discom", new StringType())
					.addScalar("Total_fedr", new IntegerType())
					.addScalar("Live_fedr", new IntegerType())
					.addScalar("avgSupplyHour", new DoubleType())
					.addScalar("defaultSupplyInHour", new StringType())
					.addScalar("num", new IntegerType())
					.addScalar("percentage", new DoubleType())
					.addScalar("avgSupplyLessThan22", new DoubleType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
		}
		else if (reportType.equalsIgnoreCase("DISCOM")) {
			 sql = "select feeder_id feederId , discom_id discomId,count(distinct town_name) No_of_town, count(id) Total_fedr , count(id) Live_fedr,project_id project,"
					+ "discom_name discom,remark remark, avg(interruption_count) totInttCount,avg(interruption_duration) inttInMin,"
					+ "round(sum(interruption_duration)/60,2) inttInHour, '24' defaultSupplyInHour, "
					+ "SUM(IF(round(supply_duration) < 22*60 , 1, 0)) AS num,"
					+ "round(SUM(IF(round(supply_duration) < 22*60 , 1, 0))/SUM(IF(id=id, 1, 0))*100,2) as percentage,"
					+ "round(sum(IF(round(supply_duration) < 22*60 , supply_duration/60, 0))/SUM(IF(round(supply_duration) < 22*60 , 1, 0)),2) as avgSupplyLessThan22,"
					+ "round(avg(supply_duration)/60,2) avgSupplyHour from uppcl_master_data_log where event_date='" + date
					+ "'"+projectName+discomName+" group by discom_name";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
					.addScalar("No_of_town", new IntegerType()).addScalar("discom", new StringType())
					.addScalar("Total_fedr", new IntegerType()).addScalar("Live_fedr", new IntegerType())
					.addScalar("avgSupplyHour", new DoubleType())
					.addScalar("defaultSupplyInHour", new StringType())
					.addScalar("num", new IntegerType())
					.addScalar("percentage", new DoubleType())
					.addScalar("avgSupplyLessThan22", new DoubleType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
		}
		else if (reportType.equalsIgnoreCase("DISTRICT")) {
			 sql = "select feeder_id feederId , discom_id discomId,count(distinct town_name) No_of_town, count(id) Total_fedr , count(id) Live_fedr,project_id project,"
					+ "discom_name discom,remark remark, avg(interruption_count) totInttCount,avg(interruption_duration) inttInMin,"
					+ "round(sum(interruption_duration)/60,2) inttInHour, '24' defaultSupplyInHour, "
					+ "SUM(IF(round(supply_duration) < 22*60 , 1, 0)) AS num,"
					+ "round(SUM(IF(round(supply_duration) < 22*60 , 1, 0))/SUM(IF(id=id, 1, 0))*100,2) as percentage,"
					+ "round(sum(IF(round(supply_duration) < 22*60 , supply_duration/60, 0))/SUM(IF(round(supply_duration) < 22*60 , 1, 0)),2) as avgSupplyLessThan22,"
					+ "round(avg(supply_duration)/60,2) avgSupplyHour,district_name district from uppcl_master_data_log where event_date='" + date
					+ "'"+projectName+discomName+" group by district_name";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("feederId", new StringType())
					.addScalar("No_of_town", new IntegerType()).addScalar("discom", new StringType())
					.addScalar("Total_fedr", new IntegerType()).addScalar("Live_fedr", new IntegerType())
					.addScalar("avgSupplyHour", new DoubleType())
					.addScalar("defaultSupplyInHour", new StringType())
					.addScalar("num", new IntegerType())
					.addScalar("percentage", new DoubleType())
					.addScalar("avgSupplyLessThan22", new DoubleType())
					.addScalar("district", new StringType())
					.setResultTransformer(Transformers.aliasToBean(SupplyReportDetails.class));
		}
		List<SupplyReportDetails> ls = query.list();
		return ls;
	
	}

	@Override
	public List<WeeklyReportModel> getWeeklyReport(DataRequest dataRequest) {

		String projectId=dataRequest.getProject_id();
		String discomId=dataRequest.getDiscom_id();
		String monthYear=dataRequest.getDate();
		String data[]=monthYear.split("-");
		Integer year=Integer.parseInt(data[0]);
		Integer month=Integer.parseInt(data[1])-1;
		String reportType=dataRequest.getReportType();
		String limit=dataRequest.getLimit();
		String dateFrom=null;
		String dateTo=null;
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.DATE,01);
        int lastDate = cal.getActualMaximum(Calendar.DATE);
        String formattedLastDate = String.format("%02d", lastDate);
		dateFrom=monthYear+"-"+"01";
		dateTo=monthYear+"-"+formattedLastDate;
		String dateDifference = "7";
		String discomName="";
		String projectName="";
		Query query=null;
		String sql="";
		Date date;
		
		int dayFrom=0;
		int dayTo=0;
		String currentDate=null;
		List<WeeklyReportModel> ls=new ArrayList<>();
		String arr[]=new String[lastDate];
		int j=0;
		for(int i=1;i<=lastDate;i++)
		{
			String da=String.format("%02d", i);
			String da1=monthYear+"-"+da;
			arr[j]=da1;
			j++;
		}
		/*if(limit.equalsIgnoreCase("WK1"))
		{
			dayFrom=1;
			dayTo=7;
			dateFrom=monthYear+"-"+"01";
			dateTo=monthYear+"-"+"07";
		}
		else if(limit.equalsIgnoreCase("WK2"))
		{
			dayFrom=8;
			dayTo=15;
			dateFrom=monthYear+"-"+"08";
			dateTo=monthYear+"-"+"15";
		}
		else if(limit.equalsIgnoreCase("WK3"))
		{
			dayFrom=16;
			dayTo=23;
			dateFrom=monthYear+"-"+"16";
			dateTo=monthYear+"-"+"23";
		}
		else if(limit.equalsIgnoreCase("WK4"))
		{
			dayFrom=24;
			dayTo=30;
			dateFrom=monthYear+"-"+"24";
			dateTo=monthYear+"-"+"30";
		}*/
			if(!discomId.equalsIgnoreCase("all"))	
				discomName=" and discom_id='"+discomId+"'";
			
			if(!projectId.equalsIgnoreCase("all"))
			{
				if(projectId.equalsIgnoreCase("eodb") || projectId.equalsIgnoreCase("ipds") || projectId.equalsIgnoreCase("ntpf"))
				projectName=" and project_id='"+projectId+"'";
				if(projectId.equalsIgnoreCase("16 NN") || projectId.equalsIgnoreCase("152"))
				projectName=" and remark like '"+projectId+"%'";
			}
		if(reportType.equalsIgnoreCase("DETAILS"))
		{
		 /*sql = "select discom_id,discom_name discom,zone_name zone,division_name division,circle_name circle,town_name town,district_name district,"
				+ "project_id project,serial_no serial_no,site_name ss, feeder_name,supply_duration , round(avg(interruption_duration)/"
				+ dateDifference + ",2) outage_duration ," + "round((avg(interruption_duration)/60)/"+dateDifference+",2) inttInHour ,"
				+ "round((avg(supply_duration)/60)/"+dateDifference+",2) avgSupplyHour,count(id) count , "
				+ "avg(interruption_count)/"+dateDifference+" totInttCount,interruption_count outagecount,event_date eventDate from uppcl_master_data_log  where "
						+ " event_date>='"+dateFrom+"' && event_date<='"+dateTo+"'"+discomName+projectName+ "  group by discom_id,event_date ";*/
			String sql0="select distinct serial_no from uppcl_master_data_log where event_date >='"+dateFrom+"' && event_date<='"+dateTo+"'"+discomName+projectName+" group by serial_no,event_date order by serial_no,event_date";
			Query qry=this.sessionFactory.getCurrentSession().createSQLQuery(sql0);
			List<String> list=qry.list();
			
			sql="select discom_id,discom_name discom,zone_name zone,division_name division,circle_name circle,town_name town,district_name district,project_id project,serial_no serial_no,site_name ss, feeder_name,interruption_count outagecount,interruption_duration outage_duration,count(id) remark ,event_date eventDate from uppcl_master_data_log  " + 
					"where  event_date >='"+dateFrom+"' && event_date<='"+dateTo+"'"+discomName+projectName+" group by serial_no,event_date order by serial_no,event_date";
		 query = this.sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("discom", new StringType()).addScalar("zone", new StringType())
				.addScalar("division", new StringType())
				.addScalar("circle", new StringType()).addScalar("town", new StringType()).addScalar("district", new StringType())
				.addScalar("project", new StringType()).addScalar("serial_no", new StringType())
				.addScalar("ss", new StringType()).addScalar("feeder_name", new StringType())
				.addScalar("remark", new StringType()).addScalar("outage_duration", new StringType())
				.addScalar("outagecount",new StringType()).addScalar("eventDate",new StringType())
				.setResultTransformer(Transformers.aliasToBean((Class) MasterData.class));
		 List<MasterData> ls1 =query.list();
		 for(String dataList: list)
		 {
			 String serialNo=dataList;
			 WeeklyReportModel modelObj=new WeeklyReportModel();
			 modelObj.setSerialNo(serialNo);
			 List<InterruptDetailsModel> interrdata=new ArrayList<>();
			  long count1=ls1.stream().filter(m->m.getSerial_no().equals(serialNo)).count();
			  if(count1>0)
			  {
				 List<MasterData> masterObject=ls1.stream().filter(model->model.getSerial_no().equals(serialNo)).collect(Collectors.toList());
				 for(MasterData mastData: masterObject)
				 {
					 
					 InterruptDetailsModel innerModelObj=new InterruptDetailsModel();
					 modelObj.setCircle(mastData.getCircle());
					 modelObj.setDiscom(mastData.getDiscom());
					 modelObj.setFeederName(mastData.getFeeder_name());
					 modelObj.setSiteName(mastData.getSs());
					 modelObj.setTown(mastData.getTown());



					 String pattern = "yyyy-MM-dd";
					 LocalDate localDate1 = null;

					 SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern);

					 try {
						 Date date4 = simpleDateFormat2.parse(mastData.getEventDate());

						 localDate1 = date4.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					 } catch (ParseException e) {
						 e.printStackTrace();
					 }


					 LocalDate localDate2 = null;

					 SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(pattern);

					 try {
						 Date date4 = simpleDateFormat3.parse(mastData.getEventDate());

						 localDate2 = date4.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					 } catch (ParseException e) {
						 e.printStackTrace();
					 }

					 if(dataRequest.getLimit().equalsIgnoreCase("WK1")) {
						 if(localDate2.getDayOfMonth() < 8) {
							 innerModelObj.setDate(mastData.getEventDate());
							 innerModelObj.setIntrruptionCount(mastData.getOutagecount());
							 innerModelObj.setOutageDuration(mastData.getOutage_duration());
							 interrdata.add(innerModelObj);
 						 } else if(dataRequest.getLimit().equalsIgnoreCase("WK2")) {
							 if(localDate2.getDayOfMonth() <= 8 && localDate2.getDayOfMonth() < 15 ) {
								 innerModelObj.setDate(mastData.getEventDate());
								 innerModelObj.setIntrruptionCount(mastData.getOutagecount());
								 innerModelObj.setOutageDuration(mastData.getOutage_duration());
								 interrdata.add(innerModelObj);
 							 }
						 }else if(dataRequest.getLimit().equalsIgnoreCase("WK3")) {
							 if(localDate2.getDayOfMonth() <= 15 && localDate2.getDayOfMonth() < 23 ) {
								 innerModelObj.setDate(mastData.getEventDate());
								 innerModelObj.setIntrruptionCount(mastData.getOutagecount());
								 innerModelObj.setOutageDuration(mastData.getOutage_duration());
								 interrdata.add(innerModelObj);
 							 }
						 }else if(dataRequest.getLimit().equalsIgnoreCase("WK4")) {
							 if(localDate2.getDayOfMonth() <= 23 && localDate2.getDayOfMonth() < 32 ) {
								 innerModelObj.setDate(mastData.getEventDate());
								 innerModelObj.setIntrruptionCount(mastData.getOutagecount());
								 innerModelObj.setOutageDuration(mastData.getOutage_duration());
								 interrdata.add(innerModelObj);
							 }
						 }
 						 }
				 }
				 for(int i=0;i<arr.length;i++)
				 {

					 String s=arr[i];
					 String pattern = "yyyy-MM-dd";
					 LocalDate localDate = null;

					 SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern);

					 try {
						 Date date3 = simpleDateFormat2.parse(s);

						 localDate = date3.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					 } catch (ParseException e) {
						 e.printStackTrace();
					 }
					 LocalDate finalLocalDate = localDate;
					 List<MasterData> masterList=masterObject.stream().filter(
					 		model->{
								if(dataRequest.getLimit().equalsIgnoreCase("WK1")) {
									if(finalLocalDate.getDayOfMonth() < 8) {

										return model.getEventDate().equals(s);
									}else{
										return false;
									}
								}else if(dataRequest.getLimit().equalsIgnoreCase("WK2")) {
									if(finalLocalDate.getDayOfMonth() <= 8 && finalLocalDate.getDayOfMonth() < 15 ) {

										return model.getEventDate().equals(s);
									}else{
										return false;
									}
								}else if(dataRequest.getLimit().equalsIgnoreCase("WK3")) {
									if(finalLocalDate.getDayOfMonth() <= 15 && finalLocalDate.getDayOfMonth() < 23 ) {

										return model.getEventDate().equals(s);
									}else{
										return false;
									}
								}else if(dataRequest.getLimit().equalsIgnoreCase("WK4")) {
									if(finalLocalDate.getDayOfMonth() <= 23 && finalLocalDate.getDayOfMonth() < 31 ) {

										return model.getEventDate().equals(s);
									}else{
										return false;
									}
								}else{
									return false;
								}

							}

					 ).collect(Collectors.toList());
					 List<InterruptDetailsModel> newList=interrdata.stream().filter(f->{
						 if(dataRequest.getLimit().equalsIgnoreCase("WK1")) {
							 if(finalLocalDate.getDayOfMonth() < 8) {

								 return f.getDate().equals(s);
							 }else{
								 return false;
							 }
						 }else if(dataRequest.getLimit().equalsIgnoreCase("WK2")) {
							 if(finalLocalDate.getDayOfMonth() <= 8 && finalLocalDate.getDayOfMonth() < 15 ) {

								 return f.getDate().equals(s);
							 }else{
								 return false;
							 }
						 }else if(dataRequest.getLimit().equalsIgnoreCase("WK3")) {
							 if(finalLocalDate.getDayOfMonth() <= 15 && finalLocalDate.getDayOfMonth() < 23 ) {

								 return f.getDate().equals(s);
							 }else{
								 return false;
							 }
						 }else if(dataRequest.getLimit().equalsIgnoreCase("WK4")) {
							 if(finalLocalDate.getDayOfMonth() <= 23 && finalLocalDate.getDayOfMonth() < 31 ) {

								  	return f.getDate().equals(s);

							 }else{
								 return false;
							 }
						 }else{
							 return false;
						 }

					 }



					 ).collect(Collectors.toList());

					 if(masterList.size()==0 && newList.size()==0)
					 {
					System.out.println(dataRequest.getLimit());




System.out.println(localDate.getDayOfMonth());
						 System.out.println(dataRequest.getLimit());
						 if(dataRequest.getLimit().equalsIgnoreCase("WK1")){
							 if(localDate.getDayOfMonth() < 8){

								 InterruptDetailsModel innerModelObj=new InterruptDetailsModel();
								 innerModelObj.setDate(s);
								 interrdata.add(innerModelObj);

							 }

 					}else if(dataRequest.getLimit().equalsIgnoreCase("WK2")  ){
							 if(localDate.getDayOfMonth() >= 8 && localDate.getDayOfMonth() < 15){
								 InterruptDetailsModel innerModelObj=new InterruptDetailsModel();
								 innerModelObj.setDate(s);
								 interrdata.add(innerModelObj);


							 }

					}else if(dataRequest.getLimit().equalsIgnoreCase("WK3")  ){
							 if(localDate.getDayOfMonth() >= 15 && localDate.getDayOfMonth() < 24){
								 InterruptDetailsModel innerModelObj=new InterruptDetailsModel();
								 innerModelObj.setDate(s);
								 interrdata.add(innerModelObj);


							 }

					}else if(dataRequest.getLimit().equalsIgnoreCase("WK4")  ){
							 if(localDate.getDayOfMonth() >= 24 && localDate.getDayOfMonth() < 31){
								 InterruptDetailsModel innerModelObj=new InterruptDetailsModel();
								 innerModelObj.setDate(s);
								 interrdata.add(innerModelObj);


							 }
					}
					 }
				 }
				
			  }
			 modelObj.setData(interrdata);
			 ls.add(modelObj);
		 }
		}
		return ls;
	}
}