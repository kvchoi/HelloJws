import org.junit.*;
import jws.test.*;
import jws.mvc.*;
import jws.mvc.Http.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(jws.Jws.defaultWebEncoding, response);
    }
    
}
