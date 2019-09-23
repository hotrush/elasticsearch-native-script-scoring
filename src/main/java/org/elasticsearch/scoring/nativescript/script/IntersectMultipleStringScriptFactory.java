package org.elasticsearch.scoring.nativescript.script;

import java.util.*;

import org.elasticsearch.script.GeneralScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
//import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class IntersectMultipleStringScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        int limit = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("limit"), 0);
        int multiple = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple"), 0);
        String field = params == null ? null : XContentMapValues.nodeStringValue(params.get("field"), null);
        String itemsStr = params == null ? null : XContentMapValues.nodeStringValue(params.get("items"), null);
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(itemsStr.split(",,,")));
        if (field == null || items == null) {
            throw new GeneralScriptException("Missing the field parameter");
        }
        return new IntersectMultipleStringScript(field,items,limit,multiple);
    }

    @Override
    public boolean needsScores() {
        return false;
    }

    @Override
    public String getName() {
        return "multiple_terms_string_script_score";
    }

    private static class IntersectMultipleStringScript extends AbstractDoubleSearchScript {

        private final String field;
        private final int limit;
        private final int multiple;
        private final ArrayList<String> items;

        public IntersectMultipleStringScript(String field,ArrayList<String> items,int limit, int multiple) {
            this.field = field;
            this.limit = limit;
            this.multiple = multiple;
            this.items = items;
        }

        @Override
        public double runAsDouble() {
            ScriptDocValues source_doc_values = (ScriptDocValues) doc().get(field);
            if (source_doc_values != null && !source_doc_values.isEmpty()) {

                List<String> source_items_list = ((ScriptDocValues.Strings) source_doc_values).getValues();
                ArrayList<String> source_items = new ArrayList<String>(source_items_list.size());
                source_items.addAll(source_items_list);

                source_items.retainAll(items);
                int intersections_num = source_items.size();
                if (intersections_num > 0) {
                    if (intersections_num >= limit) {
                        return (double) limit*multiple;
                    } else {
                        return (double) intersections_num*multiple;
                    }
                }
            }
            return 0;
        }
    }
}
