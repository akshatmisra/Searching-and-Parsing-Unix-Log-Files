import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class SearchLog {

	HashMap<String,String> hmap = new HashMap<String,String>();
	
	public void findEntry(String command) {
		boolean isValid = Validate.validateCommand(command);
		// QUERY IP cpu_id time_start time_end
		if(isValid)
		{
			String[] cmd = command.split(" ");
			String ip = cmd[1];
			int cpuId = Integer.parseInt(cmd[2].trim());
			String startTime = cmd[3]+" "+cmd[4];
			String endTime = cmd[5]+" "+cmd[6];
			searchData(ip, cpuId,startTime, endTime);
		}

	}

	public void searchData(String ip, int cpuId,String startTime, String endTime)
	{
		ArrayList<String> cpuDetails = new ArrayList<String>();
		 long start = getTime(startTime);
		 long end = getTime(endTime);
		 String startkey = getTime(startTime)+","+ip+","+String.valueOf(cpuId);
		 while(start < end)
		 {
			 cpuDetails.add(hmap.get(startkey));
			 start = start+60;
			 startkey = start+","+ip+","+String.valueOf(cpuId);
		 }
		 System.out.println("CPU usage on "+ip);
		 System.out.println(cpuDetails);
	}
	
	public long getTime(String time)
	{
		long t = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = df.parse(time);
			t = date.getTime()/1000;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public void enterData(String key, String value)
	{
		hmap.put(key, value);
	}
	
}
