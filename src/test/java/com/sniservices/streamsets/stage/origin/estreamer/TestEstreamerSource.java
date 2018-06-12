package com.sniservices.streamsets.stage.origin.estreamer;

import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.sdk.SourceRunner;
import com.streamsets.pipeline.sdk.StageRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestEstreamerSource {
  private static final int MAX_BATCH_SIZE = 5;

  @Test
  public void testOrigin() throws Exception {
    SourceRunner runner = new SourceRunner.Builder(EstreamerDSource.class)
        .addConfiguration("config", "value")
        .addConfiguration("port", "9999" )
        .addConfiguration("serverName", "localhost" )
        .addConfiguration("serverCertificate", "./server.cert" )
        .addConfiguration("passphrase", "Password" )
        .addOutputLane("lane")
        .build();

    try {
      runner.runInit();

      final String lastSourceOffset = null;
      StageRunner.Output output = runner.runProduce(lastSourceOffset, MAX_BATCH_SIZE);
      Assert.assertTrue("input value problem"+output.getNewOffset(), Integer.parseInt(output.getNewOffset())  >=  20);
      List<Record> records = output.getRecords().get("lane");
      Assert.assertTrue( records.size() > 20);
      Assert.assertTrue(records.get(0).has("/record"));

    } finally {
      runner.runDestroy();
    }
  }


}
