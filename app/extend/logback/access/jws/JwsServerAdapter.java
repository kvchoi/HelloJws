package extend.logback.access.jws;

import java.util.Map;

import jws.mvc.Http;
import extend.logback.access.spi.ServerAdapter;

public class JwsServerAdapter implements ServerAdapter {

    Http.Request request;
    Http.Response response;

    public JwsServerAdapter(Http.Request request, Http.Response response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public long getRequestTimestamp() {
        if (this.request != null) {
            return this.request.entryJwsTime;
        }
        return 0;
    }

    @Override
    public long getContentLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getStatusCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Map<String, String> buildResponseHeaderMap() {
        // TODO Auto-generated method stub
        return null;
    }

}
