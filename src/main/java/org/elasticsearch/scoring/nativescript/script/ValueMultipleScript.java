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
import org.elasticsearch.script.AbstractSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class ValueMultipleScript extends AbstractSearchScript {

    String field;
    ArrayList<HashMap<String, Long>> value_multiples;
    int fieldValue;

    final static public String SCRIPT_NAME = "value_multiple_script_score";

    public static class Factory implements NativeScriptFactory {
        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            return new ValueMultipleScript(params);
        }
    }

    private ValueMultipleScript(Map<String, Object> params) {
        params.entrySet();
        // field name
        field = (String) params.get("field");
        if (field == null) {
            throw new ScriptException("cannot initialize " + SCRIPT_NAME + ": field or value_multiples parameter missing!");
        }
    }

    @Override
    public Object run() {

        ScriptDocValues source_doc_value = (ScriptDocValues) doc().get(field);
        if (source_doc_value != null && !source_doc_value.isEmpty()) {

            ScriptDocValues.Longs fieldValue = (ScriptDocValues.Longs) source_doc_value;

            if (fieldValue.getValue() == 2) {
                return score()*1.5;
            }
            if (fieldValue.getValue() == 3) {
                return score()*2;
            }

        }
        return score();
    }

}
