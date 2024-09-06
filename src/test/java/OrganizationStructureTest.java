import com.salaryanalyzer.exception.EmployeeNotFoundException;
import com.salaryanalyzer.exception.InvalidCSVFormatException;
import com.salaryanalyzer.service.OrganizationStructure;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class OrganizationStructureTest {
    private OrganizationStructure org;

    private  String path= "\\src\\test\\resources\\employee.csv";
    private  String path1= "\\src\\test\\resources\\employee1.csv";
    private  String path2= "\\src\\test\\resources\\employee2.csv";
    private  String path3= "\\src\\test\\resources\\employee3.csv";
    private  String path4= "\\src\\test\\resources\\employee4.csv";

    @Before
    public void setup()  {
        org = new OrganizationStructure();

    }

    @Test
    public void testValidCSVLoad() throws InvalidCSVFormatException, IOException  {
        org.loadEmployeesFromCSV(path);
        assertNotNull(org);
    }

    @Test(expected = InvalidCSVFormatException.class)
    public void testEmptyCSVFound() throws InvalidCSVFormatException, IOException {
        org.loadEmployeesFromCSV(path1);
    }

    @Test(expected = InvalidCSVFormatException.class)
    public void testInvalidSalaryFormat() throws InvalidCSVFormatException, IOException {
        org.loadEmployeesFromCSV(path2);
    }
    @Test(expected = InvalidCSVFormatException.class)
    public void testMissingCEOData() throws InvalidCSVFormatException, IOException {
        org.loadEmployeesFromCSV(path3);
    }

    @Test
    public void testCheckReportingLines() throws EmployeeNotFoundException, InvalidCSVFormatException, IOException {
        org.loadEmployeesFromCSV(path);
        org.checkReportingLines();
    }

    @Test
    public void testCheckManagerSalaries() throws EmployeeNotFoundException, InvalidCSVFormatException, IOException {
        org.loadEmployeesFromCSV(path);
        org.checkManagerSalaries();
    }
}
