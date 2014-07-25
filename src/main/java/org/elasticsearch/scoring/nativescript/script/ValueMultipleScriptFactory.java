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

public class ValueMultipleScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        String fieldName = params == null ? null : XContentMapValues.nodeStringValue(params.get("field"), null);
        String type = params == null ? null : XContentMapValues.nodeStringValue(params.get("type"), null);
        if (fieldName == null || type == null) {
            throw new ScriptException("Missing the field parameter");
        }
        return new ValueMultipleScript(fieldName, type);
    }

    private static class ValueMultipleScript extends AbstractFloatSearchScript {

        private final String field;
        private final String type;

        public ValueMultipleScript(String field, String type) {
            this.field = field;
            this.type = type;
        }

        @Override
        public float runAsFloat() {
            ScriptDocValues source_doc_value = (ScriptDocValues) doc().get(field);
            if (source_doc_value != null && !source_doc_value.isEmpty()) {

                ScriptDocValues.Longs fieldValue = (ScriptDocValues.Longs) source_doc_value;

                if (fieldValue.getValue() == 2) {
                    if (type.equals("matching")) {
                        return (float) 3;
                    } else {
                        return (float) 1;
                    }
                }
                if (fieldValue.getValue() == 3) {
                    if (type.equals("matching")) {
                        return (float) 5;
                    } else {
                        return (float) 2;
                    }
                }

            }
            return 0;
        }
    }
}
