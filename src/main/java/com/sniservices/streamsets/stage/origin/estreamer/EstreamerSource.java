package com.sniservices.streamsets.stage.origin.estreamer;

import com.sniservices.streamsets.stage.lib.estreamer.Errors;
import com.streamsets.pipeline.api.BatchMaker;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.base.BaseSource;
import org.apache.commons.exec.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This source is an example and does not actually read from anywhere.
 * It does however, generate generate a simple record with one field.
 */
public abstract class EstreamerSource extends BaseSource {

    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private ExecuteWatchdog watchdog;
    private Executor executor;

    /**
     * Gives access to the UI configuration of the stage provided by the {@link EstreamerDSource} class.
     */
    public abstract String getConfig();

    @Override
    protected List<ConfigIssue> init() {
        // Validate configuration values and open any required resources.
        List<ConfigIssue> issues = super.init();

        if (getConfig().equals("invalidValue")) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.ESTREAMER.name(), "config", Errors.ESTREAMER_00, "Here's what's wrong..."
                    )
            );
        }

        // If issues is not empty, the UI will inform the user of each configuration issue in the list.
        return issues;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        if (watchdog != null){
            watchdog.destroyProcess();
        }
        super.destroy();
    }

    /** {@inheritDoc} */
    @Override
    public String produce(String lastSourceOffset, int maxBatchSize, BatchMaker batchMaker) {
        // Offsets can vary depending on the data source. Here we use an integer as an example only.
        long nextSourceOffset = 0;
        if (lastSourceOffset != null) {
            nextSourceOffset = Long.parseLong(lastSourceOffset);
        }


        try {

            if ( executor == null ){
                String cmd = "/usr/bin/tail -20f /var/log/system.log &";
                CommandLine commandLine = CommandLine.parse(cmd);
                DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
                executor = new DefaultExecutor();
                watchdog = executor.getWatchdog();
                executor.setExitValue(1);
                executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
                    @Override
                    protected void processLine(String s, int i) {

                        try {
                            queue.put(s);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }));

                executor.execute(commandLine, resultHandler);
                Thread.sleep(1000);
            }

            if (queue.size() != 0) {
                List<String> list = new ArrayList<>(maxBatchSize);
                queue.drainTo(list);
                for (String s : list) {
                    Record record = getContext().createRecord("estreamer::" + nextSourceOffset);
                    Map<String, Field> map = new HashMap<>();
                    map.put("record", Field.create(s));
                    record.set(Field.create(map));
                    batchMaker.addRecord(record);
                    ++nextSourceOffset;
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return String.valueOf(nextSourceOffset);
    }

}
