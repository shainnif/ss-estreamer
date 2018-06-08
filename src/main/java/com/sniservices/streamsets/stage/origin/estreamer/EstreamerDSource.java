/**
 * Copyright 2015 StreamSets Inc.
 *
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sniservices.streamsets.stage.origin.estreamer;

import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.ExecutionMode;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.StageDef;

import java.util.List;

@StageDef(
    version = 1,
    label = "Estreamer Origin",
    description = "connect to an Estreamer service ",
    icon = "default.png",
    execution = ExecutionMode.STANDALONE,
    recordsByRef = true,
    onlineHelpRefUrl = ""
)
@ConfigGroups(value = Groups.class)
@GenerateResourceBundle

public class EstreamerDSource extends EstreamerSource {

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      defaultValue = "default",
      label = "Estreamer Config",
      displayPosition = 10,
      group = "ESTREAMER"
  )


  public String config;

  /** {@inheritDoc} */
  @Override
  public String getConfig() {
    return config;
  }

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.NUMBER,
      label = "Port",
      defaultValue = "[\"9999\"]",
      description = "Port to connect to",
      group = "ESTREAMER",
      displayPosition = 5
    )
    public Number port;

    public Number getPort(){
        return port;
    }

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.STRING,
            label = "Server",
            defaultValue = "[\"9999\"]",
            description = "Server to connect to",
            group = "ESTREAMER",
            displayPosition = 6
    )
    public String serverName;

    public String getServerName(){
        return serverName;
    }

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.STRING,
            label = "Server Key",
            defaultValue = "[\"Server.key\"]",
            description = "Authentication key from Estreamer",
            group = "ESTREAMER",
            displayPosition = 7
    )
    public String serverKey;

    public String getServerKey(){
        return serverKey;
    }

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.STRING,
            label = "Server Certificate",
            defaultValue = "[\"server.cert\"]",
            description = "Server certificate to use for authentication ",
            group = "ESTREAMER",
            displayPosition = 8
    )
    public String serverCertificate; // string so we can listen on multiple ports in the future

    public String getServerCertificate(){
        return serverCertificate;
    }

}
