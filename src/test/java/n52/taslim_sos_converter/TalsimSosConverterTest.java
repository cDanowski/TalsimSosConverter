package n52.taslim_sos_converter;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import n52.talsim_sos_converter.TalsimSosConverter;

/**
 * Unit test for simple App.
 */
public class TalsimSosConverterTest 
    extends TestCase
{
	private static final String PATH_TO_TALSIM_EXAMPLE_FILE = "TalsimResult_example.xml";
	private static final String SOS_URL = "http://tamis.dev.52north.org/sos/service";
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TalsimSosConverterTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TalsimSosConverterTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testConverter() throws Exception
    {
    	InputStream talsim_input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(PATH_TO_TALSIM_EXAMPLE_FILE);
    	
    	TalsimSosConverter converter = new TalsimSosConverter();
    	
    	URL sosURL = new URL(SOS_URL);
    	
    	converter.insertOutputToSOS(talsim_input, sosURL);
    	
    	assertTrue( true );
    }
}
