package org.elasticsearch.scoring.nativescript.script;

import java.util.*;

import org.elasticsearch.script.ScriptException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
//import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

public class IntersectMultipleScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        int limit = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("limit"), 0);
        int multiple = params == null ? null : XContentMapValues.nodeIntegerValue(params.get("multiple"), 0);
        String field = params == null ? null : XContentMapValues.nodeStringValue(params.get("field"), null);
        String itemsStr = params == null ? null : XContentMapValues.nodeStringValue(params.get("items"), null);
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(itemsStr.split(",,,")));
        if (field == null || items == null) {
            throw new ScriptException("Missing the field parameter");
        }
        return new IntersectMultipleScript(field,items,limit,multiple);
    }

    private static class IntersectMultipleScript extends AbstractFloatSearchScript {

        private final String field;
        private final int limit;
        private final int multiple;
        private final ArrayList<String> items;

        public IntersectMultipleScript(String field,ArrayList<String> items,int limit, int multiple) {
            this.field = field;
            this.limit = limit;
            this.multiple = multiple;
            this.items = items;
        }

        @Override
        public float runAsFloat() {
            ScriptDocValues source_doc_values = (ScriptDocValues) doc().get(field);
            if (source_doc_values != null && !source_doc_values.isEmpty()) {

                List<String> source_items_list = ((ScriptDocValues.Strings) source_doc_values).getValues();
                ArrayList<String> source_items = new ArrayList<String>(source_items_list.size());
                source_items.addAll(source_items_list);

                source_items.retainAll(items);
                int intersections_num = source_items.size();
                if (intersections_num > 0) {
                    if (intersections_num >= limit) {
                        //return (float) limit*multiple;
                        return (float) limit*multiple;
                    } else {
                        //return (float) intersections_num*multiple;
                        return (float) intersections_num*multiple;
                    }
                }
            }
            return 0;
        }
    }
}