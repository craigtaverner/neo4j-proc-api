package org.amanzi.neo4j.api;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.DataWriteOperations;
import org.neo4j.kernel.api.KernelTransaction;
import org.neo4j.kernel.api.Statement;
import org.neo4j.kernel.api.exceptions.ProcedureException;
import org.neo4j.kernel.api.exceptions.Status;
import org.neo4j.kernel.api.security.AccessMode;
import org.neo4j.kernel.impl.core.ThreadToStatementContextBridge;
import org.neo4j.kernel.impl.logging.LogService;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.Log;

public class ProcAPI {
    private final Log log;
    GraphDatabaseAPI graph;

    ThreadToStatementContextBridge txBridge;

    public ProcAPI(GraphDatabaseAPI graph, ThreadToStatementContextBridge txBridge, LogService logService) {
        this.graph = graph;
        this.txBridge = txBridge;
        this.log = logService.getUserLog(ProcAPI.class);
    }

    public long countNodes() {
        long result;
        try (Transaction tx = graph.beginTransaction(KernelTransaction.Type.explicit, AccessMode.Static.READ)) {
            Statement statement = this.txBridge.get();
            result = statement.readOperations().countsForNode(-1);
            tx.success();
        }
        return result;
    }

    public long makeNode(String label) throws ProcedureException {
        long result;
        try (Transaction tx = graph.beginTransaction(KernelTransaction.Type.explicit, AccessMode.Static.WRITE)) {
            Statement statement = this.txBridge.get();
            DataWriteOperations writeOps = statement.dataWriteOperations();
            long nodeId = writeOps.nodeCreate();
            int labelId = writeOps.labelGetOrCreateForName(label);
            writeOps.nodeAddLabel(nodeId, labelId);
            result = nodeId;
            tx.success();
        } catch (Exception e) {
            log.error("Failed to create node: " + e.getMessage());
            throw new ProcedureException(Status.Procedure.ProcedureCallFailed,
                    "Failed to create node: " + e.getMessage(), e);
        }
        return result;
    }
}
