import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.IOException;


public class NGRIDReader {
    public String getAccountNumber(String pdfFilePath) {
		String result = null;
        FileInputStream is = null;
        PDDocument document = null;
        try {
            is = new FileInputStream(pdfFilePath);
            document = PDDocument.load(is);
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            Rectangle rect = new Rectangle(310, 60, 90, 10);
            stripper.addRegion( "class1", rect );
            PDPage firstPage = document.getPage(0);
    		stripper.extractRegions( firstPage );
    		System.out.print("Account number: ");
    		System.out.println(stripper.getTextForRegion( "class1" ) );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public String getCurrentCharge(String pdfFilePath) {
		String result = null;
        FileInputStream is = null;
        PDDocument document = null;
        try {
            is = new FileInputStream(pdfFilePath);
            document = PDDocument.load(is);
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            Rectangle rect = new Rectangle(500, 200, 90, 10);
            stripper.addRegion( "class1", rect );
            PDPage firstPage = document.getPage(0);
    		stripper.extractRegions( firstPage );
    		System.out.print("Current charge: ");
    		System.out.println(stripper.getTextForRegion( "class1" ) );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public String getTotalUsage(String pdfFilePath) {
		String result = null;
        FileInputStream is = null;
        PDDocument document = null;
        try {
            is = new FileInputStream(pdfFilePath);
            document = PDDocument.load(is);
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            Rectangle rect = new Rectangle(500, 150, 200, 10);
            stripper.addRegion( "class1", rect );
            PDPage firstPage = document.getPage(1);
    		stripper.extractRegions( firstPage );
    		System.out.print("Total Usage: ");
    		System.out.println(stripper.getTextForRegion( "class1" ) );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
	public static void main(String [] args) {
        String pdfFilePath = "/Users/aie/Desktop/ngrid_sample_elec.pdf";
        NGRIDReader ngrid = new NGRIDReader();
        ngrid.getAccountNumber(pdfFilePath);
        ngrid.getCurrentCharge(pdfFilePath);
        ngrid.getTotalUsage(pdfFilePath);
     } 
}
