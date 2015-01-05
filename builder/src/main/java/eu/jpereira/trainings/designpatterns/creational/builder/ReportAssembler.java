
package eu.jpereira.trainings.designpatterns.creational.builder;

import java.util.Iterator;

import eu.jpereira.trainings.designpatterns.creational.builder.json.JSONReportBody;
import eu.jpereira.trainings.designpatterns.creational.builder.model.Report;
import eu.jpereira.trainings.designpatterns.creational.builder.model.SaleEntry;
import eu.jpereira.trainings.designpatterns.creational.builder.model.SoldItem;
import eu.jpereira.trainings.designpatterns.creational.builder.xml.XMLReportBody;

/**
 * @author jpereira
 * 
 */

class Document {
    private Document report;
    String name;
    
    public void setdocument(String name) {
    	this.name = name;
    }
    public Document render() {
        return this.report;
    }
}
interface Builder{
    public void buildDocument();
    public Document getDocument();

}

class JSONBuilder implements Builder {
    private Document report = new Document();
 
    /* CONSTRUCT JSON */
    public void construct() {
        this.report = new Document();
    }
    
    /* BUILD JSON */
    public void buildDocument() {
    	this.report.setdocument("JSON");
    }
    
    /* GET JSON */
    public Document getDocument() {
        return this.report;
    }
}

class Director{
    private Builder builder;
 
    public void construct(Builder builder) {
        this.builder = builder;
    }
    public void construct() {
        this.builder.buildDocument();
    }
    public Document getResult() {
        return this.builder.getDocument();
    }
}


public class ReportAssembler {

	private SaleEntry saleEntry;

	/**
	 * @param reportDate
	 */
	public void setSaleEntry(SaleEntry saleEntry) {
		this.saleEntry = saleEntry;

	}

	/**
	 * @param type
	 * @return
	 */
	
	
	public Report getReport(String type) {
		Report report = new Report();

		// Algorithms to build the body objects are different
		if (type.equals("JSON")) {

			JSONReportBody reportBody = new JSONReportBody();
			//Add customer info
			reportBody.addContent("sale:{customer:{");
			reportBody.addContent("name:\"");
			reportBody.addContent(saleEntry.getCustomer().getName());
			reportBody.addContent("\",phone:\"");
			reportBody.addContent(saleEntry.getCustomer().getPhone());
			reportBody.addContent("\"}");
			//add array of items
			reportBody.addContent(",items:[");
			Iterator<SoldItem> it = saleEntry.getSoldItems().iterator();
			while ( it.hasNext() ) {
				SoldItem item = it.next();
				reportBody.addContent("{name:\"");
				reportBody.addContent(item.getName());
				reportBody.addContent("\",quantity:");
				reportBody.addContent(String.valueOf(item.getQuantity()));
				reportBody.addContent(",price:");
				reportBody.addContent(String.valueOf(item.getUnitPrice()));
				reportBody.addContent("}");
				if ( it.hasNext() ) {
					reportBody.addContent(",");
				}
				
			}
			reportBody.addContent("]}");
			
			
			report.setReportBody(reportBody);

		} else if (type.equals("XML")) {
			XMLReportBody reportBody = new XMLReportBody();
			reportBody.putContent("<sale><customer><name>");
			reportBody.putContent(this.saleEntry.getCustomer().getName());
			reportBody.putContent("</name><phone>");
			reportBody.putContent(this.saleEntry.getCustomer().getPhone());
			reportBody.putContent("</phone></customer>");
			
			reportBody.putContent("<items>");
			
			Iterator<SoldItem> it = saleEntry.getSoldItems().iterator();
			while ( it.hasNext() ) {
				SoldItem soldEntry= it.next();
				reportBody.putContent("<item><name>");
				reportBody.putContent(soldEntry.getName());
				reportBody.putContent("</name><quantity>");
				reportBody.putContent(soldEntry.getQuantity());
				reportBody.putContent("</quantity><price>");
				reportBody.putContent(soldEntry.getUnitPrice());
				reportBody.putContent("</price></item>");
			}
			reportBody.putContent("</items></sale>");
			report.setReportBody(reportBody);
		} else if (type.equals("HTML")) {
			
			HTMLReportBody reportBody = new HTMLReportBody();
			reportBody.putContent("<span class=\"customerName\">");
			reportBody.putContent(this.saleEntry.getCustomer().getName());
			reportBody.putContent("</span><span class=\"customerPhone\">");
			reportBody.putContent(this.saleEntry.getCustomer().getPhone());
			reportBody.putContent("</span>");
			
			reportBody.putContent("<items>");
			
			Iterator<SoldItem> it = saleEntry.getSoldItems().iterator();
			while ( it.hasNext() ) {
				SoldItem soldEntry= it.next();
				reportBody.putContent("<item><name>");
				reportBody.putContent(soldEntry.getName());
				reportBody.putContent("</name><quantity>");
				reportBody.putContent(soldEntry.getQuantity());
				reportBody.putContent("</quantity><price>");
				reportBody.putContent(soldEntry.getUnitPrice());
				reportBody.putContent("</price></item>");
			}
			reportBody.putContent("</items>");
			report.setReportBody(reportBody);
		}

		return report;
	}

}
