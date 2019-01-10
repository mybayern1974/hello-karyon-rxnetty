/**
 * Copyright 2015 Kenzan, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kenzan.karyon.rxnetty.resource;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import netflix.karyon.transport.http.SimpleUriRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Func1;

import com.kenzan.karyon.rxnetty.endpoint.HelloEndpoint;
import java.io.*;
import java.net.InetAddress;

public class IndexResource implements RequestHandler<ByteBuf, ByteBuf>{

    private final SimpleUriRouter<ByteBuf, ByteBuf> delegate;
    private final HelloEndpoint endpoint;
    private static final Logger logger = LoggerFactory.getLogger(IndexResource.class); 

    public static String execCmd(String cmd) throws java.io.IOException {
        java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    public IndexResource() {
        endpoint = new HelloEndpoint();
        delegate = new SimpleUriRouter<>();

        delegate
        .addUri("/", new RequestHandler<ByteBuf, ByteBuf>() {
            @Override
            public Observable<Void> handle(HttpServerRequest<ByteBuf> request,
                    final HttpServerResponse<ByteBuf> response) {

                return endpoint.getHello()
                .flatMap(new Func1<String, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(String body) {
                        String hostNameAndIp = "";
                        String userdata = "";

                        try{
                            userdata = System.getenv("USERDATA");
                            if (userdata == null) {
                                userdata = "See log file";
                                logger.info("User Data not found. \nCurrent Environment Variables: {}", System.getenv());
                            }

                            hostNameAndIp = GetIpAddressFromIfconfigOnLinux();

                        } catch (Exception e){
                            hostNameAndIp = e.getMessage();
                            e.printStackTrace();
                        }
                        response.writeString("<html><head><style>body{text-align:center; font-family:'Lucida Grande'; color: white; background-color: black}</style></head><body><img src='https://files.readme.io/RXZIYEYlRb68CArUf6OJ_spinnaker-header-transparent.png' /><h2>Example Spinnaker Application</h2><h3>Instance Id " + hostNameAndIp + "</h3><h3>$USERDATA ENV VAR: " + userdata + "</h3></body></html>");
                        return response.close();
                    }
                });
            }
        });
    }

    private String GetIpAddressFromIfconfigOnLinux(){
        String ret = "";
        try{
            Process p = Runtime.getRuntime().exec("ifconfig");
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null){
                if(line.contains("inet addr") && !line.contains("127.0.0.1")){
                    ret = line.split(":")[1].split(" ")[0];
                    break;
                }
            }

            br.close();
        }
        catch(Exception e){
            ret = e.getMessage();
        }

        return ret;
    }

    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request,
            HttpServerResponse<ByteBuf> response) {
        return delegate.handle(request, response);
    }

}
