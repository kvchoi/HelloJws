/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package extend.logback.access.spi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jws.mvc.Http;
import extend.logback.access.AccessConstants;

public class Util {
  static final int BUF_SIZE= 128;
  
  public static String readToString(InputStream in) throws IOException {
    if(in == null) {
      return null;
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buf = new byte[BUF_SIZE];
    int n = 0;
    while( (n = in.read(buf, 0, BUF_SIZE)) != -1) {
      baos.write(buf, 0, n);
    }
    return baos.toString();
  }
  
  public static boolean isFormUrlEncoded(Http.Request request) {

      String contentTypeStr = request.contentType;
      if ("POST".equalsIgnoreCase(request.method)
              && contentTypeStr != null
              && contentTypeStr.startsWith(AccessConstants.X_WWW_FORM_URLECODED)) {
        return true;
      } else {
        return false;
      }
    }

    public static boolean isImageResponse(Http.Response response) {

      String responseType = response.contentType;

      if (responseType != null && responseType.startsWith(AccessConstants.IMAGE_CONTENT_TYPE)) {
        return true;
      } else {
        return false;
      }
    }
}