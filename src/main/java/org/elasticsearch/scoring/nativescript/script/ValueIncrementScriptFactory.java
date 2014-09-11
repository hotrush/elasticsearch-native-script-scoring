package org.elasticsearch.scoring.nativescript.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.elasticsearch.script.ScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class ValueIncrementScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        String fieldName = params == null ? null : XContentMapValues.nodeStringValue(params.get("field"), null);
        if (fieldName == null) {
            throw new ScriptException("Missing the field parameter");
        }
        return new ValueIncrementScript(fieldName);
    }

    private static class ValueIncrementScript extends AbstractFloatSearchScript {

        private final String field;

        public ValueIncrementScript(String field) {
            this.field = field;
        }

        @Override
        public float runAsFloat() {
            ScriptDocValues source_doc_value = (ScriptDocValues) doc().get(field);
            if (source_doc_value != null && !source_doc_value.isEmpty()) {

                ScriptDocValues.Longs fieldValue = (ScriptDocValues.Longs) source_doc_value;

                //return fieldValue.getValue();
                return 100;

            }
            return 0;
        }
    }
}
