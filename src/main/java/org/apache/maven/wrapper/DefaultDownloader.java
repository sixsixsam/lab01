/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.maven.wrapper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Hans Dockter
 */
public class DefaultDownloader implements Downloader {
  private static final int PROGRESS_CHUNK = 500000;

  private static final int BUFFER_SIZE = 10000;

  private final String applicationName;

  private final String applicationVersion;

  public DefaultDownloader(String applicationName, String applicationVersion) {
    this.applicationName = applicationName;
    this.applicationVersion = applicationVersion;
    configureProxyAuthentication();
  }

  private void configureProxyAuthentication() {
    if (System.getProperty("http.proxyUser") != null) {
      Authenticator.setDefault(new SystemPropertiesProxyAuthenticator());
    }
  }

  public void download(URI address, File destination) throws Exception {
    if (destination.exists()) {
      return;
    }
    destination.getParentFile().mkdirs();

    downloadInternal(address, destination);
  }

  private void downloadInternal(URI address, File destination) throws Exception {
    OutputStream out = null;
    URLConnection conn;
    InputStream in = null;
    try {
      URL url = address.toURL();
      out = new BufferedOutputStream(new FileOutputStream(destination));
      conn = url.openConnection();
      final String userAgentValue = calculateUserAgent();
      conn.setRequestProperty("User-Agent", userAgentValue);
      in = conn.getInputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int numRead;
      long progressCounter = 0;
      while ((numRead = in.read(buffer)) != -1) {
        progressCounter += numRead;
        if (progressCounter / PROGRESS_CHUNK > 0) {
          Logger.info(".");
          progressCounter = progressCounter - PROGRESS_CHUNK;
        }
        out.write(buffer, 0, numRead);
      }
    } finally {
      Logger.info("");
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
    }
  }

  private String calculateUserAgent() {
    String appVersion = applicationVersion;

    String javaVendor = System.getProperty("java.vendor");
    String javaVersion = System.getProperty("java.version");
    String javaVendorVersion = System.getProperty("java.vm.version");
    String osName = System.getProperty("os.name");
    String osVersion = System.getProperty("os.version");
    String osArch = System.getProperty("os.arch");
    return String.format("%s/%s (%s;%s;%s) (%s;%s;%s)", applicationName, appVersion, osName, osVersion, osArch, javaVendor, javaVersion, javaVendorVersion);
  }

  private static class SystemPropertiesProxyAuthenticator extends Authenticator {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(System.getProperty("http.proxyUser"), System.getProperty("http.proxyPassword", "").toCharArray());
    }
  }
}
