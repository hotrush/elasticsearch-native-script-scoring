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
        int multipleRecommend = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple_recommend"), 0);
        int multipleHighRecommend = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple_high_recommend"), 0);
        if (fieldName == null) {
            throw new ScriptException("Missing the field parameter");
        }
        return new ValueMultipleScript(fieldName, multipleRecommend, multipleHighRecommend);
    }

    private static class ValueMultipleScript extends AbstractFloatSearchScript {

        private final String field;
        private final int multipleRecommend;
        private final int multipleHighRecommend;

        public ValueMultipleScript(String field, int multipleRecommend, int multipleHighRecommend) {
            this.field = field;
            this.multipleRecommend = multipleRecommend;
            this.multipleHighRecommend = multipleHighRecommend;
        }

        @Override
        public float runAsFloat() {
            ScriptDocValues source_doc_value = (ScriptDocValues) doc().get(field);
            if (source_doc_value != null && !source_doc_value.isEmpty()) {

                ScriptDocValues.Longs fieldValue = (ScriptDocValues.Longs) source_doc_value;

                if (fieldValue.getValue() == 2) {
                    return (float) multipleRecommend;
                }
                if (fieldValue.getValue() == 3) {
                    return (float) multipleHighRecommend;
                }

            }
            return 0;
        }
    }
}
