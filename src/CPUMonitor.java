import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CPUMonitor {
	
	final static String fileName = "log.txt";
	static long epoch = 0;
	static long maxepoch = 0;
	static ArrayList<String> ipaddr = new ArrayList<String>();
	SearchLog sl = null;
	static long timecounter = 0;
	
	CPUMonitor()
	{
		sl = new SearchLog();
	}

	public static void main(String args[])
	{
		CPUMonitor cpuMonitor = new CPUMonitor();
		cpuMonitor.initialize();
		
		Scanner sc = new Scanner(System.in);
		String terminate = "exit";
		String command;
		do
		{
			System.out.println("Enter Query: ");
			command = sc.nextLine();
			if(!command.toLowerCase().equals(terminate))
				cpuMonitor.sl.findEntry(command);
		}while(!command.toLowerCase().equals(terminate));
		sc.close();
	}
	
	private void initialize()
	{
		 File f = new File(fileName);
		 if(f.exists())
		 {
			 f.delete();
		 }
		 
		String str = "2014-10-31 00:00";
		String str2 = "2014-10-31 23:59";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = df.parse(str);
			epoch = date.getTime()/1000;
			date = df.parse(str2);
			maxepoch = date.getTime()/1000;
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		generateIp();
		timecounter = epoch;
		while(timecounter < maxepoch)
		{
			System.out.println("Writing to a file");
			
			for(String ip : ipaddr)
			{
				this.generateLog(String.valueOf(timecounter),ip,0,generateUsage());
				this.generateLog(String.valueOf(timecounter),ip,1,generateUsage());
			}
			timecounter = generateTime();
		}
		System.out.println("File write complete");
	}
	
	private static long generateTime()
	{
		timecounter +=60;
		return timecounter;
	}
	
	private static void generateIp()
	{
		for(int j = 0; j<3; j++)
		{
			for(int i = 1 ; i<255; i++)
			{
				ipaddr.add("192.168."+j+"."+i);
			}
		}
		for(int i = 1; i<239; i++)
		{
			ipaddr.add("192.168.3."+i);
		}
	}
	
	private static int generateUsage()
	{
		Random r = new Random();
		int low = 0;
		int high = 100;
		return r.nextInt(high-low) + low;
	}
	
	public void generateLog(String time,String ip,int cpuId,int usage)
	{
		// log for all 1000 servers in a single file
		PrintWriter pw = null;
		try {
			
			 pw = new PrintWriter(new FileWriter(fileName,true));
			 
			 StringBuilder sb = new StringBuilder();
			 sb.append(time +" ");
			 sb.append(ip + " ");
			 sb.append(String.valueOf(cpuId)+ " ");
			 sb.append(String.valueOf(usage));
			 pw.println(sb.toString());
			 pw.flush();
			 
			 StringBuilder key = new StringBuilder();
			 key.append(time+",");
			 key.append(ip+",");
			 key.append(String.valueOf(cpuId));
			 
			 StringBuilder value = new StringBuilder();
			 value.append(Validate.getDate(Long.parseLong(time))+",");
			 value.append(String.valueOf(usage)+"%");
			 
			 this.sl.enterData(key.toString(),value.toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(pw!=null)
				pw.close();
		}
	}

}
