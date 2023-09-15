package com.icezhg.athena.generator;

import com.icezhg.generator.config.Config;
import com.icezhg.generator.handler.FileWriterResultHandler;
import com.icezhg.generator.invoker.Invoker;
import com.icezhg.generator.invoker.SimpleInvoker;

/**
 * Created by zhongjibing on 2023/09/15.
 */
public class InvokerFactory {

    public static Invoker simpleInvoker(Config config) {
        return new SimpleInvoker(config, new FileWriterResultHandler());
    }


}
