package org.elasticsearch.scoring.nativescript.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.elasticsearch.script.GeneralScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class ValueIncrementScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        String fieldName = params == null ? null : XContentMapValues.nodeStringValue(params.get("field"), null);
        if (fieldName == null) {
            throw new GeneralScriptException("Missing the field parameter");
        }
        return new ValueIncrementScript(fieldName);
    }

    @Override
    public boolean needsScores() {
        return false;
    }

    @Override
    public String getName() {
        return "value_increment_script_score";
    }

    private static class ValueIncrementScript extends AbstractDoubleSearchScript {

        private final String field;

        public ValueIncrementScript(String field) {
            this.field = field;
        }

        @Override
        public double runAsDouble() {
            ScriptDocValues source_doc_value = (ScriptDocValues) doc().get(field);
            if (source_doc_value != null && !source_doc_value.isEmpty()) {

                ScriptDocValues.Longs fieldValue = (ScriptDocValues.Longs) source_doc_value;

                return (double) fieldValue.getValue();

            }
            return 0;
        }
    }
}
