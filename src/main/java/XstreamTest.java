import java.util.Date;

import com.technion.project.model.Report;
import com.thoughtworks.xstream.XStream;

public class XstreamTest
{

	public static void main(final String[] args)
	{
		final Report report = new Report();
		report.setContent("test content");
		report.setExpiration(new Date());
		report.setId(0);
		report.setUsername("hagitc");
		report.setTitle("title 1");

		final XStream xStream = new XStream();
		xStream.processAnnotations(Report.class);
		final String xml = xStream.toXML(report);
		xml.toString();
	}

}
