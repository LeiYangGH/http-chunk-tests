package org.example;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import rx.apache.http.ObservableHttpResponse;
import rx.apache.http.ObservableHttp;

public class App {

  private static void useApacheHc() throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
//    HttpGet httpGet = new HttpGet("http://127.0.0.1:8080");
    HttpGet httpGet = new HttpGet("http://10.71.20.181:8080");
    CloseableHttpResponse response = httpclient.execute(httpGet);
    try {
      HttpEntity responseEntity = response.getEntity();
      BufferedInputStream remoteBIS = new BufferedInputStream(responseEntity.getContent());
      int maxChunkSizePossible = 1000000;
      ByteArrayOutputStream baos = new ByteArrayOutputStream(maxChunkSizePossible);
      byte[] buf = new byte[maxChunkSizePossible];
      int bytesRead = 0;
      while (bytesRead >= 0) {
        baos.write(buf, 0, bytesRead);
        try {
          bytesRead = remoteBIS.read(buf);
          System.out.println(bytesRead);
          //output is:
//          10
//          100
//          1000
//          8186
//          1814
//          -1
        } catch (IOException ex) {
          remoteBIS.close();
          break;
        }
      }
      baos.close();
    } finally {
      response.close();
    }
  }

  private static void useJavaHttpClient() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://10.71.20.181:8080"))
        .build();

    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());
  }

  private static void useRxApacheHttp() throws IOException, InterruptedException {
    CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
    httpClient.start();

    ObservableHttp
        .createRequest(HttpAsyncMethods.createGet("http://10.71.20.181:8080"), httpClient)
        .toObservable()
        .flatMap(response ->
            response.getContent().map(bb -> new String(bb))
        )
        .forEach(resp ->
            // this will be invoked for each event
            System.out.println(resp)
        );
  }

  public static void main(String[] args) throws IOException, InterruptedException {
//    useApacheHc();
//    useJavaHttpClient();
    useRxApacheHttp();
  }
}
