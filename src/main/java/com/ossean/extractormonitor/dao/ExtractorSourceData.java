package com.ossean.extractormonitor.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ExtractorSourceData {
	@Select("select count(*) from ${table} where ${timeCol} >= #{start} and ${timeCol} < #{end}")
	public int selectByTime(@Param("table") String table, @Param("timeCol") String timeCol, @Param("start") String start, @Param("end") String end);
	
	//SELECT COUNT(DISTINCT Url) FROM `cnblog_news`;
	@Select("select count(distinct Url) from ${table}")
	public int countNum(@Param("table")String table);
<<<<<<< HEAD

	@Select("select * from table ${sourceTable} where postTime>=${startTime} and postTime<=${endTime} ")
	public List<Map<String,Object>> selSourceData(@Param("sourcetable") String sourceTable , @Param("startTime")String startTime,@Param("endTime")String endTime );

=======
	
	@Select("select * from ${table} where postTime >= ${startTime} and postTime <= ${endTime}")
	public List<Map<String,Object>> selSourceData(@Param("table") String table , @Param("startTime")String startTime , @Param("endTime")String endTime);
>>>>>>> test
}
