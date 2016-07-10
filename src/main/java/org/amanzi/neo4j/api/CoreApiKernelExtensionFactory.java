package org.amanzi.neo4j.api;

import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.core.ThreadToStatementContextBridge;
import org.neo4j.kernel.impl.logging.LogService;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.neo4j.kernel.lifecycle.LifecycleAdapter;

public class CoreApiKernelExtensionFactory extends KernelExtensionFactory<CoreApiKernelExtensionFactory.Dependencies> {
    public CoreApiKernelExtensionFactory() {
        super("AmanziAPI");
    }

    @Override
    public Lifecycle newInstance(KernelContext context, Dependencies dependencies)
            throws Throwable {
        dependencies.procedures().registerComponent(ProcAPI.class, (ctx) -> new ProcAPI(dependencies.getGraphDatabaseAPI(), dependencies.txBridge(), dependencies.logService()));
        return new LifecycleAdapter();
    }

    public interface Dependencies {
        LogService logService();

        Procedures procedures();

        GraphDatabaseAPI getGraphDatabaseAPI();

        ThreadToStatementContextBridge txBridge();
    }
}
