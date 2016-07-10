package org.amanzi.neo4j.api;

import org.neo4j.kernel.api.exceptions.ProcedureException;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.PerformsWrites;
import org.neo4j.procedure.Procedure;

import java.util.stream.Stream;

/**
 * Sample procedures, provided here as a template. Also used in test code.
 */
public class AmanziProcedures {

    @Context
    public ProcAPI api;

    @Procedure("amanzi.makeNode")
    @PerformsWrites
    public Stream<LongResult> makeNode(@Name("label") String label) throws ProcedureException {
        return Stream.of(new LongResult(api.makeNode(label)));
    }

    @Procedure("amanzi.willFail")
    public Stream<LongResult> willFail() throws ProcedureException {
        return Stream.of(new LongResult(api.makeNode("Test")));
    }

    @Procedure("amanzi.countNodes")
    public Stream<LongResult> countNodes() {
        return Stream.of(new LongResult(api.countNodes()));
    }

    public static class LongResult {
        public long value;

        public LongResult(long value) {
            this.value = value;
        }
    }
}
